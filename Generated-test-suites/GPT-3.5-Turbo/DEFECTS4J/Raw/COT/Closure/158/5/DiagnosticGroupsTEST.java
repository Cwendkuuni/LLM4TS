import com.google.javascript.jscomp.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", DiagnosticType.TYPE_ERROR);
    assertNotNull(group);
    assertEquals("testGroup", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(DiagnosticType.TYPE_ERROR, group.getTypes().get(0));
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", DiagnosticType.TYPE_ERROR, DiagnosticType.WARNING);
    assertNotNull(group);
    assertEquals("testGroup", group.getName());
    assertEquals(2, group.getTypes().size());
    assertEquals(DiagnosticType.TYPE_ERROR, group.getTypes().get(0));
    assertEquals(DiagnosticType.WARNING, group.getTypes().get(1));
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroup subGroup1 = DiagnosticGroups.registerGroup("subGroup1", DiagnosticType.TYPE_ERROR);
    DiagnosticGroup subGroup2 = DiagnosticGroups.registerGroup("subGroup2", DiagnosticType.WARNING);
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", subGroup1, subGroup2);
    assertNotNull(group);
    assertEquals("testGroup", group.getName());
    assertEquals(2, group.getGroups().size());
    assertEquals(subGroup1, group.getGroups().get(0));
    assertEquals(subGroup2, group.getGroups().get(1));
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertTrue(diagnosticGroups.getRegisteredGroups().isEmpty());

    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", DiagnosticType.TYPE_ERROR);
    diagnosticGroups.getRegisteredGroups().put("testGroup", group);

    assertEquals(1, diagnosticGroups.getRegisteredGroups().size());
    assertEquals(group, diagnosticGroups.getRegisteredGroups().get("testGroup"));
  }

  @Test
  public void testForName() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", DiagnosticType.TYPE_ERROR);
    assertEquals(group, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testNonStandardJsDocsGroup() {
    DiagnosticGroup group = DiagnosticGroups.NON_STANDARD_JSDOC;
    assertEquals("nonStandardJsDocs", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(RhinoErrorReporter.BAD_JSDOC_ANNOTATION, group.getTypes().get(0));
  }

  @Test
  public void testAccessControlsGroup() {
    DiagnosticGroup group = DiagnosticGroups.ACCESS_CONTROLS;
    assertEquals("accessControls", group.getName());
    assertEquals(2, group.getGroups().size());
    assertEquals(DiagnosticGroups.DEPRECATED, group.getGroups().get(0));
    assertEquals(DiagnosticGroups.VISIBILITY, group.getGroups().get(1));
  }

  @Test
  public void testInvalidCastsGroup() {
    DiagnosticGroup group = DiagnosticGroups.INVALID_CASTS;
    assertEquals("invalidCasts", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(TypeValidator.INVALID_CAST, group.getTypes().get(0));
  }

  @Test
  public void testFileoverviewJsdocGroup() {
    DiagnosticGroup group = DiagnosticGroups.FILEOVERVIEW_JSDOC;
    assertEquals("fileoverviewTags", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(RhinoErrorReporter.EXTRA_FILEOVERVIEW, group.getTypes().get(0));
  }

  @Test
  public void testStrictModuleDepCheckGroup() {
    DiagnosticGroup group = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
    assertEquals("strictModuleDepCheck", group.getName());
    assertEquals(2, group.getTypes().size());
    assertEquals(VarCheck.STRICT_MODULE_DEP_ERROR, group.getTypes().get(0));
    assertEquals(CheckGlobalNames.STRICT_MODULE_DEP_QNAME, group.getTypes().get(1));
  }

  @Test
  public void testExternsValidationGroup() {
    DiagnosticGroup group = DiagnosticGroups.EXTERNS_VALIDATION;
    assertEquals("externsValidation", group.getName());
    assertEquals(2, group.getTypes().size());
    assertEquals(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, group.getTypes().get(0));
    assertEquals(VarCheck.UNDEFINED_EXTERN_VAR_ERROR, group.getTypes().get(1));
  }

  @Test
  public void testAmbiguousFunctionDeclGroup() {
    DiagnosticGroup group = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
    assertEquals("ambiguousFunctionDecl", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL, group.getTypes().get(0));
  }

  @Test
  public void testUnknownDefinesGroup() {
    DiagnosticGroup group = DiagnosticGroups.UNKNOWN_DEFINES;
    assertEquals("unknownDefines", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(ProcessDefines.UNKNOWN_DEFINE_WARNING, group.getTypes().get(0));
  }

  @Test
  public void testTweaksGroup() {
    DiagnosticGroup group = DiagnosticGroups.TWEAKS;
    assertEquals("tweakValidation", group.getName());
    assertEquals(3, group.getTypes().size());
    assertEquals(ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING, group.getTypes().get(0));
    assertEquals(ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING, group.getTypes().get(1));
    assertEquals(ProcessTweaks.UNKNOWN_TWEAK_WARNING, group.getTypes().get(2));
  }

  @Test
  public void testMissingPropertiesGroup() {
    DiagnosticGroup group = DiagnosticGroups.MISSING_PROPERTIES;
    assertEquals("missingProperties", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(TypeCheck.INEXISTENT_PROPERTY, group.getTypes().get(0));
  }

  @Test
  public void testInternetExplorerChecksGroup() {
    DiagnosticGroup group = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
    assertEquals("internetExplorerChecks", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(RhinoErrorReporter.TRAILING_COMMA, group.getTypes().get(0));
  }

  @Test
  public void testUndefinedVariablesGroup() {
    DiagnosticGroup group = DiagnosticGroups.UNDEFINED_VARIABLES;
    assertEquals("undefinedVars", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(VarCheck.UNDEFINED_VAR_ERROR, group.getTypes().get(0));
  }

  @Test
  public void testCheckRegExpGroup() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_REGEXP;
    assertEquals("checkRegExp", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(CheckRegExp.REGEXP_REFERENCE, group.getTypes().get(0));
  }

  @Test
  public void testCheckTypesGroup() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_TYPES;
    assertEquals("checkTypes", group.getName());
    assertEquals(2, group.getTypes().size());
    assertEquals(TypeValidator.ALL_DIAGNOSTICS, group.getTypes().get(0));
    assertEquals(TypeCheck.ALL_DIAGNOSTICS, group.getTypes().get(1));
  }

  @Test
  public void testCheckVariablesGroup() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_VARIABLES;
    assertEquals("checkVars", group.getName());
    assertEquals(2, group.getTypes().size());
    assertEquals(VarCheck.UNDEFINED_VAR_ERROR, group.getTypes().get(0));
    assertEquals(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, group.getTypes().get(1));
  }

  @Test
  public void testCheckUselessCodeGroup() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_USELESS_CODE;
    assertEquals("uselessCode", group.getName());
    assertEquals(2, group.getTypes().size());
    assertEquals(CheckSideEffects.USELESS_CODE_ERROR, group.getTypes().get(0));
    assertEquals(CheckUnreachableCode.UNREACHABLE_CODE, group.getTypes().get(1));
  }

  @Test
  public void testTypeInvalidationGroup() {
    DiagnosticGroup group = DiagnosticGroups.TYPE_INVALIDATION;
    assertEquals("typeInvalidation", group.getName());
    assertEquals(1, group.getTypes().size());
    assertEquals(DisambiguateProperties.Warnings.INVALIDATION, group.getTypes().get(0));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup"), CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("testGroup")));
  }

}