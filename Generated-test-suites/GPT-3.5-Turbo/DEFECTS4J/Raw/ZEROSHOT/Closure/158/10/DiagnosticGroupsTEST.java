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
    assertNull(diagnosticGroups.forName("nonExistentGroup"));
  }

  @Test
  public void testGLOBAL_THIS() {
    assertEquals(DiagnosticGroups.registerGroup("globalThis",
        CheckGlobalThis.GLOBAL_THIS), DiagnosticGroups.GLOBAL_THIS);
  }

  @Test
  public void testDEPRECATED() {
    assertEquals(DiagnosticGroups.registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON), DiagnosticGroups.DEPRECATED);
  }

  @Test
  public void testVISIBILITY() {
    assertEquals(DiagnosticGroups.registerGroup("visibility",
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH), DiagnosticGroups.VISIBILITY);
  }

  @Test
  public void testCONSTANT_PROPERTY() {
    assertEquals(DiagnosticGroups.registerGroup("constantProperty",
        CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE), DiagnosticGroups.CONSTANT_PROPERTY);
  }

  @Test
  public void testNON_STANDARD_JSDOC(){
    assertEquals(DiagnosticGroups.registerGroup("nonStandardJsDocs",
        RhinoErrorReporter.BAD_JSDOC_ANNOTATION), DiagnosticGroups.NON_STANDARD_JSDOC);
  }

  @Test
  public void testACCESS_CONTROLS() {
    assertEquals(DiagnosticGroups.registerGroup("accessControls",
        DiagnosticGroups.DEPRECATED, DiagnosticGroups.VISIBILITY), DiagnosticGroups.ACCESS_CONTROLS);
  }

  @Test
  public void testINVALID_CASTS() {
    assertEquals(DiagnosticGroups.registerGroup("invalidCasts",
        TypeValidator.INVALID_CAST), DiagnosticGroups.INVALID_CASTS);
  }

  @Test
  public void testFILEOVERVIEW_JSDOC() {
    assertEquals(DiagnosticGroups.registerGroup("fileoverviewTags",
        RhinoErrorReporter.EXTRA_FILEOVERVIEW), DiagnosticGroups.FILEOVERVIEW_JSDOC);
  }

  @Test
  public void testSTRICT_MODULE_DEP_CHECK() {
    assertEquals(DiagnosticGroups.registerGroup("strictModuleDepCheck",
        VarCheck.STRICT_MODULE_DEP_ERROR,
        CheckGlobalNames.STRICT_MODULE_DEP_QNAME), DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
  }

  @Test
  public void testEXTERNS_VALIDATION() {
    assertEquals(DiagnosticGroups.registerGroup("externsValidation",
        VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
        VarCheck.UNDEFINED_EXTERN_VAR_ERROR), DiagnosticGroups.EXTERNS_VALIDATION);
  }

  @Test
  public void testAMBIGUOUS_FUNCTION_DECL() {
    assertEquals(DiagnosticGroups.registerGroup("ambiguousFunctionDecl",
        VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL), DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
  }

  @Test
  public void testUNKNOWN_DEFINES() {
    assertEquals(DiagnosticGroups.registerGroup("unknownDefines",
        ProcessDefines.UNKNOWN_DEFINE_WARNING), DiagnosticGroups.UNKNOWN_DEFINES);
  }

  @Test
  public void testTWEAKS() {
    assertEquals(DiagnosticGroups.registerGroup("tweakValidation",
        ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
        ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
        ProcessTweaks.UNKNOWN_TWEAK_WARNING), DiagnosticGroups.TWEAKS);
  }

  @Test
  public void testMISSING_PROPERTIES() {
    assertEquals(DiagnosticGroups.registerGroup("missingProperties",
        TypeCheck.INEXISTENT_PROPERTY), DiagnosticGroups.MISSING_PROPERTIES);
  }

  @Test
  public void testINTERNET_EXPLORER_CHECKS() {
    assertEquals(DiagnosticGroups.registerGroup("internetExplorerChecks",
        RhinoErrorReporter.TRAILING_COMMA), DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
  }

  @Test
  public void testUNDEFINED_VARIABLES() {
    assertEquals(DiagnosticGroups.registerGroup("undefinedVars",
        VarCheck.UNDEFINED_VAR_ERROR), DiagnosticGroups.UNDEFINED_VARIABLES);
  }

  @Test
  public void testCHECK_REGEXP() {
    assertEquals(DiagnosticGroups.registerGroup("checkRegExp",
        CheckRegExp.REGEXP_REFERENCE), DiagnosticGroups.CHECK_REGEXP);
  }

  @Test
  public void testCHECK_TYPES() {
    assertEquals(DiagnosticGroups.registerGroup("checkTypes",
        TypeValidator.ALL_DIAGNOSTICS,
        TypeCheck.ALL_DIAGNOSTICS), DiagnosticGroups.CHECK_TYPES);
  }

  @Test
  public void testCHECK_VARIABLES() {
    assertEquals(DiagnosticGroups.registerGroup("checkVars",
        VarCheck.UNDEFINED_VAR_ERROR,
        SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR), DiagnosticGroups.CHECK_VARIABLES);
  }

  @Test
  public void testCHECK_USELESS_CODE() {
    assertEquals(DiagnosticGroups.registerGroup("uselessCode",
        CheckSideEffects.USELESS_CODE_ERROR,
        CheckUnreachableCode.UNREACHABLE_CODE), DiagnosticGroups.CHECK_USELESS_CODE);
  }

  @Test
  public void testTYPE_INVALIDATION() {
    assertEquals(DiagnosticGroups.registerGroup("typeInvalidation",
        DisambiguateProperties.Warnings.INVALIDATION), DiagnosticGroups.TYPE_INVALIDATION);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.setWarningLevels(options,DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(","), CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(DiagnosticGroups.DEPRECATED) == CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(DiagnosticGroups.VISIBILITY) == CheckLevel.WARNING);
  }

}