import org.junit.Test;

import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    // Basic JUnit test case for the registerGroup(name, group) method
    @Test
    public void testRegisterGroupWithGroup() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup();
        DiagnosticGroup result = DiagnosticGroups.registerGroup(name, group);
        assertEquals(group, result);
    }

    // Basic JUnit test case for the registerGroup(name, types) method
    @Test
    public void testRegisterGroupWithTypes() {
        String name = "testGroup";
        DiagnosticType[] types = new DiagnosticType[]{new DiagnosticType()};
        DiagnosticGroup group = DiagnosticGroups.registerGroup(name, types);
        assertNotNull(group);
        assertEquals(name, group.getName());
        for (DiagnosticType type : types) {
            assertTrue(group.contains(type));
        }
    }

    // Basic JUnit test case for the registerGroup(name, groups) method
    @Test
    public void testRegisterGroupWithGroups() {
        String name = "testGroup";
        DiagnosticGroup subGroup = new DiagnosticGroup();
        DiagnosticGroup[] groups = new DiagnosticGroup[]{subGroup};
        DiagnosticGroup group = DiagnosticGroups.registerGroup(name, groups);
        assertNotNull(group);
        assertEquals(name, group.getName());
        assertTrue(group.contains(subGroup));
    }

    // Test exception handling for the forName(name) method when the name is not registered
    @Test
    public void testForNameWithUnregisteredName() {
        String name = "unregisteredGroup";
        DiagnosticGroup group = DiagnosticGroups.forName(name);
        assertNull(group);
    }

    // Test the getRegisteredGroups() method
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertEquals(diagnosticGroups.getRegisteredGroups(), DiagnosticGroups.groupsByName);
    }

    // Test the GLOBAL_THIS constant
    @Test
    public void testGlobalThisConstant() {
        DiagnosticGroup globalThis = DiagnosticGroups.GLOBAL_THIS;
        assertNotNull(globalThis);
        assertTrue(globalThis.contains(CheckGlobalThis.GLOBAL_THIS));
    }

    // Test the DEPRECATED constant
    @Test
    public void testDeprecatedConstant() {
        DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
        assertNotNull(deprecated);
        assertTrue(deprecated.contains(
                CheckAccessControls.DEPRECATED_NAME,
                CheckAccessControls.DEPRECATED_NAME_REASON,
                CheckAccessControls.DEPRECATED_PROP,
                CheckAccessControls.DEPRECATED_PROP_REASON,
                CheckAccessControls.DEPRECATED_CLASS,
                CheckAccessControls.DEPRECATED_CLASS_REASON));
    }

    // Test the VISIBILITY constant
    @Test
    public void testVisibilityConstant() {
        DiagnosticGroup visibility = DiagnosticGroups.VISIBILITY;
        assertNotNull(visibility);
        assertTrue(visibility.contains(
                CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
                CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
                CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
                CheckAccessControls.PRIVATE_OVERRIDE,
                CheckAccessControls.VISIBILITY_MISMATCH));
    }

    // Test the CONSTANT_PROPERTY constant
    @Test
    public void testConstantPropertyConstant() {
        DiagnosticGroup constantProperty = DiagnosticGroups.CONSTANT_PROPERTY;
        assertNotNull(constantProperty);
        assertTrue(constantProperty.contains(
                CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE));
    }

    // Test the NON_STANDARD_JSDOC constant
    @Test
    public void testNonStandardJsDocConstant() {
        DiagnosticGroup nonStandardJsDocs = DiagnosticGroups.NON_STANDARD_JSDOC;
        assertNotNull(nonStandardJsDocs);
        assertTrue(nonStandardJsDocs.contains(RhinoErrorReporter.BAD_JSDOC_ANNOTATION));
    }

    // Test the ACCESS_CONTROLS constant
    @Test
    public void testAccessControlsConstant() {
        DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;
        assertNotNull(accessControls);
        assertTrue(accessControls.contains(DiagnosticGroups.DEPRECATED));
        assertTrue(accessControls.contains(DiagnosticGroups.VISIBILITY));
    }

    // Test the INVALID_CASTS constant
    @Test
    public void testInvalidCastsConstant() {
        DiagnosticGroup invalidCasts = DiagnosticGroups.INVALID_CASTS;
        assertNotNull(invalidCasts);
        assertTrue(invalidCasts.contains(TypeValidator.INVALID_CAST));
    }

    // Test the FILEOVERVIEW_JSDOC constant
    @Test
    public void testFileoverviewJsdocConstant() {
        DiagnosticGroup fileoverviewJsDoc = DiagnosticGroups.FILEOVERVIEW_JSDOC;
        assertNotNull(fileoverviewJsDoc);
        assertTrue(fileoverviewJsDoc.contains(RhinoErrorReporter.EXTRA_FILEOVERVIEW));
    }

    // Test the STRICT_MODULE_DEP_CHECK constant
    @Test
    public void testStrictModuleDepCheckConstant() {
        DiagnosticGroup strictModuleDepCheck = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
        assertNotNull(strictModuleDepCheck);
        assertTrue(strictModuleDepCheck.contains(
                VarCheck.STRICT_MODULE_DEP_ERROR,
                CheckGlobalNames.STRICT_MODULE_DEP_QNAME));
    }

    // Test the EXTERNS_VALIDATION constant
    @Test
    public void testExternsValidationConstant() {
        DiagnosticGroup externsValidation = DiagnosticGroups.EXTERNS_VALIDATION;
        assertNotNull(externsValidation);
        assertTrue(externsValidation.contains(
                VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
                VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
    }

    // Test the AMBIGUOUS_FUNCTION_DECL constant
    @Test
    public void testAmbiguousFunctionDeclConstant() {
        DiagnosticGroup ambiguousFunctionDecl = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
        assertNotNull(ambiguousFunctionDecl);
        assertTrue(ambiguousFunctionDecl.contains(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL));
    }

    // Test the UNKNOWN_DEFINES constant
    @Test
    public void testUnknownDefinesConstant() {
        DiagnosticGroup unknownDefines = DiagnosticGroups.UNKNOWN_DEFINES;
        assertNotNull(unknownDefines);
        assertTrue(unknownDefines.contains(ProcessDefines.UNKNOWN_DEFINE_WARNING));
    }

    // Test the TWEAKS constant
    @Test
    public void testTweaksConstant() {
        DiagnosticGroup tweaks = DiagnosticGroups.TWEAKS;
        assertNotNull(tweaks);
        assertTrue(tweaks.contains(
                ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
                ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
                ProcessTweaks.UNKNOWN_TWEAK_WARNING));
    }

    // Test the MISSING_PROPERTIES constant
    @Test
    public void testMissingPropertiesConstant() {
        DiagnosticGroup missingProperties = DiagnosticGroups.MISSING_PROPERTIES;
        assertNotNull(missingProperties);
        assertTrue(missingProperties.contains(TypeCheck.INEXISTENT_PROPERTY));
    }

    // Test the INTERNET_EXPLORER_CHECKS constant
    @Test
    public void testInternetExplorerChecksConstant() {
        DiagnosticGroup internetExplorerChecks = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
        assertNotNull(internetExplorerChecks);
        assertTrue(internetExplorerChecks.contains(RhinoErrorReporter.TRAILING_COMMA));
    }

    // Test the UNDEFINED_VARIABLES constant
    @Test
    public void testUndefinedVariablesConstant() {
        DiagnosticGroup undefinedVariables = DiagnosticGroups.UNDEFINED_VARIABLES;
        assertNotNull(undefinedVariables);
        assertTrue(undefinedVariables.contains(VarCheck.UNDEFINED_VAR_ERROR));
    }

    // Test the CHECK_REGEXP constant
    @Test
    public void testCheckRegExpConstant() {
        DiagnosticGroup checkRegExp = DiagnosticGroups.CHECK_REGEXP;
        assertNotNull(checkRegExp);
        assertTrue(checkRegExp.contains(CheckRegExp.REGEXP_REFERENCE));
    }

    // Test the CHECK_TYPES constant
    @Test
    public void testCheckTypesConstant() {
        DiagnosticGroup checkTypes = DiagnosticGroups.CHECK_TYPES;
        assertNotNull(checkTypes);
        assertTrue(checkTypes.contains(TypeValidator.ALL_DIAGNOSTICS));
        assertTrue(checkTypes.contains(TypeCheck.ALL_DIAGNOSTICS));
    }

    // Test the CHECK_VARIABLES constant
    @Test
    public void testCheckVariablesConstant() {
        DiagnosticGroup checkVariables = DiagnosticGroups.CHECK_VARIABLES;
        assertNotNull(checkVariables);
        assertTrue(checkVariables.contains(
                VarCheck.UNDEFINED_VAR_ERROR,
                SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR));
    }

    // Test the CHECK_USELESS_CODE constant
    @Test
    public void testCheckUselessCodeConstant() {
        DiagnosticGroup checkUselessCode = DiagnosticGroups.CHECK_USELESS_CODE;
        assertNotNull(checkUselessCode);
        assertTrue(checkUselessCode.contains(
                CheckSideEffects.USELESS_CODE_ERROR,
                CheckUnreachableCode.UNREACHABLE_CODE));
    }

    // Test the TYPE_INVALIDATION constant
    @Test
    public void testTypeInvalidationConstant() {
        DiagnosticGroup typeInvalidation = DiagnosticGroups.TYPE_INVALIDATION;
        assertNotNull(typeInvalidation);
        assertTrue(typeInvalidation.contains(DisambiguateProperties.Warnings.INVALIDATION));
    }
}

