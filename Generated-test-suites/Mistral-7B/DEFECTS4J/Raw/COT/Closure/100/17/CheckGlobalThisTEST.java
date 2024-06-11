package com.google.javascript.jscomp.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.Warning;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test for {@link CheckGlobalThis} class.
 */
public class CheckGlobalThisTest {

  private static final String TEST_CODE = "function evil() { this.property = 1; }";

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private NodeFactory nodeFactory;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, Warning);
    traversal = new NodeTraversal(new NodeFactory(), checker);
    nodeFactory = new NodeFactory();
    sourceFile = new SourceFile("TestFile.js", TEST_CODE);
  }

  @Test
  public void shouldReportGlobalThis() {
    Node node = nodeFactory.parse(sourceFile, null, null).body;

    when(compiler.report(ArgumentCaptor.capture(DiagnosticPosition.class),
        ArgumentCaptor.capture(CheckLevel.class),
        ArgumentCaptor.capture(DiagnosticType.class)))
        .thenReturn(null);

    traversal.traverse(node, null);

    assertTrue(compiler.reportWasCalled());
  }

  @Test
  public void shouldNotReportGlobalThisInConstructor() {
    String constructorCode = "function Constructor() { this.property = 1; }";
    Node node = nodeFactory.parse(new SourceFile("Constructor.js", constructorCode),
        null, null).body;

    JSDocInfo jsDoc = node.getJSDocInfo();
    when(compiler.report(ArgumentCaptor.capture(DiagnosticPosition.class),
        ArgumentCaptor.capture(CheckLevel.class),
        ArgumentCaptor.capture(DiagnosticType.class)))
        .thenReturn(null);

    Node constructorNode = node.getFirstChild();
    traversal.traverse(constructorNode, null);

    assertFalse(compiler.reportWasCalled());
    assertTrue(jsDoc.isConstructor());
  }

  @Test
  public void shouldNotReportGlobalThisInPrototypeMethod() {
    String prototypeCode = "function Constructor() {} Constructor.prototype.method = function() {"
        + " this.property = 1; }";
    Node node = nodeFactory.parse(new SourceFile("Constructor.js", prototypeCode),
        null, null).body;

    JSDocInfo jsDoc = node.getFirstChild().getJSDocInfo();
    when(compiler.report(ArgumentCaptor.capture(DiagnosticPosition.class),
        ArgumentCaptor.capture(CheckLevel.class),
        ArgumentCaptor.capture(DiagnosticType.class)))
        .thenReturn(null);

    Node constructorNode = node.getFirstChild();
    Node methodNode = constructorNode.getLastChild();
    traversal.traverse(methodNode, null);

    assertFalse(compiler.reportWasCalled());
    assertTrue(jsDoc != null && jsDoc.hasPrototype());
  }

  @Test
  public void shouldNotReportGlobalThisInThisAnnotatedFunction() {
    String thisAnnotatedCode = "@this\nfunction evil() { this.property = 1; }";
    Node node = nodeFactory.parse(new SourceFile("TestFile.js", thisAnnotatedCode),
        null, null).body;

    when(compiler.report(ArgumentCaptor.capture(DiagnosticPosition.class),
        ArgumentCaptor.capture(CheckLevel.class),
        ArgumentCaptor.capture(DiagnosticType.class)))
        .thenReturn(null);

    traversal.traverse(node, null);

    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void shouldNotReportGlobalThisInFunctionWithNoThisAnnotation() {
    String noThisAnnotationCode = "function evil() { this.property = 1; }";
    Node node = nodeFactory.parse(new SourceFile("TestFile.js", noThisAnnotationCode),
        null, null).body;

    when(compiler.report(ArgumentCaptor.capture(DiagnosticPosition.class),
        ArgumentCaptor.capture(CheckLevel.class),
        ArgumentCaptor.capture(DiagnosticType.class)))
        .thenReturn(null);

    traversal.traverse(node, null);

    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void shouldNotReportGlobalThisInAssignmentToVariable() {
    String assignmentToVariableCode = "var a = function() { this.property = 1; }";
    Node node = nodeFactory.parse(new SourceFile("TestFile.js", assignmentToVariableCode),
        null, null).body;

    when(compiler.report(ArgumentCaptor.capture(DiagnosticPosition.class),
        ArgumentCaptor.capture(CheckLevel.class),
        ArgumentCaptor.capture(DiagnosticType.class)))
        .thenReturn(null);

    traversal.traverse(node, null);

    assertFalse(compiler.reportWasCalled());
  }
}