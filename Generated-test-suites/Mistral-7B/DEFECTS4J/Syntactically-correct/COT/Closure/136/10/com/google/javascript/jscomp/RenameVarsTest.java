import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeTraversal;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.*;

public class RenameVarsTest {

  @Test
  public void testProcess() {
    // Test case 1: No variables to rename
    String inputCode = "function foo() {}";
    CompilerPass renameVars = new RenameVars(null, null, false, false, false, null, null, null);
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = Node.createTree(inputCode);
    Node root = Node.createTree(inputCode);

    renameVars.process(externs, root);

    // Test case 2: Simple variables renaming
    String inputCode2 = "function foo(a, b) { var c = a + b; }";
    CompilerPass renameVars2 = new RenameVars(null, null, false, false, false, null, null, null);
    AbstractCompiler compiler2 = new AbstractCompiler();
    Node externs2 = Node.createTree(inputCode2);
    Node root2 = Node.createTree(inputCode2);

    renameVars2.process(externs2, root2);

    // Test case 3: Variables with prefix renaming
    String inputCode3 = "function foo(a, b) { var fa = a; var fb = b; }";
    CompilerPass renameVars3 = new RenameVars(null, "_fa", false, false, false, null, null, null);
    AbstractCompiler compiler3 = new AbstractCompiler();
    Node externs3 = Node.createTree(inputCode3);
    Node root3 = Node.createTree(inputCode3);

    renameVars3.process(externs3, root3);

    // Test case 4: Variables renaming with exceptions handling
    String inputCode4 = "function foo(a, b) { try { var c = a; } catch (e) { var d = e; } }";
    CompilerPass renameVars4 = new RenameVars(null, null, false, false, false, null, null, null);
    AbstractCompiler compiler4 = new AbstractCompiler();
    Node externs4 = Node.createTree(inputCode4);
    Node root4 = Node.createTree(inputCode4);

    renameVars4.process(externs4, root4);

    // Test case 5: Variables renaming with reserved words
    String inputCode5 = "function foo(if, else) { var a = if; var b = else; }";
    CompilerPass renameVars5 = new RenameVars(null, null, false, false, false, null, null, null);
    AbstractCompiler compiler5 = new AbstractCompiler();
    Node externs5 = Node.createTree(inputCode5);
    Node root5 = Node.createTree(inputCode5);

    try {
      renameVars5.process(externs5, root5);
      fail("Expected IllegalNameException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test case 6: Variables renaming with external variables
    String inputCode6 = "var a = 1, b = 2, c = 3; function foo(a, b) { }";
    CompilerPass renameVars6 = new RenameVars(null, null, false, false, false, null, null, null);
    AbstractCompiler compiler6 = new AbstractCompiler();
    Node externs6 = Node.createTree(inputCode6);
    Node root6 = Node.createTree(inputCode6);

    try {
      renameVars6.process(externs6, root6);
      fail("Expected IllegalNameException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetVariableMap() {
    String inputCode = "function foo(a, b) { var c = a + b; }";
    CompilerPass renameVars = new RenameVars(null, null, false, false, false, null, null, null);
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = Node.createTree(inputCode);
    Node root = Node.createTree(inputCode);

    renameVars.process(externs, root);

    VariableMap variableMap = renameVars.getVariableMap();

    Multimap<String, String> expectedMap = ArrayListMultimap.create();
    expectedMap.put("a", "a");
    expectedMap.put("b", "b");
    expectedMap.put("c", "c");

    assertEquals(expectedMap, variableMap);
  }
}