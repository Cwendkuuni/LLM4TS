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
    public void testRegisterGroupWithDiagnosticGroupParameter() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, result);
    }

    @Test
    public void testRegisterGroupWithDiagnosticTypeArrayParameter() {
        DiagnosticType[] types = new DiagnosticType[]{DiagnosticType.TEST_TYPE};
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", types);
        assertEquals(1, result.getTypes().size());
        assertTrue(result.getTypes().contains(DiagnosticType.TEST_TYPE));
    }

    @Test
    public void testRegisterGroupWithDiagnosticGroupArrayParameter() {
        DiagnosticGroup[] groups = new DiagnosticGroup[]{new DiagnosticGroup("testGroup1"), new DiagnosticGroup("testGroup2")};
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", groups);
        assertEquals(2, result.getGroups().size());
        assertTrue(result.getGroups().contains(new DiagnosticGroup("testGroup1")));
        assertTrue(result.getGroups().contains(new DiagnosticGroup("testGroup2")));
    }

    @Test
    public void testGetRegisteredGroups() {
        assertEquals(27, diagnosticGroups.getRegisteredGroups().size());
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        diagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, diagnosticGroups.forName("testGroup"));
    }

    @Test
    public void testGlobalThis() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("globalThis",
                CheckGlobalThis.GLOBAL_THIS);
        assertEquals(expected, DiagnosticGroups.GLOBAL_THIS);
    }

    @Test
    public void testDeprecated() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("deprecated",
                CheckAccessControls.DEPRECATED_NAME,
                CheckAccessControls.DEPRECATED_NAME_REASON,
                CheckAccessControls.DEPRECATED_PROP,
                CheckAccessControls.DEPRECATED_PROP_REASON,
                CheckAccessControls.DEPRECATED_CLASS,
                CheckAccessControls.DEPRECATED_CLASS_REASON);
        assertEquals(expected, DiagnosticGroups.DEPRECATED);
    }

    @Test
    public void testVisibility() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("visibility",
                CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
                CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
                CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
                CheckAccessControls.PRIVATE_OVERRIDE,
                CheckAccessControls.VISIBILITY_MISMATCH);
        assertEquals(expected, DiagnosticGroups.VISIBILITY);
    }

    @Test
    public void testConstantProperty() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("constantProperty",
                CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE);
        assertEquals(expected, DiagnosticGroups.CONSTANT_PROPERTY);
    }

    @Test
    public void testNonStandardJsDocs() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("nonStandardJsDocs",
                RhinoErrorReporter.BAD_JSDOC_ANNOTATION);
        assertEquals(expected, DiagnosticGroups.NON_STANDARD_JSDOC);
    }

    @Test
    public void testAccessControls() {
        DiagnosticGroup deprecatedGroup = DiagnosticGroups.registerGroup("deprecated",
                CheckAccessControls.DEPRECATED_NAME,
                CheckAccessControls.DEPRECATED_NAME_REASON,
                CheckAccessControls.DEPRECATED_PROP,
                CheckAccessControls.DEPRECATED_PROP_REASON,
                CheckAccessControls.DEPRECATED_CLASS,
                CheckAccessControls.DEPRECATED_CLASS_REASON);

        DiagnosticGroup visibilityGroup = DiagnosticGroups.registerGroup("visibility",
                CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
                CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
                CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
                CheckAccessControls.PRIVATE_OVERRIDE,
                CheckAccessControls.VISIBILITY_MISMATCH);

        DiagnosticGroup expected = DiagnosticGroups.registerGroup("accessControls",
                deprecatedGroup, visibilityGroup);
        assertEquals(expected, DiagnosticGroups.ACCESS_CONTROLS);
    }

    @Test
    public void testInvalidCasts() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("invalidCasts",
                TypeValidator.INVALID_CAST);
        assertEquals(expected, DiagnosticGroups.INVALID_CASTS);
    }

    @Test
    public void testFileoverviewTags() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("fileoverviewTags",
                RhinoErrorReporter.EXTRA_FILEOVERVIEW);
        assertEquals(expected, DiagnosticGroups.FILEOVERVIEW_JSDOC);
    }

    @Test
    public void testStrictModuleDepCheck() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("strictModuleDepCheck",
                VarCheck.STRICT_MODULE_DEP_ERROR,
                CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
        assertEquals(expected, DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
    }

    @Test
    public void testExternsValidation() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("externsValidation",
                VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
                VarCheck.UNDEFINED_EXTERN_VAR_ERROR);
        assertEquals(expected, DiagnosticGroups.EXTERNS_VALIDATION);
    }

    @Test
    public void testAmbiguousFunctionDecl() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("ambiguousFunctionDecl",
                VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL);
        assertEquals(expected, DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
    }

    @Test
    public void testUnknownDefines() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("unknownDefines",
                ProcessDefines.UNKNOWN_DEFINE_WARNING);
        assertEquals(expected, DiagnosticGroups.UNKNOWN_DEFINES);
    }

    @Test
    public void testTweaks() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("tweakValidation",
                ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
                ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
                ProcessTweaks.UNKNOWN_TWEAK_WARNING);
        assertEquals(expected, DiagnosticGroups.TWEAKS);
    }

    @Test
    public void testMissingProperties() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("missingProperties",
                TypeCheck.INEXISTENT_PROPERTY);
        assertEquals(expected, DiagnosticGroups.MISSING_PROPERTIES);
    }

    @Test
    public void testInternetExplorerChecks() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("internetExplorerChecks",
                RhinoErrorReporter.TRAILING_COMMA);
        assertEquals(expected, DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
    }

    @Test
    public void testUndefinedVariables() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("undefinedVars",
                VarCheck.UNDEFINED_VAR_ERROR);
        assertEquals(expected, DiagnosticGroups.UNDEFINED_VARIABLES);
    }

    @Test
    public void testCheckRegExp() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("checkRegExp",
                CheckRegExp.REGEXP_REFERENCE);
        assertEquals(expected, DiagnosticGroups.CHECK_REGEXP);
    }

    @Test
    public void testCheckTypes() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("checkTypes",
                TypeValidator.ALL_DIAGNOSTICS,
                TypeCheck.ALL_DIAGNOSTICS);
        assertEquals(expected, DiagnosticGroups.CHECK_TYPES);
    }

    @Test
    public void testCheckVariables() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("checkVars",
                VarCheck.UNDEFINED_VAR_ERROR,
                SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR);
        assertEquals(expected, DiagnosticGroups.CHECK_VARIABLES);
    }

    @Test
    public void testCheckUselessCode() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("uselessCode",
                CheckSideEffects.USELESS_CODE_ERROR,
                CheckUnreachableCode.UNREACHABLE_CODE);
        assertEquals(expected, DiagnosticGroups.CHECK_USELESS_CODE);
    }

    @Test
    public void testTypeInvalidation() {
        DiagnosticGroup expected = DiagnosticGroups.registerGroup("typeInvalidation",
                DisambiguateProperties.Warnings.INVALIDATION);
        assertEquals(expected, DiagnosticGroups.TYPE_INVALIDATION);
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("deprecated", "visibility", "invalidCasts");
        diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.DEPRECATED));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.VISIBILITY));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.INVALID_CASTS));
    }
}