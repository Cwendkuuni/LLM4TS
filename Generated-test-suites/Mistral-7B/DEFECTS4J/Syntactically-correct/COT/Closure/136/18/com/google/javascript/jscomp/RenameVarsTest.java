package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@RunWith(JUnit4.class)
public class RenameVarsTest {
  private RenameVars renamer;
  private VariableMap prevUsedRenameMap;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new Compiler();
    String prefix = "testPrefix";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    Set<String> reservedNames = Sets.newHashSet();
    Set<Character> reservedCharacters = Sets.newHashSet();
    VariableMap prevVariableMap = new VariableMap();

    renamer = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevVariableMap,
        reservedCharacters.toArray(new char[0]), reservedNames);
    prevUsedRenameMap = new VariableMap();
  }

  @Test
  public void testRenameSimpleVariables() {
    String sourceCode = "function testFunction(x, y) { var z = x + y; }";
    Node externs = null;
    Node root = Parser.parse(sourceCode, null, null);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("x", "y", "z");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameExistingVariables() {
    String sourceCode = "function testFunction(x, y) { var z = x + y; var z = 10; }";
    Node externs = null;
    Node root = Parser.parse(sourceCode, null, null);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("x", "y", "z_1", "z_2");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameLocalVariablesWithPrefix() {
    String sourceCode = "function testFunction(x, y) { var L_a = x; var L_b = y; }";
    Node externs = null;
    Node root = Parser.parse(sourceCode, null, null);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("x", "y", "L_a", "L_b");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameGlobalVariables() {
    String sourceCode = "var x = 1,\n" +
        "        y = 2,\n" +
        "        z = 3;\n" +
        "\n" +
        "function testFunction() {\n" +
        "  var a = x,\n" +
        "      b = y,\n" +
        "      c = z;\n" +
        "}\n";
    Node externs = Parser.parse(sourceCode, null, null);
    Node root = Parser.parse("", null, externs);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("x", "y", "z", "a", "b", "c");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameGlobalVariablesWithExistingNames() {
    String sourceCode = "var x = 1,\n" +
        "        y = 2,\n" +
        "        z = 3;\n" +
        "\n" +
        "function testFunction() {\n" +
        "  var a = x,\n" +
        "      b = y,\n" +
        "      x = 5,\n" +
        "      z = 10;\n" +
        "}\n";
    Node externs = Parser.parse(sourceCode, null, null);
    Node root = Parser.parse("", null, externs);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("x_1", "y", "z_1", "a", "b", "x_2");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameLocalVariablesWithSameName() {
    String sourceCode = "function testFunction() {\n" +
        "  var x = 1,\n" +
        "      x = 2,\n" +
        "      x = 3;\n" +
        "}\n";
    Node externs = null;
    Node root = Parser.parse(sourceCode, null, null);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("x_1", "x_2", "x_3");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameVariablesWithSameNameInDifferentScopes() {
    String sourceCode = "function outerFunction() {\n" +
        "  var x = 1;\n" +
        "  function innerFunction() {\n" +
        "    var x = 2;\n" +
        "  }\n" +
        "}\n";
    Node externs = Parser.parse(sourceCode, null, null);
    Node root = Parser.parse("", null, externs);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("x_1", "outerFunction$x", "innerFunction$x");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameAnonymousFunction() {
    String sourceCode = "function testFunction() {\n" +
        "  (function() {\n" +
        "  }())\n" +
        "}\n";
    Node externs = null;
    Node root = Parser.parse(sourceCode, null, null);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("testFunction$anon$0");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameVariablesWithComplexNames() {
    String sourceCode = "function testFunction() {\n" +
        "  var a_b_c = 1,\n" +
        "      a_b_d = 2,\n" +
        "      a_e = 3,\n" +
        "      a_f_g = 4,\n" +
        "      x = 5,\n" +
        "      y = 6,\n" +
        "      z = 7;\n" +
        "}\n";
    Node externs = null;
    Node root = Parser.parse(sourceCode, null, null);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("a_b_c", "a_b_d", "a_e", "a_f_g", "x", "y", "z");

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  @Test
  public void testRenameVariablesWithSpecialChars() {
    String sourceCode = "function testFunction() {\n" +
        "  var $x = 1,\n" +
        "      %y = 2,\n" +
        "      @z = 3,\n" +
        "      ^a = 4,\n" +
        "      &b = 5,\n" +
        "      *c = 6,\n" +
        "      (d = 7),\n" +
        "      )e = 8,\n" +
        "      _f = 9,\n" +
        "      +g = 10,\n" +
        "      -h = 11,\n" +
        "      =i = 12,\n" +
        "      >j = 13,\n" +
        "      <k = 14,\n" +
        "      !l = 15,\n" +
        "      ~m = 16,\n" +
        "      |n = 17,\n" +
        "      |o = 18;\n" +
        "}\n";
    Node externs = null;
    Node root = Parser.parse(sourceCode, null, null);

    renamer.process(externs, root);

    Map<String, String> renameMap = renamer.getVariableMap();
    Set<String> expectedRenamedVariables =
        Sets.newHashSet("$x", "%y", "@z", "^a", "&b", "*c", "(d)", ")", "_f",
            "+" + getRenamedVariableName(renamer, "g"),
            "-" + getRenamedVariableName(renamer, "h"),
            "=" + getRenamedVariableName(renamer, "i"),
            ">" + getRenamedVariableName(renamer, "j"),
            "<" + getRenamedVariableName(renamer, "k"),
            "!" + getRenamedVariableName(renamer, "l"),
            "~" + getRenamedVariableName(renamer, "m"),
            "|" + getRenamedVariableName(renamer, "n"),
            "|" + getRenamedVariableName(renamer, "o"));

    assertEquals(expectedRenamedVariables.size(), renameMap.size());
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      assertEquals(expectedRenamedVariables.contains(entry.getKey()),
          entry.getValue() != null);
    }
  }

  /**
   * Helper method to get the renamed variable name for a specific test case.
   */
  private String getRenamedVariableName(RenameVars renamer, String name) {
    String prefix = renamer.prefix;
    for (int i = 1; i < 10; i++) {
      String candidate = prefix + i + name.substring(1);
      if (!renamer.okToRenameVar(candidate, false)) {
        continue;
      }
      return candidate;
    }
    throw new IllegalStateException("Could not find a valid renamed variable name");
  }
}