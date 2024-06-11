import org.junit.*;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup_withNameAndGroup_shouldRegisterGroup() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup();

        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(name, group);

        assertEquals(group, registeredGroup);
        assertNotNull(DiagnosticGroups.getRegisteredGroups().get(name));
    }

    @Test
    public void testRegisterGroup_withNameAndTypes_shouldRegisterGroup() {
        String name = "testGroup";
        DiagnosticType[] types = {DiagnosticType.WARNING_1, DiagnosticType.WARNING_2};

        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(name, types);

        assertEquals(types.length, registeredGroup.getTypes().size());
        assertNotNull(DiagnosticGroups.getRegisteredGroups().get(name));
    }

    @Test
    public void testRegisterGroup_withNameAndGroups_shouldRegisterGroup() {
        String name = "testGroup";
        DiagnosticGroup group1 = new DiagnosticGroup();
        DiagnosticGroup group2 = new DiagnosticGroup();
        DiagnosticGroup[] groups = {group1, group2};

        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(name, groups);

        assertEquals(groups.length, registeredGroup.getGroups().size());
        assertNotNull(DiagnosticGroups.getRegisteredGroups().get(name));
    }

    @Test
    public void testGetRegisteredGroups_shouldReturnCopyOfRegisteredGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup();
        DiagnosticGroup group2 = new DiagnosticGroup();
        DiagnosticGroups.registerGroup("group1", group1);
        DiagnosticGroups.registerGroup("group2", group2);

        Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();

        assertNotSame(registeredGroups, DiagnosticGroups.getRegisteredGroups());
        assertEquals(2, registeredGroups.size());
    }

    @Test
    public void testForName_withRegisteredName_shouldReturnDiagnosticGroup() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup();
        DiagnosticGroups.registerGroup(name, group);

        DiagnosticGroup registeredGroup = DiagnosticGroups.forName(name);

        assertEquals(group, registeredGroup);
    }

    @Test
    public void testForName_withNonRegisteredName_shouldReturnNull() {
        String name = "nonRegisteredGroup";

        DiagnosticGroup registeredGroup = DiagnosticGroups.forName(name);

        assertNull(registeredGroup);
    }

    @Test
    public void testSetWarningLevels_withValidOptionsAndGroups_shouldSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("group1", "group2");
        CheckLevel level = CheckLevel.WARNING;
        DiagnosticGroup group1 = new DiagnosticGroup();
        DiagnosticGroup group2 = new DiagnosticGroup();
        DiagnosticGroups.registerGroup("group1", group1);
        DiagnosticGroups.registerGroup("group2", group2);

        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);

        assertEquals(level, options.getWarningLevel(group1));
        assertEquals(level, options.getWarningLevel(group2));
    }

    @Test(expected = NullPointerException.class)
    public void testSetWarningLevels_withInvalidOptionsAndGroups_shouldThrowException() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("nonRegisteredGroup");

        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    }
}