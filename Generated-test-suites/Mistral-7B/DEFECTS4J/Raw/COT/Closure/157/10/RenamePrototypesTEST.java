package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);

    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var x = new Foo();\n" +
                   "x.bar();\n";

    // When
    Node ast = compiler.parseTree(input, null);
    renamer.process(compiler.getExterns(), ast);
    String output = compiler.getSourceFileText(ast);

    // Then
    String expected = "function Foo() {}\n" +
                      "Foo.prototype.__c = function() {}\n" +
                      "var x = new Foo();\n" +
                      "x.__c();";

    assertEquals("Renamed prototype property", expected, output);
  }

  @Test
  public void testAggressiveRenaming() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);

    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var x = new Foo();\n" +
                   "x.bar();\n";

    // When
    Node ast = compiler.parseTree(input, null);
    renamer.process(compiler.getExterns(), ast);
    String output = compiler.getSourceFileText(ast);

    // Then
    String expected = "function Foo() {}\n" +
                      "Foo.prototype._bar = function() {}\n" +
                      "var x = new Foo();\n" +
                      "x._bar();";

    assertEquals("Renamed prototype property aggressively", expected, output);
  }

  @Test
  public void testPrototypePropertyRenamingWithExtern() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);

    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "// Externed property: Foo.prototype.indexOf = [];\n" +
                   "var x = new Foo();\n" +
                   "x.bar();\n";

    // When
    Node ast = compiler.parseTree(input, null);
    renamer.process(compiler.getExterns(), ast);
    String output = compiler.getSourceFileText(ast);

    // Then
    String expected = "function Foo() {}\n" +
                      "Foo.prototype.__c = function() {}\n" +
                      "// Externed property: Foo.prototype.indexOf = []\n" +
                      "var x = new Foo();\n" +
                      "x.__c();";

    assertEquals("Renamed prototype property but left externed property unchanged",
                expected, output);
  }

  @Test
  public void testRenameObjLitProperty() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);

    String input = "var obj = {\n" +
                   "  bar: 1,\n" +
                   "  baz: 2\n" +
                   "};\n" +
                   "obj.bar = 3;\n";

    // When
    Node ast = compiler.parseTree(input, null);
    renamer.process(compiler.getExterns(), ast);
    String output = compiler.getSourceFileText(ast);

    // Then
    String expected = "var obj = {\n" +
                      "  __b: 1,\n" +
                      "  baz: 2\n" +
                      "};\n" +
                      "obj.__b = 3;";

    assertEquals("Renamed obj lit property", expected, output);
  }

  @Test
  public void testRenameObjLitPropertyWithAggressiveRenaming() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);

    String input = "var obj = {\n" +
                   "  bar: 1,\n" +
                   "  baz: 2\n" +
                   "};\n" +
                   "obj.bar = 3;\n";

    // When
    Node ast = compiler.parseTree(input, null);
    renamer.process(compiler.getExterns(), ast);
    String output = compiler.getSourceFileText(ast);

    // Then
    String expected = "var obj = {\n" +
                      "  _bar: 1,\n" +
                      "  baz: 2\n" +
                      "};\n" +
                      "obj._bar = 3;";

    assertEquals("Renamed obj lit property aggressively", expected, output);
  }

  @Test
  public void testRenameProtoPropertiesWithSameName() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);

    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var x = new Foo();\n" +
                   "x.bar();\n" +
                   "x.bar();";

    // When
    Node ast = compiler.parseTree(input, null);
    renamer.process(compiler.getExterns(), ast);
    String output = compiler.getSourceFileText(ast);

    // Then
    String expected = "function Foo() {}\n" +
                      "Foo.prototype.__b = function() {}\n" +
                      "Foo.prototype.__b_1 = function() {}\n" +
                      "var x = new Foo();\n" +
                      "x.__b();\n" +
                      "x.__b_1();";

    assertEquals("Renamed prototype properties with same name", expected, output);
  }

  @Test
  public void testRenamePrototypePropertiesWithSameNameAndAggressiveRenaming() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);

    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var x = new Foo();\n" +
                   "x.bar();\n" +
                   "x.bar();";

    // When
    Node ast = compiler.parseTree(input, null);
    renamer.process(compiler.getExterns(), ast);
    String output = compiler.getSourceFileText(ast);

    // Then
    String expected = "function Foo() {}\n" +
                      "Foo.prototype._bar = function() {}\n" +
                      "Foo.prototype._bar_1 = function() {}\n" +
                      "var x = new Foo();\n" +
                      "x._bar();\n" +
                      "x._bar_1();";

    assertEquals("Renamed prototype properties with same name aggressively", expected, output);
  }

  @Test(expected = java.lang.IllegalArgumentException.class)
  public void testRenamePrototypePropertiesWithSameNameAndNoRenaming() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);

    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var x = new Foo();\n" +
                   "x.bar();\n" +
                   "x.bar();";

    // When
    Node ast = compiler.parseTree(input, null);
    renamer.process(compiler.getExterns(), ast);
    String output = compiler.getSourceFileText(ast);

    // Then
    // This test should throw an IllegalArgumentException since we cannot
    // rename prototype properties with the same name without aggressive
    // renaming
  }
}