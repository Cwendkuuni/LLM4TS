import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private Node root;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    root = new Node(Token.SCRIPT, null);
  }

  @Test
  public void testRemoveConstantExpression() {
    Node exprBody = new Node(Token.NUM, 1);
    Node constantExpression = new Node(Token.EXPR_RESULT, exprBody);
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToEnd(constantExpression);
    root.addChildToEnd(parent);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node simplifiedExpression = pass.getSimplifiedExpression(parent);
    assertNull(simplifiedExpression);
    assertEquals(1, root.getChildCount());
  }

  @Test
  public void testRemoveConstantExpressionWithSideEffect() {
    Node exprBody1 = new Node(Token.NUM, 1);
    Node sideEffect1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), exprBody1);
    Node constantExpression1 = new Node(Token.EXPR_RESULT, sideEffect1);

    Node exprBody2 = new Node(Token.NUM, 2);
    Node sideEffect2 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), exprBody2);
    Node constantExpression2 = new Node(Token.EXPR_RESULT, sideEffect2);

    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToEnd(constantExpression1);
    parent.addChildToEnd(constantExpression2);
    root.addChildToEnd(parent);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node simplifiedExpression = pass.getSimplifiedExpression(parent);
    assertNotNull(simplifiedExpression);
    assertEquals(2, simplifiedExpression.getChildCount());
    assertEquals(Token.ASSIGN, simplifiedExpression.getFirstChild().getType());
    assertEquals("x", simplifiedExpression.getFirstChild().getFirstChild().getString());
  }

  @Test(expected = AssertionError.class)
  public void testRemoveConstantExpressionWithNoSideEffectParent() {
    Node exprBody = new Node(Token.NUM, 1);
    Node constantExpression = new Node(Token.EXPR_RESULT, exprBody);
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToEnd(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 2)));
    parent.addChildToEnd(constantExpression);
    root.addChildToEnd(parent);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    pass.getSimplifiedExpression(parent);
  }

  @Test
  public void testRemoveConstantExpressionWithNoSideEffectGrandparent() {
    Node exprBody = new Node(Token.NUM, 1);
    Node constantExpression = new Node(Token.EXPR_RESULT, exprBody);
    Node parent1 = new Node(Token.EXPR_STATEMENT, null);
    parent1.addChildToEnd(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 2)));
    Node parent2 = new Node(Token.EXPR_STATEMENT, null);
    parent2.addChildToEnd(constantExpression);
    parent1.addChildToEnd(parent2);
    root.addChildToEnd(parent1);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node simplifiedExpression = pass.getSimplifiedExpression(parent1);
    assertNotNull(simplifiedExpression);
    assertEquals(1, simplifiedExpression.getChildCount());
    assertEquals(Token.NUM, simplifiedExpression.getType());
    assertEquals(1, simplifiedExpression.getFirstChild().getInt());
  }

  @Test(expected = AssertionError.class)
  public void testRemoveConstantExpressionWithCyclicDepencencies() {
    Node exprBody1 = new Node(Token.NAME, "x");
    Node constantExpression1 = new Node(Token.EXPR_RESULT, exprBody1);
    Node exprBody2 = new Node(Token.NAME, "x");
    Node constantExpression2 = new Node(Token.EXPR_RESULT, exprBody2);
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToEnd(constantExpression1);
    parent.addChildToEnd(constantExpression2);
    root.addChildToEnd(parent);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    pass.getSimplifiedExpression(parent);
  }

  @Test
  public void testRemoveConstantExpressionWithComplexSubexpressions() {
    Node exprBody1 = new Node(Token.NAME, "foo");
    Node sideEffect1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 1));
    Node constantExpression1 = new Node(Token.EXPR_RESULT, sideEffect1);

    Node exprBody2 = new Node(Token.NAME, "bar");
    Node sideEffect2 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUM, 2));
    Node constantExpression2 = new Node(Token.EXPR_RESULT, sideEffect2);

    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToEnd(constantExpression1);
    parent.addChildToEnd(constantExpression2);
    root.addChildToEnd(parent);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node simplifiedExpression = pass.getSimplifiedExpression(parent);
    assertNotNull(simplifiedExpression);
    assertEquals(2, simplifiedExpression.getChildCount());
    assertEquals(Token.ASSIGN, simplifiedExpression.getFirstChild().getType());
    assertEquals("x", simplifiedExpression.getFirstChild().getFirstChild().getString());
    assertEquals(Token.ASSIGN, simplifiedExpression.getChild(1).getType());
    assertEquals("y", simplifiedExpression.getChild(1).getFirstChild().getString());
  }

  RemoveConstantExpressions pass;

  @Test
  public void testProcess() {
    RemoveConstantExpressionsTest test = this;
    root = test.createComplexTree();

    pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node simplifiedExpression = pass.getSimplifiedExpression(root);
    assertNotNull(simplifiedExpression);
    assertEquals(3, simplifiedExpression.getChildCount());
    assertEquals("x = foo(); y = bar(); z = 1 + 2;", simplifiedExpression.toString());
  }

  private Node createComplexTree() {
    Node exprBody1 = new Node(Token.NAME, "foo");
    Node sideEffect1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 1));
    Node constantExpression1 = new Node(Token.EXPR_RESULT, sideEffect1);

    Node exprBody2 = new Node(Token.NAME, "bar");
    Node sideEffect2 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUM, 2));
    Node constantExpression2 = new Node(Token.EXPR_RESULT, sideEffect2);

    Node exprBody3 = new Node(Token.NUM, 1);
    Node exprBody4 = new Node(Token.NUM, 2);

    Node parent1 = new Node(Token.EXPR_STATEMENT, null);
    parent1.addChildToEnd(constantExpression1);

    Node parent2 = new Node(Token.EXPR_STATEMENT, null);
    parent2.addChildToEnd(new Node(Token.ASSIGN, null, new Node(Token.NAME, "z"), new Node(Token.BINARY_OPERATOR, null, new Node(Token.ADD), exprBody3, exprBody4)));
    parent1.addChildToEnd(parent2);

    Node parent3 = new Node(Token.EXPR_STATEMENT, null);
    parent3.addChildToEnd(constantExpression2);
    parent1.addChildToEnd(parent3);

    Node script = new Node(Token.SCRIPT, null);
    script.addChildToEnd(parent1);

    return script;
  }

  RemoveConstantExpressions pass;

  @Test
  public void testGetSimplifiedExpression() {
    removeConstantExpressionsTestSetup();

    pass = new RemoveConstantExpressions(compiler);
    Node simplifiedExpression = pass.getSimplifiedExpression(root);
    assertNotNull(simplifiedExpression);
    assertEquals(3, simplifiedExpression.getChildCount());
    assertEquals("x = foo(); y = bar(); z = 1 + 2;", simplifiedExpression.toString());
  }

  @Test
  public void testGetSimplifiedExpressionWithRootAsConstantExpression() {
    removeConstantExpressionsTestSetup();

    Node constantExpression = new Node(Token.EXPR_RESULT, root);
    pass = new RemoveConstantExpressions(compiler);
    Node simplifiedExpression = pass.getSimplifiedExpression(constantExpression);
    assertNotNull(simplifiedExpression);
    assertEquals("x = foo(); y = bar(); z = 1 + 2;", simplifiedExpression.toString());
  }

  private void removeConstantExpressionsTestSetup() {
    RemoveConstantExpressionsTest test = this;
    root = test.createComplexTree();

    pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);
  }
}