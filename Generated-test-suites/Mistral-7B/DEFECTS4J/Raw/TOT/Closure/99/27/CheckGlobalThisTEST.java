import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticMessage;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  Checker checker;
  Diagnostics diagnostics;
  CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions(true);
    checker = new Checker(options);
    diagnostics = new Diagnostics();
  }

  @After
  public void tearDown() {
    checker = null;
    diagnostics = null;
  }

  @Test
  public void testNoGlobalThis() {
    String code = "function foo() {}";
    Node node = Node.parseTree(code, null, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(node, check);
    traversal.traverse();
    assertFalse(diagnostics.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String code = "function MyFunction(@this) {}";
    Node node = Node.parseTree(code, null, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(node, check);
    traversal.traverse();
    assertFalse(diagnostics.hasErrors());
  }

  @Test
  public void testFunctionWithPrototypeMethod() {
    String code = "function MyObject() {} MyObject.prototype.myMethod = function() { this.x = 1; };";
    Node node = Node.parseTree(code, null, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(node, check);
    traversal.traverse();
    assertFalse(diagnostics.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotatedProperty() {
    String code = "function MyObject() { this.x = 1; } MyObject.prototype.myMethod = function() { this.x = 2; }";
    Node node = Node.parseTree(code, null, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(node, check);
    traversal.traverse();
    DiagnosticMessage message = diagnostics.getFirstError();
    assertNotNull(message);
    assertTrue(message.getMessage().equals("dangerous use of the global 'this' object"));
  }

  @Test
  public void testAssignmentToVariable() {
    String code = "var x = this;";
    Node node = Node.parseTree(code, null, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(node, check);
    traversal.traverse();
    assertFalse(diagnostics.hasErrors());
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    String code = "var MyObject = function() {}; MyObject.prototype.x = this;";
    Node node = Node.parseTree(code, null, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(node, check);
    traversal.traverse();
    assertFalse(diagnostics.hasErrors());
  }

  @Test
  public void testAssignmentWithNestedParent() {
    String code = "a.x = this; function a() { b.y = this; }";
    Node node = Node.parseTree(code, null, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(node, check);
    traversal.traverse();
    assertFalse(diagnostics.hasErrors());
  }
}