import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
    @Test
    public void testRegisterGroupWithDiagnosticGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, registeredGroup);
    }

    @Test
    public void testRegisterGroupWithDiagnosticTypeArray() {
        DiagnosticType type1 = new DiagnosticType("testType1");
        DiagnosticType type2 = new DiagnosticType("testType2");
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", type1, type2);
        assertEquals(2, group.size());
        assertEquals(type1, group.get(0));
        assertEquals(type2, group.get(1));
    }

    @Test
    public void testRegisterGroupWithDiagnosticGroupArray() {
        DiagnosticType type1 = new DiagnosticType("testType1");
        DiagnosticType type2 = new DiagnosticType("testType2");
        DiagnosticGroup innerGroup = DiagnosticGroups.registerGroup("innerGroup", type1);
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", innerGroup);
        assertEquals(1, group.size());
        assertEquals(innerGroup, group.get(0));
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroups.registerGroup("testGroup1", group1);
        DiagnosticGroups.registerGroup("testGroup2", group2);
        Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
        assertEquals(2, registeredGroups.size());
        assertEquals(group1, registeredGroups.get("testGroup1"));
        assertEquals(group2, registeredGroups.get("testGroup2"));
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("testGroup", group);
        DiagnosticGroup foundGroup = DiagnosticGroups.forName("testGroup");
        assertEquals(group, foundGroup);
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("group1", "group2", "group3");
        CheckLevel level = CheckLevel.WARNING;
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroup group3 = new DiagnosticGroup("group3");
        DiagnosticGroups.registerGroup("group1", group1);
        DiagnosticGroups.registerGroup("group2", group2);
        DiagnosticGroups.registerGroup("group3", group3);

        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);

        assertEquals(level, options.getWarningLevel(group1));
        assertEquals(level, options.getWarningLevel(group2));
        assertEquals(level, options.getWarningLevel(group3));
    }

    @Test(expected = NullPointerException.class)
    public void testSetWarningLevelsWithUnknownGroup() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("unknownGroup");
        CheckLevel level = CheckLevel.WARNING;

        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    }
}