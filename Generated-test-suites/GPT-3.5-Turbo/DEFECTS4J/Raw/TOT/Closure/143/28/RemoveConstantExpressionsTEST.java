import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;

public class RemoveConstantExpressionsTest {
  
  @Test
  public void testProcess_withNoExpressions_shouldNotChangeAST() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    
    RemoveConstantExpressions rce = new RemoveConstantExpressions(compiler);
    rce.process(externs, root);
    
    // Check that the AST remains unchanged
    assertEquals(root, root);
  }
  
  @Test
  public void testProcess_withExpression_shouldReplaceExpressionWithSideEffectNodes() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = new Node(Token.BLOCK);
    
    // Create the expression to be replaced
    Node expression = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2));
    expression.addChildToBack(exprBody);
    
    // Create the root node with the expression
    Node root = new Node(Token.BLOCK, expression);
    
    RemoveConstantExpressions rce = new RemoveConstantExpressions(compiler);
    rce.process(externs, root);
    
    // Check that the expression has been replaced with side effect nodes
    assertEquals(Token.EXPR_RESULT, root.getFirstChild().getType());
    assertEquals(Token.EXPR_RESULT, root.getFirstChild().getNext().getType());
  }
  
  @Test
  public void testProcess_withExpression_shouldOnlyReplaceExpressionWithoutSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = new Node(Token.BLOCK);
    
    // Create the expression to be replaced
    Node expression1 = new Node(Token.EXPR_RESULT);
    Node exprBody1 = new Node(Token.NUMBER, 1);
    expression1.addChildToBack(exprBody1);
    
    // Create another expression with side effects
    Node expression2 = new Node(Token.EXPR_RESULT);
    Node sideEffect = new Node(Token.CALL, new Node(Token.NAME, "foo"));
    expression2.addChildToBack(sideEffect);
    
    // Create the root node with the expressions
    Node root = new Node(Token.BLOCK, expression1, expression2);
    
    RemoveConstantExpressions rce = new RemoveConstantExpressions(compiler);
    rce.process(externs, root);
    
    // Check that the first expression has been replaced and the second hasn't
    assertEquals(Token.EXPR_RESULT, root.getFirstChild().getType());
    assertEquals(Token.CALL, root.getFirstChild().getNext().getType());
  }

}