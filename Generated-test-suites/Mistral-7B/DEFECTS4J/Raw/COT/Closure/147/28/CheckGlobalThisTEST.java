import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(checker, null, null);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    Node jsDocInfo = new JSDocInfo("myFunction");
    jsDocInfo.setConstructor(true);

    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);
    assertFalse(checker.shouldTraverse(traversal, new Node(Token.FUNCTION), null));
  }

  @Test
  public void testShouldTraverseFunctionWithThisType() {
    Node functionNode = new Node(Token.FUNCTION);
    Node jsDocInfo = new JSDocInfo("myFunction");
    jsDocInfo.setThisType("MyType");

    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);
    assertFalse(checker.shouldTraverse(traversal, new Node(Token.FUNCTION), null));
  }

  @Test
  public void testShouldTraverseFunctionWithInterface() {
    Node functionNode = new Node(Token.FUNCTION);
    Node jsDocInfo = new JSDocInfo("myFunction");
    jsDocInfo.setInterface(true);

    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);
    assertFalse(checker.shouldTraverse(traversal, new Node(Token.FUNCTION), null));
  }

  @Test
  public void testShouldTraverseFunctionWithOverride() {
    Node functionNode = new Node(Token.FUNCTION);
    Node jsDocInfo = new JSDocInfo("myFunction");
    jsDocInfo.setOverride(true);

    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);
    assertFalse(checker.shouldTraverse(traversal, new Node(Token.FUNCTION), null));
  }

  @Test
  public void testShouldTraverseFunctionInScriptContext() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = functionNode;

    when(functionNode.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(scriptNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, scriptNode));
  }

  @Test
  public void testShouldTraverseFunctionInPrototypePropertyContext() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeNode = new Node(Token.GETPROP, new Node(Token.NAME, "prototype"), null);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "a"), prototypeNode);
    Node rhs = functionNode;

    when(functionNode.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(new Node(Token.BLOCK));
    when(prototypeNode.getParent()).thenReturn(new Node(Token.GETPROP, new Node(Token.NAME, "b"), null));

    assertFalse(checker.shouldTraverse(traversal, functionNode, parent.getParent()));
  }

  @Test
  public void testShouldTraverseFunctionInPrototypeSubPropertyContext() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeNode = new Node(Token.GETPROP, new Node(Token.NAME, "prototype"), null);
    Node prototypeSubNode = new Node(Token.GETPROP, new Node(Token.NAME, "subProperty"), null);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "a"), prototypeNode);
    Node rhs = functionNode;

    when(functionNode.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(new Node(Token.BLOCK));
    when(prototypeNode.getParent()).thenReturn(new Node(Token.GETPROP, new Node(Token.NAME, "b"), null));
    when(prototypeSubNode.getParent()).thenReturn(prototypeNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parent.getParent()));
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.THIS);

    assertFalse(checker.shouldTraverse(traversal, rhs, assignNode));
    assertTrue(checker.shouldReportThis(rhs, assignNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.NAME, "b");

    assertFalse(checker.shouldTraverse(traversal, rhs, assignNode));
    assertNull(checker.assignLhsChild);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.THIS);

    assertTrue(checker.shouldReportThis(rhs, assignNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node parentNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "a"), null);
    Node rhs = new Node(Token.THIS);

    assertTrue(checker.shouldReportThis(rhs, parentNode));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnLeftSideOfAssignmentWithNestedAssignments() {
    Node assignNode1 = new Node(Token.ASSIGN);
    Node assignNode2 = new Node(Token.ASSIGN);
    Node assignLhsChild1 = new Node(Token.NAME, "a");
    Node assignLhsChild2 = new Node(Token.NAME, "b");
    Node rhs = new Node(Token.THIS);

    when(assignNode1.getFirstChild()).thenReturn(assignLhsChild1);
    when(assignNode2.getParent()).thenReturn(assignNode1);
    when(assignNode2.getFirstChild()).thenReturn(assignLhsChild2);

    checker.shouldTraverse(traversal, rhs, assignNode2);
  }
}