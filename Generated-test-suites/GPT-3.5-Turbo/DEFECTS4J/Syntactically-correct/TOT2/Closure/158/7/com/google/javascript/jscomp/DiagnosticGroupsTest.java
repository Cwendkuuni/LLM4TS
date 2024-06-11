import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupWithTypes() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                new DiagnosticType("testType1"),
                new DiagnosticType("testType2"));

        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertTrue(group.containsType("testType1"));
        assertTrue(group.containsType("testType2"));
    }

    @Test
    public void testRegisterGroupWithGroups() {
        DiagnosticGroup subGroup = DiagnosticGroups.registerGroup("subGroup",
                new DiagnosticType("subType1"),
                new DiagnosticType("subType2"));
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                subGroup);

        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertTrue(group.containsType("subType1"));
        assertTrue(group.containsType("subType2"));
    }

    @Test
    public void testGetRegisteredGroups() {
        Map<String, DiagnosticGroup> groups = new DiagnosticGroups().getRegisteredGroups();

        assertNotNull(groups);
        assertFalse(groups.isEmpty());
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroups().forName("testGroup");

        assertNull(group);
    }
}