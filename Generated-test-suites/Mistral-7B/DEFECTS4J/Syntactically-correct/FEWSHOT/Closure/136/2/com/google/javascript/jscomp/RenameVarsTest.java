package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RenameVarsTest {

  @Test
  public void testProcessVars() {
    String code = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractCompiler.parseTree(code, 0, null, null, null);
    Node root = AbstractCompiler.parseTree(code, 0, null, null, null);

    renameVars.process(externs, root);

    List<String> expectedLocalNames = Arrays.asList("L 0", "L 1", "L 2", "L 3", "L 4");
    List<String> actualLocalNames = new ArrayList<>();
    for (Node localNameNode : renameVars.localNameNodes) {
      actualLocalNames.add(localNameNode.getString());
    }
    Collections.sort(expectedLocalNames);
    Collections.sort(actualLocalNames);
    assertEquals(expectedLocalNames, actualLocalNames);

    List<String> expectedGlobals = Arrays.asList("foo", "c");
    Set<String> actualGlobals = new TreeSet<>(renameVars.globalNameNodes.stream().map(Node::getString).collect(Collectors.toList()));
    assertEquals(expectedGlobals, actualGlobals);
  }

  @Test
  public void testProcessVarsWithExterns() {
    String code = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = 3;\n" +
        "  var d = 4;\n" +
        "  var e = 5;\n" +
        "}\n" +
        "function bar() {\n" +
        "  var a = 6;\n" +
        "  var b = 7;\n" +
        "  var c = 8;\n" +
        "  var d = 9;\n" +
        "  var e = 10;\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractCompiler.parseTree(code, 0, null, null, null);
    Node root = AbstractCompiler.parseTree(code, 0, null, null, null);

    renameVars.process(externs, root);

    Set<String> expectedExterns = Sets.newHashSet("foo", "bar", "a", "b", "c", "d", "e");
    Set<String> actualExterns = new TreeSet<>(renameVars.globalNameNodes.stream().map(Node::getString).collect(Collectors.toList()));
    assertEquals(expectedExterns, actualExternals);

    List<String> expectedLocals = Arrays.asList("L 0", "L 1", "L 2", "L 3", "L 4", "L 5", "L 6", "L 7", "L 8", "L 9", "L 10");
    List<String> actualLocals = new ArrayList<>();
    for (Node localNameNode : renameVars.localNameNodes) {
      actualLocals.add(localNameNode.getString());
    }
    Collections.sort(expectedLocals);
    Collections.sort(actualLocals);
    assertEquals(expectedLocals, actualLocals);
  }

  @Test
  public void testProcessVarsWithPreserveAnonymousFunctionNames() {
    String code = "function foo() {\n" +
        "  (function(a, b) {\n" +
        "    var c = a + b;\n" +
        "  })(1, 2);\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, true, false, null, null, null);

    Node externs = AbstractCompiler.parseTree(code, 0, null, null, null);
    Node root = AbstractCompiler.parseTree(code, 0, null, null, null);

    renameVars.process(externs, root);

    List<String> expectedLocals = Arrays.asList("(anonymous_function)_0", "(anonymous_function)_1", "(anonymous_function)_2");
    List<String> actualLocals = new ArrayList<>();
    for (Node localNameNode : renameVars.localNameNodes) {
      actualLocals.add(localNameNode.getString());
    }
    Collections.sort(expectedLocals);
    Collections.sort(actualLocals);
    assertEquals(expectedLocals, actualLocals);
  }

  @Test
  public void testProcessVarsWithGeneratePseudoNames() {
    String code = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = 3;\n" +
        "  var d = 4;\n" +
        "  var e = 5;\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, true, null, null, null);

    Node externs = AbstractCompiler.parseTree(code, 0, null, null, null);
    Node root = AbstractCompiler.parseTree(code, 0, null, null, null);

    renameVars.process(externs, root);

    List<String> expectedLocals = Arrays.asList("$1$$a", "$2$$b", "$3$$c", "$4$$d", "$5$$e");
    List<String> actualLocals = new ArrayList<>();
    for (Node localNameNode : renameVars.localNameNodes) {
      actualLocals.add(localNameNode.getString());
    }
    Collections.sort(expectedLocals);
    Collections.sort(actualLocals);
    assertEquals(expectedLocals, actualLocals);
  }

  @Test
  public void testProcessVarsWithReservedNames() {
    String code = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var __define = 2;\n" +
        "  var b = 3;\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, Sets.newHashSet("__define"));

    Node externs = AbstractCompiler.parseTree(code, 0, null, null, null);
    Node root = AbstractCompiler.parseTree(code, 0, null, null, null);

    renameVars.process(externs, root);

    List<String> expectedLocals = Arrays.asList("a", "b");
    List<String> actualLocals = new ArrayList<>();
    for (Node localNameNode : renameVars.localNameNodes) {
      actualLocals.add(localNameNode.getString());
    }
    Collections.sort(expectedLocals);
    Collections.sort(actualLocals);
    assertEquals(expectedLocals, actualLocals);

    Set<String> actualReservedNames = new TreeSet<>(renameVars.reservedNames);
    assertEquals(Arrays.asList("__define"), actualReservedNames);
  }

  @Test
  public void testProcessVarsWithExternNamesCollision() {
    String code = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var a = 3;\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);

    Node externs = AbstractCompiler.parseTree(code, 0, null, null, null);
    Node root = AbstractCompiler.parseTree(code, 0, null, null, null);

    renameVars.process(externs, root);

    List<String> expectedLocals = Arrays.asList("a_1", "b");
    List<String> actualLocals = new ArrayList<>();
    for (Node localNameNode : renameVars.localNameNodes) {
      actualLocals.add(localNameNode.getString());
    }
    Collections.sort(expectedLocals);
    Collections.sort(actualLocals);
    assertEquals(expectedLocals, actualLocals);

    Set<String> actualReservedNames = new TreeSet<>(renameVars.reservedNames);
    assertEquals(Collections.emptyList(), actualReservedNames);
  }
}