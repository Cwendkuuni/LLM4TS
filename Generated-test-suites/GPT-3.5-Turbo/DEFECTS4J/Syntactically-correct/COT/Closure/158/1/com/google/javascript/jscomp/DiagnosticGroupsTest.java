import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupStringDiagnosticGroup() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup();
        DiagnosticGroup result = DiagnosticGroups.registerGroup(name, group);
        assertEquals(group, result);
    }

    @Test
    public void testRegisterGroupStringDiagnosticTypeArray() {
        String name = "testGroup";
        DiagnosticType[] types = new DiagnosticType[3];
        types[0] = new DiagnosticType();
        types[1] = new DiagnosticType();
        types[2] = new DiagnosticType();
        DiagnosticGroup group = new DiagnosticGroup(name, types);
        DiagnosticGroup result = DiagnosticGroups.registerGroup(name, types);
        assertEquals(group, result);
    }

    @Test
    public void testRegisterGroupStringDiagnosticGroupArray() {
        String name = "testGroup";
        DiagnosticGroup[] groups = new DiagnosticGroup[2];
        groups[0] = new DiagnosticGroup();
        groups[1] = new DiagnosticGroup();
        DiagnosticGroup group = new DiagnosticGroup(name, groups);
        DiagnosticGroup result = DiagnosticGroups.registerGroup(name, groups);
        assertEquals(group, result);
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups instance = new DiagnosticGroups();
        Map<String, DiagnosticGroup> expected = ImmutableMap.copyOf(DiagnosticGroups.groupsByName);
        Map<String, DiagnosticGroup> result = instance.getRegisteredGroups();
        assertEquals(expected, result);
    }

    @Test
    public void testForName() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup();
        DiagnosticGroups.groupsByName.put(name, group);
        DiagnosticGroup result = DiagnosticGroups.forName(name);
        assertEquals(group, result);
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
        CheckLevel level = CheckLevel.WARNING;
        DiagnosticGroup group1 = new DiagnosticGroup();
        DiagnosticGroup group2 = new DiagnosticGroup();
        groupsByName.put("testGroup1", group1);
        groupsByName.put("testGroup2", group2);
        DiagnosticGroups instance = new DiagnosticGroups();
        instance.setWarningLevels(options, diagnosticGroups, level);
        assertEquals(level, options.getWarningLevel(group1));
        assertEquals(level, options.getWarningLevel(group2));
    }

    // Edge cases and exception handling scenarios:
    // - Empty name and group for registerGroupStringDiagnosticGroup
    // - Invalid name and empty types array for registerGroupStringDiagnosticTypeArray
    // - Empty name and empty groups array for registerGroupStringDiagnosticGroupArray
    // - Empty diagnosticGroups list for setWarningLevels

    @Test(expected = NullPointerException.class)
    public void testRegisterGroupStringDiagnosticGroupNullName() {
        DiagnosticGroup group = new DiagnosticGroup();
        DiagnosticGroups.registerGroup(null, group);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterGroupStringDiagnosticGroupNullGroup() {
        String name = "testGroup";
        DiagnosticGroups.registerGroup(name, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterGroupStringDiagnosticTypeArrayEmptyName() {
        DiagnosticType[] types = new DiagnosticType[3];
        types[0] = new DiagnosticType();
        types[1] = new DiagnosticType();
        types[2] = new DiagnosticType();
        DiagnosticGroups.registerGroup("", types);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterGroupStringDiagnosticTypeArrayNullTypes() {
        String name = "testGroup";
        DiagnosticGroups.registerGroup(name, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterGroupStringDiagnosticGroupArrayEmptyName() {
        DiagnosticGroup[] groups = new DiagnosticGroup[2];
        groups[0] = new DiagnosticGroup();
        groups[1] = new DiagnosticGroup();
        DiagnosticGroups.registerGroup("", groups);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterGroupStringDiagnosticGroupArrayNullGroups() {
        String name = "testGroup";
        DiagnosticGroups.registerGroup(name, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetWarningLevelsNullOptions() {
        List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
        CheckLevel level = CheckLevel.WARNING;
        DiagnosticGroups instance = new DiagnosticGroups();
        instance.setWarningLevels(null, diagnosticGroups, level);
    }

    @Test(expected = NullPointerException.class)
    public void testSetWarningLevelsNullDiagnosticGroups() {
        CompilerOptions options = new CompilerOptions();
        CheckLevel level = CheckLevel.WARNING;
        DiagnosticGroups instance = new DiagnosticGroups();
        instance.setWarningLevels(options, null, level);
    }

    @Test
    public void testSetWarningLevelsEmptyDiagnosticGroups() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Collections.emptyList();
        CheckLevel level = CheckLevel.WARNING;
        DiagnosticGroups instance = new DiagnosticGroups();
        instance.setWarningLevels(options, diagnosticGroups, level);
        // No exceptions should be thrown
    }

}