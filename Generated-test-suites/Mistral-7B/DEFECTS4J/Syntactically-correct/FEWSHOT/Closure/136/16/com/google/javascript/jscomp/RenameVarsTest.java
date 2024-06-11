package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    AbstractCompiler compiler = new Compiler();
    String prefix = "test_";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    @Nullable char[] reservedCharacters = null;
    @Nullable Set<String> reservedNames = new HashSet<>();

    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

    String inputCode = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "}";

    Node externs = NodeOperations.createTopLevelNodeFromString(inputCode, null);
    Node root = NodeOperations.createTopLevelNodeFromString("function bar() { return foo(1, 2) }", null);

    renameVars.process(externs, root);

    String outputCode = NodeOperations.getSource(root);

    String expectedCode = "function bar() {\n" +
        "  var L_1 = function(L_2, L_3) { return foo(L_2, L_3) }\n" +
        "  var L_4 = 1;\n" +
        "  var L_5 = 2;\n" +
        "  function foo(a, b) {\n" +
        "    var c = function(d, e) { return a };\n" +
        "    var L_6 = 1;\n" +
        "    var L_7 = 2;\n" +
        "  }\n" +
        "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testRenameVarsWithReservedNames() {
    AbstractCompiler compiler = new Compiler();
    String prefix = "test_";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    @Nullable char[] reservedCharacters = null;
    @Nullable Set<String> reservedNames = new HashSet<String>() {{
      add("console");
      add("alert");
    }};

    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

    String inputCode = "function foo() {\n" +
        "  console.log('Hello, world!');\n" +
        "}";

    Node externs = NodeOperations.createTopLevelNodeFromString(inputCode, null);
    Node root = NodeOperations.createTopLevelNodeFromString("", null);

    renameVars.process(externs, root);

    String outputCode = NodeOperations.getSource(root);

    String expectedCode = "function foo() {\n" +
        "  var t$0$ = java.lang.System.out;\n" +
        "  t$0$.println(\"Hello, world!\");\n" +
        "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testRenameVarsWithLocalRenamingOnly() {
    AbstractCompiler compiler = new Compiler();
    String prefix = "test_";
    boolean localRenamingOnly = true;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    @Nullable char[] reservedCharacters = null;
    @Nullable Set<String> reservedNames = new HashSet<>();

    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

    String inputCode = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "}";

    Node externs = NodeOperations.createTopLevelNodeFromString(inputCode, null);
    Node root = NodeOperations.createTopLevelNodeFromString("function bar() { return foo(1, 2) }", null);

    renameVars.process(externs, root);

    String outputCode = NodeOperations.getSource(root);

    String expectedCode = "function bar() {\n" +
        "  var L_1 = function(L_2, L_3) { return foo(L_2, L_3) }\n" +
        "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testRenameVarsWithPreserveAnonymousFunctionNames() {
    AbstractCompiler compiler = new Compiler();
    String prefix = "test_";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = true;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    @Nullable char[] reservedCharacters = null;
    @Nullable Set<String> reservedNames = new HashSet<>();

    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

    String inputCode = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; };\n" +
        "}";

    Node externs = NodeOperations.createTopLevelNodeFromString(inputCode, null);
    Node root = NodeOperations.createTopLevelNodeFromString("function bar() { return foo(1, 2) }", null);

    renameVars.process(externs, root);

    String outputCode = NodeOperations.getSource(root);

    String expectedCode = "function bar() {\n" +
        "  var c$0$ = function(d, e) { return foo(d, e) };\n" +
        "  foo(1, 2);\n" +
        "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testRenameVarsWithGeneratePseudoNames() {
    AbstractCompiler compiler = new Compiler();
    String prefix = "test_";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = true;
    VariableMap prevUsedRenameMap = null;
    @Nullable char[] reservedCharacters = null;
    @Nullable Set<String> reservedNames = new HashSet<>();

    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

    String inputCode = "function foo(a, b) {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "}";

    Node externs = NodeOperations.createTopLevelNodeFromString(inputCode, null);
    Node root = NodeOperations.createTopLevelNodeFromString("function bar() { return foo(1, 2) }", null);

    renameVars.process(externs, root);

    String outputCode = NodeOperations.getSource(root);

    String expectedCode = "function bar() {\n" +
        "  var $x$0$ = 1;\n" +
        "  var $y$1$ = 2;\n" +
        "  function foo(a, b) {\n" +
        "    var x = $x$0$\n" +
        "    var y = $y$1$\n" +
        "  }\n" +
        "}";

    assertEquals(expectedCode, outputCode);
  }
}