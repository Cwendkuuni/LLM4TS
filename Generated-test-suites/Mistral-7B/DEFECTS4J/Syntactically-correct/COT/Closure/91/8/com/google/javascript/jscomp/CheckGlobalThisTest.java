import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.Token.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.util.CodeCoverage;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenFactory;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory nodeFactory;
  private TokenFactory tokenFactory;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checker = new Checker(compiler, new DiagnosticType[0]);
    nodeFactory = compiler.getFactory();
    tokenFactory = compiler.getTokenFactory();
  }

  @Test
  public void testCheckGlobalThis_NormalCase() {
    String source = "function foo() {}";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String source = "function Foo() {}";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithThis() {
    String source = "function Foo@this() {}";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoLogicalPlaceToPutThisAnnotation() {
    String source = "function foo() { this.bar = 1; }";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    String source = "var Foo = function() {}; Foo.prototype.bar = function() { this.baz = 1; }";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_AssignmentToPrototype() {
    String source = "var Foo = function() {}; Foo.prototype.bar = this;";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getType());
    assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testCheckGlobalThis_AssignmentToPrototypeWithLhs() {
    String source = "var Foo = function() {}; Foo.prototype.bar = { x: this };";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getType());
    assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testCheckGlobalThis_AssignmentToPrototypeWithLhsChild() {
    String source = "function Foo() { this.bar = { x: this.baz }; }";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_FunctionGettingLentToPrototype() {
    String source = "function Foo() {} Foo.prototype.bar = Foo.prototype.getBaz.bind(this);";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_FunctionGettingLentToPrototypeWithLhs() {
    String source = "function Foo() {} Foo.prototype.bar = function() { this.baz = 1; }.bind(this);";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThis_FunctionGettingLentToPrototypeWithLhsAndChild() {
    String source = "function Foo() { this.bar = { baz: this.qux }; } Foo.prototype.qux = function() { this.zux = 1; }.bind(this);";
    Node ast = Parser.parse(nodeFactory, tokenFactory, source);

    NodeTraversal traversal = new NodeTraversal(checker, ast);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }
}