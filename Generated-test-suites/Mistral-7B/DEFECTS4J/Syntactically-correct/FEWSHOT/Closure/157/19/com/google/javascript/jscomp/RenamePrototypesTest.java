package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

public class RenamePrototypesTest {

  @Test
  public void testProcess() {
    CompilerOptions options = new CompilerOptions(true, null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.foo = function() {}\n" +
        "var f = new Foo();\n" +
        "f.__proto__.foo = function() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype = { getBar: function() {}}");

    Node externs = input.getTree();
    Node root = input.getMain();

    pass.process(externs, root);

    assertNotNull(pass.getPropertyMap());

    // Check that Foo.prototype.foo was renamed
    assertEquals("fooRenamed", pass.getPropertyMap().lookupNewName("foo"));

    // Check that Foo.prototype was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("prototype"));

    // Check that Foo.prototype.bar was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("bar"));

    // Check that Foo.prototype.getBar was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("getBar"));
  }

  @Test
  public void testNoRename() {
    CompilerOptions options = new CompilerOptions(true, null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);

    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.foo = function() {}\n" +
        "var f = new Foo();\n" +
        "f.__proto__.foo = function() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype = { getBar: function() {}}");

    Node externs = input.getTree();
    Node root = input.getMain();

    pass.process(externs, root);

    assertNotNull(pass.getPropertyMap());

    // Check that Foo.prototype.foo was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("foo"));

    // Check that Foo.prototype was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("prototype"));

    // Check that Foo.prototype.bar was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("bar"));

    // Check that Foo.prototype.getBar was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("getBar"));
  }

  @Test
  public void testAggressiveRenaming() {
    CompilerOptions options = new CompilerOptions(true, null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.foo = function() {}\n" +
        "var f = new Foo();\n" +
        "f.__proto__.foo = function() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype = { getBar: function() {}}");

    Node externs = input.getTree();
    Node root = input.getMain();

    pass.process(externs, root);

    assertNotNull(pass.getPropertyMap());

    // Check that Foo.prototype.foo was renamed
    assertEquals("fooRenamed", pass.getPropertyMap().lookupNewName("foo"));

    // Check that Foo.prototype was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("prototype"));

    // Check that Foo.prototype.bar was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("bar"));

    // Check that Foo.prototype.getBar was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("getBar"));

    // Check that a property with a capital letter was renamed
    assertEquals("BazRenamed", pass.getPropertyMap().lookupNewName("Baz"));
  }

  @Test
  public void testReservedNames() {
    CompilerOptions options = new CompilerOptions(true, null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.indexOf = function() {}\n" +
        "var f = new Foo();\n" +
        "f.__proto__.lastIndexOf = function() {}\n" +
        "Foo.prototype.toString = function() {}\n" +
        "Foo.prototype.valueOf = function() {}");

    Node externs = input.getTree();
    Node root = input.getMain();

    pass.process(externs, root);

    assertNotNull(pass.getPropertyMap());

    // Check that indexOf was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("indexOf"));

    // Check that lastIndexOf was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("lastIndexOf"));

    // Check that toString was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("toString"));

    // Check that valueOf was not renamed
    assertNull(pass.getPropertyMap().lookupNewName("valueOf"));
  }

  @Test
  public void testPropertyMap() {
    CompilerOptions options = new CompilerOptions(true, null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.foo = function() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "var f = new Foo();\n" +
        "f.__proto__.foo = function() {}\n" +
        "Foo.prototype = { getBar: function() {}}");

    Node externs = input.getTree();
    Node root = input.getMain();

    pass.process(externs, root);

    VariableMap map = pass.getPropertyMap();

    // Check that fooRenamed maps to Foo.prototype.foo
    assertSame(map.lookupVariable("fooRenamed"), externs.getChild(2));

    // Check that getBar maps to Foo.prototype.getBar
    assertSame(map.lookupVariable("getBar"), externs.getChild(7));
  }
}