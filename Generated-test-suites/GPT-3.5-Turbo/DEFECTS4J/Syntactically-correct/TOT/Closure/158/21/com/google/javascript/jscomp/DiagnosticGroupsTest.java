import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setup() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void registerGroup_shouldRegisterGroupWithNameAndTypes() {
        DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup",
                DiagnosticType.ERROR,
                DiagnosticType.WARNING);

        assertNotNull(group);
        assertEquals("testGroup", group.getName());
        assertEquals(DiagnosticType.ERROR, group.getTypes().get(0));
        assertEquals(DiagnosticType.WARNING, group.getTypes().get(1));
    }

    @Test
    public void getRegisteredGroups_shouldReturnMapOfRegisteredGroups() {
        diagnosticGroups.registerGroup("group1",
                DiagnosticType.ERROR,
                DiagnosticType.WARNING);

        diagnosticGroups.registerGroup("group2",
                DiagnosticType.WARNING,
                DiagnosticType.MISSING);

        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

        assertNotNull(registeredGroups);
        assertEquals(2, registeredGroups.size());
        assertNotNull(registeredGroups.get("group1"));
        assertNotNull(registeredGroups.get("group2"));
    }

    @Test
    public void forName_shouldReturnDiagnosticGroupForRegisteredName() {
        diagnosticGroups.registerGroup("group1",
                DiagnosticType.ERROR,
                DiagnosticType.WARNING);

        DiagnosticGroup group = diagnosticGroups.forName("group1");

        assertNotNull(group);
        assertEquals("group1", group.getName());
        assertEquals(DiagnosticType.ERROR, group.getTypes().get(0));
        assertEquals(DiagnosticType.WARNING, group.getTypes().get(1));
    }

    @Test
    public void setWarningLevels_shouldSetCorrectWarningLevelsForGivenNames() {
        CompilerOptions options = new CompilerOptions();
        diagnosticGroups.registerGroup("group1",
                DiagnosticType.ERROR,
                DiagnosticType.WARNING);

        diagnosticGroups.registerGroup("group2",
                DiagnosticType.WARNING,
                DiagnosticType.MISSING);

        List<String> diagnosticGroups = Arrays.asList("group1", "group2");

        diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

        assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.forName("group1")));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.forName("group2")));
    }
}