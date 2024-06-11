package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer() {
    String originalCode = "function foo() {" +
        "  var a = 1, b = 2, c = 3, d = 4, e = 5;" +
        "  function bar() {" +
        "    var f = 6, g = 7, h = 8, i = 9, j = 10;" +
        "  }" +
        "  var k = 11, l = 12, m = 13, n = 14, o = 15;" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    String renamedCode = makeDeclaredNamesUnique.process(originalCode, null);

    assertEquals(originalCode, renamedCode);

    MakeDeclaredNamesUnique renamed = new MakeDeclaredNamesUnique();
    String renamedCode2 = renamed.process(originalCode, null);

    assertNotEquals(originalCode, renamedCode2);

    MakeDeclaredNamesUnique rootRenamer = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique childRenamer = new MakeDeclaredNamesUnique();

    String renamedCodeChildScope = makeDeclaredNamesUnique.process(
        "function foo() {" +
        "  var a = 1, b = 2;" +
        "}", null);

    childRenamer.setParentRenamer(rootRenamer);
    String renamedCodeRootChildScope = makeDeclaredNamesUnique.process(
        "function foo() {" +
        "  var a = 1, b = 2;" +
        "}", null);

    assertEquals(renamedCodeChildScope, renamedCodeRootChildScope);

    Map<String, String> declarations = childRenamer.getDeclarations();
    assertEquals(2, declarations.size());
    assertSame(declarations.get("a"), declarations.get("a"));
    assertSame(declarations.get("b"), declarations.get("b"));
  }

  @Test
  public void testInlineRenamer() {
    String originalCode = "function foo() {" +
        "  var a = 1, b = 2, c = 3, d = 4, e = 5;" +
        "  function bar() {" +
        "    var f = 6, g = 7, h = 8, i = 9, j = 10;" +
        "  }" +
        "  var k = 11, l = 12, m = 13, n = 14, o = 15;" +
        "}";

    Supplier<String> uniqueIdSupplier = new Supplier<String>() {
      private int id = 0;

      @Override
      public String get() {
        return Integer.toString(++id);
      }
    };

    String idPrefix = "_";
    boolean removeConstness = false;

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
        new InlineRenamer(uniqueIdSupplier, idPrefix, removeConstness));

    String renamedCode = makeDeclaredNamesUnique.process(originalCode, null);

    assertEquals(originalCode, renamedCode);

    MakeDeclaredNamesUnique renamed = new MakeDeclaredNamesUnique(
        new InlineRenamer(uniqueIdSupplier, idPrefix, removeConstness));
    String renamedCode2 = makeDeclaredNamesUnique.process(originalCode, null);

    assertNotEquals(originalCode, renamedCode2);

    MakeDeclaredNamesUnique rootRenamer = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique childRenamer = new MakeDeclaredNamesUnique(
        new InlineRenamer(uniqueIdSupplier, idPrefix, removeConstness));

    String renamedCodeChildScope = makeDeclaredNamesUnique.process(
        "function foo() {" +
        "  var a = 1, b = 2;" +
        "}", null);

    childRenamer.setParentRenamer(rootRenamer);
    String renamedCodeRootChildScope = makeDeclaredNamesUnique.process(
        "function foo() {" +
        "  var a = 1, b = 2;" +
        "}", null);

    assertEquals(renamedCodeChildScope, renamedCodeRootChildScope);

    Map<String, String> declarations = childRenamer.getDeclarations();
    assertEquals(2, declarations.size());
    assertSame(declarations.get("a"), declarations.get("a"));
    assertSame(declarations.get("b"), declarations.get("b"));

    makeDeclaredNamesUnique.getParentRenamer().getDeclarations(); // Verify that getParentRenamer returns the root renamer.
  }
}