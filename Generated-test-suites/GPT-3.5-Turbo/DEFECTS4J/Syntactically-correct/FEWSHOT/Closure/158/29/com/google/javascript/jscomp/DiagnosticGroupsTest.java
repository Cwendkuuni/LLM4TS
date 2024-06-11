import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {
    
    @Test
    public void testRegisterGroup_withNameAndGroup() {
        // Test case for registering a group with name and group
        DiagnosticGroup group = new DiagnosticGroup();
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, result);
    }
    
    @Test
    public void testRegisterGroup_withNameAndTypes() {
        // Test case for registering a group with name and types
        DiagnosticType[] types = {new DiagnosticType()};
        DiagnosticGroup group = new DiagnosticGroup(types);
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", types);
        assertEquals(group, result);
    }
    
    @Test
    public void testRegisterGroup_withNameAndGroups() {
        // Test case for registering a group with name and groups
        DiagnosticGroup[] groups = {new DiagnosticGroup(), new DiagnosticGroup()};
        DiagnosticGroup group = new DiagnosticGroup(groups);
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", groups);
        assertEquals(group, result);
    }
    
    @Test
    public void testGetRegisteredGroups() {
        // Test case for getting registered groups
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.registerGroup("testGroup", new DiagnosticGroup());
        assertTrue(diagnosticGroups.getRegisteredGroups().containsKey("testGroup"));
    }
    
    @Test
    public void testForName() {
        // Test case for finding a diagnostic group by name
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        DiagnosticGroup group = new DiagnosticGroup();
        diagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, diagnosticGroups.forName("testGroup"));
    }
    
    @Test
    public void testRegisterGroup_GLOBAL_THIS() {
        // Test case for registering the GLOBAL_THIS diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("globalThis",
            CheckGlobalThis.GLOBAL_THIS);
        assertEquals(group, DiagnosticGroups.GLOBAL_THIS);
    }
    
    @Test
    public void testRegisterGroup_DEPRECATED() {
        // Test case for registering the DEPRECATED diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("deprecated",
            CheckAccessControls.DEPRECATED_NAME,
            CheckAccessControls.DEPRECATED_NAME_REASON,
            CheckAccessControls.DEPRECATED_PROP,
            CheckAccessControls.DEPRECATED_PROP_REASON,
            CheckAccessControls.DEPRECATED_CLASS,
            CheckAccessControls.DEPRECATED_CLASS_REASON);
        assertEquals(group, DiagnosticGroups.DEPRECATED);
    }
    
    @Test
    public void testRegisterGroup_VISIBILITY() {
        // Test case for registering the VISIBILITY diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("visibility",
            CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
            CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
            CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
            CheckAccessControls.PRIVATE_OVERRIDE,
            CheckAccessControls.VISIBILITY_MISMATCH);
        assertEquals(group, DiagnosticGroups.VISIBILITY);
    }
    
    @Test
    public void testRegisterGroup_CONSTANT_PROPERTY() {
        // Test case for registering the CONSTANT_PROPERTY diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("constantProperty",
            CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE);
        assertEquals(group, DiagnosticGroups.CONSTANT_PROPERTY);
    }
    
    @Test
    public void testRegisterGroup_NON_STANDARD_JSDOC() {
        // Test case for registering the NON_STANDARD_JSDOC diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("nonStandardJsDocs",
            RhinoErrorReporter.BAD_JSDOC_ANNOTATION);
        assertEquals(group, DiagnosticGroups.NON_STANDARD_JSDOC);
    }
    
    @Test
    public void testRegisterGroup_ACCESS_CONTROLS() {
        // Test case for registering the ACCESS_CONTROLS diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("accessControls", 
            DiagnosticGroups.DEPRECATED, DiagnosticGroups.VISIBILITY);
        assertEquals(group, DiagnosticGroups.ACCESS_CONTROLS);
    }
    
    @Test
    public void testRegisterGroup_INVALID_CASTS() {
        // Test case for registering the INVALID_CASTS diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("invalidCasts",
            TypeValidator.INVALID_CAST);
        assertEquals(group, DiagnosticGroups.INVALID_CASTS);
    }
    
    @Test
    public void testRegisterGroup_FILEOVERVIEW_JSDOC() {
        // Test case for registering the FILEOVERVIEW_JSDOC diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("fileoverviewTags",
            RhinoErrorReporter.EXTRA_FILEOVERVIEW);
        assertEquals(group, DiagnosticGroups.FILEOVERVIEW_JSDOC);
    }
    
    @Test
    public void testRegisterGroup_STRICT_MODULE_DEP_CHECK() {
        // Test case for registering the STRICT_MODULE_DEP_CHECK diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("strictModuleDepCheck",
            VarCheck.STRICT_MODULE_DEP_ERROR,
            CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
        assertEquals(group, DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
    }
    
    @Test
    public void testRegisterGroup_EXTERNS_VALIDATION() {
        // Test case for registering the EXTERNS_VALIDATION diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("externsValidation",
            VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
            VarCheck.UNDEFINED_EXTERN_VAR_ERROR);
        assertEquals(group, DiagnosticGroups.EXTERNS_VALIDATION);
    }
    
    @Test
    public void testRegisterGroup_AMBIGUOUS_FUNCTION_DECL() {
        // Test case for registering the AMBIGUOUS_FUNCTION_DECL diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("ambiguousFunctionDecl",
            VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL);
        assertEquals(group, DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
    }
    
    @Test
    public void testRegisterGroup_UNKNOWN_DEFINES() {
        // Test case for registering the UNKNOWN_DEFINES diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("unknownDefines",
            ProcessDefines.UNKNOWN_DEFINE_WARNING);
        assertEquals(group, DiagnosticGroups.UNKNOWN_DEFINES);
    }
    
    @Test
    public void testRegisterGroup_TWEAKS() {
        // Test case for registering the TWEAKS diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("tweakValidation",
            ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
            ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
            ProcessTweaks.UNKNOWN_TWEAK_WARNING);
        assertEquals(group, DiagnosticGroups.TWEAKS);
    }
    
    @Test
    public void testRegisterGroup_MISSING_PROPERTIES() {
        // Test case for registering the MISSING_PROPERTIES diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("missingProperties",
            TypeCheck.INEXISTENT_PROPERTY);
        assertEquals(group, DiagnosticGroups.MISSING_PROPERTIES);
    }
    
    @Test
    public void testRegisterGroup_INTERNET_EXPLORER_CHECKS() {
        // Test case for registering the INTERNET_EXPLORER_CHECKS diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("internetExplorerChecks",
            RhinoErrorReporter.TRAILING_COMMA);
        assertEquals(group, DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
    }
    
    @Test
    public void testRegisterGroup_UNDEFINED_VARIABLES() {
        // Test case for registering the UNDEFINED_VARIABLES diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("undefinedVars",
            VarCheck.UNDEFINED_VAR_ERROR);
        assertEquals(group, DiagnosticGroups.UNDEFINED_VARIABLES);
    }
    
    @Test
    public void testRegisterGroup_CHECK_REGEXP() {
        // Test case for registering the CHECK_REGEXP diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("checkRegExp",
            CheckRegExp.REGEXP_REFERENCE);
        assertEquals(group, DiagnosticGroups.CHECK_REGEXP);
    }
    
    @Test
    public void testRegisterGroup_CHECK_TYPES() {
        // Test case for registering the CHECK_TYPES diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("checkTypes",
            TypeValidator.ALL_DIAGNOSTICS,
            TypeCheck.ALL_DIAGNOSTICS);
        assertEquals(group, DiagnosticGroups.CHECK_TYPES);
    }
    
    @Test
    public void testRegisterGroup_CHECK_VARIABLES() {
        // Test case for registering the CHECK_VARIABLES diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("checkVars",
            VarCheck.UNDEFINED_VAR_ERROR,
            SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR);
        assertEquals(group, DiagnosticGroups.CHECK_VARIABLES);
    }
    
    @Test
    public void testRegisterGroup_CHECK_USELESS_CODE() {
        // Test case for registering the CHECK_USELESS_CODE diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("uselessCode",
            CheckSideEffects.USELESS_CODE_ERROR,
            CheckUnreachableCode.UNREACHABLE_CODE);
        assertEquals(group, DiagnosticGroups.CHECK_USELESS_CODE);
    }
    
    @Test
    public void testRegisterGroup_TYPE_INVALIDATION() {
        // Test case for registering the TYPE_INVALIDATION diagnostic group
        DiagnosticGroup group = DiagnosticGroups.registerGroup("typeInvalidation",
            DisambiguateProperties.Warnings.INVALIDATION);
        assertEquals(group, DiagnosticGroups.TYPE_INVALIDATION);
    }
    
    @Test
    public void testSetWarningLevels() {
        // Test case for setting warning levels
        CompilerOptions options = new CompilerOptions();
        options.setWarningLevel(DiagnosticGroups.ACCESS_CONTROLS, CheckLevel.WARNING);
        List<String> diagnosticGroups = Arrays.asList("accessControls");
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.ERROR);
        assertEquals(CheckLevel.ERROR, options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
    }
}