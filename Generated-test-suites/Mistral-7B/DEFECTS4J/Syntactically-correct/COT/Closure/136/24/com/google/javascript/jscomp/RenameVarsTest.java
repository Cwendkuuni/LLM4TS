import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isAnonymousFunction;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;
import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    String prefix = "test_";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    char[] reservedCharacters = null;
    Set<String> reservedNames = new HashSet<>();

    // Add some reserved names for testing
    reservedNames.add("TEST");
    reservedNames.add("test");
    reservedNames.add("_test");
    reservedNames.add("Test");

    RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    // Create test input nodes
    Node externs = Node.objectLiteral();
    Node root = Node.functionLiteral(null, null, Node.BLOCK, null);

    // Add some test variables and functions to the externs and root nodes
    addNodeToParent(externs, Node.name("testVar1"));
    addNodeToParent(externs, Node.name("testVar2"));
    addNodeToParent(externs, Node.name("testFunc1"));
    addNodeToParent(externs, Node.name("testFunc2"));
    addNodeToParent(root, Node.name("testLocalVar1"));
    addNodeToParent(root, Node.name("testLocalVar2"));
    addNodeToParent(root, Node.name("testLocalFunc1"));
    addNodeToParent(root, Node.name("testLocalFunc2"));

    // Add some test anonymous functions to the root node
    Node anonymousFunc1 = Node.functionLiteral(null, null, Node.BLOCK, null);
    Node anonymousFunc2 = Node.functionLiteral(null, null, Node.BLOCK, null);
    addNodeToParent(root, anonymousFunc1);
    addNodeToParent(root, anonymousFunc2);

    // Perform the renameVars transformation
    renameVars.process(externs, root);

    // Assert that the renamed nodes have the expected names
    assertNamesEqual(externs, Arrays.asList(
        "test_testVar1", "test_testVar2", "test_testFunc1", "test_testFunc2"
    ));
    assertNamesEqual(root, Arrays.asList(
        "test_testLocalVar1", "test_testLocalVar2", "test_testLocalFunc1",
        "test_testLocalFunc2", "_test_testLocalFunc1", "_test_testLocalFunc2"
    ));

    // Assert that anonymous functions are preserved if preserveAnonymousFunctionNames is false
    assertFalse(isAnonymousFunction(root.getChild(0).getChild(0)));
    assertFalse(isAnonymousFunction(root.getChild(1).getChild(0)));

    // Assert that anonymous functions are not renamed if preserveAnonymousFunctionNames is true
    assertTrue(isAnonymousFunction(root.getChild(0).getChild(0)));
    assertTrue(isAnonymousFunction(root.getChild(1).getChild(0)));
  }

  private void addNodeToParent(Node parent, Node node) {
    if (parent != null && node != null) {
      parent.addChild(node);
    }
  }

  private void assertNamesEqual(Node node, List<String> expectedNames) {
    Multimap<String, Node> originalNames = ArrayListMultimap.create();
    NodeTraversal.traverse(null, node, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME) {
          originalNames.put(n.getString(), n);
        }
      }
    });

    List<String> actualNames = new ArrayList<>();
    for (Node n : originalNames.values()) {
      actualNames.add(getNodeName(n));
    }

    assertEquals("Unexpected names in the given node.", expectedNames, actualNames);
  }

  private String getNodeName(Node node) {
    if (node != null) {
      return node.getString();
    }
    return null;
  }
}