import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class DiagnosticGroupsTest {

  private DiagnosticGroups diagnosticGroups;

  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void shouldRegisterGroupAndReturnRegisteredGroups() {
    DiagnosticGroup group = new DiagnosticGroup("group");
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("registered", group);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    
    assertTrue(registeredGroups.containsKey("registered"));
    assertEquals(group, registeredGroups.get("registered"));
    assertEquals(group, registeredGroup);
  }

  @Test
  public void shouldRegisterGroupWithNameAndTypesAndReturnRegisteredGroup() {
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticType type2 = new DiagnosticType("type2");
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("registered", type1, type2);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    
    assertTrue(registeredGroups.containsKey("registered"));
    assertEquals(2, registeredGroups.get("registered").getTypes().size());
    assertTrue(registeredGroups.get("registered").getTypes().contains(type1));
    assertTrue(registeredGroups.get("registered").getTypes().contains(type2));
    assertEquals(type1, registeredGroups.get("registered").getTypes().get(0));
    assertEquals(type2, registeredGroups.get("registered").getTypes().get(1));
    assertEquals(registeredGroup, registeredGroups.get("registered"));
  }

  @Test
  public void shouldRegisterGroupWithNameAndGroupsAndReturnRegisteredGroup() {
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("registered", group1, group2);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    
    assertTrue(registeredGroups.containsKey("registered"));
    assertEquals(2, registeredGroups.get("registered").getGroups().size());
    assertTrue(registeredGroups.get("registered").getGroups().contains(group1));
    assertTrue(registeredGroups.get("registered").getGroups().contains(group2));
    assertEquals(group1, registeredGroups.get("registered").getGroups().get(0));
    assertEquals(group2, registeredGroups.get("registered").getGroups().get(1));
    assertEquals(registeredGroup, registeredGroups.get("registered"));
  }

  @Test
  public void shouldReturnNullWhenFindingNonexistentGroup() {
    assertNull(diagnosticGroups.forName("nonexistent"));
  }

  @Test
  public void shouldReturnNamedRegisteredGroup() {
    DiagnosticGroup group = new DiagnosticGroup("group");
    diagnosticGroups.registerGroup("registered", group);
    
    assertEquals(group, diagnosticGroups.forName("registered"));
  }

  @Test
  public void shouldReturnNullWhenWarningClassNotFound() {
    DiagnosisGroup group = diagnosticGroups.forName("nonexistent");
    assertNull(group);
  }

  @Test
  public void shouldReturnGlobalThisRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("globalThis",
          CheckGlobalThis.GLOBAL_THIS);
          
    assertEquals(expectedGroup, DiagnosticGroups.GLOBAL_THIS);
  }

  @Test
  public void shouldReturnDeprecatedRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("deprecated",
          CheckAccessControls.DEPRECATED_NAME,
          CheckAccessControls.DEPRECATED_NAME_REASON,
          CheckAccessControls.DEPRECATED_PROP,
          CheckAccessControls.DEPRECATED_PROP_REASON,
          CheckAccessControls.DEPRECATED_CLASS,
          CheckAccessControls.DEPRECATED_CLASS_REASON);
          
    assertEquals(expectedGroup, DiagnosticGroups.DEPRECATED);
  }

  @Test
  public void shouldReturnVisibilityRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("visibility",
          CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
          CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
          CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
          CheckAccessControls.PRIVATE_OVERRIDE,
          CheckAccessControls.VISIBILITY_MISMATCH);
          
    assertEquals(expectedGroup, DiagnosticGroups.VISIBILITY);
  }

  @Test
  public void shouldReturnConstantPropertyRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("constantProperty",
          CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE);
          
    assertEquals(expectedGroup, DiagnosticGroups.CONSTANT_PROPERTY);
  }

  @Test
  public void shouldReturnNonStandardJsDocsRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("nonStandardJsDocs",
          RhinoErrorReporter.BAD_JSDOC_ANNOTATION);
          
    assertEquals(expectedGroup, DiagnosticGroups.NON_STANDARD_JSDOC);
  }

  @Test
  public void shouldReturnAccessControlsRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("accessControls",
          DiagnosticGroups.DEPRECATED, DiagnosticGroups.VISIBILITY);
          
    assertEquals(expectedGroup, DiagnosticGroups.ACCESS_CONTROLS);
  }

  @Test
  public void shouldReturnInvalidCastsRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("invalidCasts",
          TypeValidator.INVALID_CAST);
          
    assertEquals(expectedGroup, DiagnosticGroups.INVALID_CASTS);
  }

  @Test
  public void shouldReturnFileoverviewJsdocRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("fileoverviewTags",
          RhinoErrorReporter.EXTRA_FILEOVERVIEW);
          
    assertEquals(expectedGroup, DiagnosticGroups.FILEOVERVIEW_JSDOC);
  }

  @Test
  public void shouldReturnStrictModuleDepCheckRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("strictModuleDepCheck",
          VarCheck.STRICT_MODULE_DEP_ERROR,
          CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
          
    assertEquals(expectedGroup, DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
  }

  @Test
  public void shouldReturnExternsValidationRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("externsValidation",
          VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
          VarCheck.UNDEFINED_EXTERN_VAR_ERROR);
          
    assertEquals(expectedGroup, DiagnosticGroups.EXTERNS_VALIDATION);
  }

  @Test
  public void shouldReturnAmbiguousFunctionDeclRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("ambiguousFunctionDecl",
          VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL);
          
    assertEquals(expectedGroup, DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
  }

  @Test
  public void shouldReturnUnknownDefinesRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("unknownDefines",
          ProcessDefines.UNKNOWN_DEFINE_WARNING);
          
    assertEquals(expectedGroup, DiagnosticGroups.UNKNOWN_DEFINES);
  }

  @Test
  public void shouldReturnTweaksRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("tweakValidation",
          ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
          ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
          ProcessTweaks.UNKNOWN_TWEAK_WARNING);
          
    assertEquals(expectedGroup, DiagnosticGroups.TWEAKS);
  }

  @Test
  public void shouldReturnMissingPropertiesRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("missingProperties",
          TypeCheck.INEXISTENT_PROPERTY);
          
    assertEquals(expectedGroup, DiagnosticGroups.MISSING_PROPERTIES);
  }

  @Test
  public void shouldReturnInternetExplorerChecksRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("internetExplorerChecks",
          RhinoErrorReporter.TRAILING_COMMA);
          
    assertEquals(expectedGroup, DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
  }

  @Test
  public void shouldReturnUndefinedVariablesRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("undefinedVars",
          VarCheck.UNDEFINED_VAR_ERROR);
          
    assertEquals(expectedGroup, DiagnosticGroups.UNDEFINED_VARIABLES);
  }

  @Test
  public void shouldReturnCheckRegExpRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("checkRegExp",
          CheckRegExp.REGEXP_REFERENCE);
          
    assertEquals(expectedGroup, DiagnosticGroups.CHECK_REGEXP);
  }

  @Test
  public void shouldReturnCheckTypesRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("checkTypes",
          TypeValidator.ALL_DIAGNOSTICS,
          TypeCheck.ALL_DIAGNOSTICS);
          
    assertEquals(expectedGroup, DiagnosticGroups.CHECK_TYPES);
  }

  @Test
  public void shouldReturnCheckVariablesRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("checkVars",
          VarCheck.UNDEFINED_VAR_ERROR,
          SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR);
          
    assertEquals(expectedGroup, DiagnosticGroups.CHECK_VARIABLES);
  }

  @Test
  public void shouldReturnCheckUselessCodeRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("uselessCode",
          CheckSideEffects.USELESS_CODE_ERROR,
          CheckUnreachableCode.UNREACHABLE_CODE);
          
    assertEquals(expectedGroup, DiagnosticGroups.CHECK_USELESS_CODE);
  }

  @Test
  public void shouldReturnTypeInvalidationRegisteredGroup() {
    DiagnosticGroup expectedGroup = DiagnosticGroups.registerGroup("typeInvalidation",
          DisambiguateProperties.Warnings.INVALIDATION);
          
    assertEquals(expectedGroup, DiagnosticGroups.TYPE_INVALIDATION);
  }

  @Test
  public void shouldSetWarningLevelsForDiagnosticGroups() {
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    List<String> diagnosticGroups = Arrays.asList("deprecated", "visibility");
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);

    assertEquals(level, options.getWarningLevel(DiagnosticGroups.DEPRECATED));
    assertEquals(level, options.getWarningLevel(DiagnosticGroups.VISIBILITY));
  }
}