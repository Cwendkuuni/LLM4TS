import org.junit.Test;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupWithNameAndGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testName", group);

        assertEquals(group, result);
        assertTrue(DiagnosticGroups.getRegisteredGroups().containsKey("testName"));
        assertEquals(group, DiagnosticGroups.getRegisteredGroups().get("testName"));
    }

    @Test
    public void testRegisterGroupWithNameAndTypes() {
        DiagnosticType type = new DiagnosticType("testType");
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testName", type);

        assertNotNull(result);
        assertTrue(DiagnosticGroups.getRegisteredGroups().containsKey("testName"));
        assertEquals(1, DiagnosticGroups.getRegisteredGroups().get("testName").size());
        assertEquals(type, DiagnosticGroups.getRegisteredGroups().get("testName").get(0));
    }

    @Test
    public void testRegisterGroupWithNameAndGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testName", group1, group2);

        assertNotNull(result);
        assertTrue(DiagnosticGroups.getRegisteredGroups().containsKey("testName"));
        assertEquals(2, DiagnosticGroups.getRegisteredGroups().get("testName").size());
        assertTrue(DiagnosticGroups.getRegisteredGroups().get("testName").contains(group1));
        assertTrue(DiagnosticGroups.getRegisteredGroups().get("testName").contains(group2));
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("testName", group);

        Map<String, DiagnosticGroup> result = DiagnosticGroups.getRegisteredGroups();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("testName"));
        assertEquals(group, result.get("testName"));
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("testName", group);

        DiagnosticGroup result = DiagnosticGroups.forName("testName");
        assertEquals(group, result);
    }

    // Additional test cases for each public static final DiagnosticGroup

    @Test
    public void testGlobalThis() {
        DiagnosticGroup result = DiagnosticGroups.GLOBAL_THIS;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CheckGlobalThis.GLOBAL_THIS, result.get(0));
    }

    @Test
    public void testDeprecated() {
        DiagnosticGroup result = DiagnosticGroups.DEPRECATED;
        assertNotNull(result);
        assertEquals(6, result.size());
        assertTrue(result.contains(CheckAccessControls.DEPRECATED_NAME));
        assertTrue(result.contains(CheckAccessControls.DEPRECATED_NAME_REASON));
        assertTrue(result.contains(CheckAccessControls.DEPRECATED_PROP));
        assertTrue(result.contains(CheckAccessControls.DEPRECATED_PROP_REASON));
        assertTrue(result.contains(CheckAccessControls.DEPRECATED_CLASS));
        assertTrue(result.contains(CheckAccessControls.DEPRECATED_CLASS_REASON));
    }

    @Test
    public void testVisibility() {
        DiagnosticGroup result = DiagnosticGroups.VISIBILITY;
        assertNotNull(result);
        assertEquals(5, result.size());
        assertTrue(result.contains(CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS));
        assertTrue(result.contains(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS));
        assertTrue(result.contains(CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS));
        assertTrue(result.contains(CheckAccessControls.PRIVATE_OVERRIDE));
        assertTrue(result.contains(CheckAccessControls.VISIBILITY_MISMATCH));
    }

    @Test
    public void testConstantProperty() {
        DiagnosticGroup result = DiagnosticGroups.CONSTANT_PROPERTY;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE, result.get(0));
    }

    @Test
    public void testNonStandardJsDocs() {
        DiagnosticGroup result = DiagnosticGroups.NON_STANDARD_JSDOC;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RhinoErrorReporter.BAD_JSDOC_ANNOTATION, result.get(0));
    }

    @Test
    public void testAccessControls() {
        DiagnosticGroup result = DiagnosticGroups.ACCESS_CONTROLS;
        assertNotNull(result);
        assertEquals(11, result.size());
        assertTrue(result.contains(DiagnosticGroups.DEPRECATED));
        assertTrue(result.contains(DiagnosticGroups.VISIBILITY));
    }

    @Test
    public void testInvalidCasts() {
        DiagnosticGroup result = DiagnosticGroups.INVALID_CASTS;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TypeValidator.INVALID_CAST, result.get(0));
    }

    @Test
    public void testFileoverviewJsDoc() {
        DiagnosticGroup result = DiagnosticGroups.FILEOVERVIEW_JSDOC;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RhinoErrorReporter.EXTRA_FILEOVERVIEW, result.get(0));
    }

    @Test
    public void testStrictModuleDepCheck() {
        DiagnosticGroup result = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(VarCheck.STRICT_MODULE_DEP_ERROR));
        assertTrue(result.contains(CheckGlobalNames.STRICT_MODULE_DEP_QNAME));
    }

    @Test
    public void testExternsValidation() {
        DiagnosticGroup result = DiagnosticGroups.EXTERNS_VALIDATION;
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR));
        assertTrue(result.contains(VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
    }

    @Test
    public void testAmbiguousFunctionDecl() {
        DiagnosticGroup result = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL, result.get(0));
    }

    @Test
    public void testUnknownDefines() {
        DiagnosticGroup result = DiagnosticGroups.UNKNOWN_DEFINES;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ProcessDefines.UNKNOWN_DEFINE_WARNING, result.get(0));
    }

    @Test
    public void testTweaks() {
        DiagnosticGroup result = DiagnosticGroups.TWEAKS;
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING));
        assertTrue(result.contains(ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING));
        assertTrue(result.contains(ProcessTweaks.UNKNOWN_TWEAK_WARNING));
    }

    @Test
    public void testMissingProperties() {
        DiagnosticGroup result = DiagnosticGroups.MISSING_PROPERTIES;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TypeCheck.INEXISTENT_PROPERTY, result.get(0));
    }

    @Test
    public void testInternetExplorerChecks() {
        DiagnosticGroup result = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RhinoErrorReporter.TRAILING_COMMA, result.get(0));
    }

    @Test
    public void testUndefinedVariables() {
        DiagnosticGroup result = DiagnosticGroups.UNDEFINED_VARIABLES;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(VarCheck.UNDEFINED_VAR_ERROR, result.get(0));
    }

    @Test
    public void testCheckRegExp() {
        DiagnosticGroup result = DiagnosticGroups.CHECK_REGEXP;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CheckRegExp.REGEXP_REFERENCE, result.get(0));
    }

    @Test
    public void testCheckTypes() {
        DiagnosticGroup result = DiagnosticGroups.CHECK_TYPES;
        assertNotNull(result);
        assertTrue(result.containsAll(TypeValidator.ALL_DIAGNOSTICS));
        assertTrue(result.containsAll(TypeCheck.ALL_DIAGNOSTICS));
    }

    @Test
    public void testCheckVariables() {
        DiagnosticGroup result = DiagnosticGroups.CHECK_VARIABLES;
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(VarCheck.UNDEFINED_VAR_ERROR));
        assertTrue(result.contains(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR));
    }

    @Test
    public void testCheckUselessCode() {
        DiagnosticGroup result = DiagnosticGroups.CHECK_USELESS_CODE;
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(CheckSideEffects.USELESS_CODE_ERROR));
        assertTrue(result.contains(CheckUnreachableCode.UNREACHABLE_CODE));
    }

    @Test
    public void testTypeInvalidation() {
        DiagnosticGroup result = DiagnosticGroups.TYPE_INVALIDATION;
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DisambiguateProperties.Warnings.INVALIDATION, result.get(0));
    }

    // Additional test cases for setWarningLevels method

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
        CheckLevel level = CheckLevel.WARNING;

        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroups.registerGroup("testName1", group1);
        DiagnosticGroups.registerGroup("testName2", group2);

        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);

        assertEquals(level, options.getWarningLevel(group1));
        assertEquals(level, options.getWarningLevel(group2));
    }
}