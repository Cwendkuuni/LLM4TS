import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.jscomp.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private Node root;
  private RemoveConstantExpressions pass;
  private Result result;

  @Before
  public void setUp() throws Exception {
    compiler = new TestCompiler();
    root = new Node(Token.PROGRAM, Lists.newArrayList());
    pass = new RemoveConstantExpressions(compiler);
    result = new Result();
  }

  @Test
  public void testProcess_NoConstantExpressions() {
    // No constant expressions present in the root node
    pass.process(null, root);
    assertEquals(false, result.changed);
  }

  @Test
  public void testProcess_ConstantExpression_Single() {
    // Const expression with no side effects
    Node constExpr = new Node(Token.EXPR_RESULT, Lists.newArrayList(
        new Node(Token.NUMBER, 5)));
    Node statement = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(constExpr));
    Node body = new Node(Token.BLOCK, Lists.newArrayList(statement));
    root.setBody(body);

    pass.process(null, root);
    Node expectedResult = new Node(Token.EXPR_RESULT, Lists.newArrayList());
    assertEquals(expectedResult, getSideEffectNodes(constExpr).get(0));
    assertEquals(false, result.changed);
  }

  @Test
  public void testProcess_ConstantExpression_Multiple() {
    // Multiple constant expressions
    Node constExpr1 = new Node(Token.EXPR_RESULT, Lists.newArrayList(
        new Node(Token.NUMBER, 5)));
    Node statement1 = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(constExpr1));
    Node constExpr2 = new Node(Token.EXPR_RESULT, Lists.newArrayList(
        new Node(Token.NUMBER, 10)));
    Node statement2 = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(constExpr2));
    Node body = new Node(Token.BLOCK, Lists.newArrayList(
        statement1, statement2));
    root.setBody(body);

    pass.process(null, root);
    List<Node> expectedResult = Lists.newArrayList(
        new Node(Token.EXPR_RESULT, Lists.newArrayList()),
        new Node(Token.EXPR_RESULT, Lists.newArrayList()));
    for (int i = 0; i < getSideEffectNodes(constExpr1).size(); i++) {
      assertEquals(expectedResult.get(i), getSideEffectNodes(constExpr1).get(i));
    }

    for (int i = 0; i < getSideEffectNodes(constExpr2).size(); i++) {
      assertEquals(expectedResult.get(i), getSideEffectNodes(constExpr2).get(i));
    }
    assertEquals(false, result.changed);
  }

  @Test
  public void testProcess_ConstantExpression_SideEffect() {
    // Const expression with side effects
    Node constExpr = new Node(Token.EXPR_RESULT, Lists.newArrayList(
        new Node(Token.IDENT, "a"),
        new Node(Token.ASSIGN, Lists.newArrayList(new Node(Token.IDENT, "a"), new Node(Token.NUMBER, 5))),
        new Node(Token.NUMBER, 10)));
    Node statement = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(constExpr));
    Node body = new Node(Token.BLOCK, Lists.newArrayList(statement));
    root.setBody(body);

    pass.process(null, root);
    Node expectedResult = new Node(Token.ASSIGN, Lists.newArrayList(new Node(Token.IDENT, "a"), new Node(Token.NUMBER, 5)));
    assertEquals(expectedResult, getSideEffectNodes(constExpr).get(0));
    assertEquals(false, result.changed);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_NonExpressionStatement() {
    // Non-expression statement present in the root node
    Node assignment = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        new Node(Token.IDENT, "a"),
        new Node(Token.ASSIGN, Lists.newArrayList(new Node(Token.IDENT, "a"), new Node(Token.NUMBER, 5)))));
    Node body = new Node(Token.BLOCK, Lists.newArrayList(assignment));
    root.setBody(body);

    pass.process(null, root);
  }

  @Test(expected = NullPointerException.class)
  public void testProcess_NullRoot() {
    // Null root node passed
    pass.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullCompiler() {
    // Null compiler passed
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);
    RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantRValuesCallback();
    result = new Result();
    pass = removeConstantExpressions;

    pass.process(compiler, null);
  }
}