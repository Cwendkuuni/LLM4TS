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
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for the CheckGlobalThis class.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void shouldAcceptPrototypeMethod() {
    Node node = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(true);
    jsDoc.setOverride(false);
    node.setJSDocInfo(jsDoc);

    Node prototypeNode = new Node(Token.GETPROP, new int[] {0, 1});
    prototypeNode.getLastChild().setString("prototype");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(prototypeNode);
    when(traversal.getParentNode()).thenReturn(node);

    assertFalse(checker.shouldTraverse(traversal, new Node(Token.ASSIGN, new int[] {0, 1, 2}), null));
  }

  @Test
  public void shouldAcceptFunctionAnnotatedWithConstructor() {
    Node node = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(true);
    jsDoc.setInterface(false);
    jsDoc.setThisType(null);
    jsDoc.setOverride(false);
    node.setJSDocInfo(jsDoc);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(node);

    assertFalse(checker.shouldTraverse(traversal, new Node(Token.ASSIGN, new int[] {0, 1, 2}), null));
  }

  @Test
  public void shouldAcceptFunctionAnnotatedWithThis() {
    Node node = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(true);
    jsDoc.setOverride(false);
    node.setJSDocInfo(jsDoc);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(node);

    assertFalse(checker.shouldTraverse(traversal, new Node(Token.ASSIGN, new int[] {0, 1, 2}), null));
  }

  @Test
  public void shouldAcceptFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node node = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(null);
    jsDoc.setOverride(false);
    node.setJSDocInfo(jsDoc);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(node);

    assertTrue(checker.shouldTraverse(traversal, new Node(Token.ASSIGN, new int[] {0, 1, 2}), null));
  }

  @Test
  public void shouldAcceptFunctionWithNoAssignment() {
    Node node = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(null);
    jsDoc.setOverride(false);
    node.setJSDocInfo(jsDoc);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(node);

    assertFalse(checker.shouldTraverse(traversal, new Node(Token.FUNCTION, new int[] {0}), null));
  }

  @Test
  public void shouldAcceptFunctionNotInScriptOrBlockContext() {
    Node node = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(null);
    jsDoc.setOverride(false);
    node.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.VAR, new int[] {0, 1});
    Node grandParent = new Node(Token.SCRIPT, new int[] {0});

    parent.addChildToEnd(node);
    grandParent.addChildToEnd(parent);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(grandParent);

    assertFalse(checker.shouldTraverse(traversal, node, grandParent));
  }

  @Test
  public void shouldAcceptFunctionGettingLentToPrototype() {
    Node functionNode = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo functionJsDoc = new JSDocInfo();
    functionJsDoc.setIsConstructor(false);
    functionJsDoc.setInterface(false);
    functionJsDoc.setThisType(null);
    functionJsDoc.setOverride(false);
    functionNode.setJSDocInfo(functionJsDoc);

    Node prototypeNode = new Node(Token.GETPROP, new int[] {0, 1});
    prototypeNode.getLastChild().setString("prototype");

    Node prototypePropertyNode = new Node(Token.IDENT, new int[] {0});
    prototypePropertyNode.setString("property");

    prototypeNode.addChildToEnd(prototypePropertyNode);

    Node functionPrototypePropertyNode = new Node(Token.GETPROP, new int[] {0, 1, 2});
    functionPrototypePropertyNode.getLastChild().setString("prototype");

    prototypePropertyNode.addChildToEnd(functionPrototypePropertyNode);

    Node parentNode = new Node(Token.ASSIGN, new int[] {0, 1, 2});

    prototypePropertyNode.addChildToEnd(parentNode);

    Node lhs = parentNode.getFirstChild();
    Node rhs = lhs.getNext();

    if (lhs.getType() == Token.GETPROP) {
      lhs = lhs.getFirstChild();
    }

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(prototypeNode);

    when(traversal.getParentNode()).thenReturn(functionNode);
    when(traversal.getParentNode(1)).thenReturn(prototypeNode);

    assertFalse(checker.shouldTraverse(traversal, rhs, lhs));
  }

  @Test
  public void shouldReportGlobalThisOnLeftSideOfAssignment() {
    Node node = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(null);
    jsDoc.setOverride(false);
    node.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.ASSIGN, new int[] {0, 1, 2});
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();

    lhs = lhs.getFirstChild();

    Node thisNode = new Node(Token.THIS, new int[] {0});

    lhs.setNext(thisNode);

    parent.addChildToEnd(lhs);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(thisNode);

    checker.visit(traversal, thisNode, parent);

    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> checkLevelArgumentCaptor = ArgumentCaptor.forClass(CheckLevel.class);

    verify(compiler).report(traversal.makeError(thisNode, checkLevelArgumentCaptor.capture(),
        diagnosticTypeArgumentCaptor.capture()));

    assertEquals(CheckLevel.WARNING, checkLevelArgumentCaptor.getValue());
    assertEquals(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"),
        diagnosticTypeArgumentCaptor.getValue());
  }

  @Test
  public void shouldReportGlobalThisOnLeftSideOfPropertyAccess() {
    Node node = new Node(Token.FUNCTION, new int[] {0});
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(null);
    jsDoc.setOverride(false);
    node.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.ASSIGN, new int[] {0, 1, 2});
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();

    Node lhsParent = lhs.getParent();

    Node thisNode = new Node(Token.THIS, new int[] {0});

    Node getPropNode = new Node(Token.GETPROP, new int[] {0, 1});
    getPropNode.getLastChild().setString("prototype");

    Node getPropThisNode = new Node(Token.THIS, new int[] {0});

    getPropNode.addChildToEnd(getPropThisNode);

    lhs.setNext(getPropNode);

    parent.addChildToEnd(lhs);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(getPropThisNode);

    checker.visit(traversal, thisNode, lhsParent);

    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> checkLevelArgumentCaptor = ArgumentCaptor.forClass(CheckLevel.class);

    verify(compiler).report(traversal.makeError(thisNode, checkLevelArgumentCaptor.capture(),
        diagnosticTypeArgumentCaptor.capture()));

    assertEquals(CheckLevel.WARNING, checkLevelArgumentCaptor.getValue());
    assertEquals(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"),
        diagnosticTypeArgumentCaptor.getValue());
  }
}