import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registered = DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, registered);
    }

    @Test
    public void testRegisterGroupWithTypes() {
        DiagnosticType type1 = new DiagnosticType("testType1");
        DiagnosticType type2 = new DiagnosticType("testType2");
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", type1, type2);
        assertTrue(group.contains(type1));
        assertTrue(group.contains(type2));
    }

    @Test
    public void testRegisterGroupWithGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", group1, group2);
        assertTrue(group.contains(group1));
        assertTrue(group.contains(group2));
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroups.registerGroup("testGroup1", group1);
        DiagnosticGroups.registerGroup("testGroup2", group2);
        assertEquals(group1, DiagnosticGroups.getRegisteredGroups().get("testGroup1"));
        assertEquals(group2, DiagnosticGroups.getRegisteredGroups().get("testGroup2"));
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, DiagnosticGroups.forName("testGroup"));
    }

    @Test
    public void testGlobalThis() {
        assertTrue(DiagnosticGroups.GLOBAL_THIS.contains(CheckGlobalThis.GLOBAL_THIS));
    }

    @Test
    public void testDeprecated() {
        assertTrue(DiagnosticGroups.DEPRECATED.contains(
                CheckAccessControls.DEPRECATED_NAME,
                CheckAccessControls.DEPRECATED_NAME_REASON,
                CheckAccessControls.DEPRECATED_PROP,
                CheckAccessControls.DEPRECATED_PROP_REASON,
                CheckAccessControls.DEPRECATED_CLASS,
                CheckAccessControls.DEPRECATED_CLASS_REASON
        ));
    }

    @Test
    public void testVisibility() {
        assertTrue(DiagnosticGroups.VISIBILITY.contains(
                CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
                CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
                CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
                CheckAccessControls.PRIVATE_OVERRIDE,
                CheckAccessControls.VISIBILITY_MISMATCH
        ));
    }

    @Test
    public void testConstantProperty() {
        assertTrue(DiagnosticGroups.CONSTANT_PROPERTY.contains(
                CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE
        ));
    }

    @Test
    public void testNonStandardJsDocs() {
        assertTrue(DiagnosticGroups.NON_STANDARD_JSDOC.contains(
                RhinoErrorReporter.BAD_JSDOC_ANNOTATION
        ));
    }

    @Test
    public void testAccessControls() {
        assertTrue(DiagnosticGroups.ACCESS_CONTROLS.contains(
                DiagnosticGroups.DEPRECATED,
                DiagnosticGroups.VISIBILITY
        ));
    }

    @Test
    public void testInvalidCasts() {
        assertTrue(DiagnosticGroups.INVALID_CASTS.contains(
                TypeValidator.INVALID_CAST
        ));
    }

    @Test
    public void testFileOverviewJsDoc() {
        assertTrue(DiagnosticGroups.FILEOVERVIEW_JSDOC.contains(
                RhinoErrorReporter.EXTRA_FILEOVERVIEW
        ));
    }

    @Test
    public void testStrictModuleDepCheck() {
        assertTrue(DiagnosticGroups.STRICT_MODULE_DEP_CHECK.contains(
                VarCheck.STRICT_MODULE_DEP_ERROR,
                CheckGlobalNames.STRICT_MODULE_DEP_QNAME
        ));
    }

    @Test
    public void testExternsValidation() {
        assertTrue(DiagnosticGroups.EXTERNS_VALIDATION.contains(
                VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
                VarCheck.UNDEFINED_EXTERN_VAR_ERROR
        ));
    }

    @Test
    public void testAmbiguousFunctionDecl() {
        assertTrue(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL.contains(
                VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL
        ));
    }

    @Test
    public void testUnknownDefines() {
        assertTrue(DiagnosticGroups.UNKNOWN_DEFINES.contains(
                ProcessDefines.UNKNOWN_DEFINE_WARNING
        ));
    }

    @Test
    public void testTweaks() {
        assertTrue(DiagnosticGroups.TWEAKS.contains(
                ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
                ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
                ProcessTweaks.UNKNOWN_TWEAK_WARNING
        ));
    }

    @Test
    public void testMissingProperties() {
        assertTrue(DiagnosticGroups.MISSING_PROPERTIES.contains(
                TypeCheck.INEXISTENT_PROPERTY
        ));
    }

    @Test
    public void testInternetExplorerChecks() {
        assertTrue(DiagnosticGroups.INTERNET_EXPLORER_CHECKS.contains(
                RhinoErrorReporter.TRAILING_COMMA
        ));
    }

    @Test
    public void testUndefinedVariables() {
        assertTrue(DiagnosticGroups.UNDEFINED_VARIABLES.contains(
                VarCheck.UNDEFINED_VAR_ERROR
        ));
    }

    @Test
    public void testCheckRegExp() {
        assertTrue(DiagnosticGroups.CHECK_REGEXP.contains(
                CheckRegExp.REGEXP_REFERENCE
        ));
    }

    @Test
    public void testCheckTypes() {
        assertTrue(DiagnosticGroups.CHECK_TYPES.contains(
                TypeValidator.ALL_DIAGNOSTICS,
                TypeCheck.ALL_DIAGNOSTICS
        ));
    }

    @Test
    public void testCheckVariables() {
        assertTrue(DiagnosticGroups.CHECK_VARIABLES.contains(
                VarCheck.UNDEFINED_VAR_ERROR,
                SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR
        ));
    }

    @Test
    public void testCheckUselessCode() {
        assertTrue(DiagnosticGroups.CHECK_USELESS_CODE.contains(
                CheckSideEffects.USELESS_CODE_ERROR,
                CheckUnreachableCode.UNREACHABLE_CODE
        ));
    }

    @Test
    public void testTypeInvalidation() {
        assertTrue(DiagnosticGroups.TYPE_INVALIDATION.contains(
                DisambiguateProperties.Warnings.INVALIDATION
        ));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        List<String> groups = Arrays.asList("testGroup1", "testGroup2");
        diagnosticGroups.setWarningLevels(options, groups, CheckLevel.WARNING);
        assertEquals(CheckLevel.WARNING, options.getWarningLevel("testGroup1"));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel("testGroup2"));
    }
}