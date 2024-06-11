import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeTraversal = mock(NodeTraversal.class);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = createNode("function MyConstructor() {}", null);
    when(nodeTraversal.getCurrentNode()).thenReturn(functionNode);
    Node jsDocInfo = createNode("", createNode("@constructor"));
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, null, null));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node functionNode = createNode("function MyFunction() {}", null);
    when(nodeTraversal.getCurrentNode()).thenReturn(functionNode);
    Node jsDocInfo = createNode("", createNode("@this"));
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, null, null));
  }

  @Test
  public void testShouldTraverseFunctionWhereNoLogicalPlaceToPutThis() {
    Node functionNode = createNode("function MyFunction() {}", null);
    when(nodeTraversal.getCurrentNode()).thenReturn(functionNode);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutJsDoc() {
    Node functionNode = createNode("function MyFunction() {}", null);
    when(nodeTraversal.getCurrentNode()).thenReturn(functionNode);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, null, null));
  }

  @Test
  public void testShouldTraverseAssignLeftSide() {
    Node assignNode = createNode("a =", createNode("this"));
    Node leftHandSide = assignNode.getFirstChild();

    when(nodeTraversal.getCurrentNode()).thenReturn(leftHandSide);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, null, null));
  }

  @Test
  public void testShouldNotTraverseAssignRightSide() {
    Node assignNode = createNode("a =", createNode("this"));
    Node rightHandSide = assignNode.getLastChild();

    when(nodeTraversal.getCurrentNode()).thenReturn(rightHandSide);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, null, null));
  }

  @Test
  public void testShouldNotTraverseAssignRightSideWithPrototypeProperty() {
    Node assignNode = createNode("a.prototype.property =", createNode("this"));
    Node rightHandSide = assignNode.getLastChild();

    when(nodeTraversal.getCurrentNode()).thenReturn(rightHandSide);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, null, null));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssign() {
    Node assignNode = createNode("a =", createNode("this"));
    Node leftHandSide = assignNode.getFirstChild();

    when(nodeTraversal.getCurrentNode()).thenReturn(leftHandSide);

    assertTrue(checkGlobalThis.shouldReportThis(leftHandSide, assignNode));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssign() {
    Node assignNode = createNode("a =", createNode("this"));
    Node rightHandSide = assignNode.getLastChild();

    when(nodeTraversal.getCurrentNode()).thenReturn(rightHandSide);

    assertFalse(checkGlobalThis.shouldReportThis(rightHandSide, assignNode));
  }

  private Node createNode(String text, Node... children) {
    Node node = mock(Node.class);
    when(node.getText()).thenReturn(text);
    when(node.getChildren()).thenReturn(children);
    return node;
  }
}