package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
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

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_functionWithNoAnnotation() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = new Node(Token.SCRIPT, 1, null);

    when(compiler.report(traversal.makeError(node, level, DiagnosticType.warning(null, null)))).thenReturn(null);

    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_prototypeMethod() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("prototype", new Node(Token.GETPROP, 1, node));

    Node parent = new Node(Token.BLOCK, 1, null);
    parent.addChildToEnd(node);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_constructor() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("constructor", new Node(Token.TRUE, 1, null));

    Node parent = new Node(Token.SCRIPT, 1, null);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_methodWithThisAnnotation() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("@this", new Node(Token.STRING_LITERAL, 1, ""));

    Node parent = new Node(Token.SCRIPT, 1, null);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithNoLogicalPlaceToPutThisAnnotation() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);

    Node parent = new Node(Token.SCRIPT, 1, null);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);

    // Since this check does not track assignments of this to variables or objects,
    // this test only checks the possibility of traverse the function without this annotation.
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_leftSideOfAssignment() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node leftSide = new Node(Token.THIS, 1, null);
    Node rightSide = new Node(Token.NUMBER, 1, "1");
    Node assignNode = new Node(Token.ASSIGN, 1, null);

    assignNode.addChildToEnd(leftSide);
    assignNode.addChildToEnd(rightSide);

    Node parent = new Node(Token.SCRIPT, 1, null);
    parent.addChildToEnd(assignNode);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checkGlobalThis.shouldTraverse(traversal, leftSide, parent);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_rightSideOfAssignment() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node leftSide = new Node(Token.THIS, 1, null);
    Node rightSide = new Node(Token.THIS, 1, null);
    Node assignNode = new Node(Token.ASSIGN, 1, null);

    assignNode.addChildToEnd(leftSide);
    assignNode.addChildToEnd(rightSide);

    Node parent = new Node(Token.SCRIPT, 1, null);
    parent.addChildToEnd(assignNode);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checkGlobalThis.shouldTraverse(traversal, rightSide, parent);

    // Should report a warning because the assignment is on the left side of this.
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThis_functionWithNoAnnotation() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    Node parent = new Node(Token.SCRIPT, 1, null);
    parent.addChildToEnd(node);

    Node thisNode = new Node(Token.THIS, 1, null);
    node.addChildToEnd(thisNode);

    when(compiler.report(traversal.makeError(thisNode, level, DiagnosticType.warning(null, null)))).thenReturn(null);

    checkGlobalThis.visit(traversal, node, parent);

    // Should report a warning because there is no valid context for this.
    assertNull(assignLhsChild);
  }

  @Test
  public void testShouldReportThis_functionWithPrototypePropertyAccess() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("prototype", new Node(Token.GETPROP, 1, node));

    Node parent = new Node(Token.SCRIPT, 1, null);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    Node thisNode = new Node(Token.THIS, 1, null);
    node.addChildToEnd(thisNode);

    Node prototypeProperty = new Node(Token.GETPROP, 1, thisNode);
    thisNode.addChildToEnd(prototypeProperty);

    when(compiler.report(traversal.makeError(prototypeProperty, level, DiagnosticType.warning(null, null)))).thenReturn(null);

    checkGlobalThis.visit(traversal, node, parent);

    // Should not report a warning because this is inside a prototype property access.
    assertNull(assignLhsChild);
  }

  @Test
  public void testShouldReportThis_functionWithPrototypePropertyAccessDeep() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("prototype", new Node(Token.GETPROP, 1, node));

    Node parent = new Node(Token.SCRIPT, 1, null);
    parent.addChildToEnd(node);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    Node thisNode = new Node(Token.THIS, 1, null);
    node.addChildToEnd(thisNode);

    Node prototypeProperty = new Node(Token.GETPROP, 1, thisNode);
    prototypeProperty.setProperty("prototype", new Node(Token.GETPROP, 1, prototypeProperty));
    prototypeProperty.setProperty("prototype", new Node(Token.GETPROP, 1, prototypeProperty));
    thisNode.addChildToEnd(prototypeProperty);

    when(compiler.report(traversal.makeError(prototypeProperty, level, DiagnosticType.warning(null, null)))).thenReturn(null);

    checkGlobalThis.visit(traversal, node, parent);

    // Should not report a warning because this is inside a prototype property access.
    assertNull(assignLhsChild);
  }

  @Test
  public void testShouldReportThis_functionWithPropertyAccess() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);

    Node parent = new Node(Token.SCRIPT, 1, null);
    parent.addChildToEnd(node);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);

    Node thisNode = new Node(Token.THIS, 1, null);
    node.addChildToEnd(thisNode);

    Node propertyAccess = new Node(Token.GETPROP, 1, thisNode);
    propertyAccess.setString("property");
    thisNode.addChildToEnd(propertyAccess);

    when(compiler.report(traversal.makeError(propertyAccess, level, DiagnosticType.warning(null, null)))).thenReturn(null);

    checkGlobalThis.visit(traversal, node, parent);

    // Should report a warning because this is not inside a prototype property access.
    assertNull(assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);

    when(node.getJSDocInfo()).thenReturn(null);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

    assertThat(jsDocInfo).isNull();
  }

  @Test
  public void testGetFunctionJsDocInfo_functionWithName() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("name", new Node(Token.IDENT, 1, "myFunction"));

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

    assertThat(jsDocInfo).isNull();
  }

  @Test
  public void testGetFunctionJsDocInfo_functionWithNameAndConstructorAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("name", new Node(Token.IDENT, 1, "myFunction"));
    node.setProperty("constructor", new Node(Token.TRUE, 1, null));

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

    assertThat(jsDocInfo).isNotNull();
  }

  @Test
  public void testGetFunctionJsDocInfo_functionWithNameAndThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("name", new Node(Token.IDENT, 1, "myFunction"));
    node.setProperty("@this", new Node(Token.STRING_LITERAL, 1, ""));

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

    assertThat(jsDocInfo).isNotNull();
  }

  @Test
  public void testGetFunctionJsDocInfo_functionWithNameAndOverrideAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);
    node.setProperty("name", new Node(Token.IDENT, 1, "myFunction"));
    node.setProperty("@override", new Node(Token.TRUE, 1, null));

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

    assertThat(jsDocInfo).isNull();
  }

  @Test
  public void testGetFunctionJsDocInfo_functionWithoutName() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.FUNCTION, 1, null);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

    assertThat(jsDocInfo).isNull();
  }

  @Test
  public void testGetFunctionJsDocInfo_functionWithVarKeyword() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    Node node = new Node(Token.VAR, 1, null);
    node.setProperty("value", new Node(Token.FUNCTION, 1, null));

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node.getFirstChild());

    assertThat(jsDocInfo).isNull();
  }
}