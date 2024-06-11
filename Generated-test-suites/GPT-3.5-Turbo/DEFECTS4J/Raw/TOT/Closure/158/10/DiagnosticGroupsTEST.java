import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroupWithStringAndDiagnosticGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup result = DiagnosticGroups.registerGroup("test", group);

        assertEquals(group, result);
    }

    @Test
    public void testRegisterGroupWithStringAndDiagnosticTypes() {
        DiagnosticType[] types = {new DiagnosticType("type1"), new DiagnosticType("type2")};
        DiagnosticGroup group = new DiagnosticGroup("testGroup", types);
        DiagnosticGroup result = DiagnosticGroups.registerGroup("test", types);

        assertEquals(group, result);
    }

    @Test
    public void testRegisterGroupWithStringAndDiagnosticGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroup[] groups = {group1, group2};
        DiagnosticGroup group = new DiagnosticGroup("testGroup", groups);
        DiagnosticGroup result = DiagnosticGroups.registerGroup("test", groups);

        assertEquals(group, result);
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");

        DiagnosticGroups.registerGroup("group1", group1);
        DiagnosticGroups.registerGroup("group2", group2);

        assertEquals(group1, DiagnosticGroups.getRegisteredGroups().get("group1"));
        assertEquals(group2, DiagnosticGroups.getRegisteredGroups().get("group2"));
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("test", group);

        assertEquals(group, DiagnosticGroups.forName("test"));
    }

    @Test
    public void testConstructor() {
        DiagnosticGroups groups = new DiagnosticGroups();
        assertNotNull(groups);
    }

    @Test
    public void testSetWarningLevels() {
        DiagnosticGroups groups = new DiagnosticGroups();
        CompilerOptions options = new CompilerOptions();
        groups.setWarningLevels(options, Arrays.asList("group1", "group2"), CheckLevel.WARNING);

        assertEquals(CheckLevel.WARNING, options.getWarningLevel(groups.forName("group1")));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(groups.forName("group2")));
    }

    @Test
    public void testGLOBAL_THIS() {
        DiagnosticGroup group = new DiagnosticGroup("globalThis", CheckGlobalThis.GLOBAL_THIS);
        assertEquals(group, DiagnosticGroups.GLOBAL_THIS);
    }

    @Test
    public void testDEPRECATED() {
        DiagnosticGroup group = new DiagnosticGroup("deprecated",
          CheckAccessControls.DEPRECATED_NAME,
          CheckAccessControls.DEPRECATED_NAME_REASON,
          CheckAccessControls.DEPRECATED_PROP,
          CheckAccessControls.DEPRECATED_PROP_REASON,
          CheckAccessControls.DEPRECATED_CLASS,
          CheckAccessControls.DEPRECATED_CLASS_REASON);

        assertEquals(group, DiagnosticGroups.DEPRECATED);
    }

    @Test
    public void testVISIBILITY() {
        DiagnosticGroup group = new DiagnosticGroup("visibility",
          CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
          CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
          CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
          CheckAccessControls.PRIVATE_OVERRIDE,
          CheckAccessControls.VISIBILITY_MISMATCH);

        assertEquals(group, DiagnosticGroups.VISIBILITY);
    }

    @Test
    public void testCONSTANT_PROPERTY() {
        DiagnosticGroup group = new DiagnosticGroup("constantProperty",
          CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE);

        assertEquals(group, DiagnosticGroups.CONSTANT_PROPERTY);
    }

    @Test
    public void testNON_STANDARD_JSDOC() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("nonStandardJsDocs",
          RhinoErrorReporter.BAD_JSDOC_ANNOTATION);

        assertEquals(group, DiagnosticGroups.NON_STANDARD_JSDOC);
    }

    @Test
    public void testACCESS_CONTROLS() {
        DiagnosticGroup deprecatedGroup = new DiagnosticGroup("deprecated",
          CheckAccessControls.DEPRECATED_NAME,
          CheckAccessControls.DEPRECATED_NAME_REASON,
          CheckAccessControls.DEPRECATED_PROP,
          CheckAccessControls.DEPRECATED_PROP_REASON,
          CheckAccessControls.DEPRECATED_CLASS,
          CheckAccessControls.DEPRECATED_CLASS_REASON);

        DiagnosticGroup visibilityGroup = new DiagnosticGroup("visibility",
          CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
          CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
          CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
          CheckAccessControls.PRIVATE_OVERRIDE,
          CheckAccessControls.VISIBILITY_MISMATCH);

        DiagnosticGroup group = new DiagnosticGroup("accessControls", deprecatedGroup, visibilityGroup);

        assertEquals(group, DiagnosticGroups.ACCESS_CONTROLS);
    }

    @Test
    public void testINVALID_CASTS() {
        DiagnosticGroup group = new DiagnosticGroup("invalidCasts",
          TypeValidator.INVALID_CAST);

        assertEquals(group, DiagnosticGroups.INVALID_CASTS);
    }

    @Test
    public void testFILEOVERVIEW_JSDOC() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("fileoverviewTags",
          RhinoErrorReporter.EXTRA_FILEOVERVIEW);

        assertEquals(group, DiagnosticGroups.FILEOVERVIEW_JSDOC);
    }

    @Test
    public void testSTRICT_MODULE_DEP_CHECK() {
        DiagnosticGroup moduleDepGroup = new DiagnosticGroup("strictModuleDepCheck",
          VarCheck.STRICT_MODULE_DEP_ERROR,
          CheckGlobalNames.STRICT_MODULE_DEP_QNAME);

        assertEquals(group, DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
    }

    @Test
    public void testEXTERNS_VALIDATION() {
        DiagnosticGroup externsGroup = new DiagnosticGroup("externsValidation",
          VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
          VarCheck.UNDEFINED_EXTERN_VAR_ERROR);

        assertEquals(group, DiagnosticGroups.EXTERNS_VALIDATION);
    }

    @Test
    public void testAMBIGUOUS_FUNCTION_DECL() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("ambiguousFunctionDecl",
          VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL);

        assertEquals(group, DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
    }

    @Test
    public void testUNKNOWN_DEFINES() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("unknownDefines",
          ProcessDefines.UNKNOWN_DEFINE_WARNING);

        assertEquals(group, DiagnosticGroups.UNKNOWN_DEFINES);
    }

    @Test
    public void testTWEAKS() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("tweakValidation",
          ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
          ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
          ProcessTweaks.UNKNOWN_TWEAK_WARNING);

        assertEquals(group, DiagnosticGroups.TWEAKS);
    }

    @Test
    public void testMISSING_PROPERTIES() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("missingProperties",
          TypeCheck.INEXISTENT_PROPERTY);

        assertEquals(group, DiagnosticGroups.MISSING_PROPERTIES);
    }

    @Test
    public void testINTERNET_EXPLORER_CHECKS() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("internetExplorerChecks",
          RhinoErrorReporter.TRAILING_COMMA);

        assertEquals(group, DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
    }

    @Test
    public void testUNDEFINED_VARIABLES() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("undefinedVars",
          VarCheck.UNDEFINED_VAR_ERROR);

        assertEquals(group, DiagnosticGroups.UNDEFINED_VARIABLES);
    }

    @Test
    public void testCHECK_REGEXP() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("checkRegExp",
          CheckRegExp.REGEXP_REFERENCE);

        assertEquals(group, DiagnosticGroups.CHECK_REGEXP);
    }

    @Test
    public void testCHECK_TYPES() {
        DiagnosticGroup typeValidatorGroup = new DiagnosticGroup("typeValidator",
          TypeValidator.ALL_DIAGNOSTICS);

        DiagnosticGroup typeCheckGroup = new DiagnosticGroup("typeCheck",
          TypeCheck.ALL_DIAGNOSTICS);

        DiagnosticGroup group = new DiagnosticGroup("checkTypes", typeValidatorGroup, typeCheckGroup);

        assertEquals(group, DiagnosticGroups.CHECK_TYPES);
    }

    @Test
    public void testCHECK_VARIABLES() {
        DiagnosticGroup varCheckGroup = new DiagnosticGroup("varCheck",
          VarCheck.UNDEFINED_VAR_ERROR,
          SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR);

        DiagnosticGroup group = new DiagnosticGroup("checkVars", varCheckGroup);

        assertEquals(group, DiagnosticGroups.CHECK_VARIABLES);
    }

    @Test
    public void testCHECK_USELESS_CODE() {
        DiagnosticGroup checkSideEffectsGroup = new DiagnosticGroup("checkSideEffects",
          CheckSideEffects.USELESS_CODE_ERROR);

        DiagnosticGroup checkUnreachableCodeGroup = new DiagnosticGroup("checkUnreachableCode",
          CheckUnreachableCode.UNREACHABLE_CODE);

        DiagnosticGroup group = new DiagnosticGroup("checkUselessCode", checkSideEffectsGroup, checkUnreachableCodeGroup);

        assertEquals(group, DiagnosticGroups.CHECK_USELESS_CODE);
    }

    @Test
    public void testTYPE_INVALIDATION() {
        DiagnosticGroup group = new DiagnosticGroup("typeInvalidation",
          DisambiguateProperties.Warnings.INVALIDATION);

        assertEquals(group, DiagnosticGroups.TYPE_INVALIDATION);
    }

}