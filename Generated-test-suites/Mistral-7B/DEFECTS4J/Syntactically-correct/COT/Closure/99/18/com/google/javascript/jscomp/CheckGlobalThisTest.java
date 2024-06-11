import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.common.base.Preconditions.checkState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {
  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;
  private Node root;

  @Mock
  private DiagnosticReporter reporter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = new AbstractCompiler(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, reporter, false);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(compiler, null);
    root = new Node(Token.SCRIPT, Node.EMPTY_CHILDREN, null);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    checker.shouldTraverse(traversal, new Node(Token.FUNCTION, Node.EMPTY_CHILDREN, root), new Node(Token.BLOCK, Node.EMPTY_CHILDREN, root));
    assertFalse(checker.shouldTraverse(traversal, new Node(Token.FUNCTION, Node.EMPTY_CHILDREN, root), new Node(Token.VAR, Node.EMPTY_CHILDREN, root)));
    assertFalse(checker.shouldTraverse(traversal, new Node(Token.FUNCTION, Node.EMPTY_CHILDREN, root), new Node(Token.NAME, Node.EMPTY_CHILDREN, root)));
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnnotation() {
    checker.shouldTraverse(traversal, new Node(Token.FUNCTION, Node.EMPTY_CHILDREN, root), root);
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeProperty() {
    Node prototypeNode = new Node(Token.GETPROP, new Node(Token.THIS, Node.EMPTY_CHILDREN, root), new Node(Token.IDENT, "prototype", Node.EMPTY_CHILDREN, root));
    Node prototypeFunction = new Node(Token.FUNCTION, Node.EMPTY_CHILDREN, prototypeNode);
    checker.shouldTraverse(traversal, prototypeFunction, root);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN, null, new Node(Token.THIS, Node.EMPTY_CHILDREN, root), new Node(Token.NAME, "a", Node.EMPTY_CHILDREN, root));
    Node nodeToCheck = new Node(TOKEN_NULL, Node.EMPTY_CHILDREN, assignNode);
    checker.visit(traversal, traversal.traverse(nodeToCheck), assignNode);
    assertTrue(compiler.reportCount() > 0);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN, Node.EMPTY_CHILDREN, new Node(Token.NAME, "a", Node.EMPTY_CHILDREN, root), new Node(Token.THIS, Node.EMPTY_CHILDREN, root));
    Node nodeToCheck = new Node(TOKEN_NULL, Node.EMPTY_CHILDREN, assignNode);
    checker.visit(traversal, traversal.traverse(nodeToCheck), assignNode);
    checkState(compiler.reportCount() == 0, "No reports expected for this test");
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node getPropNode = new Node(Token.GETPROP, new Node(Token.THIS, Node.EMPTY_CHILDREN, root), new Node(Token.IDENT, "prototype", Node.EMPTY_CHILDREN, root));
    Node nodeToCheck = new Node(TOKEN_NULL, Node.EMPTY_CHILDREN, getPropNode);
    checker.visit(traversal, traversal.traverse(nodeToCheck), getPropNode.getParent());
    assertTrue(compiler.reportCount() > 0);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    Node getPropNode = new Node(Token.GETPROP, new Node(Token.IDENT, "a", Node.EMPTY_CHILDREN, root), new Node(Token.THIS, Node.EMPTY_CHILDREN, root));
    Node nodeToCheck = new Node(TOKEN_NULL, Node.EMPTY_CHILDREN, getPropNode);
    checker.visit(traversal, traversal.traverse(nodeToCheck), getPropNode.getParent());
    checkState(compiler.reportCount() == 0, "No reports expected for this test");
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisOnLeftSideOfAssignmentWithNoParent() {
    Node assignNode = new Node(Token.ASSIGN, Node.EMPTY_CHILDREN, new Node(Token.THIS, Node.EMPTY_CHILDREN, root), new Node(Token.NAME, "a", Node.EMPTY_CHILDREN, root));
    Node nodeToCheck = new Node(TOKEN_NULL, Node.EMPTY_CHILDREN, assignNode);
    checker.visit(traversal, null, assignNode);
  }

  @Test
  public void testShouldIgnoreThisInsideFunctionAnnotatedWithConstructor() {
    Node constructorNode = new Node(Token.FUNCTION, Node.EMPTY_CHILDREN, root, new Node(Token.IS_NEW, Node.EMPTY_CHILDREN, root), null);
    Node nodeToCheck = new Node(Token.THIS, Node.EMPTY_CHILDREN, constructorNode);
    checker.visit(traversal, traversal.traverse(nodeToCheck), constructorNode);
    checkState(compiler.reportCount() == 0, "No reports expected for this test");
  }

  @Test
  public void testShouldIgnoreThisInsideFunctionAnnotatedWithThis() {
    Node thisAnnotatedNode = new Node(Token.FUNCTION, Node.EMPTY_CHILDREN, root, null, new Node(Token.IDENT, "@this", Node.EMPTY_CHILDREN, root));
    Node nodeToCheck = new Node(Token.THIS, Node.EMPTY_CHILDREN, thisAnnotatedNode);
    checker.visit(traversal, traversal.traverse(nodeToCheck), thisAnnotatedNode);
    checkState(compiler.reportCount() == 0, "No reports expected for this test");
  }

  @Test
  public void testShouldIgnoreThisInsideFunctionWithNoAnnotation() {
    Node nodeToCheck = new Node(Token.THIS, Node.EMPTY_CHILDREN, new Node(Token.FUNCTION, Node.EMPTY_CHILDREN, root));
    checker.visit(traversal, traversal.traverse(nodeToCheck), nodeToCheck.getParent());
    checkState(compiler.reportCount() == 0, "No reports expected for this test");
  }
}