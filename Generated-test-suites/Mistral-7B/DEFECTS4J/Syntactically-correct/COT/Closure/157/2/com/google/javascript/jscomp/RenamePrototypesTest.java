import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.TokenStream;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes_NormalCase() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(new TokenStream(new StringReader("")), options);

    CompilerPassManager manager = new CompilerPassManager(compiler);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    manager.addPass(renamePrototypes);

    String inputCode = "function Foo() {} Foo.prototype.getBar = function() { return 42; }";
    Node root = compiler.parseTree(new CharStream(new StringReader(inputCode)), null, null, null);

    renamePrototypes.process(null, root);

    assertEquals("function Foo() {}\nFoo.prototype.getBar__ = function() { return 42; }",
        compiler.getSource(root));
  }

  @Test
  public void testRenamePrototypes_AggressiveRenaming() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(new TokenStream(new StringReader("")), options);

    CompilerPassManager manager = new CompilerPassManager(compiler);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    manager.addPass(renamePrototypes);

    String inputCode = "function Foo() {} Foo.prototype.getBar = function() { return 42; }";
    Node root = compiler.parseTree(new CharStream(new StringReader(inputCode)), null, null, null);

    renamePrototypes.process(null, root);

    assertEquals("function Foo() {}\nFoo.prototype.getBar__ = function() { return 42; }",
        compiler.getSource(root));

    inputCode = "function Foo() {} Foo.prototype.getBar = function() { return 42; }";
    root = compiler.parseTree(new CharStream(new StringReader(inputCode)), null, null, null);

    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(null, root);

    assertEquals("function Foo() {}\nFoo.prototype.getBar = function() { return 42; }",
        compiler.getSource(root));
  }

  @Test
  public void testRenamePrototypes_PrivateProperties() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(new TokenStream(new StringReader("")), options);

    CompilerPassManager manager = new CompilerPassManager(compiler);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    manager.addPass(renamePrototypes);

    String inputCode = "function Foo() { var _bar = 42; }";
    Node root = compiler.parseTree(new CharStream(new StringReader(inputCode)), null, null, null);

    renamePrototypes.process(null, root);

    assertEquals("function Foo() { var _bar__ = 42; }", compiler.getSource(root));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRenamePrototypes_NullInput() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(new TokenStream(new StringReader("")), options);

    CompilerPassManager manager = new CompilerPassManager(compiler);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    manager.addPass(renamePrototypes);

    renamePrototypes.process(null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testRenamePrototypes_NullCompiler() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = null;

    CompilerPassManager manager = new CompilerPassManager(compiler);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    manager.addPass(renamePrototypes);

    String inputCode = "function Foo() {} Foo.prototype.getBar = function() { return 42; }";
    Node root = compiler.parseTree(new CharStream(new StringReader(inputCode)), null, null, null);

    renamePrototypes.process(null, root);
  }
}