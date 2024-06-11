package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.DiagnosticsCollector;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

/**
 * Test class for {@link CheckGlobalThis}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CheckGlobalThis.class, AbstractCompiler.class})
public class CheckGlobalThisTest {

  private static final String SOURCE = "function evil() { this.property = 42; }";
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private DiagnosticsCollector diagnosticsCollector;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = PowerMockito.mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    diagnosticsCollector = new DiagnosticsCollector();
    sourceFile = Parser.parseCompilationUnit(SOURCE, null);
  }

  /**
   * Test method for {@link CheckGlobalThis#shouldTraverse(NodeTraversal, Node, Node)}.
   */
  @Test
  public void testShouldTraverse() {
    NodeTraversal traversal = PowerMockito.mock(NodeTraversal.class);
    Node parent = PowerMockito.mock(Node.class);
    Node n = PowerMockito.mock(Node.class);

    PowerMockito.when(compiler.getSourceFile()).thenReturn(sourceFile);
    PowerMockito.when(compiler.getDiagnostics()).thenReturn(diagnosticsCollector);

    PowerMockito.when(traversal.getCurrentNode()).thenReturn(n);
    PowerMockito.when(traversal.getParentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(traversal, n, parent));

    parent = new Node(Token.ASSIGN);
    n = new Node(Token.THIS);
    PowerMockito.when(parent.getLastChild()).thenReturn(n);

    assertFalse(checker.shouldTraverse(traversal, n, parent));

    n = new Node(Token.GETPROP);
    n.setQualifiedName("prototype");
    PowerMockito.when(n.getLastChild().getString()).thenReturn("prototype");

    assertFalse(checker.shouldTraverse(traversal, n, parent));

    n.setQualifiedName("prototype.property");
    PowerMockito.when(n.getQualifiedName()).thenReturn("prototype.property");

    assertFalse(checker.shouldTraverse(traversal, n, parent));

    n = new Node(Token.FUNCTION);

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    PowerMockito.when(n.getJSDocInfo()).thenReturn(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, n, parent));

    jsDoc.setConstructor(false);
    jsDoc.setThisType(true);

    assertFalse(checker.shouldTraverse(traversal, n, parent));

    PowerMockito.when(n.getJSDocInfo()).thenReturn(null);

    assertTrue(checker.shouldTraverse(traversal, n, parent));
  }

  /**
   * Test method for {@link CheckGlobalThis#visit(NodeTraversal, Node, Node)}.
   */
  @Test
  public void testVisit() {
    NodeTraversal traversal = PowerMockito.mock(NodeTraversal.class);
    Node parent = PowerMockito.mock(Node.class);
    Node n = PowerMockito.mock(Node.class);

    PowerMockito.when(compiler.getSourceFile()).thenReturn(sourceFile);
    PowerMockito.when(compiler.getDiagnostics()).thenReturn(diagnosticsCollector);

    PowerMockito.when(traversal.getCurrentNode()).thenReturn(n);
    PowerMockito.when(traversal.getParentNode()).thenReturn(parent);

    Node assignParent = new Node(Token.ASSIGN);
    Node assignLhs = new Node(Token.THIS);
    Node assignRhs = new Node(Token.NUMBER, 42);
    assignParent.addChildToEnd(assignLhs);
    assignParent.addChildToEnd(assignRhs);
    PowerMockito.when(parent).thenReturn(assignParent);

    PowerMockito.when(n.getType()).thenReturn(Token.THIS);

    checker.visit(traversal, n, parent);

    assertTrue(diagnosticsCollector.getDiagnostics().size() > 0);

    Node functionNode = new Node(Token.FUNCTION);

    PowerMockito.when(n.getType()).thenReturn(Token.FUNCTION);
    PowerMockito.when(n.getParent()).thenReturn(parent);

    checker.visit(traversal, n, parent);

    assertFalse(diagnosticsCollector.getDiagnostics().size() > 0);

    jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    PowerMockito.when(n.getJSDocInfo()).thenReturn(jsDoc);

    checker.visit(traversal, n, parent);

    assertFalse(diagnosticsCollector.getDiagnostics().size() > 0);

    jsDoc.setConstructor(false);
    jsDoc.setThisType(true);

    checker.visit(traversal, n, parent);

    assertFalse(diagnosticsCollector.getDiagnostics().size() > 0);

    PowerMockito.when(n.getJSDocInfo()).thenReturn(null);

    checker.visit(traversal, n, parent);

    assertTrue(diagnosticsCollector.getDiagnostics().size() > 0);
  }

}