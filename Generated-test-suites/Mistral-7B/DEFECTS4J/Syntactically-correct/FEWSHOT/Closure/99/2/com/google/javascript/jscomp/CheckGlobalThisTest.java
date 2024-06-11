package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
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
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(checker);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node script = Node.newScript("(function() {})();");
    NodeTraversal.traverse(script, traversal);
    assertThat(checker.assignLhsChild).isNull();
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node functionNode = Node.newFunction("function foo() {}");
    Node functionParent = Node.newName("foo");
    Node script = Node.newScript("(function() {" + "foo();" + "})();");

    NodeTraversal.traverse(script, traversal);
    assertThat(checker.assignLhsChild).isNull();

    checker.visit(traversal, functionNode, functionParent);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
    DiagnosticPosition position = positions.get(0);
    assertThat(position.getFileName()).isEqualTo("TestSource.js");
    assertThat(position.getLineNumber()).isEqualTo(2);
    assertThat(position.getColumnNumber()).isEqualTo(3);
    assertThat(position.getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @Test
  public void testShouldTraverseAssignments() {
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = this;" + ";" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
  }

  @Test
  public void testShouldNotTraversePrototypeProperties() {
    Node assignNode = Node.newAssign(Node.newName("a.prototype.x"), Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = function() {};" + "a.prototype.x = this;" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).isEmpty();
  }

  @Test
  public void testShouldReportUnsafeUsesOfThis() {
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = this;" + ";" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
  }

  @Test
  public void testShouldNotReportSafeUsesOfThis() {
    Node constructor = Node.newFunction(Token.FUNCTION, null, null, null, false, false, null, null, null, null);
    Node constructorParent = Node.newName("constructor");
    Node script = Node.newScript("(function constructor() {})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, constructor, constructorParent);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).isEmpty();
  }

  @Test
  public void testShouldNotReportThisAnnotatedFunction() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(Token.THIS);

    Node constructor = Node.newFunction(Token.FUNCTION, null, null, null, false, false, null, jsDoc, null, null);
    Node constructorParent = Node.newName("constructor");
    Node script = Node.newScript("(function constructor() {})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, constructor, constructorParent);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions =
        compiler.getDiagnostics();
    assertThat(positions).isNull();
  }

  @Test
  public void testShouldNotReportAnnotatedFunctionWithPrototype() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(Token.THIS);
    jsDoc.setConstructor(true);

    Node constructor = Node.newFunction(Token.FUNCTION, null, null, null, false, true, null, jsDoc, null, null);
    Node constructorParent = Node.newName("constructor");
    Node prototype = Node.newName("prototype");
    Node prototypeParent = Node.newAssign(prototype, Node.newName("constructor.prototype"));
    Node script = Node.newScript("(function constructor() {})().prototype;");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, constructor, constructorParent);
    checker.visit(traversal, prototype, prototypeParent);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).isNull();
  }

  @Test
  public void testShouldReportThisWithoutAnnotations() {
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = this;" + ";" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
  }

  @Test
  public void testShouldReportThisInGetProperty() {
    Node getProperty = Node.newGetProp(Node.newName("a"), Node.newName("prototype"));
    Node assignNode = Node.newAssign(getNode(assignLhsChild()), getProperty, Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = function() {}; a.prototype = this;" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
  }

  @Test
  public void testShouldNotReportThisInGetPropertyWithPrototypeName() {
    Node getProperty = Node.newGetProp(Node.newName("a.prototype"), Node.newName("constructor"));
    Node assignNode = Node.newAssign(getNode(assignLhsChild()), getProperty, Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = function() {}; a.prototype.constructor = this;" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).isEmpty();
  }

  @Test
  public void testShouldReportThisInGetPropertyWithPrototypeChain() {
    Node getProperty = Node.newGetProp(Node.newName("a.prototype"), Node.newName("b.prototype"));
    Node assignNode = Node.newAssign(getNode(assignLhsChild()), getProperty, Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = function() {}; a.prototype.b = function() {}; a.prototype.b.prototype = this;" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
  }

  @Test
  public void testShouldReportThisInGetPropertyWithPrototypeChainLongerThanTwoLevels() {
    Node getProperty = Node.newGetProp(Node.newName("a.prototype"), Node.newName("b.prototype.c.prototype"));
    Node assignNode = Node.newAssign(getNode(assignLhsChild()), getProperty, Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = function() {}; a.prototype.b = function() {}; a.prototype.b.prototype.c = function() {}; a.prototype.b.prototype.c.prototype = this;" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
  }

  @Test
  public void testShouldReportThisInGetPropertyWithPrototypeChainCircular() {
    Node getProperty = Node.newGetProp(Node.newName("a.prototype"), Node.newName("a.prototype"));
    Node assignNode = Node.newAssign(getNode(assignLhsChild()), getProperty, Node.newThis());
    Node script = Node.newScript("(function() {" + "var a = function() {}; a.prototype.a = a.prototype;" + "})());");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, assignNode, null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).isEmpty();
  }

  @Test
  public void testShouldReportThisInNestedAssignments() {
    Node script = Node.newScript("(function() {" + "var a = this;" + ";" + "var b = {" + "a: this" + "}}; a.b.a = this;" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, getNode(getNode(getNode(getNode(getNode(script), 1), 0), 0), 0), null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
  }

  @Test
  public void testShouldReportThisInNestedAssignmentsLongerChain() {
    Node script = Node.newScript("(function() {" + "var a = this;" + ";" + "var b = {" + "c: {" + "d: {" + "e: this" + "}}" + "}};" + "a.b.c.d.e = this;" + "})();");

    NodeTraversal.traverse(script, traversal);
    checker.visit(traversal, getNode(getNode(getNode(getNode(getNode(script), 1), 1), 0), 1), null);
    NodeUtil.reportErrors(compiler);

    ImmutableList<DiagnosticPosition> positions = compiler.getDiagnostics();
    assertThat(positions).hasSize(1);
  }

  private Node getNode(Node node) {
    return node != null ? node : Node.undefined();
  }

  private Node getNode(Node parent, int index) {
    return parent.getChildAt(index);
  }

  private Node getNode(Node node, int childIndex) {
    return node.getChildAt(childIndex);
  }

  private Node getNode(Node node, String childName) {
    return NodeUtil.getFirstChildWithName(node, childName);
  }

  private Node getAssignLhsChild() {
    return checker.assignLhsChild;
  }
}