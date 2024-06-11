import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupWithDiagnosticGroup() {
        // Test registering a group with DiagnosticGroup
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, registeredGroup);
    }

    @Test
    public void testRegisterGroupWithTypes() {
        // Test registering a group with DiagnosticType array
        DiagnosticType[] types = {new DiagnosticType("testType1"), new DiagnosticType("testType2")};
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", types);
        assertNotNull(registeredGroup);
    }

    @Test
    public void testGetRegisteredGroups() {
        // Test getting registered groups
        DiagnosticGroups diagGroups = new DiagnosticGroups();
        diagGroups.registerGroup("testGroup", new DiagnosticType("testType"));
        assertEquals(1, diagGroups.getRegisteredGroups().size());
    }

    @Test
    public void testForName() {
        // Test finding a diagnostic group by name
        String groupName = "testGroup";
        DiagnosticType testType = new DiagnosticType("testType");
        DiagnosticGroups.registerGroup(groupName, testType);
        DiagnosticGroup foundGroup = DiagnosticGroups.forName(groupName);
        assertNotNull(foundGroup);
    }

    @Test
    public void testSetWarningLevels() {
        // Test setting warning levels
        List<String> groups = Arrays.asList("testGroup1", "testGroup2");
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagGroups = new DiagnosticGroups();
        diagGroups.setWarningLevels(options, groups, CheckLevel.WARNING);
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(groups.get(0)));
    }
}