import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess_TypicalUseCase() {
    // Typical use case where there is a basic expression statement
    // 1 + foo() + bar() should be simplified to foo();bar()
    // Create input AST
    Node rootNode = new Node(Token.ROOT);
    Node exprStmt = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD);
    Node one = new Node(Token.NUMBER);
    one.setDouble(1);
    Node fooCall = new Node(Token.CALL, new Node(Token.NAME, "foo"));
    Node barCall = new Node(Token.CALL, new Node(Token.NAME, "bar"));
    exprBody.addChildToFront(one);
    exprBody.addChildToBack(fooCall);
    exprBody.addChildToBack(barCall);
    exprStmt.addChildToBack(exprBody);
    rootNode.addChildToBack(exprStmt);

    // Create expected output AST
    Node expectedRootNode = new Node(Token.ROOT);
    Node fooStmt = new Node(Token.EXPR_RESULT, fooCall.cloneTree());
    Node barStmt = new Node(Token.EXPR_RESULT, barCall.cloneTree());
    expectedRootNode.addChildToBack(fooStmt);
    expectedRootNode.addChildToBack(barStmt);

    // Create and execute the RemoveConstantExpressions pass
    AbstractCompiler compiler = new AbstractCompiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, rootNode);

    // Verify the result
    assertTrue(pass.getResult().changed);
    assertEquals(expectedRootNode.toStringTree(), rootNode.toStringTree());
  }

  @Test
  public void testProcess_EdgeCase_NoExpressionStatement() {
    // Edge case where the input AST doesn't contain any expression statement
    // There should be no changes after the RemoveConstantExpressions pass
    // Create input AST
    Node rootNode = new Node(Token.ROOT);
    Node varStmt = new Node(Token.VAR);
    Node name = new Node(Token.NAME, "x");
    Node value = new Node(Token.NUMBER);
    value.setDouble(42);
    varStmt.addChildToFront(name);
    varStmt.addChildToBack(value);
    rootNode.addChildToBack(varStmt);

    // Create expected output AST
    Node expectedRootNode = new Node(Token.ROOT);
    Node expectedVarStmt = new Node(Token.VAR, name.cloneTree(), value.cloneTree());
    expectedRootNode.addChildToBack(expectedVarStmt);

    // Create and execute the RemoveConstantExpressions pass
    AbstractCompiler compiler = new AbstractCompiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, rootNode);

    // Verify the result
    assertFalse(pass.getResult().changed);
    assertEquals(expectedRootNode.toStringTree(), rootNode.toStringTree());
  }

  @Test
  public void testProcess_ErrorScenario_SideEffectSubexpression() {
    // Error scenario where there is a side effect subexpression within the input AST
    // There should be no changes after the RemoveConstantExpressions pass
    // Create input AST
    Node rootNode = new Node(Token.ROOT);
    Node exprStmt = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD);
    Node sideEffectSubexpression = new Node(Token.ASSIGN,
        new Node(Token.NAME, "x"), new Node(Token.TRUE));
    Node one = new Node(Token.NUMBER);
    one.setDouble(1);
    exprBody.addChildToFront(one);
    exprBody.addChildToBack(sideEffectSubexpression);
    exprStmt.addChildToBack(exprBody);
    rootNode.addChildToBack(exprStmt);

    // Create expected output AST
    Node expectedRootNode = new Node(Token.ROOT);
    Node expectedExprStmt = new Node(Token.EXPR_RESULT, exprBody.cloneTree());
    expectedRootNode.addChildToBack(expectedExprStmt);

    // Create and execute the RemoveConstantExpressions pass
    AbstractCompiler compiler = new AbstractCompiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, rootNode);

    // Verify the result
    assertFalse(pass.getResult().changed);
    assertEquals(expectedRootNode.toStringTree(), rootNode.toStringTree());
  }
}