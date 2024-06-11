package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testVisitFunctionNoJsDoc() {
    // Arrange
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of());

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    assertThat(compiler.reportedDiagnostics).isEmpty();
  }

  @Test
  public void testVisitFunctionWithConstructorAnnotation() {
    // Arrange
    JSDocInfo jsDoc = Node.newJSDocInfo(Token.JSDOC, "constructor");
    Node functionNode = Node.newFunction(Token.FUNCTION, jsDoc, ImmutableList.of());

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    assertThat(compiler.reportedDiagnostics).isEmpty();
  }

  @Test
  public void testVisitFunctionWithThisAnnotation() {
    // Arrange
    JSDocInfo jsDoc = Node.newJSDocInfo(Token.JSDOC, "this", ImmutableList.of());
    Node functionNode = Node.newFunction(Token.FUNCTION, jsDoc, ImmutableList.of());

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    assertThat(compiler.reportedDiagnostics).isEmpty();
  }

  @Test
  public void testVisitFunctionNoParentAnnotations() {
    // Arrange
    Node nameNode = Node.newName("foo");
    Node assignNode = Node.newAssignment(Token.ASSIGN, nameNode, Node.newThis());
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(assignNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    DiagnosticType diagnosticType = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.reportedDiagnostics)
        .containsExactly(ArgumentMatchers.equalTo(compiler.makeError(assignNode, checker.level, diagnosticType)));
  }

  @Test
  public void testVisitFunctionWithPrototypePropertyAccess() {
    // Arrange
    Node objectNode = Node.newName("object");
    Node getPropNode = Node.newGetProperty(Token.GETPROP, objectNode, Node.newName("prototype"));
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(getPropNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    assertThat(compiler.reportedDiagnostics).isEmpty();
  }

  @Test
  public void testVisitFunctionWithPrototypePropertyAccessThroughDotNotation() {
    // Arrange
    Node objectNode = Node.newName("object");
    Node dotNode = Node.newMember(Token.DOT, objectNode, Node.newName("prototype"));
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(dotNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    DiagnosticType diagnosticType = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.reportedDiagnostics)
        .containsExactly(ArgumentMatchers.equalTo(compiler.makeError(dotNode, checker.level, diagnosticType)));
  }

  @Test
  public void testVisitFunctionWithPrototypePropertyAccessThroughBracketNotation() {
    // Arrange
    Node objectNode = Node.newName("object");
    Node indexNode = Node.newIntLiteral(0);
    Node indexedPropNode = Node.newIndexedProperty(Token.INDEXEDPROP, objectNode, indexNode);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(indexedPropNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    DiagnosticType diagnosticType = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.reportedDiagnostics)
        .containsExactly(ArgumentMatchers.equalTo(compiler.makeError(indexedPropNode, checker.level, diagnosticType)));
  }

  @Test
  public void testVisitFunctionWithPrototypeMethodAccess() {
    // Arrange
    Node objectNode = Node.newName("object");
    Node methodNameNode = Node.newName("method");
    Node methodAccessNode = Node.newGetMethod(Token.GETPROC, objectNode, methodNameNode);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(methodAccessNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    DiagnosticType diagnosticType = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.reportedDiagnostics)
        .containsExactly(ArgumentMatchers.equalTo(compiler.makeError(methodAccessNode, checker.level, diagnosticType)));
  }

  @Test
  public void testVisitFunctionWithPrototypeMethodAccessThroughDotNotation() {
    // Arrange
    Node objectNode = Node.newName("object");
    Node methodNameNode = Node.newName("method");
    Node methodAccessNode = Node.newMember(Token.DOT, objectNode, methodNameNode);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(methodAccessNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    DiagnosticType diagnosticType = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.reportedDiagnostics)
        .containsExactly(ArgumentMatchers.equalTo(compiler.makeError(methodAccessNode, checker.level, diagnosticType)));
  }

  @Test
  public void testVisitFunctionWithPrototypeMethodAccessThroughBracketNotation() {
    // Arrange
    Node objectNode = Node.newName("object");
    Node methodNameNode = Node.newName("method");
    Node methodAccessNode = Node.newIndexedProperty(Token.INDEXEDPROP, objectNode, methodNameNode);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(methodAccessNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    DiagnosticType diagnosticType = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.reportedDiagnostics)
        .containsExactly(ArgumentMatchers.equalTo(compiler.makeError(methodAccessNode, checker.level, diagnosticType)));
  }

  @Test
  public void testVisitFunctionWithThisOnLeftSideOfAssignment() {
    // Arrange
    Node leftSideNode = Node.newThis();
    Node rightSideNode = Node.newName("value");
    Node assignmentNode = Node.newAssignment(Token.ASSIGN, leftSideNode, rightSideNode);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(assignmentNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    DiagnosticType diagnosticType = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.reportedDiagnostics)
        .containsExactly(ArgumentMatchers.equalTo(compiler.makeError(leftSideNode, checker.level, diagnosticType)));
  }

  @Test
  public void testVisitFunctionWithThisOnLeftSideOfPropertyAccess() {
    // Arrange
    Node leftSideNode = Node.newThis();
    Node propertyNameNode = Node.newName("property");
    Node propertyAccessNode = Node.newGetProperty(Token.GETPROP, leftSideNode, propertyNameNode);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(propertyAccessNode));

    // Act
    checker.visit(NodeTraversal.create(null, functionNode), functionNode, null);

    // Assert
    DiagnosticType diagnosticType = new DiagnosticType(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.reportedDiagnostics)
        .containsExactly(ArgumentMatchers.equalTo(compiler.makeError(propertyAccessNode, checker.level, diagnosticType)));
  }

  private AbstractCompiler createCompilerMock() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.makeError(ArgumentMatchers.any(Node.class), ArgumentMatchers.anyInt(), ArgumentMatchers.any(DiagnosticType.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    return compiler;
  }

  static class DiagnosticType {
    DiagnosticType(int code, String message) {
      this.code = code;
      this.message = message;
    }

    final int code;
    final String message;
  }
}