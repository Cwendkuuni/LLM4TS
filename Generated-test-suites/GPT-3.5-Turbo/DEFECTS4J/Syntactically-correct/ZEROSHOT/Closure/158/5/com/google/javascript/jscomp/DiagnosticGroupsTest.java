import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {

  private DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

  @Test
  public void testRegisterGroupWithNameAndGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("testGroup");
    DiagnosticGroup result = diagnosticGroups.registerGroup("testName", expected);
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterGroupWithNameAndDiagnosticTypeArray() {
    DiagnosticGroup expected = new DiagnosticGroup("testGroup");
    DiagnosticGroup result = diagnosticGroups.registerGroup("testName", new DiagnosticType[]{});
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterGroupWithNameAndDiagnosticGroupArray() {
    DiagnosticGroup expected = new DiagnosticGroup("testGroup");
    DiagnosticGroup result = diagnosticGroups.registerGroup("testName", new DiagnosticGroup[]{});
    assertEquals(expected, result);
  }

  @Test
  public void testGetRegisteredGroups() {
    assertNotNull(diagnosticGroups.getRegisteredGroups());
  }

  @Test
  public void testForName() {
    DiagnosticGroup expected = new DiagnosticGroup("testGroup");
    diagnosticGroups.registerGroup("testName", expected);
    DiagnosticGroup result = diagnosticGroups.forName("testName");
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterGlobalThis() {
    assertEquals(DiagnosticGroups.GLOBAL_THIS, DiagnosticGroups.registerGroup("globalThis", CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testRegisterDeprecated() {
    assertEquals(DiagnosticGroups.DEPRECATED, DiagnosticGroups.registerGroup("deprecated", CheckAccessControls.DEPRECATED_NAME, CheckAccessControls.DEPRECATED_NAME_REASON, CheckAccessControls.DEPRECATED_PROP, CheckAccessControls.DEPRECATED_PROP_REASON, CheckAccessControls.DEPRECATED_CLASS, CheckAccessControls.DEPRECATED_CLASS_REASON));
  }

  @Test
  public void testRegisterVisibility() {
    assertEquals(DiagnosticGroups.VISIBILITY, DiagnosticGroups.registerGroup("visibility", CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS, CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS, CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS, CheckAccessControls.PRIVATE_OVERRIDE, CheckAccessControls.VISIBILITY_MISMATCH));
  }

  @Test
  public void testRegisterConstantProperty() {
    assertEquals(DiagnosticGroups.CONSTANT_PROPERTY, DiagnosticGroups.registerGroup("constantProperty", CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE));
  }

  @Test
  public void testRegisterNonStandardJsDocs() {
    assertEquals(DiagnosticGroups.NON_STANDARD_JSDOC, DiagnosticGroups.registerGroup("nonStandardJsDocs", RhinoErrorReporter.BAD_JSDOC_ANNOTATION));
  }

  @Test
  public void testRegisterAccessControls() {
    assertEquals(DiagnosticGroups.ACCESS_CONTROLS, DiagnosticGroups.registerGroup("accessControls", DiagnosticGroups.DEPRECATED, DiagnosticGroups.VISIBILITY));
  }

  @Test
  public void testRegisterInvalidCasts() {
    assertEquals(DiagnosticGroups.INVALID_CASTS, DiagnosticGroups.registerGroup("invalidCasts", TypeValidator.INVALID_CAST));
  }

  @Test
  public void testRegisterFileoverviewJSDoc() {
    assertEquals(DiagnosticGroups.FILEOVERVIEW_JSDOC, DiagnosticGroups.registerGroup("fileoverviewTags", RhinoErrorReporter.EXTRA_FILEOVERVIEW));
  }

  @Test
  public void testRegisterStrictModuleDepCheck() {
    assertEquals(DiagnosticGroups.STRICT_MODULE_DEP_CHECK, DiagnosticGroups.registerGroup("strictModuleDepCheck", VarCheck.STRICT_MODULE_DEP_ERROR, CheckGlobalNames.STRICT_MODULE_DEP_QNAME));
  }

  @Test
  public void testRegisterExternsValidation() {
    assertEquals(DiagnosticGroups.EXTERNS_VALIDATION, DiagnosticGroups.registerGroup("externsValidation", VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
  }

  @Test
  public void testRegisterAmbiguousFunctionDecl() {
    assertEquals(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL, DiagnosticGroups.registerGroup("ambiguousFunctionDecl", VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL));
  }

  @Test
  public void testRegisterUnknownDefines() {
    assertEquals(DiagnosticGroups.UNKNOWN_DEFINES, DiagnosticGroups.registerGroup("unknownDefines", ProcessDefines.UNKNOWN_DEFINE_WARNING));
  }

  @Test
  public void testRegisterTweaks() {
    assertEquals(DiagnosticGroups.TWEAKS, DiagnosticGroups.registerGroup("tweakValidation", ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING, ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING, ProcessTweaks.UNKNOWN_TWEAK_WARNING));
  }

  @Test
  public void testRegisterMissingProperties() {
    assertEquals(DiagnosticGroups.MISSING_PROPERTIES, DiagnosticGroups.registerGroup("missingProperties", TypeCheck.INEXISTENT_PROPERTY));
  }

  @Test
  public void testRegisterInternetExplorerChecks() {
    assertEquals(DiagnosticGroups.INTERNET_EXPLORER_CHECKS, DiagnosticGroups.registerGroup("internetExplorerChecks", RhinoErrorReporter.TRAILING_COMMA));
  }

  @Test
  public void testRegisterUndefinedVariables() {
    assertEquals(DiagnosticGroups.UNDEFINED_VARIABLES, DiagnosticGroups.registerGroup("undefinedVars", VarCheck.UNDEFINED_VAR_ERROR));
  }

  @Test
  public void testRegisterCheckRegExp() {
    assertEquals(DiagnosticGroups.CHECK_REGEXP, DiagnosticGroups.registerGroup("checkRegExp", CheckRegExp.REGEXP_REFERENCE));
  }

  @Test
  public void testRegisterCheckTypes() {
    assertEquals(DiagnosticGroups.CHECK_TYPES, DiagnosticGroups.registerGroup("checkTypes", TypeValidator.ALL_DIAGNOSTICS, TypeCheck.ALL_DIAGNOSTICS));
  }

  @Test
  public void testRegisterCheckVariables() {
    assertEquals(DiagnosticGroups.CHECK_VARIABLES, DiagnosticGroups.registerGroup("checkVars", VarCheck.UNDEFINED_VAR_ERROR, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR));
  }

  @Test
  public void testRegisterCheckUselessCode() {
    assertEquals(DiagnosticGroups.CHECK_USELESS_CODE, DiagnosticGroups.registerGroup("uselessCode", CheckSideEffects.USELESS_CODE_ERROR, CheckUnreachableCode.UNREACHABLE_CODE));
  }

  @Test
  public void testRegisterTypeInvalidation() {
    assertEquals(DiagnosticGroups.TYPE_INVALIDATION, DiagnosticGroups.registerGroup("typeInvalidation", DisambiguateProperties.Warnings.INVALIDATION));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.DEPRECATED));
  }
}