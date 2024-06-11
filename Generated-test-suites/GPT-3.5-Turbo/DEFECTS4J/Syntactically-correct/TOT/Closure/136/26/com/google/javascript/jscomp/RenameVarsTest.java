import static org.junit.Assert.*;
import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    // Instantiate the RenameVars class
    RenameVars renameVars = new RenameVars();

    // Test the process method
    renameVars.process(externs, root);

    // Test the getVariableMap method
    VariableMap variableMap = renameVars.getVariableMap();
    assertNotNull(variableMap);

    // Test the renaming of global variables
    assertEquals("newName1", variableMap.getNewName("oldName1"));
    assertEquals("newName2", variableMap.getNewName("oldName2"));

    // Test the renaming of local variables
    assertEquals("newLocalVar1", variableMap.getNewName("oldLocalVar1"));
    assertEquals("newLocalVar2", variableMap.getNewName("oldLocalVar2"));
  }

  @Test
  public void testOkToRenameVar() {
    // Instantiate the RenameVars class
    RenameVars renameVars = new RenameVars();

    // Test with a local variable that is not exported
    boolean result1 = renameVars.okToRenameVar("localVar", true);
    assertTrue(result1);

    // Test with a global variable that is exported
    boolean result2 = renameVars.okToRenameVar("exportedVar", false);
    assertFalse(result2);
  }

}