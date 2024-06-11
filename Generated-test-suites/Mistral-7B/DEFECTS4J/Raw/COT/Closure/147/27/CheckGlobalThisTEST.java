import static org.junit.Assert.assertFalse;
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
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    Node functionParent = new Node(Token.SCRIPT);
    when(functionParent.getType()).thenReturn(Token.SCRIPT);

    assertTrue("Should traverse a function.", checker.shouldTraverse(mock(NodeTraversal.class), functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node constructorNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    constructorNode.setJSDocInfo(jsDoc);
    Node constructorParent = new Node(Token.SCRIPT);

    assertFalse("Should not traverse a constructor.", checker.shouldTraverse(mock(NodeTraversal.class), constructorNode, constructorParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(true);
    functionNode.setJSDocInfo(jsDoc);
    Node functionParent = new Node(Token.SCRIPT);

    assertFalse("Should not traverse a function with @this annotation.", checker.shouldTraverse(mock(NodeTraversal.class), functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    functionNode.setJSDocInfo(jsDoc);
    Node functionParent = new Node(Token.SCRIPT);

    assertFalse("Should not traverse a function with @override annotation.", checker.shouldTraverse(mock(NodeTraversal.class), functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutParent() {
    Node functionNode = new Node(Token.FUNCTION);

    assertFalse("Should not traverse a function without a parent.", checker.shouldTraverse(mock(NodeTraversal.class), functionNode, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.THIS);
    Node rightSide = new Node(Token.IDENT);

    assertTrue("Should traverse the left side of an assignment.", checker.shouldTraverse(mock(NodeTraversal.class), leftSide, assignNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.IDENT);
    Node rightSide = new Node(Token.GETPROP, new Node(Token.STRING, "prototype"));

    assertFalse("Should not traverse the right side of an assignment to a prototype property.", checker.shouldTraverse(mock(NodeTraversal.class), rightSide, leftSide));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.GETPROP, new Node(Token.IDENT), new Node(Token.STRING, "prototype"));
    Node rightSide = new Node(Token.GETPROP, new Node(Token.IDENT), new Node(Token.STRING, "subProperty"));

    assertFalse("Should not traverse the right side of an assignment to a prototype subproperty.", checker.shouldTraverse(mock(NodeTraversal.class), rightSide, leftSide));
  }

  @Test
  public void testShouldTraverseRightSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.IDENT);
    Node rightSide = new Node(Token.THIS);

    assertTrue("Should traverse the right side of an assignment.", checker.shouldTraverse(mock(NodeTraversal.class), rightSide, assignNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.THIS);
    Node rightSide = new Node(Token.IDENT);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(mock(NodeTraversal.class), leftSide, assignNode);

    compiler.report(nodeCaptor.capture());
    assertFalse("Should report a THIS on the left side of an assignment.", assignLhsChild.equals(leftSide));
    assertNotNull("Should report a diagnostic.", nodeCaptor.getValue());
    assertEquals("Expected a diagnostic of type GLOBAL_THIS.", GLOBAL_THIS, nodeCaptor.getValue().getType());
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node getNode = new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "property"));

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(mock(NodeTraversal.class), getNode, null);

    compiler.report(nodeCaptor.capture());
    assertNull("Should not have assignLhsChild set after visiting a property access.", assignLhsChild);
    assertNotNull("Should report a diagnostic.", nodeCaptor.getValue());
    assertEquals("Expected a diagnostic of type GLOBAL_THIS.", GLOBAL_THIS, nodeCaptor.getValue().getType());
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessViaPrototype() {
    Node getNode = new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.GETPROP, new Node(Token.IDENT), new Node(Token.STRING, "prototype")));

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> typeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(mock(NodeTraversal.class), getNode, null);

    compiler.report(nodeCaptor.capture());
    assertNull("Should not report a diagnostic for a property access via prototype.", nodeCaptor.getValue());
  }
}