import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.common.base.Preconditions;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

 private AbstractCompiler compiler;
 private Supplier<String> safeNameIdSupplier;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {};
 safeNameIdSupplier = () -> "test_id";
 }

 @Test
 public void testMutateFunctionWithNoArguments() {
 Node fnNode = Node.newFunctionNode(0, null);
 Node callNode = Node.newExpressionStatement(
 NodeUtil.newCallNode(fnNode, Lists.newArrayList(), null, null));
 String resultName = "result";
 boolean needsDefaultResult = false;
 boolean isCallInLoop = false;

 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 Node actual = mutator.mutate(null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

 // Basic functionality test
 assertEquals(NodeUtil.getFunctionBody(fnNode), actual);
 }

 @Test
 public void testMutateFunctionWithOneArgument() {
 Node fnNode = Node.newFunctionNode(1, new String[]{"arg1"});
 Node callNode = Node.newExpressionStatement(
 NodeUtil.newCallNode(fnNode, Lists.newArrayList(Node.newString("arg1Value")), null, null));
 String resultName = "result";
 boolean needsDefaultResult = false;
 boolean isCallInLoop = false;

 FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 Node actual = mutator.mutate(null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

 // Basic functionality test
 assertEquals(NodeUtil.getFunctionBody(fnNode), actual);

 // Edge case: argument name collision
 Node fnNodeCollision = Node.newFunctionNode(1, new String[]{"arg1", "arg1"});
 Node callNodeCollision = Node.newExpressionStatement(
 NodeUtil.newCallNode(fnNodeCollision, Lists.newArrayList(Node.newString("arg1Value")), null, null));
 String resultNameCollision = "result";
 boolean needsDefaultResultCollision = false;
 boolean isCallInLoopCollision = false;

 FunctionToBlockMutator mutatorCollision = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 Node actualCollision = mutatorCollision.mutate(null, fnNodeCollision, callNodeCollision, resultNameCollision, needsDefaultResultCollision, isCallInLoopCollision);

 // Edge case: argument value is a function
 Node fnNodeArgFunc = Node.newFunctionNode(1, new String[]{"arg1"});
 Node callNodeArgFunc = Node.newExpressionStatement(
 NodeUtil.newCallNode(fnNodeArgFunc, Lists.newArrayList(Node.newFunctionNode(0, null)), null, null));
 String resultNameArgFunc = "result";
 boolean needsDefaultResultArgFunc = false;
 boolean isCallInLoopArgFunc = false;

 FunctionToBlockMutator mutatorArgFunc = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 Node actualArgFunc = mutatorArgFunc.mutate(null, fnNodeArgFunc, callNodeArgFunc, resultNameArgFunc, needsDefaultResultArgFunc, isCallInLoopArgFunc);

 // Edge case: argument value is an object
 Node fnNodeArgObj = Node.newFunctionNode(1, new String[]{"arg1"});
 Node callNodeArgObj = Node.newExpressionStatement(
 NodeUtil.newCallNode(fnNodeArgObj, Lists.newArrayList(Node.newObjectLiteral()), null, null));
 String resultNameArgObj = "result";
 boolean needsDefaultResultArgObj = false;
 boolean isCallInLoopArgObj = false;

 FunctionToBlockMutator mutatorArgObj = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
 Node actualArgObj = mutatorArgObj.mutate(null, fnNodeArgObj, callNodeArgObj, resultNameArgObj, needsDefaultResultArgObj, isCallInLoopArgObj);

 // Exception handling: ArgumentException
 Node fnNodeArgException = Node.newFunctionNode(1, new String[]{"arg1"});
 Node callNodeArgException = Node.newExpressionStatement(
 NodeUtil.newCallNode(fnNodeArgException, Lists.newArrayList(Node.newString("arg1Value")), null, null));
 String resultNameArgException = "result";
 boolean needsDefaultResultArgException = false;
 boolean isCallInLoopArgException = false;

 try {
 FunctionToBlockMutator mutatorArgException = new FunctionToBlockMutator(compiler, new ThrowingSupplier<>("test_id"));
 Node actualArgException = mutatorArgException.mutate(null, fnNodeArgException, callNodeArgException, resultNameArgException, needsDefaultResultArgException, isCallInLoopArgException);
 fail("Expected ArgumentException");
 } catch (ArgumentException e) {
 // Expected
 }
}

 @Test
 public void testMutateFunctionWithTwoArguments() {
 // ... (similar to testMutateFunctionWithOneArgument(), but with two arguments)
 }

 // ... (add more test cases as needed)

}