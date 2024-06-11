import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setup() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testGetRegisteredGroups() {
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        assertNotNull(diagnosticGroups.forName("accessControls"));
        assertNull(diagnosticGroups.forName("nonExistentGroup"));
    }

    @Test
    public void testRegisterGroup1() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup result = diagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, result);
        assertEquals(group, diagnosticGroups.getRegisteredGroups().get("testGroup"));
    }

    @Test
    public void testRegisterGroup2() {
        DiagnosticType type = new DiagnosticType("testType");
        DiagnosticGroup result = diagnosticGroups.registerGroup("testGroup", type);
        assertNotNull(result);
        assertEquals(type, result.getTypes().get(0));
        assertEquals(result, diagnosticGroups.getRegisteredGroups().get("testGroup"));
    }

    @Test
    public void testRegisterGroup3() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroup result = diagnosticGroups.registerGroup("testGroup", group1, group2);
        assertNotNull(result);
        assertEquals(group1, result.getGroups().get(0));
        assertEquals(group2, result.getGroups().get(1));
        assertEquals(result, diagnosticGroups.getRegisteredGroups().get("testGroup"));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = List.of("accessControls", "checkTypes", "checkVars");
        diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_TYPES));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_VARIABLES));
    }

}