import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testSafeUsage() {
    Compiler compiler = new Compiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.addCallback(check);

    String code = "function safe() { return this.x; }";
    compiler.compile(new Node(new Token(Token.PROGRAM, null, code), null), null, null);
    traversal.traverse(compiler.getRootNode());
    traversal.detach();

    assertTrue(check.level.wasInvoked(WARNING, 0));
    assertFalse(check.level.wasInvoked(WARNING, 1));
  }

  @org.junit.Test
  public void testConstructor() {
    Compiler compiler = new Compiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.addCallback(check);

    String code = "function Foo() { this.x = 0; }";
    compiler.compile(new Node(new Token(Token.PROGRAM, null, code), null), null, null);
    traversal.traverse(compiler.getRootNode());
    traversal.detach();

    assertTrue(check.level.wasInvoked(WARNING, 0));
    assertFalse(check.level.wasInvoked(WARNING, 1));
  }

  @org.junit.Test
  public void testPrototypeMethod() {
    Compiler compiler = new Compiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.addCallback(check);

    String code = "function Foo() { this.x = 0; } Foo.prototype.y = function() { return this.x; }";
    compiler.compile(new Node(new Token(Token.PROGRAM, null, code), null), null, null);
    traversal.traverse(compiler.getRootNode());
    traversal.detach();

    assertFalse(check.level.wasInvoked(WARNING, 0));
    assertFalse(check.level.wasInvoked(WARNING, 1));
  }

  @org.junit.Test
  public void testFunctionWithNoAnnotation() {
    Compiler compiler = new Compiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.addCallback(check);

    String code = "function evil() { this.x = 0; }";
    compiler.compile(new Node(new Token(Token.PROGRAM, null, code), null), null, null);
    traversal.traverse(compiler.getRootNode());
    traversal.detach();

    assertTrue(check.level.wasInvoked(WARNING, 0));
    assertFalse(check.level.wasInvoked(WARNING, 1));
  }

  @org.junit.Test
  public void testAssignmentToPrototypeProperty() {
    Compiler compiler = new Compiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.addCallback(check);

    String code = "function Foo() {} Foo.prototype.x = this;";
    compiler.compile(new Node(new Token(Token.PROGRAM, null, code), null), null, null);
    traversal.traverse(compiler.getRootNode());
    traversal.detach();

    assertFalse(check.level.wasInvoked(WARNING, 0));
    assertFalse(check.level.wasInvoked(WARNING, 1));
  }

  @org.junit.Test
  public void testAssignmentToPrototypeSubProperty() {
    Compiler compiler = new Compiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.addCallback(check);

    String code = "function Foo() {} Foo.prototype.a = { x: this };";
    compiler.compile(new Node(new Token(Token.PROGRAM, null, code), null), null, null);
    traversal.traverse(compiler.getRootNode());
    traversal.detach();

    assertFalse(check.level.wasInvoked(WARNING, 0));
    assertFalse(check.level.wasInvoked(WARNING, 1));
  }
}