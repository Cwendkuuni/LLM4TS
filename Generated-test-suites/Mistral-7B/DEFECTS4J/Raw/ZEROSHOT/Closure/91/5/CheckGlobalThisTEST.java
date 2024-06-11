import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testShouldNotTraverseNonGlobalContexts() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    when(node.getParent()).thenReturn(parent);
    when(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(null, null))).thenReturn(new Object());

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldTraversePrototypeMethods() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    Node prototype = new Node(Token.STRING, "prototype");

    when(node.getParent()).thenReturn(parent);
    when(parent.getLastChild()).thenReturn(prototype);
    when(prototype.getString()).thenReturn("prototype");

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(true, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldTraverseFunctionsWithNoLogicalPlaceForThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    when(node.getParent()).thenReturn(parent);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(true, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldNotTraverseAssignedPrototypeProperties() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node prototype = new Node(Token.STRING, "prototype");

    when(node.getParent()).thenReturn(parent);
    when(parent.getLastChild()).thenReturn(prototype);
    when(prototype.getString()).thenReturn("prototype");

    boolean shouldTraverse = checker.shouldReportThis(node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssign() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node assignLhsChild = new Node(Token.THIS);

    when(node.getParent()).thenReturn(parent);
    when(parent.getFirstChild()).thenReturn(assignLhsChild);

    boolean shouldTraverse = checker.shouldReportThis(node, parent);
    assertEquals(true, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    when(node.getParent()).thenReturn(parent);

    boolean shouldTraverse = checker.shouldReportThis(node, parent);
    assertEquals(true, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldNotReportThisOnRightSideOfAssign() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node assignRhs = new Node(Token.NUMBER, 1);

    when(node.getParent()).thenReturn(parent);
    when(parent.getLastChild()).thenReturn(assignRhs);

    boolean shouldTraverse = checker.shouldReportThis(node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldNotReportThisIfInsidePrototypeMethod() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node prototype = new Node(Token.STRING, "prototype");
    Node prototypeMethod = new Node(Token.FUNCTION, null);

    when(node.getParent()).thenReturn(parent);
    when(parent.getLastChild()).thenReturn(prototype);
    when(prototype.getString()).thenReturn("prototype");
    when(prototype.getFirstChild()).thenReturn(prototypeMethod);
    when(prototypeMethod.getFirstChild()).thenReturn(node);

    boolean shouldTraverse = checker.shouldReportThis(node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnFunctionWithNoAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, null);
    assertEquals(true, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);

    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnFunctionAnnotatedWithThis() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThis(true);

    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnFunctionAnnotatedWithInterface() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setInterface(true);

    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnFunctionAnnotatedWithOverride() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);

    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnObjectLiteral() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node objectLiteral = new Node(Token.OBJECT_LITERAL, null);

    when(node.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(objectLiteral);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(true, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnObjectLiteralKey() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node objectLiteral = new Node(Token.OBJECT_LITERAL, null);
    Node key = new Node(Token.STRING, "key");

    when(node.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(objectLiteral);
    when(parent.getLastChild()).thenReturn(key);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(true, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldNotReportThisOnAssignedPrototypePropertyKey() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node prototype = new Node(Token.STRING, "prototype");
    Node prototypeMethod = new Node(Token.OBJECT_LITERAL, null);
    Node key = new Node(Token.STRING, "key");

    when(node.getParent()).thenReturn(parent);
    when(parent.getLastChild()).thenReturn(prototype);
    when(prototype.getString()).thenReturn("prototype");
    when(prototype.getFirstChild()).thenReturn(prototypeMethod);
    when(prototypeMethod.getFirstChild()).thenReturn(parent);
    when(parent.getLastChild()).thenReturn(key);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldReportThisOnFunctionAnnotatedWithCustomAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setAnnotation(new Node(Token.STRING, "customAnnotation"));

    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, shouldTraverse);
  }

  @org.junit.Test
  public void testShouldNotReportThisOnFunctionAnnotatedWithMultipleAnnotations() {
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setAnnotation(new Node(Token.STRING, "customAnnotation1"));
    jsDoc.setAnnotation(new Node(Token.STRING, "customAnnotation2"));

    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    boolean shouldTraverse = checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, shouldTraverse);
  }
}