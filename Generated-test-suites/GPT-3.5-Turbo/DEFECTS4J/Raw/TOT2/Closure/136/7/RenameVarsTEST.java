import static org.junit.Assert.*;
import org.junit.Test;

public class RenameVarsTest {
  
  @Test
  public void testProcessVars() {
    // Initialize the RenameVars class
    RenameVars renameVars = new RenameVars(compiler, "test_", true, true, true, prevUsedRenameMap, null, null);
    
    // Test that the ProcessVars method correctly counts the number of occurrences of variable names in the source code
    // and keeps track of extern globals in the externNames map
    // Edge case: empty source code
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    renameVars.process(externs, root);
    // Check that the globalNameNodes and localNameNodes lists are empty
    assertEquals(0, renameVars.globalNameNodes.size());
    assertEquals(0, renameVars.localNameNodes.size());
    // Check that the externNames set is empty
    assertEquals(0, renameVars.externNames.size());
    // Check that the renameMap is empty
    assertEquals(0, renameVars.renameMap.size());
  }

  @Test
  public void testGetNewGlobalName() {
    // Initialize the RenameVars class
    RenameVars renameVars = new RenameVars(compiler, "test_", true, true, true, prevUsedRenameMap, null, null);
    
    // Test that the getNewGlobalName method correctly returns the new name for a global variable
    Node node = new Node(Token.NAME, "oldName");
    node.setString("oldName");
    renameVars.globalNameNodes.add(node);
    Assignment assignment = new Assignment("oldName", null);
    assignment.setNewName("newName");
    renameVars.assignments.put("oldName", assignment);
    String result = renameVars.getNewGlobalName(node);
    // Check that the correct new name is returned
    assertEquals("newName", result);
  }

  @Test
  public void testGetNewLocalName() {
    // Initialize the RenameVars class
    RenameVars renameVars = new RenameVars(compiler, "test_", true, true, true, prevUsedRenameMap, null, null);
    
    // Test that the getNewLocalName method correctly returns the new name for a local variable
    Node node = new Node(Token.NAME, "tempName");
    node.setString("tempName");
    renameVars.localNameNodes.add(node);
    renameVars.localTempNames.add("tempName");
    Assignment assignment = new Assignment("tempName", null);
    assignment.setNewName("newTempName");
    renameVars.assignments.put("tempName", assignment);
    String result = renameVars.getNewLocalName(node, 0);
    // Check that the correct new name is returned
    assertEquals("newTempName", result);
  }

  @Test
  public void testProcess() {
    // Initialize the RenameVars class
    RenameVars renameVars = new RenameVars(compiler, "test_", true, true, true, prevUsedRenameMap, null, null);
    
    // Test the process method
    // Edge case: empty source code
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    renameVars.process(externs, root);
    // Check that no changes are reported to the compiler
    assertFalse(compiler.hasCodeChanged());
  }

  @Test
  public void testAssignNames() {
    // Initialize the RenameVars class
    RenameVars renameVars = new RenameVars(compiler, "test_", true, true, true, prevUsedRenameMap, null, null);
    
    // Test the assignNames method
    
    // Create a set of assignments with varying frequencies
    Assignment assignment1 = new Assignment("var1", null);
    assignment1.count = 3;
    Assignment assignment2 = new Assignment("var2", null);
    assignment2.count = 2;
    Assignment assignment3 = new Assignment("var3", null);
    assignment3.count = 1;
    Set<Assignment> varsToRename = new TreeSet<>(FREQUENCY_COMPARATOR);
    varsToRename.add(assignment1);
    varsToRename.add(assignment2);
    varsToRename.add(assignment3);
    
    // Call the assignNames method
    renameVars.assignNames(varsToRename);
    
    // Check that the assignments have been finalized with new names
    assertNotNull(assignment1.newName);
    assertNotNull(assignment2.newName);
    assertNotNull(assignment3.newName);
  }
}