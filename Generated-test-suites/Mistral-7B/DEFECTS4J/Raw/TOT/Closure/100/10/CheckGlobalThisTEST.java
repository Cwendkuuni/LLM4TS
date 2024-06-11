import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testSafeUsage() {
    String source = "function MyConstructor() {}";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(0, checker.getNumErrors());
  }

  @org.junit.Test
  public void testPrototypeMethod() {
    String source = "function MyObject() {} MyObject.prototype.method = function() {};";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(0, checker.getNumErrors());
  }

  @org.junit.Test
  public void testConstructorAnnotation() {
    String source = "function MyConstructor() {} @constructor";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(0, checker.getNumErrors());
  }

  @org.junit.Test
  public void testThisAnnotation() {
    String source = "function MyFunction() { @this }";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(0, checker.getNumErrors());
  }

  @org.junit.Test
  public void testAssignmentLeftSide() {
    String source = "function evil() { this = {}; }";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(1, checker.getNumErrors());
    DiagnosticType diagnostic = checker.getDiagnostics().get(0);
    assertTrue(diagnostic.toString().contains("JSC_USED_GLOBAL_THIS"));
  }

  @org.junit.Test
  public void testPropertyAccessLeftSide() {
    String source = "function evil() { x = this.y; }";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(1, checker.getNumErrors());
    DiagnosticType diagnostic = checker.getDiagnostics().get(0);
    assertTrue(diagnostic.toString().contains("JSC_USED_GLOBAL_THIS"));
  }

  @org.junit.Test
  public void testPropertyAccessPrototype() {
    String source = "function evil() { Object.prototype.x = this; }";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(0, checker.getNumErrors());
  }

  @org.junit.Test
  public void testAssignmentRightSide() {
    String source = "function evil() { this.x = {}; }";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(0, checker.getNumErrors());
  }

  @org.junit.Test
  public void testAssignmentRightSideWithNestedAssignment() {
    String source = "function evil() { this.x.y = {}; }";
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new WarningLevel());
    Node root = checker.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    new CheckGlobalThis(compiler, new WarningLevel()).visitAll(traversal);
    assertEquals(0, checker.getNumErrors());
  }
}