package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private Checker checker;
  private CheckGlobalThis check;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CheckerFactory factory = new CheckerFactory();
    checker = factory.getChecker(compiler);
    check = new CheckGlobalThis(compiler, CHECK_LEVEL_WARNING);
  }

  private void check(String code, int lineNumber, int columnNumber, String expectedErrorMessage) {
    checker.checkSource(new SourceFile("test.js", code), new CompilerOptions());
    Diagnostics diagnostics = checker.getErrors();
    assertTrue("Expected error messages", diagnostics.length() > 0);
    assertEquals("Unexpected error messages", expectedErrorMessage, diagnostics.get(0).getMessage());
    assertEquals("Unexpected line number", lineNumber, diagnostics.get(0).getLineNumber());
    assertEquals("Unexpected column number", columnNumber, diagnostics.get(0).getColumnNumber());
  }

  @Test
  public void testNoGlobalThis() {
    String code = "function evil() { return this; }";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          check.visit(t, n, parent);
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testGlobalThisAtRoot() {
    String code = "this;";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.THIS) {
          check.visit(t, n, null);
        }
      }
    }, checker.getRoot());
    check.hasErrors();
    check.assertDiagnostics(1, "JSC_USED_GLOBAL_THIS", 1, 0);
  }

  @Test
  public void testGlobalThisInFunction() {
    String code = "function evil() { return this; }";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          check.visit(t, n, null);
        }
      }
    }, checker.getRoot());
    check.hasErrors();
    check.assertDiagnostics(1, "JSC_USED_GLOBAL_THIS", 2, 0);
  }

  @Test
  public void testGlobalThisInFunctionWithJsDoc() {
    String code = "/**\n * @constructor\n */\n function evil() { return this; }";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          check.visit(t, n, null);
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testGlobalThisOnLeftSideOfAssignment() {
    String code = "var x = this;";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ASSIGN && NodeUtil.isAssign(parent)) {
          check.visit(t, n, parent);
        }
      }
    }, checker.getRoot());
    check.hasErrors();
    check.assertDiagnostics(1, "JSC_USED_GLOBAL_THIS", 2, 1);
  }

  @Test
  public void testGlobalThisOnRightSideOfAssignment() {
    String code = "var x = 5; x = this;";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ASSIGN && NodeUtil.isAssign(parent)) {
          check.visit(t, n, parent);
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testGlobalThisOnLeftSideOfPropertyAccess() {
    String code = "var x = { get y() { return this; } }; x.y = 5;";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.GETPROP && parent != null) {
          Node getter = parent.getFirstChild();
          if (getter != null && getter.getType() == Token.FUNCTION) {
            check.visit(t, n, parent);
          }
        }
      }
    }, checker.getRoot());
    check.hasErrors();
    check.assertDiagnostics(1, "JSC_USED_GLOBAL_THIS", 5, 0);
  }

  @Test
  public void testGlobalThisOnRightSideOfPropertyAccess() {
    String code = "var x = {}; x.y = this;";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.SETPROP) {
          check.visit(t, n, parent);
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testGlobalThisOnLeftSideOfPropertyAccessToPrototype() {
    String code = "var x = { get prototype() { return this; } }; x.prototype.y = 5;";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.GETPROP && parent != null) {
          Node getter = parent.getFirstChild();
          if (getter != null && getter.getType() == Token.FUNCTION &&
              getter.getQualifiedName().equals("prototype")) {
            check.visit(t, n, parent);
          }
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testGlobalThisOnRightSideOfPropertyAccessToPrototype() {
    String code = "var x = {}; x.__proto__.y = this;";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.SETPROP && parent != null) {
          Node prop = parent.getLastChild();
          if (prop != null && prop.getString().equals("__proto__")) {
            check.visit(t, n, parent);
          }
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testNoDiagnosticForPrototypeMethod() {
    String code = "var x = { get y() { return this; } }; x.y = 5;";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.GETPROP && parent != null) {
          Node getter = parent.getFirstChild();
          if (getter != null && getter.getType() == Token.FUNCTION &&
              getter.getQualifiedName().equals("y")) {
            check.visit(t, n, parent);
          }
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testNoDiagnosticForFunctionWithThisAnnotation() {
    String code = "/**@this Window**/ function evil() { return this; }";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          JSDocInfo jsDoc = getFunctionJsDocInfo(n);
          if (jsDoc != null && jsDoc.hasThisType()) {
            check.visit(t, n, parent);
          }
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testNoDiagnosticForFunctionWithNoLogicalPlaceForThisAnnotation() {
    String code = "function evil() { this.x = 5; }";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          check.visit(t, n, parent);
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  @Test
  public void testNoDiagnosticForFunctionWithOverriddenThisAnnotation() {
    String code = "/**@this String**/ function evil() { this.x = 5; }";
    check.traverse(new NodeTraversal(checker.getRoot()) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          JSDocInfo jsDoc = getFunctionJsDocInfo(n);
          if (jsDoc != null && jsDoc.isOverride()) {
            check.visit(t, n, parent);
          }
        }
      }
    }, checker.getRoot());
    assertFalse("No global 'this' found", check.hasErrors());
  }

  private JSDocInfo getFunctionJsDocInfo(Node n) {
    JSDocInfo jsDoc = n.getJSDocInfo();
    if (jsDoc == null) {
      return null;
    }
    return jsDoc;
  }

  private void assertDiagnostics(int numDiagnostics, String diagnosticType, int lineNumber, int columnNumber) {
    assertDiagnostics(numDiagnostics, diagnosticType, new int[] { lineNumber, columnNumber });
  }

  private void assertDiagnostics(int numDiagnostics, String diagnosticType, int[] expectedLineColumns) {
    Diagnostics diagnostics = check.getDiagnostics();
    assertEquals("Unexpected number of diagnostic messages", numDiagnostics, diagnostics.length());
    for (int i = 0; i < numDiagnostics; i++) {
      Diagnostic diagnostic = diagnostics.get(i);
      assertEquals("Unexpected diagnostic message type", diagnosticType, diagnostic.getType().getName());
      int actualLineColumn[] = diagnostic.getLocation().getSourceFile().getLineColumnNumber();
      assertArrayEquals("Unexpected diagnostic line column", expectedLineColumns, actualLineColumn);
    }
  }
}