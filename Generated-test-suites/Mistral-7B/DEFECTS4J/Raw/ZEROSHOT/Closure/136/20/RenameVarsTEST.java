import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVarsTest.createRenameVars;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.*;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private static Node createNode(int type, int line, int pos) {
    return NodeFactory.createNodeFromString(
        String.format("%s(%d,%d)", Node.NAME, type, new Object[]{line, pos}),
        line, pos);
  }

  @SuppressWarnings("unchecked")
  private static List<Node> createNodes(int size) {
    List<Node> nodes = new ArrayList<Node>();
    for (int i = 0; i < size; i++) {
      nodes.add(createNode(Token.NAME, 1, 1));
    }
    return nodes;
  }

  private static RenameVars createRenameVars(
      AbstractCompiler compiler,
      String prefix,
      boolean localRenamingOnly,
      boolean preserveAnonymousFunctionNames,
      boolean generatePseudoNames,
      VariableMap prevUsedRenameMap,
      char[] reservedCharacters,
      Set<String> reservedNames) {

    return new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames,
        prevUsedRenameMap, reservedCharacters, reservedNames);
  }

  @Test
  public void testRenameVars() {
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = createRenameVars(compiler, null, false, false,
        false, null, null, Sets.newHashSet());

    List<Node> rootNodes = createNodes(4);
    Node root = NodeTraversal.buildScope(rootNodes, NodeFactory.rootScope());
    renameVars.process(NodeTraversal.getTopLevelScope(root), root);

    for (Node node : rootNodes) {
      String oldName = node.getString();
      Node renamedNode = NodeTraversal.findSingleChildOfType(root, Node.NAME);
      String newName = renamedNode.getString();

      // Check the name got renamed
      assertEquals("Expected old name: " + oldName, oldName,
          renameVars.getVariableMap().lookupOldName(newName));

      // Check that renamed variables are not renamed again
      assertNull("Expected old name: " + oldName,
          renameVars.getVariableMap().lookupOldName(oldName));
    }

    // Test empty variablename map
    renameVars = createRenameVars(compiler, null, false, false,
        false, null, null, Collections.emptySet());
    renameVars.process(NodeTraversal.getTopLevelScope(root), root);
    for (Node node : rootNodes) {
      String name = node.getString();
      assertNull("Expected name: " + name,
          renameVars.getVariableMap().lookupOldName(name));
    }

    // Test preserveAnonymousFunctionNames
    renameVars = createRenameVars(compiler, null, false, true,
        false, null, null, Sets.newHashSet());
    Node anonFunc = NodeFactory.createFunction(null, null, null, null,
        createNodes(2), null, null, null, null, null, null, null);
    ListMultimap<Integer, Node> children = NodeTraversal.getChildren(root);
    children.put(0, anonFunc);
    renameVars.process(NodeTraversal.getTopLevelScope(root), root);
    Node renamedAnonFunc = NodeTraversal.findSingleChildOfType(root,
        Node.FUNCTION);
    assertEquals("Expected old name: anonymous function", "anonymous function",
        renameVars.getVariableMap().lookupOldName(
            renamedAnonFunc.getChild(0).getString()));

    // Test generatePseudoNames
    renameVars = createRenameVars(compiler, null, false, false,
        true, null, null, Sets.newHashSet());
    renameVars.process(NodeTraversal.getTopLevelScope(root), root);
    for (Node node : rootNodes) {
      String oldName = node.getString();
      Node renamedNode = NodeTraversal.findSingleChildOfType(root, Node.NAME);
      String newName = renamedNode.getString();

      // Check the new name is unique
      assertFalse("Name should be unique: " + newName,
          renameVars.getVariableMap().lookupOldName(newName) != null);

      // Check the new name contains a pseudo name
      assertTrue("Expected pseudo name: " + newName,
          newName.startsWith("$"));

      // Check the old name got renamed
      assertEquals("Expected old name: " + oldName, oldName,
          renameVars.getVariableMap().lookupOldName(newName));
    }

    // Test localRenamingOnly
    renameVars = createRenameVars(compiler, null, true, false,
        false, null, null, Sets.newHashSet());
    Node localVarNode1 = createNode(Token.VAR, 1, 1);
    Node localVarNode2 = createNode(Token.VAR, 1, 2);
    Node localScope = NodeTraversal.buildScope(
        Arrays.asList(localVarNode1, localVarNode2), NodeFactory.rootScope());
    ListMultimap<Integer, Node> children = NodeTraversal.getChildren(root);
    children.put(1, localScope);
    renameVars.process(NodeTraversal.getTopLevelScope(root), root);

    // Check the local variables got renamed
    List<Node> localScopeNodes = NodeTraversal.getChildren(localScope, Node.NAME);
    for (Node node : localScopeNodes) {
      String oldName = node.getString();
      Node renamedNode = NodeTraversal.findSingleChildOfType(root, Node.NAME);
      String newName = renamedNode.getString();

      // Check the name got renamed
      assertEquals("Expected old name: " + oldName, oldName,
          renameVars.getVariableMap().lookupOldName(newName));

      // Check that renamed variables are not renamed again
      assertNull("Expected old name: " + oldName,
          renameVars.getVariableMap().lookupOldName(oldName));
    }

    // Check that the global variables did not get renamed
    for (Node node : rootNodes) {
      String oldName = node.getString();
      Node renamedNode = NodeTraversal.findSingleChildOfType(root, Node.NAME);
      String newName = renamedNode == null ? oldName : renamedNode.getString();
      assertNull("Expected old name: " + oldName,
          renameVars.getVariableMap().lookupOldName(newName));
    }
  }
}