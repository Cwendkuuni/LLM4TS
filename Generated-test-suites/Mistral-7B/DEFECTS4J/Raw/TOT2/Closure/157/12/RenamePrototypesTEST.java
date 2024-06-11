package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperties() {
    String jsCode = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("bar", "b_1");

    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testDontRenamePrivatePrototypeProperties() {
    String jsCode = "function Foo() { var _bar = function() {}; }";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertTrue(expectedRenameMap.isEmpty());
    assertTrue(actualRenameMap.isEmpty());
  }

  @Test
  public void testDontRenameExportedPrototypeProperties() {
    String jsCode = "function Foo() { export function bar() {}; }";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertTrue(expectedRenameMap.isEmpty());
    assertTrue(actualRenameMap.isEmpty());
  }

  @Test
  public void testRenameObjLitProperties() {
    String jsCode = "var obj = { foo: 1, bar: 2 };";
    CompilerInput input = new CompilerInput(jsCode, "Obj.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("foo", "f_0");
    expectedRenameMap.put("bar", "b_1");

    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testDontRenameObjLitPropertiesWithQuotes() {
    String jsCode = "var obj = { \"foo\": 1, \"bar\": 2 };";
    CompilerInput input = new CompilerInput(jsCode, "Obj.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertTrue(expectedRenameMap.isEmpty());
    assertTrue(actualRenameMap.isEmpty());
  }

  @Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming() {
    String jsCode = "function Foo() {} Foo.prototype.qux = function() {};";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("qux", "q_1");

    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testDontRenamePrototypePropertiesWithNonLetterNames() {
    String jsCode = "function Foo() {} Foo.prototype.#bar = function() {};";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, false, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertTrue(expectedRenameMap.isEmpty());
    assertTrue(actualRenameMap.isEmpty());
  }

  @Test
  public void testRenamePrototypePropertiesAfterAssignment() {
    String jsCode = "function Foo() {} var proto = Foo.prototype; proto.bar = function() {};";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("bar", "b_1");

    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test(expected = AssertionError.class)
  public void testIllegalRename() {
    String jsCode = "function Foo() {} Foo.prototype.toString = function() {};";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    // This should fail since toString is a reserved name.
  }

  @Test
  public void testEmptyCode() {
    String jsCode = "";
    CompilerInput input = new CompilerInput(jsCode, "Empty.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertTrue(expectedRenameMap.isEmpty());
    assertTrue(actualRenameMap.isEmpty());
  }

  @Test
  public void testNestedPrototypeProperties() {
    String jsCode = "function Foo() {} Foo.prototype.qux = function() {}; Foo.prototype.quux = function() {}; Foo.prototype.quux.qux = function() {};";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("qux", "q_1");
    expectedRenameMap.put("quux", "q_2");
    expectedRenameMap.put("qux", "q_3");

    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testRenamePropertiesWithReservedNames() {
    String jsCode = "function Foo() {} Foo.prototype.indexOf = function() {}; Foo.prototype.lastIndexOf = function() {};";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, null, null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertTrue(expectedRenameMap.isEmpty());
    assertTrue(actualRenameMap.isEmpty());
  }

  @Test
  public void testRenamePropertiesWithReservedCharacters() {
    String jsCode = "function Foo() {} Foo.prototype.toString$ = function() {};";
    CompilerInput input = new CompilerInput(jsCode, "Foo.js", 0);

    RenamePrototypes renamePrototypes = new RenamePrototypes(null, true, "\\$".toCharArray(), null);
    renamePrototypes.process(null, input.getRootNode());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("toString$", "toString_$");

    Map<String, String> actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }
}