import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {
    
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }
    
    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertEquals(DiagnosticGroups.GONE_GLOBAL, diagnosticGroups.forName("globalThis"));
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.setWarningLevels(options, Arrays.asList("globalThis", "deprecated"), CheckLevel.WARNING);
        assertTrue(options.getWarningLevel(DiagnosticGroups.GONE_GLOBAL) == CheckLevel.WARNING);
        assertTrue(options.getWarningLevel(DiagnosticGroups.DEPRECATED) == CheckLevel.WARNING);
    }
}