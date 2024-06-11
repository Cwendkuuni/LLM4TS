package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private CompilerOptions options;
  private Diagnostics diagnostics;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, WARNING);
    options = new CompilerOptions();
    diagnostics = new Diagnostics();
    nodeFactory = NodeFactory.getInitializer(options);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    // Arrange
    Node functionNode = nodeFactory.createNodeFromPrintable("function global() {}");

    // Act
    boolean result = checker.shouldTraverse(NodeTraversal.noopTraversal(null), functionNode, null);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    // Arrange
    Node constructorNode = nodeFactory.createNodeFromPrintable("function Foo() {}");
    JSDocInfo constructorJsDoc = new JSDocInfo();
    constructorJsDoc.setConstructor(true);
    constructorNode.setJSDocInfo(constructorJsDoc);

    // Act
    boolean result = checker.shouldTraverse(NodeTraversal.noopTraversal(null), constructorNode, null);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    // Arrange
    Node functionNode = nodeFactory.createNodeFromPrintable("function @this Foo() {}");

    // Act
    boolean result = checker.shouldTraverse(NodeTraversal.noopTraversal(null), functionNode, null);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    // Arrange
    Node functionNode = nodeFactory.createNodeFromPrintable("function @override Foo() {}");

    // Act
    boolean result = checker.shouldTraverse(NodeTraversal.noopTraversal(null), functionNode, null);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void testShouldNotTraverseFunctionOutsideOfScriptOrBlockScope() {
    // Arrange
    Node functionNode = nodeFactory.createNodeFromPrintable("function Foo() {}");
    Node parentNode = nodeFactory.createToken(Token.SCRIPT, null, null);

    // Act
    boolean result = checker.shouldTraverse(NodeTraversal.noopTraversal(null), functionNode, parentNode);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void testShouldNotTraverseFunctionNotAnnotatedWithThisOrOverride() {
    // Arrange
    Node functionNode = nodeFactory.createNodeFromPrintable("function Foo() {}");

    // Act
    boolean result = checker.shouldTraverse(NodeTraversal.noopTraversal(null), functionNode, null);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    // Arrange
    Node assignNode = nodeFactory.createNodeFromPrintable("var a = this;");
    Node thisNode = assignNode.getFirstChild();

    // Act
    when(compiler.report(eq(DEFAULT), eq(WARNING), any(DiagnosticType.class)))
        .thenReturn(new DiagnosticPosition(assignNode.getSourceStart()));
    checker.visit(NodeTraversal.noopTraversal(compiler), assignNode, null);

    // Assert
    assertEquals(1, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    // Arrange
    Node assignNode = nodeFactory.createNodeFromPrintable("var a = obj.this;");
    Node thisNode = nodeFactory.createToken(Token.THIS, null, null);
    Node getPropNode = nodeFactory.createNodeFromPrintable("obj.this");
    Node objNode = nodeFactory.createToken(Token.NAME, "obj", null);

    Node parentNode = nodeFactory.createBlockStart();
    parentNode.addChild(objNode);
    parentNode.addChild(assignNode);
    parentNode.addChild(nodeFactory.createBlockEnd());

    // Act
    when(compiler.report(eq(DEFAULT), eq(WARNING), any(DiagnosticType.class)))
        .thenReturn(new DiagnosticPosition(thisNode.getSourceStart()));
    checker.visit(NodeTraversal.noopTraversal(compiler), assignNode, parentNode);

    // Assert
    assertEquals(1, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    // Arrange
    Node assignNode = nodeFactory.createNodeFromPrintable("var a = this;");
    Node thisNode = assignNode.getLastChild();

    // Act
    checker.visit(NodeTraversal.noopTraversal(compiler), assignNode, null);

    // Assert
    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfPrototypePropertyAccess() {
    // Arrange
    Node assignNode = nodeFactory.createNodeFromPrintable("var a = obj.prototype.this;");
    Node thisNode = nodeFactory.createToken(Token.THIS, null, null);
    Node getPropNode = nodeFactory.createNodeFromPrintable("obj.prototype.this");
    Node objNode = nodeFactory.createToken(Token.NAME, "obj", null);

    Node parentNode = nodeFactory.createBlockStart();
    parentNode.addChild(objNode);
    parentNode.addChild(nodeFactory.createGetProp(null, getPropNode, null, null, null));
    parentNode.addChild(assignNode);
    parentNode.addChild(nodeFactory.createBlockEnd());

    // Act
    checker.visit(NodeTraversal.noopTraversal(compiler), assignNode, parentNode);

    // Assert
    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPrototypePropertyAccess() {
    // Arrange
    Node assignNode = nodeFactory.createNodeFromPrintable("var a = obj.prototype.this;");
    Node thisNode = nodeFactory.createToken(Token.THIS, null, null);
    Node objNode = nodeFactory.createToken(Token.NAME, "obj", null);
    Node getPropNode = nodeFactory.createGetProp(null, nodeFactory.createToken(Token.IDENT, "this", null), null, null, null);

    Node parentNode = nodeFactory.createBlockStart();
    parentNode.addChild(objNode);
    parentNode.addChild(assignNode);
    parentNode.addChild(nodeFactory.createBlockEnd());

    // Act
    checker.visit(NodeTraversal.noopTraversal(compiler), assignNode, parentNode);

    // Assert
    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPrototypePropertyAccessWithDotNotation() {
    // Arrange
    Node assignNode = nodeFactory.createNodeFromPrintable("var a = obj.prototype.this;");
    Node thisNode = nodeFactory.createToken(Token.THIS, null, null);
    Node objNode = nodeFactory.createToken(Token.NAME, "obj", null);
    Node getPropNode = nodeFactory.createGetProp(null, objNode, null, null, null);

    Node parentNode = nodeFactory.createBlockStart();
    parentNode.addChild(assignNode);
    parentNode.addChild(nodeFactory.createBlockEnd());

    // Act
    when(compiler.report(eq(DEFAULT), eq(WARNING), any(DiagnosticType.class)))
        .thenReturn(new DiagnosticPosition(thisNode.getSourceStart()));
    checker.visit(NodeTraversal.noopTraversal(compiler), assignNode, parentNode);

    // Assert
    assertEquals(1, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPrototypePropertyAccessWithBracketNotation() {
    // Arrange
    Node assignNode = nodeFactory.createNodeFromPrintable("var a = obj['prototype'].this;");
    Node thisNode = nodeFactory.createToken(Token.THIS, null, null);
    Node objNode = nodeFactory.createToken(Token.NAME, "obj", null);
    Node indexNode = nodeFactory.createToken(Token.STRING, "prototype", null);
    Node getPropNode = nodeFactory.createGetProp(null, objNode, null, null, indexNode);

    Node parentNode = nodeFactory.createBlockStart();
    parentNode.addChild(assignNode);
    parentNode.addChild(nodeFactory.createBlockEnd());

    // Act
    when(compiler.report(eq(DEFAULT), eq(WARNING), any(DiagnosticType.class)))
        .thenReturn(new DiagnosticPosition(thisNode.getSourceStart()));
    checker.visit(NodeTraversal.noopTraversal(compiler), assignNode, parentNode);

    // Assert
    assertEquals(1, diagnostics.getDiagnostics().size());
  }
}