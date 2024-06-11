package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.DiagnosticGrouping;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory nodeFactory;
  private Parser parser;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new Checker(new CheckerFactory() {
      @Override
      public NodeTraversal createTraversal(AbstractCompiler compiler, CheckLevel level, Node node, Node parent) {
        return new NodeTraversal(compiler, new CheckGlobalThis(compiler, level)) {
          @Override
          public void traverse(NodeTraversal t, Node n, Node parent) {
            super.traverse(t, n, parent);
            checker.visit(n);
          }
        };
      }
    }, new DiagnosticGrouping());

    nodeFactory = new NodeFactory();
    parser = new Parser(nodeFactory, null);
  }

  @Test
  public void testCheckGlobalThis() {
    String code =
        "function foo() { this.prop = 42; } " +
        "function bar(x) { return x + this.prop; } " +
        "var baz = function() { this.prop = 42; };";

    Node root = parser.parse(code, null, null);

    checker.check(root, null, null, null);

    Node foo = root.getFirstChild();
    assertEquals(1, foo.getNumDiagnostics());
    assertEquals(WARNING, foo.getDiagnostics().get(0).getLevel());
    assertEquals(DiagnosticType.JSC_USED_GLOBAL_THIS, foo.getDiagnostics().get(0).getType());

    Node bar = foo.getNext();
    assertEquals(0, bar.getNumDiagnostics());

    Node baz = bar.getNext();
    assertEquals(1, baz.getNumDiagnostics());
    assertEquals(WARNING, baz.getDiagnostics().get(0).getLevel());
    assertEquals(DiagnosticType.JSC_USED_GLOBAL_THIS, baz.getDiagnostics().get(0).getType());
  }

  @Test
  public void testCheckGlobalThisAnnotatedFunction() {
    String code =
        "/** @constructor */" +
        "function Foo() { this.prop = 42; }" +
        "/** @this {Object} */" +
        "function Bar(x) { return x + this.prop; }";

    Node root = parser.parse(code, null, null);

    checker.check(root, null, null, null);

    Node foo = root.getFirstChild();
    assertEquals(0, foo.getNumDiagnostics());

    Node bar = foo.getNext();
    assertEquals(0, bar.getNumDiagnostics());
  }

  @Test
  public void testCheckGlobalThisPrototypeProperty() {
    String code = "function Foo() { } Foo.prototype.prop = 42;";

    Node root = parser.parse(code, null, null);

    checker.check(root, null, null, null);

    Node foo = root.getFirstChild();
    assertEquals(0, foo.getNumDiagnostics());
  }

  @Test
  public void testCheckGlobalThisNestedAssignment() {
    String code = "var a = { prop: 42 }; function foo() { a = this; }";

    Node root = parser.parse(code, null, null);

    checker.check(root, null, null, null);

    Node foo = root.getNext();
    assertEquals(0, foo.getNumDiagnostics());
  }

  @Test
  public void testCheckGlobalThisNoDiagnostics() {
    String code = "function foo() { }";

    Node root = parser.parse(code, null, null);

    checker.check(root, null, null, null);

    Node foo = root.getFirstChild();
    assertEquals(0, foo.getNumDiagnostics());
  }

  @Test
  public void testCheckGlobalThisFunctionAnnotatedWithThisType() {
    String code = "/** @this {String} */ function foo() { }";

    Node root = parser.parse(code, null, null);

    checker.check(root, null, null, null);

    Node foo = root.getFirstChild();
    assertEquals(0, foo.getNumDiagnostics());
  }

  @Test
  public void testCheckGlobalThisIfNoLogicalPlaceForThisAnnotation() {
    String code = "function foo(x) { return x + this; }";

    Node root = parser.parse(code, null, null);

    checker.check(root, null, null, null);

    Node foo = root.getFirstChild();
    assertEquals(1, foo.getNumDiagnostics());
    assertEquals(WARNING, foo.getDiagnostics().get(0).getLevel());
    assertEquals(DiagnosticType.JSC_USED_GLOBAL_THIS, foo.getDiagnostics().get(0).getType());
  }

  @Test
  public void testCheckGlobalThisNoDiagnosticsForPrototypes() {
    String code = "function Foo() { } Foo.prototype = { prop: 42 };";

    Node root = parser.parse(code, null, null);

    checker.check(root, null, null, null);

    Node foo = root.getFirstChild();
    assertEquals(0, foo.getNumDiagnostics());
  }
}