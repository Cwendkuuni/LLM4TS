import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup() {
        // Typical use case - registering a new group
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", group);
        
        // Verify that the registered group is the same as the group we registered
        assertEquals(group, registeredGroup);
    }

    @Test
    public void testGetRegisteredGroups() {
        // Typical use case - getting all registered groups
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        
        // Register some groups
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        diagnosticGroups.registerGroup("group1", group1);
        
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        diagnosticGroups.registerGroup("group2", group2);
        
        // Get the registered groups
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        
        // Verify that the registered groups map contains the expected groups
        assertTrue(registeredGroups.containsKey("group1"));
        assertTrue(registeredGroups.containsKey("group2"));
        assertEquals(group1, registeredGroups.get("group1"));
        assertEquals(group2, registeredGroups.get("group2"));
    }

    @Test
    public void testForName() {
        // Typical use case - finding a registered diagnostic group
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        
        // Register a group
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        diagnosticGroups.registerGroup("testGroup", group);
        
        // Find the registered group
        DiagnosticGroup foundGroup = diagnosticGroups.forName("testGroup");
        
        // Verify that the found group is the same as the group we registered
        assertEquals(group, foundGroup);
    }

    @Test
    public void testSetWarningLevels() {
        // Typical use case - setting warning levels for diagnostic groups
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        
        // Register some groups
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        diagnosticGroups.registerGroup("group1", group1);
        
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        diagnosticGroups.registerGroup("group2", group2);
        
        // Set the warning levels for the groups
        List<String> groups = Arrays.asList("group1", "group2");
        diagnosticGroups.setWarningLevels(options, groups, CheckLevel.WARNING);
        
        // Verify that the warning levels are set correctly for the groups
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group2));
    }
}