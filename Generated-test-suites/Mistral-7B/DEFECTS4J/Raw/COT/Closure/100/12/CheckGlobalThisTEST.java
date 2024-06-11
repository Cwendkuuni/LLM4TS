import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node functionNode = Node.newFunctionNode(null, null, null, null, null, null);
    assertTrue(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.any(Node.class)));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    JSDocInfo jsDoc = Node.newJsDocInfo("constructor", null, null, null, null);
    Node functionNode = Node.newFunctionNode(null, jsDoc, null, null, null, null);
    assertFalse(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.any(Node.class)));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    JSDocInfo jsDoc = Node.newJsDocInfo(null, null, "this", null, null);
    Node functionNode = Node.newFunctionNode(null, jsDoc, null, null, null, null);
    assertFalse(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.any(Node.class)));
  }

  @Test
  public void testShouldTraverseNonFunctionNode() {
    Node node = Node.newExpressionStatementNode(Token.KEYWORD, "x = 10");
    assertTrue(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(node), ArgumentMatchers.any(Node.class)));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node parentNode = Node.newAssignmentStatementNode(Token.ASSIGN, Token.THIS, Node.newIdentifier("a"));
    Node childNode = Node.newIdentifier("b");
    when(compiler.report(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(ArgumentMatchers.isA(Node.class)), ArgumentMatchers.eq(CheckLevel.WARNING), ArgumentMatchers.eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS"))))
        .thenReturn(null);

    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(parentNode), ArgumentMatchers.any(Node.class));
    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(childNode), ArgumentMatchers.any(Node.class));

    assertTrue(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node parentNode = Node.newAssignmentStatementNode(Token.ASSIGN, Node.newIdentifier("a"), Token.THIS);
    Node childNode = Node.newIdentifier("b");

    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(parentNode), ArgumentMatchers.any(Node.class));
    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(childNode), ArgumentMatchers.any(Node.class));

    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnPrototypeProperty() {
    Node parentNode = Node.newAssignmentStatementNode(Token.ASSIGN, Node.newIdentifier("a.prototype.x"), Token.THIS);
    Node childNode = Node.newIdentifier("b");

    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(parentNode), ArgumentMatchers.any(Node.class));
    checker.visit(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(childNode), ArgumentMatchers.any(Node.class));

    assertFalse(compiler.reportWasCalled());
  }
}