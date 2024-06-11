import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import com.google.javascript.jscomp.DiagnosticGroups.*;
import com.google.javascript.jscomp.DiagnosticGroup.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.base.Preconditions;

public class DiagnosticGroupsTest {

  private DiagnosticGroups diagnosticGroups;

  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisterGroupWithStringAndDiagnosticGroup() {
    DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", DiagnosticGroup1);
    assertEquals(group, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testRegisterGroupWithStringAndDiagnosticTypeArray() {
    DiagnosticType type1 = new DiagnosticType();
    DiagnosticType type2 = new DiagnosticType();
    DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", type1, type2);
    assertEquals(group, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testRegisterGroupWithStringAndDiagnosticGroupArray() {
    DiagnosticGroup group1 = new DiagnosticGroup();
    DiagnosticGroup group2 = new DiagnosticGroup();
    DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", group1, group2);
    assertEquals(group, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertEquals(groups, ImmutableMap.copyOf(DiagnosticGroups.groupsByName));
  }

  @Test
  public void testForName() {
    DiagnosticGroup group1 = diagnosticGroups.forName("testGroup1");
    assertNull(group1);
    DiagnosticGroup group2 = diagnosticGroups.forName("testGroup2");
    assertNull(group2);
    DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup1", DiagnosticGroup1);
    assertEquals(group, diagnosticGroups.forName("testGroup1"));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = List.of("group1", "group2");
    CheckLevel level = CheckLevel.WARNING;
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    for (String name : diagnosticGroups) {
      DiagnosticGroup group = diagnosticGroups.forName(name);
      CheckLevel actualLevel = options.getWarningLevel(group);
      assertEquals(level, actualLevel);
    }
  }

  @Test
  public void testGlobalThis() {
    assertEquals(DiagnosticGroups.registerGroup("globalThis", CheckGlobalThis.GLOBAL_THIS), DiagnosticGroups.GLOBAL_THIS);
  }

  @Test
  public void testDeprecated() {
    assertEquals(DiagnosticGroups.registerGroup("deprecated", 
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON), DiagnosticGroups.DEPRECATED);
  }

  @Test
  public void testVisibility() {
    assertEquals(DiagnosticGroups.registerGroup("visibility",
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH), DiagnosticGroups.VISIBILITY);
  }

  @Test
  public void testConstantProperty() {
    assertEquals(DiagnosticGroups.registerGroup("constantProperty", CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE), DiagnosticGroups.CONSTANT_PROPERTY);
  }

  @Test
  public void testNonStandardJsDocs() {
    assertEquals(DiagnosticGroups.registerGroup("nonStandardJsDocs", RhinoErrorReporter.BAD_JSDOC_ANNOTATION), DiagnosticGroups.NON_STANDARD_JSDOC);
  }

  @Test
  public void testAccessControls() {
    assertEquals(DiagnosticGroups.registerGroup("accessControls", DiagnosticGroups.DEPRECATED, DiagnosticGroups.VISIBILITY), DiagnosticGroups.ACCESS_CONTROLS);
  }

  @Test
  public void testInvalidCasts() {
    assertEquals(DiagnosticGroups.registerGroup("invalidCasts", TypeValidator.INVALID_CAST), DiagnosticGroups.INVALID_CASTS);
  }

  @Test
  public void testFileoverviewJsdoc() {
    assertEquals(DiagnosticGroups.registerGroup("fileoverviewTags", RhinoErrorReporter.EXTRA_FILEOVERVIEW), DiagnosticGroups.FILEOVERVIEW_JSDOC);
  }

  @Test
  public void testStrictModuleDepCheck() {
    assertEquals(DiagnosticGroups.registerGroup("strictModuleDepCheck", VarCheck.STRICT_MODULE_DEP_ERROR, CheckGlobalNames.STRICT_MODULE_DEP_QNAME), DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
  }

  @Test
  public void testExternsValidation() {
    assertEquals(DiagnosticGroups.registerGroup("externsValidation", VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, VarCheck.UNDEFINED_EXTERN_VAR_ERROR), DiagnosticGroups.EXTERNS_VALIDATION);
  }

  @Test
  public void testAmbiguousFunctionDecl() {
    assertEquals(DiagnosticGroups.registerGroup("ambiguousFunctionDecl", VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL), DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
  }

  @Test
  public void testUnknownDefines() {
    assertEquals(DiagnosticGroups.registerGroup("unknownDefines", ProcessDefines.UNKNOWN_DEFINE_WARNING), DiagnosticGroups.UNKNOWN_DEFINES);
  }

  @Test
  public void testTweaks() {
    assertEquals(DiagnosticGroups.registerGroup("tweakValidation", ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING, ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING, ProcessTweaks.UNKNOWN_TWEAK_WARNING), DiagnosticGroups.TWEAKS);
  }

  @Test
  public void testMissingProperties() {
    assertEquals(DiagnosticGroups.registerGroup("missingProperties", TypeCheck.INEXISTENT_PROPERTY), DiagnosticGroups.MISSING_PROPERTIES);
  }

  @Test
  public void testInternetExplorerChecks() {
    assertEquals(DiagnosticGroups.registerGroup("internetExplorerChecks", RhinoErrorReporter.TRAILING_COMMA), DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
  }

  @Test
  public void testUndefinedVariables() {
    assertEquals(DiagnosticGroups.registerGroup("undefinedVars", VarCheck.UNDEFINED_VAR_ERROR), DiagnosticGroups.UNDEFINED_VARIABLES);
  }

  @Test
  public void testCheckRegExp() {
    assertEquals(DiagnosticGroups.registerGroup("checkRegExp", CheckRegExp.REGEXP_REFERENCE), DiagnosticGroups.CHECK_REGEXP);
  }

  @Test
  public void testCheckTypes() {
    assertEquals(DiagnosticGroups.registerGroup("checkTypes", TypeValidator.ALL_DIAGNOSTICS, TypeCheck.ALL_DIAGNOSTICS), DiagnosticGroups.CHECK_TYPES);
  }

  @Test
  public void testCheckVariables() {
    assertEquals(DiagnosticGroups.registerGroup("checkVars", VarCheck.UNDEFINED_VAR_ERROR, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR), DiagnosticGroups.CHECK_VARIABLES);
  }

  @Test
  public void testCheckUselessCode() {
    assertEquals(DiagnosticGroups.registerGroup("uselessCode", CheckSideEffects.USELESS_CODE_ERROR, CheckUnreachableCode.UNREACHABLE_CODE), DiagnosticGroups.CHECK_USELESS_CODE);
  }

  @Test
  public void testTypeInvalidation() {
    assertEquals(DiagnosticGroups.registerGroup("typeInvalidation", DisambiguateProperties.Warnings.INVALIDATION), DiagnosticGroups.TYPE_INVALIDATION);
  }

}