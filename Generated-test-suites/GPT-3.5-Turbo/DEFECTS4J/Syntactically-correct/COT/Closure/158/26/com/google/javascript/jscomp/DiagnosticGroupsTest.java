
import static org.junit.Assert.*;

import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {
    
    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        assertEquals(group, DiagnosticGroups.registerGroup("testGroup", group));
    }
    
    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, DiagnosticGroups.forName("testGroup"));
    }
    
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertEquals(DiagnosticGroups.getRegisteredGroups(), diagnosticGroups.getRegisteredGroups());
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
        CheckLevel level = CheckLevel.WARNING;
        
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        
        DiagnosticGroups.registerGroup("testGroup1", group1);
        DiagnosticGroups.registerGroup("testGroup2", group2);
        
        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
        assertEquals(level, options.getWarningLevel(group1));
        assertEquals(level, options.getWarningLevel(group2));
    }
}
