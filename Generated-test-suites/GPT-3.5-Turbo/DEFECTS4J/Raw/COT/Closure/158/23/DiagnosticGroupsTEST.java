
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {
    
    private DiagnosticGroups diagnosticGroups;
    
    @Before
    public void setup() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testRegisterGroup_stringDiagnosticGroup() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(name);
        DiagnosticGroup result = diagnosticGroups.registerGroup(name, group);
        assertEquals(group, result);
    }

    @Test
    public void testRegisterGroup_stringDiagnosticTypeArray() {
        String name = "testGroup";
        DiagnosticType[] types = { DiagnosticType.ERROR, DiagnosticType.WARNING };
        DiagnosticGroup group = new DiagnosticGroup(name, types);
        DiagnosticGroup result = diagnosticGroups.registerGroup(name, types);
        assertEquals(group, result);
    }

    @Test
    public void testRegisterGroup_stringDiagnosticGroupArray() {
        String name = "testGroup";
        DiagnosticGroup[] groups = { new DiagnosticGroup("group1"), new DiagnosticGroup("group2") };
        DiagnosticGroup group = new DiagnosticGroup(name, groups);
        DiagnosticGroup result = diagnosticGroups.registerGroup(name, groups);
        assertEquals(group, result);
    }

    @Test
    public void testGetRegisteredGroups() {
        Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(groups);
    }

    @Test
    public void testForName() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(name);
        diagnosticGroups.registerGroup(name, group);
        DiagnosticGroup result = diagnosticGroups.forName(name);
        assertEquals(group, result);
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup");
        CheckLevel level = CheckLevel.WARNING;
        diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
        assertEquals(level, options.getWarningLevel(diagnosticGroups.forName("testGroup")));
    }

}
