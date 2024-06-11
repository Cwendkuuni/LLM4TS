import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Map;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, result);
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        Map<String, DiagnosticGroup> result = diagnosticGroups.getRegisteredGroups();
        assertEquals(DiagnosticGroups.groupsByName, result);
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.groupsByName.put("testGroup", group);
        DiagnosticGroup result = new DiagnosticGroups().forName("testGroup");
        assertEquals(group, result);
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup1", "testGroup2"), CheckLevel.WARNING);
        // Assert the warning levels are set correctly in the options
    }
}