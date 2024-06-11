import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compilerMock;

  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
  }

  @Test
  public void shouldNotTraverseNonGlobalContexts() {
    Node nonGlobalContext = new Node(Token.FUNCTION, null);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = mock(Node.class);

    when(compilerMock.report(any(NodeTraversal.class), any(Node.class), any(CheckLevel.class),
        any(DiagnosticType.class))).thenReturn(null);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, nonGlobalContext, parent));
  }

  @Test
  public void shouldTraverseGlobalContexts() {
    Node globalContext = new Node(Token.SCRIPT, null);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = mock(Node.class);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, globalContext, parent));
  }

  @Test
  public void shouldTraverseFunctionsWithNoJSDocInfo() {
    Node functionNode = new Node(Token.FUNCTION, null);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = mock(Node.class);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void shouldTraverseFunctionsWithConstructorJSDocInfo() {
    Node constructorNode = new Node(Token.FUNCTION, null);
    JSDocInfo constructorJsDocInfo = new JSDocInfo("@constructor");
    constructorNode.setJSDocInfo(constructorJsDocInfo);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = mock(Node.class);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, constructorNode, parent));
  }

  @Test
  public void shouldTraverseFunctionsWithThisJSDocInfo() {
    Node thisAnnotatedNode = new Node(Token.FUNCTION, null);
    JSDocInfo thisAnnotatedJsDocInfo = new JSDocInfo("@this");
    thisAnnotatedNode.setJSDocInfo(thisAnnotatedJsDocInfo);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = mock(Node.class);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, thisAnnotatedNode, parent));
  }

  @Test
  public void shouldTraverseFunctionsWithNoLogicalPlaceToPutThisAnnotation() {
    Node noLogicalPlaceNode = new Node(Token.FUNCTION, null);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = mock(Node.class);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, noLogicalPlaceNode, parent));
  }

  @Test
  public void shouldTraverseLeftSideOfAssignment() {
    Node assignParent = new Node(Token.ASSIGN, null);
    Node assignLeftSide = new Node(Token.THIS, null);
    Node assignRightSide = new Node(Token.NUMBER, "1");

    NodeTraversal traversal = mock(NodeTraversal.class);

    checkGlobalThis.visit(traversal, assignLeftSide, assignParent);
  }

  @Test
  public void shouldReportThisOnLeftSideOfAssignment() {
    Node assignParent = new Node(Token.ASSIGN, null);
    Node assignLeftSide = new Node(Token.THIS, null);
    Node assignRightSide = new Node(Token.NUMBER, "1");

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.report(any(NodeTraversal.class), any(Node.class), any(CheckLevel.class),
        any(DiagnosticType.class))).thenReturn(new DiagnosticPosition());

    checkGlobalThis.visit(traversal, assignLeftSide, assignParent);

    NodeUtil.reportErrors(compilerMock);
  }

  @Test
  public void shouldNotReportThisOnRightSideOfAssignment() {
    Node assignParent = new Node(Token.ASSIGN, null);
    Node assignLeftSide = new Node(Token.NUMBER, "1");
    Node assignRightSide = new Node(Token.THIS, null);

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.report(any(NodeTraversal.class), any(Node.class), any(CheckLevel.class),
        any(DiagnosticType.class))).thenReturn(new DiagnosticPosition());

    checkGlobalThis.visit(traversal, assignRightSide, assignParent);

    assertTrue(NodeUtil.isEmptyDiagnosticMessages(compilerMock));
  }

  @Test
  public void shouldReportThisOnPropertyAccess() {
    Node propertyAccessParent = new Node(Token.GETPROP, null);
    Node propertyAccessThis = new Node(Token.THIS, null);
    Node propertyAccessProperty = new Node(Token.IDENTIFIER, "property");

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.report(any(NodeTraversal.class), any(Node.class), any(CheckLevel.class),
        any(DiagnosticType.class))).thenReturn(new DiagnosticPosition());

    checkGlobalThis.visit(traversal, propertyAccessThis, propertyAccessParent);

    NodeUtil.reportErrors(compilerMock);
  }

  @Test
  public void shouldNotReportThisOnPrototypePropertyAccess() {
    Node prototypePropertyAccessParent = new Node(Token.GETPROP, null);
    Node prototypePropertyAccessThis = new Node(Token.THIS, null);
    Node prototypePropertyAccessPrototype = new Node(Token.GETPROP, null);
    Node prototypePropertyAccessProperty = new Node(Token.IDENTIFIER, "prototype");

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.report(any(NodeTraversal.class), any(Node.class), any(CheckLevel.class),
        any(DiagnosticType.class))).thenReturn(new DiagnosticPosition());

    checkGlobalThis.visit(traversal, prototypePropertyAccessThis, prototypePropertyAccessParent);

    assertTrue(NodeUtil.isEmptyDiagnosticMessages(compilerMock));
  }

  @Test
  public void shouldReportThisOnNestedPropertyAccess() {
    Node nestedPropertyAccessParent = new Node(Token.GETPROP, null);
    Node nestedPropertyAccessThis = new Node(Token.THIS, null);
    Node nestedPropertyAccessPrototype = new Node(Token.GETPROP, null);
    Node nestedPropertyAccessPrototypePrototype = new Node(Token.GETPROP, null);
    Node nestedPropertyAccessProperty = new Node(Token.IDENTIFIER, "property");

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.report(any(NodeTraversal.class), any(Node.class), any(CheckLevel.class),
        any(DiagnosticType.class))).thenReturn(new DiagnosticPosition());

    checkGlobalThis.visit(traversal, nestedPropertyAccessThis, nestedPropertyAccessParent);

    NodeUtil.reportErrors(compilerMock);
  }

  @Test
  public void shouldNotReportThisOnNestedPrototypePropertyAccess() {
    Node nestedPrototypePropertyAccessParent = new Node(Token.GETPROP, null);
    Node nestedPrototypePropertyAccessThis = new Node(Token.THIS, null);
    Node nestedPrototypePropertyAccessPrototype = new Node(Token.GETPROP, null);
    Node nestedPrototypePropertyAccessPrototypePrototype = new Node(Token.GETPROP, null);
    Node nestedPrototypePropertyAccessProperty = new Node(Token.IDENTIFIER, "property");

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.report(any(NodeTraversal.class), any(Node.class), any(CheckLevel.class),
        any(DiagnosticType.class))).thenReturn(new DiagnosticPosition());

    checkGlobalThis.visit(traversal, nestedPrototypePropertyAccessThis, nestedPrototypePropertyAccessParent);

    assertTrue(NodeUtil.isEmptyDiagnosticMessages(compilerMock));
  }
}