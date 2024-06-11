import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpression() {
    AbstractCompiler compiler = new MyCompiler();
    Node root = createTestNode();
    new RemoveConstantExpressions(compiler).process(null, root);
    assertEquals(4, getExpressionCount(root));

    Node result = root.getFirstChild();
    Node foo = result.getFirstChild();
    Node plus1 = foo.getNextSibling();
    Node bar = plus1.getNextSibling();
    Node plus2 = bar.getNextSibling();

    assertEquals(Token.CALL_EXPR, foo.getType());
    assertEquals("foo", NodeUtil.getIdentifiedName(foo));
    assertEquals(Token.EXPR_RESULT, plus1.getType());
    assertEquals(Token.PLUS, plus1.getFirstChild().getType());
    assertEquals(Token.EXPR_RESULT, plus1.getLastChild().getType());
    assertEquals(Token.CALL_EXPR, bar.getType());
    assertEquals("bar", NodeUtil.getIdentifiedName(bar));
    assertEquals(Token.PLUS, plus2.getFirstChild().getType());
    assertEquals(Token.EXPR_RESULT, plus2.getType());
    assertEquals(Token.PLUS, plus2.getLastChild().getType());
  }

  @Test
  public void testRemoveConstantExpressionWithSideEffect() {
    AbstractCompiler compiler = new MyCompiler();
    Node root = createTestNodeWithSideEffect();
    new RemoveConstantExpressions(compiler).process(null, root);
    assertEquals(3, getExpressionCount(root));

    Node result = root.getFirstChild();
    Node foo = result.getFirstChild();
    Node bar = foo.getNextSibling();

    assertEquals(Token.CALL_EXPR, foo.getType());
    assertEquals("foo", NodeUtil.getIdentifiedName(foo));
    assertEquals(Token.CALL_EXPR, bar.getType());
    assertEquals("bar", NodeUtil.getIdentifiedName(bar));
  }

  @Test
  public void testRemoveConstantExpressionsWithNestedExpressions() {
    AbstractCompiler compiler = new MyCompiler();
    Node root = createTestNodeWithNestedExpressions();
    new RemoveConstantExpressions(compiler).process(null, root);
    assertEquals(6, getExpressionCount(root));

    Node result = root.getFirstChild();
    Node foo = result.getFirstChild();
    Node plus1 = result.getLastChild().getLastChild();
    Node bar = foo.getNextSibling();
    Node multiply1 = bar.getFirstChild();
    Node multiply2 = bar.getLastChild();
    Node add1 = multiply1.getLastChild();
    Node add2 = multiply2.getLastChild();

    assertEquals(Token.CALL_EXPR, foo.getType());
    assertEquals("foo", NodeUtil.getIdentifiedName(foo));
    assertEquals(Token.CALL_EXPR, bar.getType());
    assertEquals(Token.MUL, multiply1.getType());
    assertEquals(Token.EXPR_RESULT, add1.getType());
    assertEquals(Token.PLUS, add1.getFirstChild().getType());
    assertEquals(Token.EXPR_RESULT, add1.getLastChild().getType());
    assertEquals(Token.MUL, multiply2.getType());
    assertEquals(Token.EXPR_RESULT, add2.getType());
    assertEquals(Token.PLUS, add2.getFirstChild().getType());
    assertEquals(Token.EXPR_RESULT, add2.getLastChild().getType());
  }

  private Node createTestNode() {
    Node root = new Node(Token.BLOCK);
    Node expr = new Node(Token.EXPR_STMT);
    Node plus = new Node(Token.PLUS);
    Node leftExpr = new Node(Token.CALL_EXPR, "foo");
    Node rightExpr = new Node(Token.CALL_EXPR, "bar");
    expr.addChildToEnd(new Node(Token.EXPR_STMT, plus));
    plus.addChildToEnd(leftExpr);
    plus.addChildToEnd(rightExpr);
    root.addChildToEnd(expr);
    return root;
  }

  private Node createTestNodeWithSideEffect() {
    Node root = new Node(Token.BLOCK);
    Node expr = new Node(Token.EXPR_STMT);
    Node plus = new Node(Token.PLUS);
    Node leftExpr = new Node(Token.CALL_EXPR, "foo");
    Node rightExpr = new Node(Token.CALL_EXPR, "bar");
    Node sideEffect = new Node(Token.ASSIGN, "x", new Node(Token.NUMBER, 1));
    plus.addChildToEnd(leftExpr);
    plus.addChildToEnd(rightExpr);
    expr.addChildToEnd(new Node(Token.EXPR_STMT, sideEffect));
    expr.addChildToEnd(plus);
    root.addChildToEnd(expr);
    return root;
  }

  private Node createTestNodeWithNestedExpressions() {
    Node root = new Node(Token.BLOCK);
    Node expr = new Node(Token.EXPR_STMT);
    Node plus = new Node(Token.PLUS);
    Node leftExpr = new Node(Token.CALL_EXPR, "foo");
    Node multiply = new Node(Token.MUL);
    Node add1 = new Node(Token.PLUS);
    Node add2 = new Node(Token.PLUS);
    Node leftMultiply = new Node(Token.MUL);
    Node rightMultiply = new Node(Token.CALL_EXPR, "bar");
    add1.addChildToEnd(leftExpr);
    add1.addChildToEnd(rightMultiply);
    leftMultiply.addChildToEnd(new Node(Token.IDENT, "a"));
    leftMultiply.addChildToEnd(add1);
    add2.addChildToEnd(new Node(Token.NUMBER, 2));
    add2.addChildToEnd(leftMultiply);
    multiply.addChildToEnd(add1);
    multiply.addChildToEnd(add2);
    plus.addChildToEnd(leftMultiply);
    plus.addChildToEnd(multiply);
    expr.addChildToEnd(new Node(Token.EXPR_STMT, plus));
    root.addChildToEnd(expr);
    return root;
  }

  private int getExpressionCount(Node node) {
    int count = 0;
    Node current = node;
    while (current != null) {
      if (current.getType() == Token.EXPR_STMT || current.getType() == Token.EXPR_RESULT) {
        count++;
      }
      current = current.getPreviousSibling();
    }
    return count;
  }

  private static class MyCompiler extends AbstractCompiler {
    @Override
    public CompilerPass getPass(String passName) {
      if (RemoveConstantExpressions.class.getName().equals(passName)) {
        return new RemoveConstantExpressions(this);
      }
      return null;
    }
  }
}