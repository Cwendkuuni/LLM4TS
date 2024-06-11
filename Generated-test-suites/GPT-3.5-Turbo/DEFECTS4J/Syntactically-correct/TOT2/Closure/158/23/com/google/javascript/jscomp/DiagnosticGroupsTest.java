import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testRegisterGroupWithNameAndGroup() {
        String groupName = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(groupName);
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(groupName, group);
        assertNotNull(registeredGroup);
        assertEquals(group, diagnosticGroups.getRegisteredGroups().get(groupName));
    }

    @Test
    public void testRegisterGroupWithNameAndTypes() {
        String groupName = "testGroup";
        DiagnosticType type = new DiagnosticType("TEST_TYPE");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(groupName, type);
        assertNotNull(registeredGroup);
        assertEquals(type, diagnosticGroups.getRegisteredGroups().get(groupName).getTypes().get(0));
    }

    @Test
    public void testRegisterGroupWithNameAndGroups() {
        String groupName = "testGroup";
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2= new DiagnosticGroup("group2");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(groupName, group1, group2);
        assertNotNull(registeredGroup);
        assertEquals(group1, diagnosticGroups.getRegisteredGroups().get(groupName).getGroups().get(0));
        assertEquals(group2, diagnosticGroups.getRegisteredGroups().get(groupName).getGroups().get(1));
    }

    @Test
    public void testForName() {
        String groupName = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(groupName);
        DiagnosticGroups.registerGroup(groupName, group);
        assertEquals(group, diagnosticGroups.forName(groupName));
    }

    @Test
    public void testGetRegisteredGroups() {
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testGetDiagnosticGroupByExistingName() {
        assertEquals(DiagnosticGroups.DEPRECATED, diagnosticGroups.forName("deprecated"));
    }

    @Test
    public void testGetDiagnosticGroupByNonExistingName() {
        assertNull(diagnosticGroups.forName("nonExistingGroup"));
    }
}