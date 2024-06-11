
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;

public class RenameVarsTest {

    private RenameVars renameVars;

    @Before
    public void setUp() {
        renameVars = new RenameVars(new Compiler(), "", false, false, false, null, null, null);
    }

    @Test
    public void testExtractAndListPublicMethods() {
        Set<String> expectedMethods = new HashSet<String>();
        expectedMethods.add("process");
        assertEquals(expectedMethods, renameVars.extractAndListPublicMethods());
    }

    @Test
    public void testProcess() {
        // Test case for externs
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        renameVars.process(externs, root);

        // Compare the global and local name nodes after processing
        assertEquals(new ArrayList<Node>(), renameVars.getGlobalNameNodes());
        assertEquals(new ArrayList<Node>(), renameVars.getLocalNameNodes());

        // Compare the local temporary names after processing
        assertEquals(new ArrayList<String>(), renameVars.getLocalTempNames());

        // Compare the extern names after processing
        assertEquals(new HashSet<String>(), renameVars.getExternNames());

        // Compare the reserved names list after processing
        assertEquals(new HashSet<String>(), renameVars.getReservedNames());

        // Compare the rename map after processing
        assertEquals(new HashMap<String, String>(), renameVars.getRenameMap());
    }

    @Test
    public void testOkToRenameVar() {
        assertTrue(renameVars.okToRenameVar("name", false));
        assertTrue(renameVars.okToRenameVar("var", true));
        assertFalse(renameVars.okToRenameVar("name", true));
        assertFalse(renameVars.okToRenameVar("extern", false));
    }
}

