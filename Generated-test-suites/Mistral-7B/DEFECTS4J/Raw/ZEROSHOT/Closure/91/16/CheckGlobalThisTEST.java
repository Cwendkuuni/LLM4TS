import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.ParserTestBase;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory nodeFactory;
  private Parser parser;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checker = new Checker(new DiagnosticReporter(), compiler, new NodeTraversal(),
        new NodeUtil(), new SourceFile(null), new ErrorReporter());
    nodeFactory = compiler.getFactory();
    parser = new Parser(new SourceFile(null), null, null, nodeFactory, checker);
  }

  @Test
  public void testNoGlobalThis() {
    Node node = parser.parse(
        "function goodFunction() {}",
        "function goodFunctionWithThisVariable(that) { this.x = 1; }",
        "function goodFunctionWithPrototypeProperty(obj) { obj.prop.x = 1; }",
        "function goodFunctionWithPrototypeMethod() { this.method = function() {}; }");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testSimpleGlobalThis() {
    Node node = parser.parse("function badFunction() { this.x = 1; }");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of(CheckGlobalThis.GLOBAL_THIS);
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testGlobalThisInAssignment() {
    Node node = parser.parse("var x = this;");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of(CheckGlobalThis.GLOBAL_THIS);
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testGlobalThisInFunctionExpression() {
    Node node = parser.parse("var x = function() { this.x = 1; }");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of(CheckGlobalThis.GLOBAL_THIS);
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testGlobalThisInPrototypeProperty() {
    Node node = parser.parse(
        "var x = { get prop() { this.x = 1; } }; x.prop = 1;");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of(CheckGlobalThis.GLOBAL_THIS);
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testGlobalThisInPrototypeMethod() {
    Node node = parser.parse(
        "var x = { get prop() { return this; }, method: function() { this.x = 1; } }; x.method();");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of();
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionWithJSDocAnnotation() {
    Node node = parser.parse(
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Ctor() { this.x = 1; }",
        "Ctor();");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of();
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node node = parser.parse(
        "function Ctor() { @this this; this.x = 1; }",
        "Ctor();");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of();
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionWithNoJSDocOrThisAnnotation() {
    Node node = parser.parse("function Ctor() { this.x = 1; }", "Ctor();");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of(CheckGlobalThis.GLOBAL_THIS);
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionWithPrototypeProperty() {
    Node node = parser.parse(
        "function Ctor() { this.prop = 1; }",
        "var x = new Ctor(); x.prop = 2;");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of();
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionWithPrototypeMethod() {
    Node node = parser.parse(
        "function Ctor() { this.prop = 1; this.method = function() {}; }",
        "var x = new Ctor(); x.method();");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of();
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionWithPrototypeAndGlobalThis() {
    Node node = parser.parse(
        "function Ctor() { this.prop = 1; this.method = function() { this.x = 1; }; }",
        "var x = new Ctor(); x.method(); x.x = 1;");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of(CheckGlobalThis.GLOBAL_THIS);
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionWithThisAnnotationsAndPrototype() {
    Node node = parser.parse(
        "/** @constructor @this {prop: number} */",
        "function Ctor() { @this this; this.prop = 1; this.method = function() {}; }",
        "var x = new Ctor(); x.method(); x.prop = 2;");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of();
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionCallWithThisAsArgument() {
    Node node = parser.parse(
        "function Ctor() { this.x = 1; }",
        "function Foo() { Ctor.call(this); }",
        "Foo();");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of();
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionCallWithThisAsArgumentAndPrototype() {
    Node node = parser.parse(
        "function Ctor() { this.x = 1; this.method = function() {}; }",
        "function Foo() { Ctor.call(this); Ctor.prototype.method(); }",
        "Foo();");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of();
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }

  @Test
  public void testFunctionCallWithThisAsArgumentAndPrototypeAndGlobalThis() {
    Node node = parser.parse(
        "function Ctor() { this.x = 1; this.method = function() { this.x = 2; }; }",
        "function Foo() { Ctor.call(this); Ctor.prototype.method(); this.x = 3; }",
        "Foo();");

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    ImmutableList<DiagnosticType> expectedDiagnostics =
        ImmutableList.of(CheckGlobalThis.GLOBAL_THIS);
    assertEquals(expectedDiagnostics, traversal.getDiagnostics());
  }
}