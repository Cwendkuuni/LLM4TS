import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupWithSingleType() {
        // Test registering a diagnostic group with a single type
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                new DiagnosticType("testType", "Test message"));
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertTrue(group.contains("testType"));
    }

    @Test
    public void testRegisterGroupWithMultipleTypes() {
        // Test registering a diagnostic group with multiple types
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                new DiagnosticType("testType1", "Test message 1"),
                new DiagnosticType("testType2", "Test message 2"));
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertTrue(group.contains("testType1"));
        assertTrue(group.contains("testType2"));
    }

    @Test
    public void testGetRegisteredGroups() {
        // Test getting the registered diagnostic groups
        Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        assertFalse(registeredGroups.isEmpty());
    }

    @Test
    public void testForName() {
        // Test finding a diagnostic group by name
        DiagnosticGroup group = DiagnosticGroups.forName("testGroup");
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
    }

    @Test
    public void testSetWarningLevels() {
        // Test setting warning levels for diagnostic groups
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup1", "testGroup2"), CheckLevel.WARNING);
        // Verify that warning levels are correctly set
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("testGroup1")));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("testGroup2")));
    }

}