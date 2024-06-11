import static org.junit.Assert.*;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private Node createNode(String nodeType, int line, int column) {
    Node node = new Node();
    node.setType(nodeType);
    node.setLine(line);
    node.setColumn(column);
    return node;
  }

  @SuppressWarnings("unchecked")
  private Node createNameNode(String name) {
    Node node = new Node();
    node.setType(Token.NAME);
    node.setString(name);
    return node;
  }

  private void assertAssignmentsEqual(RenameVars expected, RenameVars actual) {
    assertEquals(expected.renameMap, actual.renameMap);
  }

  @SuppressWarnings("rawtypes")
  private void assertAssignmentsLogEqual(String expected, RenameVars actual) {
    assertEquals(expected, actual.assignmentLog.toString());
  }

  private void testRenameVars(String code, String expectedPrefix,
      boolean localRenamingOnly, boolean preserveAnonymousFunctionNames,
      boolean generatePseudoNames, String expectedRenameMap) {

    String inputCode = "(" + code + ")";
    CompilerPass pass = new RenameVars(
        new Compiler(), expectedPrefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, null, null, null);

    Node externs = NodeTraversal.createTree(inputCode);
    Node root = NodeTraversal.createTree("function foo() {}");
    pass.process(externs, root);

    RenameVars expectedRenameVars = new RenameVars(
        new Compiler(), expectedPrefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames,
        null, null, null);
    expectedRenameVars.process(externs, root);

    assertAssignmentsEqual(expectedRenameVars, pass);
    assertAssignmentsLogEqual(expectedRenameVars.assignmentLog.toString(), pass.assignmentLog);

    Map expectedRenameMap = expectedRenameVars.getVariableMap().getVariableMap();
    Map actualRenameMap = pass.getVariableMap().getVariableMap();
    assertEquals(expectedRenameMap.size(), actualRenameMap.size());
    for (Object k : expectedRenameMap.keySet()) {
      String key = (String) k;
      String actualValue = (String) actualRenameMap.get(key);
      String expectedValue = (String) expectedRenameMap.get(key);
      assertEquals(expectedValue, actualValue);
    }
  }

  @Test
  public void testSimpleRename() {
    testRenameVars("var a = 1, b = 1;", "", false, false, false,
        "{\n" +
        "  'a': 'a0'\n" +
        ",\n" +
        "  'b': 'b0'\n" +
        "}");
  }

  @Test
  public void testSimpleRenameWithPrefix() {
    testRenameVars("var a = 1, b = 1;", "renamed_", false, false, false,
        "{\n" +
        "  'a': 'renamed_a0'\n" +
        ",\n" +
        "  'b': 'renamed_b0'\n" +
        "}");
  }

  @Test
  public void testGlobalRename() {
    testRenameVars("function foo(a, b) { var c = a + b; }", "", false, false, false,
        "{\n" +
        "  'a': 'a0'\n" +
        ",\n" +
        "  'b': 'b0'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        "}");
  }

  @Test
  public void testLocalRename() {
    testRenameVars("function foo(a, b) { var c = a + b; }", "", true, false, false,
        "{\n" +
        "  'a': 'L0_a'\n" +
        ",\n" +
        "  'b': 'L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithPrefix() {
    testRenameVars("function foo(a, b) { var c = a + b; }", "renamed_", true, false, false,
        "{\n" +
        "  'a': 'renamed_L0_a'\n" +
        ",\n" +
        "  'b': 'renamed_L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        "}");
  }

  @Test
  public void testGlobalRenameWithAnonymousFunction() {
    testRenameVars("function foo(a, b) { var c = function() {}; }", "", false, false, false,
        "{\n" +
        "  'a': 'a0'\n" +
        ",\n" +
        "  'b': 'b0'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithAnonymousFunction() {
    testRenameVars("function foo(a, b) { var c = function() {}; }", "", true, false, false,
        "{\n" +
        "  'a': 'L0_a'\n" +
        ",\n" +
        "  'b': 'L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        "}");
  }

  @Test
  public void testPreserveAnonymousFunctionNames() {
    testRenameVars("function foo(a, b) { var c = function d() {}; }", "", false, true, false,
        "{\n" +
        "  'a': 'a0'\n" +
        ",\n" +
        "  'b': 'b0'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'd': 'd0'\n" +
        "}");
  }

  @Test
  public void testPreserveAnonymousFunctionNamesWithPrefix() {
    testRenameVars("function foo(a, b) { var c = function d() {}; }", "renamed_", false, true, false,
        "{\n" +
        "  'a': 'renamed_a0'\n" +
        ",\n" +
        "  'b': 'renamed_b0'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'd': 'renamed_d0'\n" +
        "}");
  }

  @Test
  public void testGeneratePseudoNames() {
    testRenameVars("function foo(a, b) { var c = a + b; }", "", false, false, true,
        "{\n" +
        "  'a': '$a$0'\n" +
        ",\n" +
        "  'b': '$b$1'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        "}");
  }

  @Test
  public void testGeneratePseudoNamesWithPrefix() {
    testRenameVars("function foo(a, b) { var c = a + b; }", "renamed_", false, false, true,
        "{\n" +
        "  'a': 'renamed$_a$0'\n" +
        ",\n" +
        "  'b': 'renamed$_b$1'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithPreserveAnonymousFunctionNamesAndGeneratePseudoNames() {
    testRenameVars("function foo(a, b) { var c = function d() {}; }", "", true, true, true,
        "{\n" +
        "  'a': 'L0_a'\n" +
        ",\n" +
        "  'b': 'L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'd': '$d$2'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithPreserveAnonymousFunctionNamesAndGeneratePseudoNamesWithPrefix() {
    testRenameVars("function foo(a, b) { var c = function d() {}; }", "renamed_", true, true, true,
        "{\n" +
        "  'a': 'renamed_L0_a'\n" +
        ",\n" +
        "  'b': 'renamed_L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'd': 'renamed_$_d$2'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithExternals() {
    testRenameVars("function foo(a, b) { var c = a + b; } var x = 1;", "", true, false, false,
        "{\n" +
        "  'a': 'L0_a'\n" +
        ",\n" +
        "  'b': 'L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'x': 'x'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithExternalsWithPrefix() {
    testRenameVars("function foo(a, b) { var c = a + b; } var x = 1;", "renamed_", true, false, false,
        "{\n" +
        "  'a': 'renamed_L0_a'\n" +
        ",\n" +
        "  'b': 'renamed_L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'x': 'renamed_x'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithExternalsWithPreserveAnonymousFunctionNames() {
    testRenameVars("function foo(a, b) { var c = function d() {}; } var x = 1;", "", true, true, false,
        "{\n" +
        "  'a': 'L0_a'\n" +
        ",\n" +
        "  'b': 'L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'd': 'd0'\n" +
        ",\n" +
        "  'x': 'x'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithExternalsWithPreserveAnonymousFunctionNamesAndGeneratePseudoNames() {
    testRenameVars("function foo(a, b) { var c = function d() {}; } var x = 1;", "renamed_", true, true, true,
        "{\n" +
        "  'a': 'renamed_L0_a'\n" +
        ",\n" +
        "  'b': 'renamed_L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'd': 'renamed_$_d$2'\n" +
        ",\n" +
        "  'x': 'renamed_x'\n" +
        "}");
  }

  @Test
  public void testLocalRenameWithExternalsWithPreserveAnonymousFunctionNamesAndGeneratePseudoNamesWithPrefix() {
    testRenameVars("function foo(a, b) { var c = function d() {}; } var x = 1;", "renamed_", true, true, true,
        "{\n" +
        "  'a': 'renamed_L0_a'\n" +
        ",\n" +
        "  'b': 'renamed_L1_b'\n" +
        ",\n" +
        "  'c': 'c0'\n" +
        ",\n" +
        "  'd': 'renamed_$_d$2'\n" +
        ",\n" +
        "  'x': 'renamed_x'\n" +
        "}");
  }

  @Test
  public void testRenameVarsWithExistingVariableMap() {
    String inputCode = "function foo(a, b) { var c = a + b; }";
    CompilerPass pass1 = new RenameVars(new Compiler(), "", false, false, false, null, null, null);
    Node externs1 = NodeTraversal.createTree(inputCode);
    Node root1 = NodeTraversal.createTree("function foo(a, b) { var c = a + b; }");
    pass1.process(externs1, root1);

    Map<String, String> variableMap1 = pass1.getVariableMap().getVariableMap();
    RenameVars expectedRenameVars = new RenameVars(
        new Compiler(), "", false, false, false,
        new VariableMap(variableMap1), null, null);
    Node externs2 = NodeTraversal.createTree(inputCode);
    Node root2 = NodeTraversal.createTree("function foo(a, b) { var c = a + b; }");
    expectedRenameVars.process(externs2, root2);

    assertAssignmentsEqual(expectedRenameVars, pass1);
    assertAssignmentsLogEqual(expectedRenameVars.assignmentLog.toString(), pass1.assignmentLog);
  }

  @Test
  public void testRenameVarsWithExistingVariableMapWithPrefix() {
    String inputCode = "function foo(a, b) { var c = a + b; }";
    CompilerPass pass1 = new RenameVars(new Compiler(), "renamed_", false, false, false,
        new VariableMap(Collections.singletonMap("a", "a0")), null, null);
    Node externs1 = NodeTraversal.createTree(inputCode);
    Node root1 = NodeTraversal.createTree("function foo(a, b) { var c = a + b; }");
    pass1.process(externs1, root1);

    Map<String, String> variableMap1 = pass1.getVariableMap().getVariableMap();
    RenameVars expectedRenameVars = new RenameVars(
        new Compiler(), "renamed_", false, false, false,
        new VariableMap(variableMap1), null, null);
    Node externs2 = NodeTraversal.createTree(inputCode);
    Node root2 = NodeTraversal.createTree("function foo(a, b) { var c = a + b; }");
    expectedRenameVars.process(externs2, root2);

    assertAssignmentsEqual(expectedRenameVars, pass1);
    assertAssignmentsLogEqual(expectedRenameVars.assignmentLog.toString(), pass1.assignmentLog);
  }
}