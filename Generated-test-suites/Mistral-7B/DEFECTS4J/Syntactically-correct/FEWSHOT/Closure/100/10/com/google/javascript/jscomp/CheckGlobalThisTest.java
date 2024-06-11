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
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionWithoutJsDoc() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("foo");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("foo", new Node[0], Node.newBlock(new Node[0]));
    JSDocInfo jsDoc = Node.newJsDocInfo(new Node[]{"@constructor"});

    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);
    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, null);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("foo", new Node[0], Node.newBlock(new Node[0]));
    JSDocInfo jsDoc = Node.newJsDocInfo(new Node[]{"@this"});

    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);
    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, null);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnnotationOrPrototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("foo", new Node[0], Node.newBlock(new Node[0]));
    JSDocInfo jsDoc = Node.newJsDocInfo();

    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);
    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("foo", new Node[0], Node.newBlock(new Node[0]));
    Node prototypeProperty = Node.newGetProp(functionNode, "prototype");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldTraverse(traversal, prototypeProperty, functionNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeSubProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("foo", new Node[0], Node.newBlock(new Node[0]));
    Node prototypeProperty = Node.newGetProp(functionNode, "prototype");
    Node prototypeSubProperty = Node.newGetProp(prototypeProperty, "subProperty");

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldTraverse(traversal, prototypeSubProperty, functionNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseAssignmentToNonPrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignNode = Node.newAssign(Node.newIdent("a"), Node.newThis());

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldTraverse(traversal, assignNode, null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignNode = Node.newAssign(Node.newIdent("a"), Node.newThis());

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldReportThis(assignNode, null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignNode = Node.newAssign(Node.newIdent("a"), Node.newInt(1));
    Node thisNode = Node.newThis();

    boolean result = checkGlobalThis.shouldReportThis(assignNode, thisNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getPropNode = Node.newGetProp(Node.newIdent("a"), "property");
    Node thisNode = Node.newThis();

    boolean result = checkGlobalThis.shouldReportThis(getPropNode, thisNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThisOnRightSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node getPropNode = Node.newGetProp(Node.newIdent("a"), "property");
    Node thisNode = Node.newThis();

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.any(CheckLevel.class), Mockito.any(DiagnosticType.class)))
        .thenReturn(null);

    boolean result = checkGlobalThis.shouldReportThis(getPropNode, null);

    assertThat(result).isFalse();

    checkGlobalThis.visit(traversal, getPropNode, null);

    Node diagnostic = traversal.getCurrentError();

    assertThat(diagnostic).isNotNull();
    assertThat(diagnostic.getDiagnosticType()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testShouldReturnNullForNullNode() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = null;

    assertNull(checkGlobalThis.stripLeadingHyphens(node));
  }
}