import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNull(diagnosticGroups.forName("nonExistentGroup"));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        List<String> diagnosticGroupList = Arrays.asList("accessControls", "checkTypes");
        diagnosticGroups.setWarningLevels(options, diagnosticGroupList, CheckLevel.WARNING);
        // Add assertion here to check if warning levels are set correctly
    }

    // Additional test cases for other methods in DiagnosticGroups class can be added here

}