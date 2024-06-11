import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testRegisterGroup_WithNameAndGroup_ReturnsGroup() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(name);
        
        DiagnosticGroup result = diagnosticGroups.registerGroup(name, group);
        
        assertEquals(group, result);
    }
    
    @Test
    public void testRegisterGroup_WithNameAndTypes_ReturnsGroup() {
        String name = "testGroup";
        DiagnosticType[] types = new DiagnosticType[2];
        
        DiagnosticGroup result = diagnosticGroups.registerGroup(name, types);
        
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(types.length, result.getTypes().length);
    }
    
    @Test
    public void testRegisterGroup_WithNameAndGroups_ReturnsGroup() {
        String name = "testGroup";
        DiagnosticGroup[] groups = new DiagnosticGroup[2];
        
        DiagnosticGroup result = diagnosticGroups.registerGroup(name, groups);
        
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(groups.length, result.getGroups().length);
    }
    
    @Test
    public void testGetRegisteredGroups_ReturnsMapWithRegisteredGroups() {
        Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
        
        assertNotNull(groups);
        assertFalse(groups.isEmpty());
        assertTrue(groups.containsKey("accessControls"));
        assertTrue(groups.containsKey("ambiguousFunctionDecl"));
        // ... Checking for other expected group names
    }
    
    @Test
    public void testForName_WithValidGroupName_ReturnsGroup() {
        String groupName = "accessControls";
        
        DiagnosticGroup group = diagnosticGroups.forName(groupName);
        
        assertNotNull(group);
        assertEquals(groupName, group.getName());
    }
    
    @Test
    public void testForName_WithInvalidGroupName_ReturnsNull() {
        String groupName = "invalidGroup";
        
        DiagnosticGroup group = diagnosticGroups.forName(groupName);
        
        assertNull(group);
    }
    
    @Test
    public void testSetWarningLevels_WithOptionsAndGroups_SetsWarningLevelForGroups() {
        List<String> diagnosticGroups = Arrays.asList("accessControls", "ambiguousFunctionDecl");
        CompilerOptions options = new CompilerOptions();
        CheckLevel level = CheckLevel.WARNING;
        
        diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
        
        WarningLevel accessControlsWarningLevel = options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS);
        WarningLevel ambiguousFunctionDeclWarningLevel = options.getWarningLevel(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
        
        assertEquals(level, accessControlsWarningLevel);
        assertEquals(level, ambiguousFunctionDeclWarningLevel);
    }
}