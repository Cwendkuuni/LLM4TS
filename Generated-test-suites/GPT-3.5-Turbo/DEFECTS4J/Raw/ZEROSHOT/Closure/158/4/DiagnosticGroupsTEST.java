import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = new DiagnosticGroup("name");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("name", group);
    assertEquals(group, result);
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticType[] types = {DiagnosticTypes.TYPE1, DiagnosticTypes.TYPE2};
    DiagnosticGroup group = new DiagnosticGroup("name", types);
    DiagnosticGroup result = DiagnosticGroups.registerGroup("name", types);
    assertEquals(group, result);
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    DiagnosticGroup[] groups = {group1, group2};
    DiagnosticGroup group = new DiagnosticGroup("name", groups);
    DiagnosticGroup result = DiagnosticGroups.registerGroup("name", groups);
    assertEquals(group, result);
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> expected = ImmutableMap.of();
    Map<String, DiagnosticGroup> result = diagnosticGroups.getRegisteredGroups();
    assertEquals(expected, result);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup result = diagnosticGroups.forName("name");
    assertNull(result);
  }

  @Test
  public void testRegisterGlobalThisGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("globalThis",
        CheckGlobalThis.GLOBAL_THIS);
    DiagnosticGroup result = DiagnosticGroups.GLOBAL_THIS;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterDeprecatedGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    DiagnosticGroup result = DiagnosticGroups.DEPRECATED;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterVisibilityGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("visibility",
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH);
    DiagnosticGroup result = DiagnosticGroups.VISIBILITY;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterConstantPropertyGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("constantProperty",
        CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE);
    DiagnosticGroup result = DiagnosticGroups.CONSTANT_PROPERTY;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterNonStandardJsDocsGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("nonStandardJsDocs",
        RhinoErrorReporter.BAD_JSDOC_ANNOTATION);
    DiagnosticGroup result = DiagnosticGroups.NON_STANDARD_JSDOC;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterAccessControlsGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("accessControls",
        DiagnosticGroups.DEPRECATED, DiagnosticGroups.VISIBILITY);
    DiagnosticGroup result = DiagnosticGroups.ACCESS_CONTROLS;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterInvalidCastsGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("invalidCasts",
        TypeValidator.INVALID_CAST);
    DiagnosticGroup result = DiagnosticGroups.INVALID_CASTS;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterFileoverviewJsDocGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("fileoverviewTags",
        RhinoErrorReporter.EXTRA_FILEOVERVIEW);
    DiagnosticGroup result = DiagnosticGroups.FILEOVERVIEW_JSDOC;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterStrictModuleDepCheckGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("strictModuleDepCheck",
        VarCheck.STRICT_MODULE_DEP_ERROR,
        CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
    DiagnosticGroup result = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterExternsValidationGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("externsValidation",
        VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
        VarCheck.UNDEFINED_EXTERN_VAR_ERROR);
    DiagnosticGroup result = DiagnosticGroups.EXTERNS_VALIDATION;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterAmbiguousFunctionDeclGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("ambiguousFunctionDecl",
        VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL);
    DiagnosticGroup result = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterUnknownDefinesGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("unknownDefines",
        ProcessDefines.UNKNOWN_DEFINE_WARNING);
    DiagnosticGroup result = DiagnosticGroups.UNKNOWN_DEFINES;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterTweaksGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("tweakValidation",
        ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
        ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
        ProcessTweaks.UNKNOWN_TWEAK_WARNING);
    DiagnosticGroup result = DiagnosticGroups.TWEAKS;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterMissingPropertiesGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("missingProperties",
        TypeCheck.INEXISTENT_PROPERTY);
    DiagnosticGroup result = DiagnosticGroups.MISSING_PROPERTIES;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterInternetExplorerChecksGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("internetExplorerChecks",
        RhinoErrorReporter.TRAILING_COMMA);
    DiagnosticGroup result = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterUndefinedVariablesGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("undefinedVars",
        VarCheck.UNDEFINED_VAR_ERROR);
    DiagnosticGroup result = DiagnosticGroups.UNDEFINED_VARIABLES;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterCheckRegExpGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("checkRegExp",
        CheckRegExp.REGEXP_REFERENCE);
    DiagnosticGroup result = DiagnosticGroups.CHECK_REGEXP;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterCheckTypesGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("checkTypes",
        TypeValidator.ALL_DIAGNOSTICS,
        TypeCheck.ALL_DIAGNOSTICS);
    DiagnosticGroup result = DiagnosticGroups.CHECK_TYPES;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterCheckVariablesGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("checkVars",
        VarCheck.UNDEFINED_VAR_ERROR,
        SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR);
    DiagnosticGroup result = DiagnosticGroups.CHECK_VARIABLES;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterCheckUselessCodeGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("uselessCode",
        CheckSideEffects.USELESS_CODE_ERROR,
        CheckUnreachableCode.UNREACHABLE_CODE);
    DiagnosticGroup result = DiagnosticGroups.CHECK_USELESS_CODE;
    assertEquals(expected, result);
  }

  @Test
  public void testRegisterTypeInvalidationGroup() {
    DiagnosticGroup expected = new DiagnosticGroup("typeInvalidation",
        DisambiguateProperties.Warnings.INVALIDATION);
    DiagnosticGroup result = DiagnosticGroups.TYPE_INVALIDATION;
    assertEquals(expected, result);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = new ArrayList<>();
    diagnosticGroups.add("group1");
    diagnosticGroups.add("group2");
    CheckLevel level = CheckLevel.WARNING;
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);

    DiagnosticGroup group1 = diagnosticGroups.forName("group1");
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
    
    DiagnosticGroup group2 = diagnosticGroups.forName("group2");
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group2));
  }

}