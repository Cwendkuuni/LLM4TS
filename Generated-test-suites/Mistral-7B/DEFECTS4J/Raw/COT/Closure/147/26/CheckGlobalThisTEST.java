import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.bare.BareParser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function evil() { this.useful = undefined; }");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function EvilConstructor() { }");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("var EvilPrototype = {}; EvilPrototype.prototype.evil = function() { this.useful = undefined; }");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("/** @this */ function EvilThis() { this.useful = undefined; }");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_GlobalFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function evil() { this.useful = undefined; }");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testCheckGlobalThis_NestedAssignments() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("(a = this).evil = undefined;");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testCheckGlobalThis_GlobalThisAsProperty() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("this.globalThis = undefined;");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testCheckGlobalThis_LocalFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function evil() { function inner() { this.useful = undefined; } }");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_LocalThisAnnotation() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function evil() { /** @this */ function inner() { this.useful = undefined; } }");
    NodeTraversal traverser = new NodeTraversal(root, checker, context);
    traverser.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThis_NullParent() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel( WarningLevel.VERBOSE );
    AbstractCompiler compiler = new AbstractCompiler(new BareParser(), options);
    AbstractCompiler.Context context = new AbstractCompiler.Context();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.parse("function evil() { }");
    NodeTraversal traverser = new NodeTraversal(root, checker, null);
    traverser.traverse();

    // Should throw an AssertionError due to null parent
  }
}