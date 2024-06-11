package com.google.javascript.jscomp.test;

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
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.jscomp.test.testTypes.CheckGlobalThisTest;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private NodeTraversal traversal;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse() {
    Node node = new TestSourceFile("test.js", "function test() { this.x = 1; }")
        .getAST();

    Node parent = node.getParent();

    assertThat(checker.shouldTraverse(traversal, node, parent)).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node node = new TestSourceFile("test.js", "@this class X { this.x = 1; }")
        .getAST();

    Node parent = node.getParent();

    assertThat(checker.shouldTraverse(traversal, node, parent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node node = new TestSourceFile("test.js", "function X() { this.x = 1; }")
        .getAST();

    Node parent = node.getParent();

    assertThat(checker.shouldTraverse(traversal, node, parent)).isFalse();
  }

  @Test
  public void testShouldNotTraversePrototypeProperty() {
    Node node = new TestSourceFile("test.js", "function X() {} X.prototype.x = function() { this.y = 1; }")
        .getAST();

    Node parent = node.getParent();

    assertThat(checker.shouldTraverse(traversal, node, parent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node node = new TestSourceFile("test.js", "function X() {} X.prototype.x = { get: function() { this.y = 1; } }")
        .getAST();

    Node parent = node.getParent();

    assertThat(checker.shouldTraverse(traversal, node, parent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    Node node = new TestSourceFile("test.js", "function test() { this.x = 1; }")
        .getAST();

    assertThat(checker.shouldTraverse(traversal, node, null)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParentAndNoParentType() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    when(compilerMock.getCurrentScope()).thenReturn(null);
    NodeTraversal traversalMock = mock(NodeTraversal.class);
    CheckGlobalThis checker = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);

    Node node = new TestSourceFile("test.js", "function test() { this.x = 1; }")
        .getAST();

    assertThat(checker.shouldTraverse(traversalMock, node, null)).isFalse();
  }

  @Test
  public void testShouldReportThis() {
    Node node = new TestSourceFile("test.js", "function test() { this.x = 1; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { test(); }")
        .getAST();

    when(compiler.report(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);

    checker.visit(traversal, node, parent);

    NodeUtil.assertErrors(compiler, 1);
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccess() {
    Node node = new TestSourceFile("test.js", "function test() { return this.x; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { console.log(test()); }")
        .getAST();

    checker.visit(traversal, node, parent);

    assertNull(NodeUtil.getFirstError(compiler));
  }

  @Test
  public void testShouldNotReportThisOnPrototypeAccess() {
    Node node = new TestSourceFile("test.js", "function test() { return Test.prototype.x; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { console.log(test()); }")
        .getAST();

    checker.visit(traversal, node, parent);

    assertNull(NodeUtil.getFirstError(compiler));
  }

  @Test
  public void testShouldNotReportThisOnPrototypeMethodCall() {
    Node node = new TestSourceFile("test.js", "function test() { Test.prototype.meth.call(this); }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { test(); }")
        .getAST();

    checker.visit(traversal, node, parent);

    assertNull(NodeUtil.getFirstError(compiler));
  }

  @Test
  public void testShouldNotReportThisOnFunctionCallWithThisArg() {
    Node node = new TestSourceFile("test.js", "function test(that) { this.x = that; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { test(this); }")
        .getAST();

    checker.visit(traversal, node, parent);

    assertNull(NodeUtil.getFirstError(compiler));
  }

  @Test
  public void testShouldNotReportThisOnProtoFunctionCallWithThisArg() {
    Node node = new TestSourceFile("test.js", new CheckGlobalThisTest().getProtoCallThisArgCode())
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { test(this); }")
        .getAST();

    checker.visit(traversal, node, parent);

    assertNull(NodeUtil.getFirstError(compiler));
  }

  @Test
  public void testShouldReportThisOnFunctionCallWithThisKeyword() {
    Node node = new TestSourceFile("test.js", "function test() { this.x = 1; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { test.call(this); }")
        .getAST();

    when(compiler.report(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);

    checker.visit(traversal, node, parent);

    NodeUtil.assertErrors(compiler, 1);
  }

  @Test
  public void testShouldReportThisOnGetPropertyAccessWithThisKeyword() {
    Node node = new TestSourceFile("test.js", "function test() { return this.x; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { console.log(test.call(this).x); }")
        .getAST();

    when(compiler.report(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);

    checker.visit(traversal, node, parent);

    NodeUtil.assertErrors(compiler, 1);
  }

  @Test
  public void testShouldReportThisOnGetPropertyAccessWithDotNotation() {
    Node node = new TestSourceFile("test.js", "function test() { return this.x; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { console.log(test.x); }")
        .getAST();

    when(compiler.report(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);

    checker.visit(traversal, node, parent);

    NodeUtil.assertErrors(compiler, 1);
  }

  @Test
  public void testShouldReportThisOnGetPropertyAccessWithBracketNotation() {
    Node node = new TestSourceFile("test.js", "function test() { return this['x']; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { console.log(test.x); }")
        .getAST();

    when(compiler.report(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);

    checker.visit(traversal, node, parent);

    NodeUtil.assertErrors(compiler, 1);
  }

  @Test
  public void testShouldReportThisOnSetPropertyAccessWithThisKeyword() {
    Node node = new TestSourceFile("test.js", "function test() { this.x = 1; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { test.call(this).x = 1; }")
        .getAST();

    when(compiler.report(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);

    checker.visit(traversal, node, parent);

    NodeUtil.assertErrors(compiler, 1);
  }

  @Test
  public void testShouldReportThisOnSetPropertyAccessWithDotNotation() {
    Node node = new TestSourceFile("test.js", "function test() { this.x = 1; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { test.x = 1; }")
        .getAST();

    when(compiler.report(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);

    checker.visit(traversal, node, parent);

    NodeUtil.assertErrors(compiler, 1);
  }

  @Test
  public void testShouldReportThisOnSetPropertyAccessWithBracketNotation() {
    Node node = new TestSourceFile("test.js", "function test() { this['x'] = 1; }")
        .getAST();

    Node parent = new TestSourceFile("test.js", "function main() { test['x'] = 1; }")
        .getAST();

    when(compiler.report(traversal.makeError(node, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(null);

    checker.visit(traversal, node, parent);

    NodeUtil.assertErrors(compiler, 1);
  }

  private String getProtoCallThisArgCode() {
    return "function X() {} X.prototype.meth = function(that) { this.x = that; }";
  }
}