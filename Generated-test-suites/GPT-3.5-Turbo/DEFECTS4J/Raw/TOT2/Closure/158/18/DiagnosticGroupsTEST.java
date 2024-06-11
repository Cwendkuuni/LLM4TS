import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupWithType() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup", DiagnosticType.WARNING);
        assertNotNull(DiagnosticGroups.registerGroup("testGroup", group));
    }

    @Test
    public void testGetRegisteredGroups() {
        assertTrue(DiagnosticGroups.getRegisteredGroups().containsKey("testGroup"));
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup", DiagnosticType.WARNING);
        DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, DiagnosticGroups.forName("testGroup"));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup");
        DiagnosticGroup group = new DiagnosticGroup("testGroup", DiagnosticType.WARNING);
        DiagnosticGroups.registerGroup("testGroup", group);
        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group));
    }

}