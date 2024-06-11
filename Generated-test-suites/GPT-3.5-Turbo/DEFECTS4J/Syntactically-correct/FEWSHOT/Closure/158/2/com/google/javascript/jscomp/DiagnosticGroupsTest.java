import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup1() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", 
            DiagnosticType1.INSTANCE, 
            DiagnosticType2.INSTANCE);
        
        DiagnosticGroup registeredGroup = DiagnosticGroups.getRegisteredGroups().get("testGroup");
        assertNotNull(registeredGroup);
        assertEquals(group, registeredGroup);
    }
    
    @Test
    public void testRegisterGroup2() {
        DiagnosticGroup nestedGroup1 = DiagnosticGroups.registerGroup("nestedGroup1", 
            DiagnosticType1.INSTANCE, 
            DiagnosticType2.INSTANCE);
        
        DiagnosticGroup nestedGroup2 = DiagnosticGroups.registerGroup("nestedGroup2", 
            DiagnosticType3.INSTANCE, 
            DiagnosticType4.INSTANCE);
        
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", 
            nestedGroup1, 
            nestedGroup2);
        
        DiagnosticGroup registeredGroup = DiagnosticGroups.getRegisteredGroups().get("testGroup");
        assertNotNull(registeredGroup);
        assertEquals(group, registeredGroup);
    }
    
    @Test
    public void testRegisterGroup3() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", 
            DiagnosticType1.INSTANCE, 
            DiagnosticType2.INSTANCE);
        
        DiagnosticGroup registeredGroup = DiagnosticGroups.getRegisteredGroups().get("testGroup");
        assertNotNull(registeredGroup);
        assertEquals(group, registeredGroup);
    }
    
    @Test
    public void testGetRegisteredGroups() {
        Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
        assertEquals(13, registeredGroups.size());
        assertNotNull(registeredGroups.get("accessControls"));
        assertNotNull(registeredGroups.get("ambiguousFunctionDecl"));
        assertNotNull(registeredGroups.get("checkRegExp"));
        assertNotNull(registeredGroups.get("checkTypes"));
        assertNotNull(registeredGroups.get("checkVars"));
        assertNotNull(registeredGroups.get("constantProperty"));
        assertNotNull(registeredGroups.get("deprecated"));
        assertNotNull(registeredGroups.get("externsValidation"));
        assertNotNull(registeredGroups.get("fileoverviewTags"));
        assertNotNull(registeredGroups.get("globalThis"));
        assertNotNull(registeredGroups.get("internetExplorerChecks"));
        assertNotNull(registeredGroups.get("invalidCasts"));
        assertNotNull(registeredGroups.get("missingProperties"));
        assertNotNull(registeredGroups.get("nonStandardJsDocs"));
        assertNotNull(registeredGroups.get("strictModuleDepCheck"));
        assertNotNull(registeredGroups.get("typeInvalidation"));
        assertNotNull(registeredGroups.get("undefinedVars"));
        assertNotNull(registeredGroups.get("unknownDefines"));
        assertNotNull(registeredGroups.get("uselessCode"));
        assertNotNull(registeredGroups.get("visibility"));
    }
    
    @Test
    public void testForName() {
        DiagnosticGroup group1 = DiagnosticGroups.forName("accessControls");
        assertNotNull(group1);
        assertEquals(DiagnosticGroups.ACCESS_CONTROLS, group1);
        
        DiagnosticGroup group2 = DiagnosticGroups.forName("invalidCasts");
        assertNotNull(group2);
        assertEquals(DiagnosticGroups.INVALID_CASTS, group2);
        
        DiagnosticGroup group3 = DiagnosticGroups.forName("missingProperties");
        assertNotNull(group3);
        assertEquals(DiagnosticGroups.MISSING_PROPERTIES, group3);
        
        DiagnosticGroup group4 = DiagnosticGroups.forName("unknownGroup");
        assertNull(group4);
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("accessControls", "invalidCasts");
        
        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
        
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.INVALID_CASTS));
        
        CheckLevel defaultLevel = options.getWarningLevel(JSError.getDefaultGroup1());
        for (Map.Entry<String, DiagnosticGroup> entry : DiagnosticGroups.getRegisteredGroups().entrySet()) {
            if (!entry.getKey().equals("accessControls") && !entry.getKey().equals("invalidCasts")) {
                assertEquals(defaultLevel, options.getWarningLevel(entry.getValue()));
            }
        }
    }
}