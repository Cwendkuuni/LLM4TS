import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

 private AbstractCompiler compiler;
 private FunctionToBlockMutator mutator;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 mutator = new FunctionToBlockMutator(compiler, () -> "test_id");
 }

 @Test
 public void testMutateSimpleFunction() {
 Node fnNode = Node.newFunctionNode("testFunction", new Node[]{}, new Node[]{});
 Node callNode = Node.newCallNode(fnNode, new Node[]{Node.newUndefinedNode(null)});
 String resultName = "testResult";
 String labelName = mutator.getLabelNameForFunction("testFunction");
 Node injectableBlock = mutator.mutate(
 "testFunction", fnNode, callNode, resultName, false, false);

 // Basic functionality test
 Node expectedResult = NodeUtil.newIntNode(42);
 Node actualResult = NodeUtil.extractReturnValue(injectableBlock);
 assertEquals(expectedResult, actualResult);

 // Test with arguments
 Map<String, Node> argMap = Maps.newLinkedHashMap();
 argMap.put("arg1", NodeUtil.newIntNode(1));
 argMap.put("arg2", NodeUtil.newIntNode(2));
 LinkedHashMap<String, Node> arguments = Lists.newLinkedHashMap(argMap.entrySet());
 Node injectableBlockWithArgs = mutator.mutate(
 "testFunction", fnNode, callNode, resultName, false, false, arguments);

 Node extractedArguments = NodeUtil.extractArguments(injectableBlockWithArgs);
 assertEquals(arguments.size(), extractedArguments.getNumChildren());
 for (Map.Entry<String, Node> entry : arguments.entrySet()) {
 Node argNode = extractedArguments.getChild(entry.getKey().hashCode());
 Node expectedArgValue = entry.getValue();
 assertEquals(expectedArgValue, argNode.getFirstChild());
 }
 }

 @Test(expected = AssertionError.class)
 public void testMutateFunctionWithUnreachableCode() {
 Node fnNodeWithUnreachableCode = Node.newFunctionNode("testFunction", new Node[]{}, new Node[]{});
 Node callNode = Node.newCallNode(fnNodeWithUnreachableCode, new Node[]{Node.newUndefinedNode(null)});
 String resultName = "testResult";
 String labelName = mutator.getLabelNameForFunction("testFunction");
 Node injectableBlock = mutator.mutate(
 "testFunction", fnNodeWithUnreachableCode, callNode, resultName, false, false);
 fail("Expected an AssertionError due to unreachable code.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMutateFunctionWithMissingParameters() {
 Node fnNodeWithMissingParameters = Node.newFunctionNode("testFunction", new Node[]{Node.newNameNode("arg1")}, new Node[]{});
 Node callNode = Node.newCallNode(fnNodeWithMissingParameters, new Node[]{});
 String resultName = "testResult";
 String labelName = mutator.getLabelNameForFunction("testFunction");
 try {
 mutator.mutate(
 "testFunction", fnNodeWithMissingParameters, callNode, resultName, false, false);
 fail("Expected an IllegalArgumentException due to missing parameters.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}