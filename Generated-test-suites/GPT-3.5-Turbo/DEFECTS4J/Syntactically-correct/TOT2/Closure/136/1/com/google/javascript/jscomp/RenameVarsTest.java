import static org.junit.Assert.*;
import org.junit.Test;

public class RenameVarsTest {

    @Test
    public void testProcessVars() {
        RenameVars renameVars = new RenameVars(compiler, "test_", true, false, false, null, null, null);
        ProcessVars processVars = renameVars.new ProcessVars(false);
        
        // Test for a global variable
        Node globalNode = new Node(Token.NAME, "globalVar");
        processVars.incCount("globalVar", null);
        assertEquals(1, assignments.get("globalVar").count);

        // Test for a local variable
        Node localNode = new Node(Token.NAME, "localVar");
        Scope.Var localVar = new Scope.Var("localVar", true);
        Node parent = new Node(Token.FUNCTION);
        localVar.setParentNode(parent);
        processVars.incCount("localVar", null);
        assertEquals(1, assignments.get("localVar").count);
    }

    @Test
    public void testGetNewGlobalName() {
        RenameVars renameVars = new RenameVars(compiler, "test_", true, false, false, null, null, null);

        Node globalNode = new Node(Token.NAME, "globalVar");
        assignments.get("globalVar").setNewName("test_globalVar");
        assertEquals("test_globalVar", renameVars.getNewGlobalName(globalNode));
    }

    @Test
    public void testGetNewLocalName() {
        RenameVars renameVars = new RenameVars(compiler, "test_", true, false, false, null, null, null);

        Node localNode = new Node(Token.NAME, "localVar");
        localTempNames.add("test_localVar");
        assertEquals("test_localVar", renameVars.getNewLocalName(localNode, 0));
    }

    @Test
    public void testGetPseudoName() {
        RenameVars renameVars = new RenameVars(compiler, "test_", true, false, true, null, null, null);
        assertEquals("$test_var$$", renameVars.getPseudoName("var"));
    }

    @Test
    public void testAssignNames() {
        RenameVars renameVars = new RenameVars(compiler, "test_", true, false, false, null, null, null);
        Set<Assignment> varsToRename = new TreeSet<Assignment>(FREQUENCY_COMPARATOR);

        Assignment assignment1 = new Assignment("var1", null);
        assignment1.count = 3;
        Assignment assignment2 = new Assignment("var2", null);
        assignment2.count = 2;
        varsToRename.add(assignment1);
        varsToRename.add(assignment2);

        renameVars.assignNames(varsToRename);

        assertEquals("test_var1", assignment1.newName);
        assertEquals("test_var2", assignment2.newName);
    }

    @Test
    public void testOkToRenameVar() {
        RenameVars renameVars = new RenameVars(compiler, "test_", true, false, false, null, null, null);
        
        assertTrue(renameVars.okToRenameVar("var1", true)); // Local variable can be renamed
        assertFalse(renameVars.okToRenameVar("var2", false)); // Global variable can't be renamed
        assertFalse(renameVars.okToRenameVar("var3", false)); // Excluded variable can't be renamed
    }
}