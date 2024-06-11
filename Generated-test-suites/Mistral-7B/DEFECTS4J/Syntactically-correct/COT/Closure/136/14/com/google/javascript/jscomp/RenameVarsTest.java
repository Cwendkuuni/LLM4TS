import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.NodeTraversal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private static <T> T newInstance(Class<T> clazz, Object... initArgs) throws Exception {
    return clazz.getConstructor(initArgs.getClass()).newInstance(initArgs);
  }

  /**
   * Basic test case for the process method which checks if the method is able to rename variables correctly.
   */
  @Test
  public void testProcess() {
    String prefix = "test_";
    CompilerPass renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node root = Node.newTextNode("function test() { var a = 1, b = 2; }");

    renameVars.process(null, root);

    // Assert new names are as expected
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(renameVars, null, root);
    List<Node> nodes = traversal.getWalkResult();
    assertEquals(2, nodes.size());
    Node aNode = nodes.get(0);
    Node bNode = nodes.get(1);

    assertNotNull(aNode.getName());
    assertEquals("test_a", aNode.getName().getString());
    assertNotNull(bNode.getName());
    assertEquals("test_b", bNode.getName().getString());
  }

  /**
   * Test edge case for renaming global variables with the same name.
   */
  @Test
  public void testRenameGlobalVariablesWithSameName() {
    String prefix = "test_";
    CompilerPass renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node root = Node.newTextNode("function test() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}");

    renameVars.process(null, root);

    // Assert new names are as expected
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    traversal.traverse(renameVars, null, root);
    List<Node> nodes = traversal.getWalkResult();
    assertEquals(1, nodes.size());
    Node aNode = nodes.get(0);

    assertNotNull(aNode.getName());
    assertEquals("test_a_1", aNode.getName().getString());
  }

  /**
   * Test exception handling scenario for renaming non-existent variables.
   */
  @Test(expected = RuntimeException.class)
  public void testRenameNonExistentVariable() {
    String prefix = "test_";
    CompilerPass renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node root = Node.newTextNode("function test() {" +
        "  var a = 1;" +
        "  var nonExistentVar = 2;" +
        "}");

    renameVars.process(null, root);
  }

  /**
   * Test edge case for renaming local variables with the same name.
   */
  @Test
  public void testRenameLocalVariablesWithSameName() {
    String prefix = "test_";
    CompilerPass renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    CompilerInput compilerInput1 = new CompilerInput("", 1, 1, 1, 1, null, null, null);
    CompilerInput compilerInput2 = new CompilerInput("", 1, 1, 1, 1, null, null, null);

    Node root = Node.newTextNode("function test() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}");

    renameVars.process(null, root);

    // Assert new names are as expected
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    Node localA = traversal.traverse(renameVars, null, root, Node.EXPRESSION)
        .getWalkResult().get(0);
    Node localB = traversal.traverse(renameVars, null, root, Node.EXPRESSION)
        .getWalkResult().get(1);

    assertNotNull(localA.getName());
    assertEquals("test_L0_a", localA.getName().getString());
    assertNotNull(localB.getName());
    assertEquals("test_L0_a_1", localB.getName().getString());
  }

  /**
   * Test edge case for renaming anonymous functions with the same name.
   */
  @Test
  public void testRenameAnonymousFunctionsWithSameName() {
    String prefix = "test_";
    CompilerPass renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node root = Node.newTextNode("function test() {" +
        "  var a = function() {};" +
        "  var a = function() {};" +
        "}");

    renameVars.process(null, root);

    // Assert new names are as expected
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    Node functionA = traversal.traverse(renameVars, null, root, Node.FUNCTION_DECLARATION)
        .getWalkResult().get(0);
    Node functionB = traversal.traverse(renameVars, null, root, Node.FUNCTION_DECLARATION)
        .getWalkResult().get(1);

    assertNotNull(functionA.getName());
    assertEquals("test_L0_$function$", functionA.getName().getString());
    assertNotNull(functionB.getName());
    assertEquals("test_L0_$function_1$", functionB.getName().getString());
  }

  /**
   * Test edge case for renaming external variables with the same name.
   */
  @Test
  public void testRenameExternalVariablesWithSameName() {
    String prefix = "test_";
    CompilerPass renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node root = Node.newTextNode("function test() {" +
        "  var x = 1;" +
        "  var __GOOGLE_EXPORT__x = 2;" +
        "}");

    renameVars.process(null, root);

    // Assert new names are as expected
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler());
    Node xNode = traversal.traverse(renameVars, null, root, Node.VAR_DECLARATION)
        .getWalkResult().get(0);
    Node xExportNode = traversal.traverse(renameVars, null, root, Node.ASSIGNMENT)
        .getWalkResult().get(0);

    assertNotNull(xNode.getName());
    assertEquals("test_x", xNode.getName().getString());
    assertNotNull(xExportNode.getName());
    assertEquals("test_x_1", xExportNode.getName().getString());
  }
}