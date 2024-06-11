package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeTraversal;

public class RenameVarsTest {
  Compiler compiler;
  RenameVars renameVars;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn();
    options.setLanguageOut("ECMASCRIPT5");
    compiler = new Compiler();
    compiler.setOptions(options);
    renameVars = new RenameVars(compiler, "", false, false, false, null, null,
        null);
  }

  @Test
  public void testSimpleRename() {
    String input = "function foo(a, b) { var c = a + b; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(1, 2); }");

    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);

    // Check that the local variable 'c' has a new name
    assertEquals("L0", traversal.getLocalVarName(0));

    // Check that the parameters have their original names
    assertEquals("a", traversal.getLocalVarName(1));
    assertEquals("b", traversals.getLocalVarName(2));

    // Check that the function call uses the new local variable name
    Node callNode = traversal.findFirstChildOfType(root, Node.CALL);
    assertEquals("L0", callNode.getFirstChild().getIdent().getName());
  }

  @Test
  public void testReservedNames() {
    String input = "function foo() { var if = 1; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(); }");

    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    assertEquals("if_", traversal.getLocalVarName(0));
  }

  @Test
  public void testAnonymousFunctions() {
    String input = "function foo() { var bar = function() { return 1; }; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(); }");

    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);

    // Check that the anonymous function keeps its original name
    Node anonymousFunction = traversal.getFirstChildOfType(
        traversal.getFirstChildOfType(root, Node.BLOCK), Node.CALL);
    assertEquals("bar", anonymousFunction.getFirstChild().getIdent().getName());
  }

  @Test
  public void testReusePreviousNames() {
    String input = "function foo() { var a = 1, b = 2, c = 3; }";

    String input2 = "function foo2() { var a = 4, b = 5, c = 6; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(); }");

    // First rename vars in 'foo'
    renameVars.process(externs, root);

    // Now rename vars in 'foo2'
    externs = Node.parse(input2);
    Node root2 = Node.parse("function main() { foo2(); }");
    renameVars.process(externs, root2);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);

    // Check that 'a' in 'foo2' has the same name as in 'foo'
    NodeTraversal traversal2 = NodeTraversal.create(compiler, root2, null);
    assertEquals(renamVars.getVariableMap().lookupName("a"),
        traversal2.getLocalVarName(0));

    // Check that 'b' in 'foo2' has a new name
    assertEquals("b_", traversal2.getLocalVarName(1));

    // Check that 'c' in 'foo2' has a new name
    assertEquals("c_", traversal2.getLocalVarName(2));
  }

  @Test
  public void testNoRename() {
    String input = "function foo() { var a = 1; this.b = 2; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(); }");

    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    assertEquals("a", traversal.getLocalVarName(0));
    assertEquals("b", traversal.getLocalVarName(1));
  }

  @Test
  public void testPreserveAnonymousFunctionNames() {
    String input = "function foo() { var anonymous = function() {}; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(); }");

    renameVars.preserveAnonymousFunctionNames = true;
    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    Node anonymousFunction = traversal.getFirstChildOfType(
        traversal.getFirstChildOfType(root, Node.BLOCK), Node.CALL);
    assertEquals("anonymous", anonymousFunction.getFirstChild().getIdent().getName());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidName() {
    String input = "function foo() { var a; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(); }");

    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    Node varNode = traversal.getFirstChildOfType(
        traversal.getFirstChildOfType(root, Node.BLOCK), Node.VAR);
    assertNotNull(varNode);

    String invalidName = "!";
    try {
      renameVars.okToRenameVar(invalidName, false);
    } catch (AssertionError e) {
      // expected
    }
  }

  @Test
  public void testMultipleassignments() {
    String input = "function foo() { var a = 1, b = 2, c = a = 3, d = 4; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(); }");

    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);

    // Check that 'a' has a new name
    assertEquals("a_", traversal.getLocalVarName(0));

    // Check that 'b' has a new name
    assertEquals("b_", traversal.getLocalVarName(1));

    // Check that 'c' has a new name
    assertEquals("c_", traversal.getLocalVarName(2));

    // Check that 'd' has a new name
    assertEquals("d_", traversal.getLocalVarName(3));
  }

  @Test
  public void testMixedAssignments() {
    String input = "function foo() { var a = 1, b = 2, c = a = 3, d = 4, a = 5; }";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() { foo(); }");

    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);

    // Check that 'a' has a new name
    assertEquals("a_", traversal.getLocalVarName(0));

    // Check that 'b' has a new name
    assertEquals("b_", traversal.getLocalVarName(1));

    // Check that 'c' has a new name
    assertEquals("c_", traversal.getLocalVarName(2));

    // Check that 'd' has a new name
    assertEquals("d_", traversal.getLocalVarName(3));

    // Check that 'a' has a new name for the second assignment
    assertEquals("a_1", traversal.getLocalVarName(4));
  }

  @Test
  public void testEmptyInput() {
    String input = "";

    Node externs = Node.parse(input);
    Node root = Node.parse("function main() {}");

    renameVars.process(externs, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    assertTrue(traversal.getFirstChildOfType(root, Node.BLOCK) == null);
  }
}