import static org.junit.Assert.*;
import org.junit.Test;

public class RenameVarsTest {
  
  @Test
  public void testProcess() {
    // Test case for the process method of RenameVars class

    // Initialize a mock compiler and create an instance of RenameVars
    MockCompiler compiler = new MockCompiler();
    RenameVars renameVars = new RenameVars(compiler, "prefix", false, false, false, null, null, null);
    
    // Create some sample nodes for testing
    Node externs = new Node(Token.EXTERNs, new Node(Token.NAME, "externNode1"));
    Node root = new Node(Token.ROOT, new Node(Token.NAME, "rootNode1"), new Node(Token.NAME, "rootNode2"));

    // Invoke the process method with the sample nodes
    renameVars.process(externs, root);

    // Verify that the process method executed successfully
    assertNotNull(compiler.getDebugLog());
    assertEquals("JS var assignments:\n", compiler.getDebugLog());
  }

  @Test
  public void testGetNewGlobalName() {
    // Test case for the getNewGlobalName method of RenameVars class

    // Initialize a mock node with a sample name
    Node node = new Node(Token.NAME, "sampleName");

    // Create an instance of RenameVars and set up assignments
    RenameVars renameVars = new RenameVars(new MockCompiler(), "prefix", false, false, false, null, null, null);
    Assignment assignment = renameVars.new Assignment("sampleName", null);
    assignment.setNewName("newName");
    renameVars.assignments.put("sampleName", assignment);

    // Invoke the getNewGlobalName method with the mock node
    String newName = renameVars.getNewGlobalName(node);

    // Verify that the correct new name is returned
    assertEquals("newName", newName);
  }

  @Test
  public void testGetNewLocalName() {
    // Test case for the getNewLocalName method of RenameVars class

    // Initialize a mock node with a sample name
    Node node = new Node(Token.NAME, "localName1");

    // Create an instance of RenameVars and set up localTempNames
    RenameVars renameVars = new RenameVars(new MockCompiler(), "prefix", false, false, false, null, null, null);
    renameVars.localTempNames.add("tempName1");

    // Invoke the getNewLocalName method with the mock node and index
    String newName = renameVars.getNewLocalName(node, 0);

    // Verify that the correct new name is returned
    assertEquals("tempName1", newName);
  }

  @Test
  public void testGetPseudoName() {
    // Test case for the getPseudoName method of RenameVars class

    // Initialize a sample string
    String name = "sampleName";

    // Create an instance of RenameVars with generatePseudoNames set to true
    RenameVars renameVars = new RenameVars(new MockCompiler(), "prefix", false, false, true, null, null, null);

    // Invoke the getPseudoName method with the sample string
    String pseudoName = renameVars.getPseudoName(name);

    // Verify that the correct pseudo name is generated
    assertEquals("$sampleName$$", pseudoName);
  }

  @Test
  public void testReusePreviouslyUsedVariableMap() {
    // Test case for the reusePreviouslyUsedVariableMap method of RenameVars class

    // Create an instance of RenameVars with a mock prevUsedRenameMap
    VariableMap prevUsedRenameMap = new VariableMap();
    prevUsedRenameMap.addMapping("oldName", "prevNewName");

    RenameVars renameVars = new RenameVars(new MockCompiler(), "prefix", false, false, false, prevUsedRenameMap, null, null);

    // Invoke the method to reuse previously used variable map
    renameVars.reusePreviouslyUsedVariableMap();

    // Verify that the mapping is added to reservedNames and renameMap
    assertTrue(renameVars.reservedNames.contains("prevNewName"));
    assertTrue(renameVars.renameMap.containsKey("oldName"));
  }

  @Test
  public void testAssignNames() {
    // Test case for the assignNames method of RenameVars class

    // Create an instance of RenameVars with some sample assignments
    Assignment assignment1 = new Assignment("name1", null);
    Assignment assignment2 = new Assignment("name2", null);

    Set<Assignment> assignments = new TreeSet<>();
    assignments.add(assignment1);
    assignments.add(assignment2);

    RenameVars renameVars = new RenameVars(new MockCompiler(), "prefix", false, false, false, null, null, null);

    // Invoke the assignNames method with the sample assignments
    renameVars.assignNames(assignments);

    // Verify that the assignments are finalized with new names
    assertNotNull(assignment1.newName);
    assertNotNull(assignment2.newName);
  }

  @Test
  public void testFinalizeNameAssignment() {
    // Test case for the finalizeNameAssignment method of RenameVars class

    // Create an instance of Assignment with an old name
    Assignment assignment = new RenameVars(new MockCompiler(), "prefix", false, false, false, null, null, null).new Assignment("oldName", null);

    // Create an instance of RenameVars and assign a new name
    RenameVars renameVars = new RenameVars(new MockCompiler(), "prefix", false, false, false, null, null, null);
    String newName = "newName";

    // Invoke the method to finalize the name assignment
    renameVars.finalizeNameAssignment(assignment, newName);

    // Verify that the new name is correctly assigned to the assignment
    assertEquals("newName", assignment.newName);
    assertTrue(renameVars.renameMap.containsKey("oldName"));
  }

  @Test
  public void testGetVariableMap() {
    // Test case for the getVariableMap method of RenameVars class

    // Create an instance of RenameVars with some mapping
    RenameVars renameVars = new RenameVars(new MockCompiler(), "prefix", false, false, false, null, null, null);
    renameVars.renameMap.put("oldName", "newName");

    // Invoke the getVariableMap method
    VariableMap variableMap = renameVars.getVariableMap();

    // Verify that the variable map is correctly returned
    assertNotNull(variableMap);
    assertTrue(variableMap.containsKey("oldName"));
    assertEquals("newName", variableMap.lookupNewName("oldName"));
  }

  @Test
  public void testOkToRenameVar() {
    // Test case for the okToRenameVar method of RenameVars class

    // Create an instance of RenameVars and set up some sample data
    RenameVars renameVars = new RenameVars(new MockCompiler(), "prefix", false, false, false, null, null, null);
    renameVars.reservedNames.add("reservedName");

    // Test renaming local variable with reserved name
    assertFalse(renameVars.okToRenameVar("reservedName", true));

    // Test renaming global variable with reserved name
    assertFalse(renameVars.okToRenameVar("reservedName", false));

    // Test renaming non-reserved variable
    assertTrue(renameVars.okToRenameVar("nonReservedName", false));
  }
}