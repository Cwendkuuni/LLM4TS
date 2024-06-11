import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNull(diagnosticGroups.forName("nonExistingGroup"));
    }

    @Test
    public void testGLOBAL_THIS() {
        DiagnosticGroup globalThisGroup = DiagnosticGroups.GLOBAL_THIS;
        assertNotNull(globalThisGroup);
        assertEquals("globalThis", globalThisGroup.getName());
        assertTrue(globalThisGroup.containsType(CheckGlobalThis.GLOBAL_THIS));
    }

    @Test
    public void testDEPRECATED() {
        DiagnosticGroup deprecatedGroup = DiagnosticGroups.DEPRECATED;
        assertNotNull(deprecatedGroup);
        assertEquals("deprecated", deprecatedGroup.getName());
        assertTrue(deprecatedGroup.containsType(CheckAccessControls.DEPRECATED_NAME));
        assertTrue(deprecatedGroup.containsType(CheckAccessControls.DEPRECATED_NAME_REASON));
        assertTrue(deprecatedGroup.containsType(CheckAccessControls.DEPRECATED_PROP));
        assertTrue(deprecatedGroup.containsType(CheckAccessControls.DEPRECATED_PROP_REASON));
        assertTrue(deprecatedGroup.containsType(CheckAccessControls.DEPRECATED_CLASS));
        assertTrue(deprecatedGroup.containsType(CheckAccessControls.DEPRECATED_CLASS_REASON));
    }

    @Test
    public void testVISIBILITY() {
        DiagnosticGroup visibilityGroup = DiagnosticGroups.VISIBILITY;
        assertNotNull(visibilityGroup);
        assertEquals("visibility", visibilityGroup.getName());
        assertTrue(visibilityGroup.containsType(CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS));
        assertTrue(visibilityGroup.containsType(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS));
        assertTrue(visibilityGroup.containsType(CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS));
        assertTrue(visibilityGroup.containsType(CheckAccessControls.PRIVATE_OVERRIDE));
        assertTrue(visibilityGroup.containsType(CheckAccessControls.VISIBILITY_MISMATCH));
    }

    @Test
    public void testCONSTANT_PROPERTY() {
        DiagnosticGroup constantPropertyGroup = DiagnosticGroups.CONSTANT_PROPERTY;
        assertNotNull(constantPropertyGroup);
        assertEquals("constantProperty", constantPropertyGroup.getName());
        assertTrue(constantPropertyGroup.containsType(CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE));
    }

    @Test
    public void testNON_STANDARD_JSDOC() {
        DiagnosticGroup nonStandardJsDocGroup = DiagnosticGroups.NON_STANDARD_JSDOC;
        assertNotNull(nonStandardJsDocGroup);
        assertEquals("nonStandardJsDocs", nonStandardJsDocGroup.getName());
        assertTrue(nonStandardJsDocGroup.containsType(RhinoErrorReporter.BAD_JSDOC_ANNOTATION));
    }

    @Test
    public void testACCESS_CONTROLS() {
        DiagnosticGroup accessControlsGroup = DiagnosticGroups.ACCESS_CONTROLS;
        assertNotNull(accessControlsGroup);
        assertEquals("accessControls", accessControlsGroup.getName());
        assertTrue(accessControlsGroup.containsGroup(DiagnosticGroups.DEPRECATED));
        assertTrue(accessControlsGroup.containsGroup(DiagnosticGroups.VISIBILITY));
    }

    @Test
    public void testINVALID_CASTS() {
        DiagnosticGroup invalidCastsGroup = DiagnosticGroups.INVALID_CASTS;
        assertNotNull(invalidCastsGroup);
        assertEquals("invalidCasts", invalidCastsGroup.getName());
        assertTrue(invalidCastsGroup.containsType(TypeValidator.INVALID_CAST));
    }

    @Test
    public void testFILEOVERVIEW_JSDOC() {
        DiagnosticGroup fileoverviewJsdocGroup = DiagnosticGroups.FILEOVERVIEW_JSDOC;
        assertNotNull(fileoverviewJsdocGroup);
        assertEquals("fileoverviewTags", fileoverviewJsdocGroup.getName());
        assertTrue(fileoverviewJsdocGroup.containsType(RhinoErrorReporter.EXTRA_FILEOVERVIEW));
    }

    @Test
    public void testSTRICT_MODULE_DEP_CHECK() {
        DiagnosticGroup strictModuleDepCheckGroup = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
        assertNotNull(strictModuleDepCheckGroup);
        assertEquals("strictModuleDepCheck", strictModuleDepCheckGroup.getName());
        assertTrue(strictModuleDepCheckGroup.containsType(VarCheck.STRICT_MODULE_DEP_ERROR));
        assertTrue(strictModuleDepCheckGroup.containsType(CheckGlobalNames.STRICT_MODULE_DEP_QNAME));
    }

    @Test
    public void testEXTERNS_VALIDATION() {
        DiagnosticGroup externsValidationGroup = DiagnosticGroups.EXTERNS_VALIDATION;
        assertNotNull(externsValidationGroup);
        assertEquals("externsValidation", externsValidationGroup.getName());
        assertTrue(externsValidationGroup.containsType(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR));
        assertTrue(externsValidationGroup.containsType(VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
    }

    @Test
    public void testAMBIGUOUS_FUNCTION_DECL() {
        DiagnosticGroup ambiguousFunctionDeclGroup = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
        assertNotNull(ambiguousFunctionDeclGroup);
        assertEquals("ambiguousFunctionDecl", ambiguousFunctionDeclGroup.getName());
        assertTrue(ambiguousFunctionDeclGroup.containsType(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL));
    }

    @Test
    public void testUNKNOWN_DEFINES() {
        DiagnosticGroup unknownDefinesGroup = DiagnosticGroups.UNKNOWN_DEFINES;
        assertNotNull(unknownDefinesGroup);
        assertEquals("unknownDefines", unknownDefinesGroup.getName());
        assertTrue(unknownDefinesGroup.containsType(ProcessDefines.UNKNOWN_DEFINE_WARNING));
    }

    @Test
    public void testTWEAKS() {
        DiagnosticGroup tweaksGroup = DiagnosticGroups.TWEAKS;
        assertNotNull(tweaksGroup);
        assertEquals("tweakValidation", tweaksGroup.getName());
        assertTrue(tweaksGroup.containsType(ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING));
        assertTrue(tweaksGroup.containsType(ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING));
        assertTrue(tweaksGroup.containsType(ProcessTweaks.UNKNOWN_TWEAK_WARNING));
    }

    @Test
    public void testMISSING_PROPERTIES() {
        DiagnosticGroup missingPropertiesGroup = DiagnosticGroups.MISSING_PROPERTIES;
        assertNotNull(missingPropertiesGroup);
        assertEquals("missingProperties", missingPropertiesGroup.getName());
        assertTrue(missingPropertiesGroup.containsType(TypeCheck.INEXISTENT_PROPERTY));
    }

    @Test
    public void testINTERNET_EXPLORER_CHECKS() {
        DiagnosticGroup internetExplorerChecksGroup = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
        assertNotNull(internetExplorerChecksGroup);
        assertEquals("internetExplorerChecks", internetExplorerChecksGroup.getName());
        assertTrue(internetExplorerChecksGroup.containsType(RhinoErrorReporter.TRAILING_COMMA));
    }

    @Test
    public void testUNDEFINED_VARIABLES() {
        DiagnosticGroup undefinedVariablesGroup = DiagnosticGroups.UNDEFINED_VARIABLES;
        assertNotNull(undefinedVariablesGroup);
        assertEquals("undefinedVars", undefinedVariablesGroup.getName());
        assertTrue(undefinedVariablesGroup.containsType(VarCheck.UNDEFINED_VAR_ERROR));
    }

    @Test
    public void testCHECK_REGEXP() {
        DiagnosticGroup checkRegexpGroup = DiagnosticGroups.CHECK_REGEXP;
        assertNotNull(checkRegexpGroup);
        assertEquals("checkRegExp", checkRegexpGroup.getName());
        assertTrue(checkRegexpGroup.containsType(CheckRegExp.REGEXP_REFERENCE));
    }

    @Test
    public void testCHECK_TYPES() {
        DiagnosticGroup checkTypesGroup = DiagnosticGroups.CHECK_TYPES;
        assertNotNull(checkTypesGroup);
        assertEquals("checkTypes", checkTypesGroup.getName());
        // Add assertions for each DiagnosticType contained in checkTypesGroup
    }

    @Test
    public void testCHECK_VARIABLES() {
        DiagnosticGroup checkVariablesGroup = DiagnosticGroups.CHECK_VARIABLES;
        assertNotNull(checkVariablesGroup);
        assertEquals("checkVars", checkVariablesGroup.getName());
        // Add assertions for each DiagnosticType contained in checkVariablesGroup
    }

    @Test
    public void testCHECK_USELESS_CODE() {
        DiagnosticGroup checkUselessCodeGroup = DiagnosticGroups.CHECK_USELESS_CODE;
        assertNotNull(checkUselessCodeGroup);
        assertEquals("uselessCode", checkUselessCodeGroup.getName());
        assertTrue(checkUselessCodeGroup.containsType(CheckSideEffects.USELESS_CODE_ERROR));
        assertTrue(checkUselessCodeGroup.containsType(CheckUnreachableCode.UNREACHABLE_CODE));
    }

    @Test
    public void testTYPE_INVALIDATION() {
        DiagnosticGroup typeInvalidationGroup = DiagnosticGroups.TYPE_INVALIDATION;
        assertNotNull(typeInvalidationGroup);
        assertEquals("typeInvalidation", typeInvalidationGroup.getName());
        assertTrue(typeInvalidationGroup.containsType(DisambiguateProperties.Warnings.INVALIDATION));
    }
}