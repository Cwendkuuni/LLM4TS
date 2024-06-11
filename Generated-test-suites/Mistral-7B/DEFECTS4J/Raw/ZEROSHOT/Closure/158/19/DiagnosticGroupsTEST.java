import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  private static Map<String, DiagnosticGroup> registeredGroups;

  @BeforeClass
  public static void setup() {
    DiagnosticGroups.registerGroup("globalThis", DiagnosticGroups.GLOBAL_THIS);
    DiagnosticGroups.registerGroup("deprecated", DEPRECATED_GROUP);
    DiagnosticGroups.registerGroup("visibility", VISIBILITY_GROUP);
    DiagnosticGroups.registerGroup("constantProperty", CONSTANT_PROPERTY_GROUP);
    DiagnosticGroups.registerGroup("nonStandardJsDocs", NON_STANDARD_JSDOC_GROUP);
    DiagnosticGroups.registerGroup("accessControls", ACCESS_CONTROLS_GROUP);
    DiagnosticGroups.registerGroup("invalidCasts", INVALID_CASTS_GROUP);
    DiagnosticGroups.registerGroup("fileoverviewTags", FILEOVERVIEW_JSDOC_GROUP);
    DiagnosticGroups.registerGroup("strictModuleDepCheck", STRICT_MODULE_DEP_CHECK_GROUP);
    DiagnosticGroups.registerGroup("externsValidation", EXTERNS_VALIDATION_GROUP);
    DiagnosticGroups.registerGroup("ambiguousFunctionDecl", AMBIGUOUS_FUNCTION_DECL_GROUP);
    DiagnosticGroups.registerGroup("unknownDefines", UNKNOWN_DEFINES_GROUP);
    DiagnosticGroups.registerGroup("tweakValidation", TWEAKS_GROUP);
    DiagnosticGroups.registerGroup("missingProperties", MISSING_PROPERTIES_GROUP);
    DiagnosticGroups.registerGroup("internetExplorerChecks", INTERNET_EXPLORER_CHECKS_GROUP);
    DiagnosticGroups.registerGroup("undefinedVars", UNDEFINED_VARIABLES_GROUP);
    DiagnosticGroups.registerGroup("checkRegExp", CHECK_REGEXP_GROUP);
    DiagnosticGroups.registerGroup("checkTypes", CHECK_TYPES_GROUP);
    DiagnosticGroups.registerGroup("checkVars", CHECK_VARIABLES_GROUP);
    DiagnosticGroups.registerGroup("uselessCode", CHECK_USELESS_CODE_GROUP);
    DiagnosticGroups.registerGroup("typeInvalidation", TYPE_INVALIDATION_GROUP);

    registeredGroups = DiagnosticGroups.getRegisteredGroups();
  }

  @Test
  public void testRegisteredDiagnosticGroups() {
    assertNotNull(registeredGroups);
  }

  @Test
  public void testGetDiagnosticGroupByName() {
    DiagnosticGroup deprecatedGroup = registeredGroups.get("deprecated");
    assertNotNull(deprecatedGroup);

    DiagnosticGroup unknownGroup = registeredGroups.get("unknown");
    assertNull(unknownGroup);
  }

  private static DiagnosticGroup DEPRECATED_GROUP = new DiagnosticGroup("deprecated",
      DiagnosticType.DEPRECATED_NAME, DiagnosticType.DEPRECATED_NAME_REASON,
      DiagnosticType.DEPRECATED_PROP, DiagnosticType.DEPRECATED_PROP_REASON,
      DiagnosticType.DEPRECATED_CLASS, DiagnosticType.DEPRECATED_CLASS_REASON);

  private static DiagnosticGroup VISIBILITY_GROUP = new DiagnosticGroup("visibility",
      DiagnosticType.BAD_PRIVATE_GLOBAL_ACCESS,
      DiagnosticType.BAD_PRIVATE_PROPERTY_ACCESS,
      DiagnosticType.BAD_PROTECTED_PROPERTY_ACCESS, DiagnosticType.PRIVATE_OVERRIDE,
      DiagnosticType.VISIBILITY_MISMATCH);

  private static DiagnosticGroup CONSTANT_PROPERTY_GROUP = new DiagnosticGroup("constantProperty",
      DiagnosticType.CONST_PROPERTY_REASSIGNED_VALUE);

  private static DiagnosticGroup NON_STANDARD_JSDOC_GROUP = new DiagnosticGroup("nonStandardJsDocs",
      DiagnosticType.BAD_JSDOC_ANNOTATION);

  private static DiagnosticGroup ACCESS_CONTROLS_GROUP = new DiagnosticGroup("accessControls",
      DEPRECATED_GROUP, VISIBILITY_GROUP);

  private static DiagnosticGroup INVALID_CASTS_GROUP = new DiagnosticGroup("invalidCasts",
      DiagnosticType.INVALID_CAST);

  private static DiagnosticGroup FILEOVERVIEW_JSDOC_GROUP = new DiagnosticGroup("fileoverviewTags",
      DiagnosticType.EXTRA_FILEOVERVIEW);

  private static DiagnosticGroup STRICT_MODULE_DEP_CHECK_GROUP = new DiagnosticGroup("strictModuleDepCheck",
          VarCheck.STRICT_MODULE_DEP_ERROR, CheckGlobalNames.STRICT_MODULE_DEP_QNAME);

  private static DiagnosticGroup EXTERNS_VALIDATION_GROUP = new DiagnosticGroup("externsValidation",
      VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, VarCheck.UNDEFINED_EXTERN_VAR_ERROR);

  private static DiagnosticGroup AMBIGUOUS_FUNCTION_DECL_GROUP = new DiagnosticGroup("ambiguousFunctionDecl",
      VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL);

  private static DiagnosticGroup UNKNOWN_DEFINES_GROUP = new DiagnosticGroup("unknownDefines",
      ProcessDefines.UNKNOWN_DEFINE_WARNING);

  private static DiagnosticGroup TWEAKS_GROUP = new DiagnosticGroup("tweakValidation",
          ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
          ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
          ProcessTweaks.UNKNOWN_TWEAK_WARNING);

  private static DiagnosticGroup MISSING_PROPERTIES_GROUP = new DiagnosticGroup("missingProperties",
          TypeCheck.INEXISTENT_PROPERTY);

  private static DiagnosticGroup INTERNET_EXPLORER_CHECKS_GROUP = new DiagnosticGroup("internetExplorerChecks",
          RhinoErrorReporter.TRAILING_COMMA);

  private static DiagnosticGroup UNDEFINED_VARIABLES_GROUP = new DiagnosticGroup("undefinedVars",
          VarCheck.UNDEFINED_VAR_ERROR);

  private static DiagnosticGroup CHECK_REGEXP_GROUP = new DiagnosticGroup("checkRegExp",
          CheckRegExp.REGEXP_REFERENCE);

  private static DiagnosticGroup CHECK_TYPES_GROUP = new DiagnosticGroup("checkTypes",
          TypeValidator.ALL_DIAGNOSTICS, TypeCheck.ALL_DIAGNOSTICS);

  private static DiagnosticGroup CHECK_VARIABLES_GROUP = new DiagnosticGroup("checkVars",
          VarCheck.UNDEFINED_VAR_ERROR, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR);

  private static DiagnosticGroup CHECK_USELESS_CODE_GROUP = new DiagnosticGroup("uselessCode",
          CheckSideEffects.USELESS_CODE_ERROR, CheckUnreachableCode.UNREACHABLE_CODE);

  private static DiagnosticGroup TYPE_INVALIDATION_GROUP = new DiagnosticGroup("typeInvalidation",
          DisambiguateProperties.Warnings.INVALIDATION);
}