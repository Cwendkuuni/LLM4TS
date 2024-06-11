import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        
        assertEquals(group1, DiagnosticGroups.registerGroup("group1", group1));
        assertNotNull(DiagnosticGroups.registerGroup("group2", group2));
    }
    
    @Test
    public void testRegisterGroupWithTypes() {
        DiagnosticType type1 = new DiagnosticType("type1");
        DiagnosticType type2 = new DiagnosticType("type2");
        
        DiagnosticGroup group = DiagnosticGroups.registerGroup("group", type1, type2);
        
        assertEquals(2, group.getTypes().size());
        assertTrue(group.getTypes().contains(type1));
        assertTrue(group.getTypes().contains(type2));
    }
    
    @Test
    public void testRegisterGroupWithGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        
        DiagnosticGroup group = DiagnosticGroups.registerGroup("group", group1, group2);
        
        assertEquals(2, group.getGroups().size());
        assertTrue(group.getGroups().contains(group1));
        assertTrue(group.getGroups().contains(group2));
    }
    
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        
        DiagnosticGroups.registerGroup("group1", group1);
        DiagnosticGroups.registerGroup("group2", group2);
        
        Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
        assertEquals(2, registeredGroups.size());
        assertTrue(registeredGroups.containsKey("group1"));
        assertTrue(registeredGroups.containsKey("group2"));
        assertEquals(group1, registeredGroups.get("group1"));
        assertEquals(group2, registeredGroups.get("group2"));
    }
    
    @Test
    public void testForName() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        
        DiagnosticGroups.registerGroup("group1", group1);
        DiagnosticGroups.registerGroup("group2", group2);
        
        assertEquals(group1, DiagnosticGroups.forName("group1"));
        assertEquals(group2, DiagnosticGroups.forName("group2"));
        assertNull(DiagnosticGroups.forName(""));
        assertNull(DiagnosticGroups.forName(null));
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("group1", "group2");
        CheckLevel level = CheckLevel.WARNING;
        
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1");
        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2");
        DiagnosticGroups.registerGroup("group3");
        
        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
        
        assertEquals(level, options.getWarningLevel(group1));
        assertEquals(level, options.getWarningLevel(group2));
    }
}