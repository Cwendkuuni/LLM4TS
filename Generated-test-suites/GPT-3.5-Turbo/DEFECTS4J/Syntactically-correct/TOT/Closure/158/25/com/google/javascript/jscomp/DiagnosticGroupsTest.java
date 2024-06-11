import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testRegisterGroup_withNameAndDiagnosticGroup_shouldAddGroupToMap() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        diagnosticGroups.registerGroup("test", group);
        
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        assertEquals(1, registeredGroups.size());
        assertEquals(group, registeredGroups.get("test"));
    }

    @Test
    public void testRegisterGroup_withNameAndDiagnosticTypes_shouldAddGroupToMap() {
        DiagnosticType type = new DiagnosticType("testType");
        DiagnosticGroup group = diagnosticGroups.registerGroup("test", type);

        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        assertEquals(1, registeredGroups.size());
        assertEquals(group, registeredGroups.get("test"));
        assertEquals(1, group.getTypes().size());
        assertEquals(type, group.getTypes().get(0));
    }

    @Test
    public void testRegisterGroup_withNameAndDiagnosticGroups_shouldAddGroupToMap() {
        DiagnosticGroup subgroup1 = new DiagnosticGroup("subgroup1");
        DiagnosticGroup subgroup2 = new DiagnosticGroup("subgroup2");
        DiagnosticGroup group = diagnosticGroups.registerGroup("test", subgroup1, subgroup2);

        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        assertEquals(1, registeredGroups.size());
        assertEquals(group, registeredGroups.get("test"));
        assertEquals(2, group.getGroups().size());
        assertEquals(subgroup1, group.getGroups().get(0));
        assertEquals(subgroup2, group.getGroups().get(1));
    }

    @Test
    public void testForName_withExistingGroupName_shouldReturnGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        diagnosticGroups.registerGroup("test", group);

        DiagnosticGroup result = diagnosticGroups.forName("test");
        assertNotNull(result);
        assertEquals(group, result);
    }

    @Test
    public void testForName_withNonExistingGroupName_shouldReturnNull() {
        DiagnosticGroup result = diagnosticGroups.forName("nonExistingGroup");
        assertNull(result);
    }

    @Test
    public void testSetWarningLevels_withValidDiagnosticGroups_shouldSetWarningLevel() {
        CompilerOptions options = new CompilerOptions();
        diagnosticGroups.registerGroup("testGroup", new DiagnosticType("testType"));

        diagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup"), CheckLevel.WARNING);

        assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.forName("testGroup")));
    }

    @Test(expected = NullPointerException.class)
    public void testSetWarningLevels_withInvalidDiagnosticGroup_shouldThrowNullPointerException() {
        CompilerOptions options = new CompilerOptions();

        diagnosticGroups.setWarningLevels(options, Arrays.asList("nonExistingGroup"), CheckLevel.WARNING);
    }
}