import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(name);

        DiagnosticGroup result = diagnosticGroups.registerGroup(name, group);

        // Check if the group is registered correctly
        Assert.assertEquals(group, result);

        // Check if the group is in the registered groups
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        Assert.assertEquals(group, registeredGroups.get(name));
    }

    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(name);

        diagnosticGroups.registerGroup(name, group);

        // Check if the group is found correctly by name
        Assert.assertEquals(group, diagnosticGroups.forName(name));
    }

    @Test
    public void testSetWarningLevels() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        CompilerOptions options = new CompilerOptions();
        CheckLevel level = CheckLevel.WARNING;

        String name1 = "testGroup1";
        String name2 = "testGroup2";
        diagnosticGroups.registerGroup(name1, new DiagnosticGroup(name1));
        diagnosticGroups.registerGroup(name2, new DiagnosticGroup(name2));

        List<String> warningGroups = new ArrayList<>();
        warningGroups.add(name1);
        warningGroups.add(name2);

        diagnosticGroups.setWarningLevels(options, warningGroups, level);

        // Check if the warning levels are set correctly
        Assert.assertEquals(level, options.getWarningLevel(diagnosticGroups.forName(name1)));
        Assert.assertEquals(level, options.getWarningLevel(diagnosticGroups.forName(name2)));
    }

}