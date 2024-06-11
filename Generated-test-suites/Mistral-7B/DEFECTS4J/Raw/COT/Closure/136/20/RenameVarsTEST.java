import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.Assignment.*;
import static com.google.javascript.jscomp.RenameVars.ProcessVars.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.*;

import org.junit.*;

public class RenameVarsTest {

  @BeforeClass
  public static void setUpBeforeClass() {
    // Set up JsCompiler for testing
    AbstractCompiler compiler = new AbstractCompiler();
  }

  @AfterClass
  public static void tearDownAfterClass() {
    // Tear down JsCompiler
  }

  @Test
  public void testRenameVarsEmpty() {
    String prefix = "";
    RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node root = NodeOperations.createRoot();
    renamer.process(NodeOperations.createExterns(), root);
    assertEquals(0, renamer.assignments.size());
  }

  @Test
  public void testRenameVarsSimple() {
    String prefix = "";
    RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node externs = NodeOperations.createExterns();
    Node root = NodeOperations.createRoot();
    Node varNode = NodeOperations.createName("varA");
    Node assignmentNode = NodeOperations.createAssignment(NAME, varNode, NUM_LITERAL, 1);
    Node functionNode = NodeOperations.createFunctionDeclaration("functionA", LIST, NONE, NONE, LIST,
        NodeOperations.createBlock(NODE_LIST, assignmentNode));
    Node functionCallNode = NodeOperations.createCall(NAME, NodeOperations.createName("functionA"), LIST);
    externs.addChild(varNode);
    externs.addChild(functionNode);
    root.addChild(functionCallNode);

    renamer.process(externs, root);

    // The variable and function names should not be renamed since they are externs
    assertEquals("varA", varNode.getString());
    assertEquals("functionA", functionNode.getString());

    // The function call name should be renamed to the assigned variable name
    assertEquals("varA", functionCallNode.getFirstChild().getString());
  }

  @Test
  public void testRenameVarsLocalVariables() {
    String prefix = "";
    RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node externs = NodeOperations.createRoot();
    Node root = NodeOperations.createRoot();
    Node functionNode = NodeOperations.createFunctionDeclaration("functionA", LIST, NONE, NONE, LIST,
        NodeOperations.createBlock(NODE_LIST,
            NodeOperations.createVariable("varA"),
            NodeOperations.createVariable("varB"),
            NodeOperations.createAssignment(NAME, NodeOperations.createName("varA"), NUM_LITERAL, 1),
            NodeOperations.createAssignment(NAME, NodeOperations.createName("varB"), NUM_LITERAL, 2)
        ));
    root.addChild(functionNode);

    renamer.process(externs, root);

    // The local variables should be renamed to temporary names
    List<Assignment> assignments = new ArrayList<>(renamer.assignments.values());
    assertEquals(2, assignments.size());
    assertEquals("varA", assignments.get(0).oldName);
    assertEquals("L 0", assignments.get(0).newName);
    assertEquals("varB", assignments.get(1).oldName);
    assertEquals("L 1", assignments.get(1).newName);
  }

  @Test
  public void testRenameVarsDuplicateVariables() {
    String prefix = "";
    RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node externs = NodeOperations.createRoot();
    Node root = NodeOperations.createRoot();
    Node functionNode = NodeOperations.createFunctionDeclaration("functionA", LIST, NONE, NONE, LIST,
        NodeOperations.createBlock(NODE_LIST,
            NodeOperations.createVariable("varA"),
            NodeOperations.createVariable("varA"),
            NodeOperations.createAssignment(NAME, NodeOperations.createName("varA"), NUM_LITERAL, 1),
            NodeOperations.createAssignment(NAME, NodeOperations.createName("varA"), NUM_LITERAL, 2)
        ));
    root.addChild(functionNode);

    renamer.process(externs, root);

    // The duplicate variables should be renamed to different temporary names
    List<Assignment> assignments = new ArrayList<>(renamer.assignments.values());
    assertEquals(2, assignments.size());
    assertEquals("varA", assignments.get(0).oldName);
    assertEquals("L 0", assignments.get(0).newName);
    assertEquals("varA", assignments.get(1).oldName);
    assertEquals("L 1", assignments.get(1).newName);
  }

  @Test
  public void testRenameVarsFunctionCall() {
    String prefix = "";
    RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
    Node externs = NodeOperations.createRoot();
    Node root = NodeOperations.createRoot();
    Node functionNode = NodeOperations.createFunctionDeclaration("functionA", LIST, NONE, NONE, LIST,
        NodeOperations.createBlock(NODE_LIST,
            NodeOperations.createVariable("varA"),
            NodeOperations.createAssignment(NAME, NodeOperations.createName("varA"), NUM_LITERAL, 1),
            NodeOperations.createFunctionDeclaration("functionB", LIST, NONE, NONE, LIST,
                NodeOperations.createBlock(NODE_LIST,
                    NodeOperations.createVariable("varB"),
                    NodeOperations.createAssignment(NAME, NodeOperations.createName("varB"), NUM_LITERAL, 2),
                    NodeOperations.createCall(NAME, NodeOperations.createName("functionA"))
                )
            )
        ));
    root.addChild(functionNode);

    renamer.process(externs, root);

    // The function call name should be renamed based on the renamed function name
    List<Assignment> assignments = new ArrayList<>(renamer.assignments.values());
    assertEquals(3, assignments.size());
    assertEquals("varA", assignments.get(0).oldName);
    assertEquals("L 0", assignments.get(0).newName);
    assertEquals("varB", assignments.get(1).oldName);
    assertEquals("L 1", assignments.get(1).newName);
    assertEquals("functionA", assignments.get(2).oldName);
    assertEquals("functionL_0", assignments.get(2).newName);
  }

  @Test
  public void testRenameVarsPreserveAnonymousFunctions() {
    String prefix = "";
    RenameVars renamer = new RenameVars(null, prefix, false, true, false, null, null, null);
    Node externs = NodeOperations.createRoot();
    Node root = NodeOperations.createRoot();
    Node functionNode = NodeOperations.createFunctionDeclaration("functionA", LIST, NONE, NONE, LIST,
        NodeOperations.createBlock(NODE_LIST,
            NodeOperations.createVariable("varA"),
            NodeOperations.createFunctionDeclaration("anonymousFunction", NONE, NONE, NONE, LIST,
                NodeOperations.createBlock(NODE_LIST,
                    NodeOperations.createVariable("varAnonymous"),
                    NodeOperations.createAssignment(NAME, NodeOperations.createName("varAnonymous"), NUM_LITERAL, 1),
                    NodeOperations.createReturnValue(NAME, NodeOperations.createName("varAnonymous"))
                )
            ),
            NodeOperations.createAssignment(NAME, NodeOperations.createName("varA"), NodeOperations.createName("anonymousFunction"))
        ));
    root.addChild(functionNode);

    renamer.process(externs, root);

    // The anonymous function name should not be renamed
    List<Assignment> assignments = new ArrayList<>(renamer.assignments.values());
    assertEquals(4, assignments.size());
    assertEquals("varA", assignments.get(0).oldName);
    assertEquals("L 0", assignments.get(0).newName);
    assertEquals("varAnonymous", assignments.get(1).oldName);
    assertEquals("anonymousFunction", assignments.get(1).newName);
    assertEquals("functionL_0", assignments.get(2).oldName);
    assertEquals("functionL_0", assignments.get(2).newName);
  }

  @Test(expected = IllegalStateException.class)
  public void testRenameVarsInvalidConstructor() {
    String prefix = "";
    RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, "invalidSet");
  }

  @Test
  public void testRenameVarsReservedNames() {
    String prefix = "";
    Set<String> reservedNames = new HashSet<>();
    RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, reservedNames);
    Node externs = NodeOperations.createRoot();
    Node root = NodeOperations.createRoot();
    Node varNode = NodeOperations.createName("varReserved");
    externs.addChild(varNode);
    Node functionNode = NodeOperations.createFunctionDeclaration("functionA", LIST, NONE, NONE, LIST,
        NodeOperations.createBlock(NODE_LIST, NodeOperations.createVariable("varA")));
    root.addChild(functionNode);

    try {
      renamer.process(externs, root);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertTrue(e.getMessage().contains("Reserved name: varReserved"));
    }
  }

  @Test
  public void testRenameVarsReservedNamesWithPrefix() {
    String prefix = "prefix_";
    Set<String> reservedNames = new HashSet<>();
    RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, reservedNames);
    Node externs = NodeOperations.createRoot();
    Node root = NodeOperations.createRoot();
    Node varNode = NodeOperations.createName("prefix_varReserved");
    externs.addChild(varNode);
    Node functionNode = NodeOperations.createFunctionDeclaration("functionA", LIST, NONE, NONE, LIST,
        NodeOperations.createBlock(NODE_LIST, NodeOperations.createVariable("varA")));
    root.addChild(functionNode);

    try {
      renamer.process(externs, root);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertTrue(e.getMessage().contains("Reserved name: prefix_varReserved"));
    }
  }
}