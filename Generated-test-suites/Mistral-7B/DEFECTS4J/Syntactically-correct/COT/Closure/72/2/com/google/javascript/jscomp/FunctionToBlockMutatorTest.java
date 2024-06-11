import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class FunctionToBlockMutatorTest {

  FunctionToBlockMutator functionToBlockMutator;
  AbstractCompiler compiler;
  Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    compiler = new AbstractCompiler() {
      // Empty stub implementation
    };
    safeNameIdSupplier = () -> "test_id";
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_NullFunctionNode() {
    String fnName = "testFunction";
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, null, callNode, resultName,
        needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutate_NullCallNode() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, null, resultName,
        needsDefaultResult, isCallInLoop);
  }

  @Test
  public void testMutate_FunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
  }

  @Test
  public void testReplaceReturns_FunctionWithSingleReturnStatement() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node block = Node.newBlock(null);
    block.addChildToBack(Node.newReturn(Token.RETURN, null));

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node result = replaceReturns(mutatedNode, resultName, null, false);

    assertNotNull(result);
  }

  @Test
  public void testReplaceReturns_FunctionWithMultipleReturnStatements() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node block = Node.newBlock(null);
    block.addChildToBack(Node.newReturn(Token.RETURN, Node.newInt(1)));
    block.addChildToBack(Node.newReturn(Token.RETURN, Node.newInt(2)));

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node result = replaceReturns(mutatedNode, resultName, null, false);

    assertNotNull(result);
    assertEquals(Node.newInt(1), result.getFirstChild());
  }

  @Test
  public void testReplaceReturns_FunctionWithNoReturnStatement() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node block = Node.newBlock(null);

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node result = replaceReturns(mutatedNode, resultName, null, false);

    assertNotNull(result);
    assertEquals(Node.newUndefined(), result);
  }

  @Test
  public void testReplaceReturns_FunctionWithResultVariable() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node block = Node.newBlock(null);
    block.addChildToBack(Node.newVar(Token.VAR, Node.newName(Token.NAME, "result"), null));

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node result = replaceReturns(mutatedNode, null, null, true);

    assertNotNull(result);
    assertEquals(Node.newUndefined(), result);
  }

  @Test
  public void testReplaceReturns_FunctionWithLabel() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node labelName = "testLabel";
    Node block = Node.newBlock(null);
    block.addChildToBack(Node.newLabel(Token.LABEL, labelName));
    block.addChildToBack(Node.newReturn(Token.RETURN, Node.newName(Token.NAME, "testLabel")));

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node result = replaceReturns(mutatedNode, resultName, labelName, false);

    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReplaceReturns_NullBlock() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newName(Token.NAME, "testFunction");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    replaceReturns(mutatedNode, resultName, null, false);
  }

  private Node replaceReturns(Node block, String resultName, String labelName,
      boolean needsDefaultResult) {
    return functionToBlockMutator.replaceReturns(block, resultName, labelName,
        needsDefaultResult);
  }
}