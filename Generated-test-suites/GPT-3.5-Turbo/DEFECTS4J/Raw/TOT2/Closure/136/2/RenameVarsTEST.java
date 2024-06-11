import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RenameVarsTest {

    private RenameVars renameVars;

    @Before
    public void setUp() {
        renameVars = new RenameVars(mock(AbstractCompiler.class), "test", false, true, true, null, null, null);
    }

    @Test
    public void testProcessVars() {
        ProcessVars processVars = renameVars.new ProcessVars(false);

        Node node = mock(Node.class);
        NodeTraversal t = mock(NodeTraversal.class);
        Node parent = mock(Node.class);

        when(node.getType()).thenReturn(Token.NAME);
        when(node.getString()).thenReturn("testName");

        processVars.visit(t, node, parent);

        assertEquals(1, renameVars.globalNameNodes.size());
        assertEquals(node, renameVars.globalNameNodes.get(0));
    }

    @Test
    public void testGetNewGlobalName() {
        Node node = mock(Node.class);
        when(node.getString()).thenReturn("oldName");

        Assignment assignment = renameVars.new Assignment("oldName", null);
        assignment.setNewName("newName");
        renameVars.assignments.put("oldName", assignment);

        String result = renameVars.getNewGlobalName(node);

        assertEquals("newName", result);
    }

    @Test
    public void testGetNewLocalName() {
        Node node = mock(Node.class);
        when(node.getString()).thenReturn("oldName");

        Assignment assignment = renameVars.new Assignment("localVar_0", null);
        assignment.setNewName("temp");
        renameVars.assignments.put("localVar_0", assignment);

        String result = renameVars.getNewLocalName(node, 0);

        assertEquals("temp", result);
    }

    @Test
    public void testGeneratePseudoName() {
        renameVars.generatePseudoNames = true;
        String result = renameVars.getPseudoName("test");

        assertEquals("$test$$", result);
    }
    
    @Test
    public void testOkToRenameVar() {
        boolean result = renameVars.okToRenameVar("testName", true);
        assertTrue(result);
    }
}