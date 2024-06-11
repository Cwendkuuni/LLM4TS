import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

        Assert.assertNotNull(registeredGroups);
        Assert.assertEquals(16, registeredGroups.size());
    }

    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

        Assert.assertNull(diagnosticGroups.forName("unknownGroup"));

        DiagnosticGroup group = diagnosticGroups.forName("invalidCasts");
        Assert.assertNotNull(group);
        Assert.assertEquals("invalidCasts", group.getName());
        Assert.assertEquals(1, group.getTypes().size());
    }

    @Test
    public void testSetWarningLevels() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        CompilerOptions options = new CompilerOptions();

        List<String> diagnosticGroupNames = List.of("invalidCasts", "checkVars", "uselessCode");
        diagnosticGroups.setWarningLevels(options, diagnosticGroupNames, CheckLevel.WARNING);

        Assert.assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.INVALID_CASTS));
        Assert.assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_VARIABLES));
        Assert.assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_USELESS_CODE));
    }
}