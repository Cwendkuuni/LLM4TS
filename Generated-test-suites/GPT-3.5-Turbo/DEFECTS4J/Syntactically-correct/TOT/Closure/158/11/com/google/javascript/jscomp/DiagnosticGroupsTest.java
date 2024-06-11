import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @BeforeEach
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testRegisterGroupWithNameAndGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", group);
        assertNotNull(diagnosticGroups.getRegisteredGroups().get("testGroup"));
        assertEquals(group, registeredGroup);
    }

    @Test
    public void testRegisterGroupWithNameAndTypes() {
        DiagnosticType type1 = new DiagnosticType("testType1");
        DiagnosticType type2 = new DiagnosticType("testType2");
        DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", type1, type2);
        assertNotNull(diagnosticGroups.getRegisteredGroups().get("testGroup"));
        assertTrue(group.contains(type1));
        assertTrue(group.contains(type2));
    }

    @Test
    public void testRegisterGroupWithNameAndGroups() {
        DiagnosticType type1 = new DiagnosticType("testType1");
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1", type1);
        DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", group1);
        assertNotNull(diagnosticGroups.getRegisteredGroups().get("testGroup"));
        assertTrue(group.contains(type1));
        assertTrue(group.containsDiagnosticGroup(group1));
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        diagnosticGroups.registerGroup("testGroup1", group1);
        diagnosticGroups.registerGroup("testGroup2", group2);

        assertEquals(group1, diagnosticGroups.getRegisteredGroups().get("testGroup1"));
        assertEquals(group2, diagnosticGroups.getRegisteredGroups().get("testGroup2"));
    }

    @Test
    public void testForName() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        diagnosticGroups.registerGroup("testGroup1", group1);
        diagnosticGroups.registerGroup("testGroup2", group2);

        assertEquals(group1, diagnosticGroups.forName("testGroup1"));
        assertEquals(group2, diagnosticGroups.forName("testGroup2"));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        diagnosticGroups.registerGroup("testGroup", new DiagnosticGroup("testGroup"));
        diagnosticGroups.setWarningLevels(options, List.of("testGroup"), CheckLevel.WARNING);

        assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.getRegisteredGroups().get("testGroup")));
    }

    // Add more test methods for other methods if needed

}