import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.TokenType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript("function evil() { this.useful = undefined; }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostics.getError(0).getCode());
  }

  @Test
  public void testCheckGlobalThisFunctionAnnotatedWithConstructor() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "function MyConstructor() { this.useful = undefined; }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test
  public void testCheckGlobalThisFunctionAnnotatedWithThis() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "@this\nfunction foo() { this.useful = undefined; }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test
  public void testCheckGlobalThisFunctionAnnotatedWithOverride() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "@override\nfunction foo() { this.useful = undefined; }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test
  public void testCheckGlobalThisPrototypeMethod() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "function MyObject() {}", null);
    sourceNode = sourceNode.getLastChild();
    sourceNode = sourceNode.getLastChild();
    sourceNode = sourceNode.getLastChild();
    Node prototypeNode = sourceNode.getFirstChild();
    prototypeNode = prototypeNode.getLastChild();
    prototypeNode = prototypeNode.getLastChild();
    Node prototypeMethodNode = prototypeNode.getLastChild();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(prototypeMethodNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test
  public void testCheckGlobalThisFunctionLentToPrototype() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "function MyObject() { this.foo = function() { this.useful = undefined; } }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThisFunctionInsideBlock() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript("{ function evil() { this.useful = undefined; } }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    fail("Expected a warning, but nothing was reported.");
  }

  @Test
  public void testCheckGlobalThisFunctionInsideFunction() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "function outer() { function inner() { this.useful = undefined; } }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test
  public void testCheckGlobalThisFunctionInsideAssignment() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "function outer() { var foo = this; this.useful = undefined; }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostics.getError(0).getCode());
  }

  @Test
  public void testCheckGlobalThisInsideAssignmentToPrototypeProperty() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "function MyObject() { this.foo = function() { this.useful = undefined; } }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test
  public void testCheckGlobalThisInsideAssignmentToPrototypeSubProperty() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "function MyObject() { this.foo = { bar: function() { this.useful = undefined; } } }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test
  public void testCheckGlobalThisInsideVariableDeclaration() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "function outer() { var foo = this; this.useful = undefined; }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostics.getError(0).getCode());
  }

  @Test
  public void testCheckGlobalThisInsideVariableDeclarationInsideBlock() {
    Compiler compiler = new Compiler();
    Diagnostics diagnostics = new Diagnostics();
    Node sourceNode = AbstractCompiler.parseJavaScript(
        "{ var foo = this; function evil() { this.useful = undefined; } }", null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(sourceNode, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getNumErrors());
  }
}