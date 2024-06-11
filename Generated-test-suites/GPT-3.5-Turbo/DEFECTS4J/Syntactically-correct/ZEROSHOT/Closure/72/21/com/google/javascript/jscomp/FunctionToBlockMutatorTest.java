import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {
  
  @Test
  public void testMutate() {
    AbstractCompiler compiler = new AbstractCompiler();
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, () -> "1");
    
    // Test case 1: fnName is null, resultName is null, needsDefaultResult is false, isCallInLoop is false
    Node fnNode1 = Node.newNumber(10);
    Node callNode1 = Node.newCall(Node.newName("foo"));
    Node result1 = mutator.mutate(null, fnNode1, callNode1, null, false, false);
    assertEquals("The result node should be the same as the original function node", fnNode1, result1);
    
    // Test case 2: fnName is not null, resultName is not null, needsDefaultResult is true, isCallInLoop is true
    Node fnNode2 = Node.newNumber(20);
    Node callNode2 = Node.newCall(Node.newName("bar"));
    Node result2 = mutator.mutate("testFunction", fnNode2, callNode2, "result", true, true);
    assertEquals("The result node should be a block with a LABEL node in front of the function body", Token.BLOCK, result2.getType());
    Node label2 = result2.getFirstChild();
    assertEquals("The label name should be correct", "JSCompiler_inline_label_testFunction_1", label2.getString());
    assertEquals("The label node should be followed by the function node", fnNode2, label2.getNext());
    
    // Test case 3: fnName is not empty, resultName is null, needsDefaultResult is true, isCallInLoop is false
    Node fnNode3 = Node.newNumber(30);
    Node callNode3 = Node.newCall(Node.newName("baz"));
    Node result3 = mutator.mutate("testFunction2", fnNode3, callNode3, null, true, false);
    assertEquals("The result node should be a block with a LABEL node in front of the function body", Token.BLOCK, result3.getType());
    Node label3 = result3.getFirstChild();
    assertEquals("The label name should be correct", "JSCompiler_inline_label_testFunction2_1", label3.getString());
    assertEquals("The label node should be followed by the function node", fnNode3, label3.getNext());
    }
}