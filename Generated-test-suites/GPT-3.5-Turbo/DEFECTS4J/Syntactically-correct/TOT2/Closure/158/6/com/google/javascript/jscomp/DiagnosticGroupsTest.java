import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupWithType() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                CheckAccessControls.DEPRECATED_NAME,
                CheckAccessControls.DEPRECATED_NAME_REASON);
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
    }

    @Test
    public void testRegisterGroupWithGroups() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                DiagnosticGroups.GLOBAL_THIS,
                DiagnosticGroups.DEPRECATED);
        assertNotNull(group);
        assertEquals("testGroup", group.getName());
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
                CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS);
        assertEquals(group, diagnosticGroups.forName("testGroup"));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup");
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.forName("testGroup")));
    }
}