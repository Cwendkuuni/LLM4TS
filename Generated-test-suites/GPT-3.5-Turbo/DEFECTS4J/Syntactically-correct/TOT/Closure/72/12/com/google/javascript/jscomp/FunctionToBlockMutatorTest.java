import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      @Override
      public String get() {
        return "test";
      }
    };
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    // Test for a normal function call with result assignment
    Node fnNode = new Node("FUNCTION");
    Node callNode = new Node("CALL");
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node result = mutator.mutate("fn", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Assertions
    assertNotNull(result);
    assertTrue(result.isBlock());
    assertEquals(1, result.getChildCount());
    assertEquals(Token.EXPR_RESULT, result.getFirstChild().getType());
    assertEquals("result", result.getFirstChild().getFirstChild().getFirstChild().getString());

    // Test for a function call without result assignment
    resultName = null;
    result = mutator.mutate("fn", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Assertions
    assertNotNull(result);
    assertTrue(result.isBlock());
    assertEquals(1, result.getChildCount());
    assertEquals(Token.EXPR_RESULT, result.getFirstChild().getType());
    assertEquals(Token.NAME, result.getFirstChild().getFirstChild().getType());
    assertEquals("fnNode", result.getFirstChild().getFirstChild().getString());

    // Test for a function call in a loop
    isCallInLoop = true;
    result = mutator.mutate("fn", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Assertions
    assertNotNull(result);
    assertTrue(result.isBlock());
    assertEquals(1, result.getChildCount());
    assertEquals(Token.EXPR_RESULT, result.getFirstChild().getType());
    assertEquals(Token.NAME, result.getFirstChild().getFirstChild().getType());
    assertEquals("fnNode", result.getFirstChild().getFirstChild().getString());
  }
}