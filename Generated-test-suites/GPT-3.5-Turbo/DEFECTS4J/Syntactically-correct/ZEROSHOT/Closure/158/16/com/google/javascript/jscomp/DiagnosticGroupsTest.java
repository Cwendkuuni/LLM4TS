import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup1() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testName", group);
    assertEquals(group, result);
    // Test if group is successfully added to groupsByName map.
    assertTrue(DiagnosticGroups.groupsByName.containsKey("testName"));
    assertEquals(group, DiagnosticGroups.groupsByName.get("testName"));
  }

  @Test
  public void testRegisterGroup2() {
    DiagnosticType type1 = new DiagnosticType("testType1");
    DiagnosticType type2 = new DiagnosticType("testType2");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testName", type1, type2);

    assertTrue(DiagnosticGroups.groupsByName.containsKey("testName"));
    assertEquals(result, DiagnosticGroups.groupsByName.get("testName"));

    List<DiagnosticType> types = result.getTypes();
    assertTrue(types.contains(type1));
    assertTrue(types.contains(type2));
  }

  @Test
  public void testRegisterGroup3() {
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testName", group1, group2);

    assertTrue(DiagnosticGroups.groupsByName.containsKey("testName"));
    assertEquals(result, DiagnosticGroups.groupsByName.get("testName"));

    List<DiagnosticGroup> groups = result.getGroups();
    assertTrue(groups.contains(group1));
    assertTrue(groups.contains(group2));
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    // Check if the size of the map is correct.
    assertEquals(DiagnosticGroups.groupsByName.size(), registeredGroups.size());
    // Check if the entries in the map are the same.
    for (String name : DiagnosticGroups.groupsByName.keySet()) {
      assertTrue(registeredGroups.containsKey(name));
      assertEquals(DiagnosticGroups.groupsByName.get(name), registeredGroups.get(name));
    }
  }

  @Test
  public void testForName() {
    String name = "testName";
    DiagnosticGroup group = new DiagnosticGroup(name);
    DiagnosticGroups.groupsByName.put(name, group);

    DiagnosticGroup result = DiagnosticGroups.forName(name);
    assertEquals(group, result);
  }

  @Test
  public void testGLOBAL_THIS() {
    DiagnosticGroup group = DiagnosticGroups.GLOBAL_THIS;
    assertEquals("globalThis", group.getName());
    assertTrue(group.getTypes().contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testDEPRECATED() {
    DiagnosticGroup group = DiagnosticGroups.DEPRECATED;
    assertEquals("deprecated", group.getName());
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_NAME));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_NAME_REASON));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_PROP));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_PROP_REASON));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_CLASS));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_CLASS_REASON));
  }

  @Test
  public void testVISIBILITY() {
    DiagnosticGroup group = DiagnosticGroups.VISIBILITY;
    assertEquals("visibility", group.getName());
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.PRIVATE_OVERRIDE));
    assertTrue(group.getTypes().contains(CheckAccessControls.VISIBILITY_MISMATCH));
  }

  @Test
  public void testCONSTANT_PROPERTY() {
    DiagnosticGroup group = DiagnosticGroups.CONSTANT_PROPERTY;
    assertEquals("constantProperty", group.getName());
    assertTrue(group.getTypes().contains(CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE));
  }

  @Test
  public void testNON_STANDARD_JSDOC() {
    DiagnosticGroup group = DiagnosticGroups.NON_STANDARD_JSDOC;
    assertEquals("nonStandardJsDocs", group.getName());
    assertTrue(group.getTypes().contains(RhinoErrorReporter.BAD_JSDOC_ANNOTATION));
  }

  @Test
  public void testACCESS_CONTROLS() {
    DiagnosticGroup group = DiagnosticGroups.ACCESS_CONTROLS;
    assertEquals("accessControls", group.getName());
    assertTrue(group.getGroups().contains(DiagnosticGroups.DEPRECATED));
    assertTrue(group.getGroups().contains(DiagnosticGroups.VISIBILITY));
  }

  @Test
  public void testINVALID_CASTS() {
    DiagnosticGroup group = DiagnosticGroups.INVALID_CASTS;
    assertEquals("invalidCasts", group.getName());
    assertTrue(group.getTypes().contains(TypeValidator.INVALID_CAST));
  }

  @Test
  public void testFILEOVERVIEW_JSDOC() {
    DiagnosticGroup group = DiagnosticGroups.FILEOVERVIEW_JSDOC;
    assertEquals("fileoverviewTags", group.getName());
    assertTrue(group.getTypes().contains(RhinoErrorReporter.EXTRA_FILEOVERVIEW));
  }

  @Test
  public void testSTRICT_MODULE_DEP_CHECK() {
    DiagnosticGroup group = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
    assertEquals("strictModuleDepCheck", group.getName());
    assertTrue(group.getTypes().contains(VarCheck.STRICT_MODULE_DEP_ERROR));
    assertTrue(group.getTypes().contains(CheckGlobalNames.STRICT_MODULE_DEP_QNAME));
  }

  @Test
  public void testEXTERNS_VALIDATION() {
    DiagnosticGroup group = DiagnosticGroups.EXTERNS_VALIDATION;
    assertEquals("externsValidation", group.getName());
    assertTrue(group.getTypes().contains(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR));
    assertTrue(group.getTypes().contains(VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
  }

  @Test
  public void testAMBIGUOUS_FUNCTION_DECL() {
    DiagnosticGroup group = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
    assertEquals("ambiguousFunctionDecl", group.getName());
    assertTrue(group.getTypes().contains(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL));
  }

  @Test
  public void testUNKNOWN_DEFINES() {
    DiagnosticGroup group = DiagnosticGroups.UNKNOWN_DEFINES;
    assertEquals("unknownDefines", group.getName());
    assertTrue(group.getTypes().contains(ProcessDefines.UNKNOWN_DEFINE_WARNING));
  }

  @Test
  public void testTWEAKS() {
    DiagnosticGroup group = DiagnosticGroups.TWEAKS;
    assertEquals("tweakValidation", group.getName());
    assertTrue(group.getTypes().contains(ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING));
    assertTrue(group.getTypes().contains(ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING));
    assertTrue(group.getTypes().contains(ProcessTweaks.UNKNOWN_TWEAK_WARNING));
  }

  @Test
  public void testMISSING_PROPERTIES() {
    DiagnosticGroup group = DiagnosticGroups.MISSING_PROPERTIES;
    assertEquals("missingProperties", group.getName());
    assertTrue(group.getTypes().contains(TypeCheck.INEXISTENT_PROPERTY));
  }

  @Test
  public void testINTERNET_EXPLORER_CHECKS() {
    DiagnosticGroup group = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
    assertEquals("internetExplorerChecks", group.getName());
    assertTrue(group.getTypes().contains(RhinoErrorReporter.TRAILING_COMMA));
  }

  @Test
  public void testUNDEFINED_VARIABLES() {
    DiagnosticGroup group = DiagnosticGroups.UNDEFINED_VARIABLES;
    assertEquals("undefinedVars", group.getName());
    assertTrue(group.getTypes().contains(VarCheck.UNDEFINED_VAR_ERROR));
  }

  @Test
  public void testCHECK_REGEXP() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_REGEXP;
    assertEquals("checkRegExp", group.getName());
    assertTrue(group.getTypes().contains(CheckRegExp.REGEXP_REFERENCE));
  }

  @Test
  public void testCHECK_TYPES() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_TYPES;
    assertEquals("checkTypes", group.getName());
    for (DiagnosticType type : TypeValidator.ALL_DIAGNOSTICS) {
      assertTrue(group.getTypes().contains(type));
    }
    for (DiagnosticType type : TypeCheck.ALL_DIAGNOSTICS) {
      assertTrue(group.getTypes().contains(type));
    }
  }

  @Test
  public void testCHECK_VARIABLES() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_VARIABLES;
    assertEquals("checkVars", group.getName());
    assertTrue(group.getTypes().contains(VarCheck.UNDEFINED_VAR_ERROR));
    assertTrue(group.getTypes().contains(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR));
  }

  @Test
  public void testCHECK_USELESS_CODE() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_USELESS_CODE;
    assertEquals("uselessCode", group.getName());
    assertTrue(group.getTypes().contains(CheckSideEffects.USELESS_CODE_ERROR));
    assertTrue(group.getTypes().contains(CheckUnreachableCode.UNREACHABLE_CODE));
  }

  @Test
  public void testTYPE_INVALIDATION() {
    DiagnosticGroup group = DiagnosticGroups.TYPE_INVALIDATION;
    assertEquals("typeInvalidation", group.getName());
    assertTrue(group.getTypes().contains(DisambiguateProperties.Warnings.INVALIDATION));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = List.of("testGroup1", "testGroup2");
    CheckLevel level = CheckLevel.WARNING;

    DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
    DiagnosticGroups.groupsByName.put("testGroup1", group1);
    DiagnosticGroups.groupsByName.put("testGroup2", group2);

    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);

    assertEquals(level, options.getWarningLevel(group1));
    assertEquals(level, options.getWarningLevel(group2));
  }

}