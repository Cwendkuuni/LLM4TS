import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Test
    public void testRegisterGroupWithNameAndDiagnosticGroup() {
        diagnosticGroups = new DiagnosticGroups();
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", group);

        assertNotNull(registeredGroup);
        assertEquals(group, registeredGroup);
    }

    @Test
    public void testRegisterGroupWithNameAndDiagnosticTypes() {
        diagnosticGroups = new DiagnosticGroups();
        DiagnosticType type = new DiagnosticType("testType");
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", type);

        assertNotNull(group);
        assertEquals(1, group.getTypes().size());
        assertEquals(type, group.getTypes().get(0));
    }

    @Test
    public void testRegisterGroupWithNameAndDiagnosticGroups() {
        diagnosticGroups = new DiagnosticGroups();
        DiagnosticGroup innerGroup = new DiagnosticGroup("innerGroup");
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", innerGroup);

        assertNotNull(group);
        assertEquals(1, group.getGroups().size());
        assertEquals(innerGroup, group.getGroups().get(0));
    }

    @Test
    public void testGetRegisteredGroups() {
        diagnosticGroups = new DiagnosticGroups();
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        diagnosticGroups = new DiagnosticGroups();
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("testGroup", group);

        assertEquals(group, diagnosticGroups.forName("testGroup"));
        assertNull(diagnosticGroups.forName("nonExistentGroup"));
    }

    // Add more test cases for other methods in DiagnosticGroups class

}