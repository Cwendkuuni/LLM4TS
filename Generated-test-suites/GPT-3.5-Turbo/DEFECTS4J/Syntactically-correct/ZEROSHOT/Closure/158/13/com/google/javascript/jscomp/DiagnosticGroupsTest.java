import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupWithNameAndGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup1");
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testName1", group);

    assertEquals(group, registeredGroup);
    assertEquals(group, DiagnosticGroups.forName("testName1"));
  }

  @Test
  public void testRegisterGroupWithNameAndTypes() {
    DiagnosticType type1 = new DiagnosticType("testType1");
    DiagnosticType type2 = new DiagnosticType("testType2");
    DiagnosticGroup group = new DiagnosticGroup("testGroup2", type1, type2);
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testName2", type1, type2);

    assertEquals(group, registeredGroup);
    assertEquals(group, DiagnosticGroups.forName("testName2"));
  }

  @Test
  public void testRegisterGroupWithNameAndGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup3");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup4");
    DiagnosticGroup group = new DiagnosticGroup("testGroup5", group1, group2);
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testName3", group1, group2);

    assertEquals(group, registeredGroup);
    assertEquals(group, DiagnosticGroups.forName("testName3"));
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup6");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup7");

    DiagnosticGroups.registerGroup("testName4", group1);
    DiagnosticGroups.registerGroup("testName5", group2);

    assertEquals(group1, DiagnosticGroups.getRegisteredGroups().get("testName4"));
    assertEquals(group2, DiagnosticGroups.getRegisteredGroups().get("testName5"));
  }

  @Test
  public void testForName() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup8");
    DiagnosticGroups.registerGroup("testName6", group);

    assertEquals(group, DiagnosticGroups.forName("testName6"));
  }

  @Test
  public void testGLOBAL_THIS() {
    DiagnosticGroup group = DiagnosticGroups.GLOBAL_THIS;

    assertNotNull(group);
    assertEquals("globalThis", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testDEPRECATED() {
    DiagnosticGroup group = DiagnosticGroups.DEPRECATED;

    assertNotNull(group);
    assertEquals("deprecated", group.getName());
    assertEquals(6, group.getTypes().size());
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

    assertNotNull(group);
    assertEquals("visibility", group.getName());
    assertEquals(5, group.getTypes().size());
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.PRIVATE_OVERRIDE));
    assertTrue(group.getTypes().contains(CheckAccessControls.VISIBILITY_MISMATCH));
  }

  @Test
  public void testCONSTANT_PROPERTY() {
    DiagnosticGroup group = DiagnosticGroups.CONSTANT_PROPERTY;

    assertNotNull(group);
    assertEquals("constantProperty", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE));
  }

  @Test
  public void testNON_STANDARD_JSDOC() {
    DiagnosticGroup group = DiagnosticGroups.NON_STANDARD_JSDOC;

    assertNotNull(group);
    assertEquals("nonStandardJsDocs", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(RhinoErrorReporter.BAD_JSDOC_ANNOTATION));
  }

  @Test
  public void testACCESS_CONTROLS() {
    DiagnosticGroup group = DiagnosticGroups.ACCESS_CONTROLS;

    assertNotNull(group);
    assertEquals("accessControls", group.getName());
    assertEquals(11, group.getTypes().size());
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_NAME));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_NAME_REASON));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_PROP));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_PROP_REASON));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_CLASS));
    assertTrue(group.getTypes().contains(CheckAccessControls.DEPRECATED_CLASS_REASON));
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS));
    assertTrue(group.getTypes().contains(CheckAccessControls.PRIVATE_OVERRIDE));
    assertTrue(group.getTypes().contains(CheckAccessControls.VISIBILITY_MISMATCH));
  }

  @Test
  public void testINVALID_CASTS() {
    DiagnosticGroup group = DiagnosticGroups.INVALID_CASTS;

    assertNotNull(group);
    assertEquals("invalidCasts", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(TypeValidator.INVALID_CAST));
  }

  @Test
  public void testFILEOVERVIEW_JSDOC() {
    DiagnosticGroup group = DiagnosticGroups.FILEOVERVIEW_JSDOC;

    assertNotNull(group);
    assertEquals("fileoverviewTags", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(RhinoErrorReporter.EXTRA_FILEOVERVIEW));
  }

  @Test
  public void testSTRICT_MODULE_DEP_CHECK() {
    DiagnosticGroup group = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;

    assertNotNull(group);
    assertEquals("strictModuleDepCheck", group.getName());
    assertEquals(2, group.getTypes().size());
    assertTrue(group.getTypes().contains(VarCheck.STRICT_MODULE_DEP_ERROR));
    assertTrue(group.getTypes().contains(CheckGlobalNames.STRICT_MODULE_DEP_QNAME));
  }

  @Test
  public void testEXTERNS_VALIDATION() {
    DiagnosticGroup group = DiagnosticGroups.EXTERNS_VALIDATION;

    assertNotNull(group);
    assertEquals("externsValidation", group.getName());
    assertEquals(2, group.getTypes().size());
    assertTrue(group.getTypes().contains(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR));
    assertTrue(group.getTypes().contains(VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
  }

  @Test
  public void testAMBIGUOUS_FUNCTION_DECL() {
    DiagnosticGroup group = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;

    assertNotNull(group);
    assertEquals("ambiguousFunctionDecl", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL));
  }

  @Test
  public void testUNKNOWN_DEFINES() {
    DiagnosticGroup group = DiagnosticGroups.UNKNOWN_DEFINES;

    assertNotNull(group);
    assertEquals("unknownDefines", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(ProcessDefines.UNKNOWN_DEFINE_WARNING));
  }

  @Test
  public void testTWEAKS() {
    DiagnosticGroup group = DiagnosticGroups.TWEAKS;

    assertNotNull(group);
    assertEquals("tweakValidation", group.getName());
    assertEquals(3, group.getTypes().size());
    assertTrue(group.getTypes().contains(ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING));
    assertTrue(group.getTypes().contains(ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING));
    assertTrue(group.getTypes().contains(ProcessTweaks.UNKNOWN_TWEAK_WARNING));
  }

  @Test
  public void testMISSING_PROPERTIES() {
    DiagnosticGroup group = DiagnosticGroups.MISSING_PROPERTIES;

    assertNotNull(group);
    assertEquals("missingProperties", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(TypeCheck.INEXISTENT_PROPERTY));
  }

  @Test
  public void testINTERNET_EXPLORER_CHECKS() {
    DiagnosticGroup group = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;

    assertNotNull(group);
    assertEquals("internetExplorerChecks", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(RhinoErrorReporter.TRAILING_COMMA));
  }

  @Test
  public void testUNDEFINED_VARIABLES() {
    DiagnosticGroup group = DiagnosticGroups.UNDEFINED_VARIABLES;

    assertNotNull(group);
    assertEquals("undefinedVars", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(VarCheck.UNDEFINED_VAR_ERROR));
  }

  @Test
  public void testCHECK_REGEXP() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_REGEXP;

    assertNotNull(group);
    assertEquals("checkRegExp", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(CheckRegExp.REGEXP_REFERENCE));
  }

  @Test
  public void testCHECK_TYPES() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_TYPES;

    assertNotNull(group);
    assertEquals("checkTypes", group.getName());
    assertEquals(2, group.getTypes().size());
    assertTrue(group.getTypes().contains(TypeValidator.ALL_DIAGNOSTICS));
    assertTrue(group.getTypes().contains(TypeCheck.ALL_DIAGNOSTICS));
  }

  @Test
  public void testCHECK_VARIABLES() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_VARIABLES;

    assertNotNull(group);
    assertEquals("checkVars", group.getName());
    assertEquals(2, group.getTypes().size());
    assertTrue(group.getTypes().contains(VarCheck.UNDEFINED_VAR_ERROR));
    assertTrue(group.getTypes().contains(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR));
  }

  @Test
  public void testCHECK_USELESS_CODE() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_USELESS_CODE;

    assertNotNull(group);
    assertEquals("uselessCode", group.getName());
    assertEquals(2, group.getTypes().size());
    assertTrue(group.getTypes().contains(CheckSideEffects.USELESS_CODE_ERROR));
    assertTrue(group.getTypes().contains(CheckUnreachableCode.UNREACHABLE_CODE));
  }

  @Test
  public void testTYPE_INVALIDATION() {
    DiagnosticGroup group = DiagnosticGroups.TYPE_INVALIDATION;

    assertNotNull(group);
    assertEquals("typeInvalidation", group.getName());
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(DisambiguateProperties.Warnings.INVALIDATION));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    diagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup9", "testGroup10"), CheckLevel.WARNING);

    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("testGroup9")));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("testGroup10")));
  }
}