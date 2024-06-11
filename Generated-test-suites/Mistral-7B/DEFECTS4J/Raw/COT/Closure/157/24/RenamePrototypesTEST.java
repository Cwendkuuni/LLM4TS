import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {
  private AbstractCompiler compiler;
  private RenamePrototypes renamePrototypes;
  private CompilerInput input;

  @Before
  public void setUp() {
    compiler = new CompilerOptions().setOptCompile(CompilerOptions.Option.COMPILE, false)
        .setOptCompile(CompilerOptions.Option.AGGRESSIVE_RENAMING, false)
        .setOptCompile(CompilerOptions.Option.RESERVED_CHARS, "").buildCompiler();

    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    input = new CompilerInput("test.js");
  }

  @Test
  public void testBasicRenamePrototypeProperty() {
    Node root = createNode("Foo.prototype.foo = function() { }");
    renamePrototypes.process(null, root);
    Node foo = root.getFirstChild().getNext();
    assertEquals("foo_", foo.getString());
  }

  @Test
  public void testRenamePrototypePropertyWithLeadingUnderscore() {
    Node root = createNode("Foo.prototype._foo = function() { }");
    renamePrototypes.process(null, root);
    Node foo = root.getFirstChild().getNext();
    assertEquals("_foo", foo.getString());
  }

  @Test
  public void testRenamePrototypePropertyWithTrailingUnderscore() {
    Node root = createNode("Foo.prototype.foo_ = function() { }");
    renamePrototypes.process(null, root);
    Node foo = root.getFirstChild().getNext();
    assertEquals("foo", foo.getString());
  }

  @Test
  public void testRenamePrototypePropertyWithoutUnderscores() {
    Node root = createNode("Foo.prototype.bar = function() { }");
    renamePrototypes.process(null, root);
    Node bar = root.getFirstChild().getNext();
    assertFalse("bar should not be renamed", bar.getString().equals("bar_"));
  }

  @Test
  public void testRenamePrototypePropertyWithAggressiveRenaming() {
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    Node root = createNode("Foo.prototype.foo = function() { }");
    renamePrototypes.process(null, root);
    Node foo = root.getFirstChild().getNext();
    assertFalse("foo should be renamed aggressively", foo.getString().equals("foo_"));
  }

  @Test(expected = AssertionError.class)
  public void testRenamePrototypePropertyWithReservedName() {
    Node root = createNode("Foo.prototype.indexOf = function() { }");
    renamePrototypes.process(null, root);
  }

  @Test
  public void testRenameObjLitProperty() {
    Node root = createNode("var obj = { foo : 1 };");
    renamePrototypes.process(null, root);
    Node foo = root.getFirstChild().getNext();
    assertEquals("foo", foo.getString());
  }

  @Test(expected = AssertionError.class)
  public void testRenameExternedProperty() {
    Node root = createNode("window.foo = 1;");
    renamePrototypes.process(null, root);
  }

  @Test
  public void testRenamePrototypePropertyWithPrivateKeyword() {
    Node root = createNode("function Foo() { this._foo = 1; }");
    renamePrototypes.process(null, root);
    Node foo = root.getFirstChild().getNext();
    assertEquals("_foo", foo.getString());
  }

  @Test
  public void testRenamePrototypePropertyWithExportedKeyword() {
    Node root = createNode("exports.foo = function() { }");
    renamePrototypes.process(null, root);
    Node foo = root.getFirstChild();
    assertFalse("foo should not be renamed", foo.getString().equals("foo_"));
  }

  @Test
  public void testRenamePrototypePropertyWithMixedCasing() {
    Node root = createNode("Foo.prototype.Foo = function() { }");
    renamePrototypes.process(null, root);
    Node foo = root.getFirstChild().getNext();
    assertEquals("Foo", foo.getString());
  }

  private Node createNode(String code) {
    TokenStream ts = new TokenStream(new StringReader(code));
    Node n = new Parser(null, null, null).parseProgram(ts, null, null);
    return n.getFirstChild();
  }
}