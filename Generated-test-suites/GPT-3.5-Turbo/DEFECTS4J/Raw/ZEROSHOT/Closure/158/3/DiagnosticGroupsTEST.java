import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {
  
  private DiagnosticGroups diagnosticGroups;
  private Map<String, DiagnosticGroup> registeredGroups;

  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
    registeredGroups = diagnosticGroups.getRegisteredGroups();
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", group);
    Assert.assertEquals(group, registeredGroup);
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticType type1 = new DiagnosticType("testType1");
    DiagnosticType type2 = new DiagnosticType("testType2");
    DiagnosticGroup group = new DiagnosticGroup("testGroup", type1, type2);
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", type1, type2);
    Assert.assertEquals(group, registeredGroup);
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
    DiagnosticGroup group = new DiagnosticGroup("testGroup", group1, group2);
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", group1, group2);
    Assert.assertEquals(group, registeredGroup);
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    Assert.assertEquals(groupsByName, groups);
  }

  @Test
  public void testForName() {
    DiagnosticGroup group = diagnosticGroups.forName("globalThis");
    Assert.assertEquals(DiagnosticGroups.GLOBAL_THIS, group);
  }

  @Test
  public void testGLOBAL_THIS() {
    Assert.assertEquals("globalThis", DiagnosticGroups.GLOBAL_THIS.getName());
    List<DiagnosticType> types = DiagnosticGroups.GLOBAL_THIS.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(CheckGlobalThis.GLOBAL_THIS, types.get(0));
  }

  @Test
  public void testDEPRECATED() {
    Assert.assertEquals("deprecated", DiagnosticGroups.DEPRECATED.getName());
    List<DiagnosticType> types = DiagnosticGroups.DEPRECATED.getTypes();
    Assert.assertEquals(6, types.size());
    Assert.assertEquals(CheckAccessControls.DEPRECATED_NAME, types.get(0));
    Assert.assertEquals(CheckAccessControls.DEPRECATED_NAME_REASON, types.get(1));
    Assert.assertEquals(CheckAccessControls.DEPRECATED_PROP, types.get(2));
    Assert.assertEquals(CheckAccessControls.DEPRECATED_PROP_REASON, types.get(3));
    Assert.assertEquals(CheckAccessControls.DEPRECATED_CLASS, types.get(4));
    Assert.assertEquals(CheckAccessControls.DEPRECATED_CLASS_REASON, types.get(5));
  }

  @Test
  public void testVISIBILITY() {
    Assert.assertEquals("visibility", DiagnosticGroups.VISIBILITY.getName());
    List<DiagnosticType> types = DiagnosticGroups.VISIBILITY.getTypes();
    Assert.assertEquals(5, types.size());
    Assert.assertEquals(CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS, types.get(0));
    Assert.assertEquals(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS, types.get(1));
    Assert.assertEquals(CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS, types.get(2));
    Assert.assertEquals(CheckAccessControls.PRIVATE_OVERRIDE, types.get(3));
    Assert.assertEquals(CheckAccessControls.VISIBILITY_MISMATCH, types.get(4));
  }

  @Test
  public void testCONSTANT_PROPERTY() {
    Assert.assertEquals("constantProperty", DiagnosticGroups.CONSTANT_PROPERTY.getName());
    List<DiagnosticType> types = DiagnosticGroups.CONSTANT_PROPERTY.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE, types.get(0));
  }

  @Test
  public void testNON_STANDARD_JSDOC() {
    Assert.assertEquals("nonStandardJsDocs", DiagnosticGroups.NON_STANDARD_JSDOC.getName());
    List<DiagnosticType> types = DiagnosticGroups.NON_STANDARD_JSDOC.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(RhinoErrorReporter.BAD_JSDOC_ANNOTATION, types.get(0));
  }

  @Test
  public void testACCESS_CONTROLS() {
    Assert.assertEquals("accessControls", DiagnosticGroups.ACCESS_CONTROLS.getName());
    List<DiagnosticType> types = DiagnosticGroups.ACCESS_CONTROLS.getTypes();
    Assert.assertEquals(11, types.size());
    Assert.assertEquals(DiagnosticGroups.DEPRECATED.getTypes(), types.subList(0, 6));
    Assert.assertEquals(DiagnosticGroups.VISIBILITY.getTypes(), types.subList(6, 11));
  }

  @Test
  public void testINVALID_CASTS() {
    Assert.assertEquals("invalidCasts", DiagnosticGroups.INVALID_CASTS.getName());
    List<DiagnosticType> types = DiagnosticGroups.INVALID_CASTS.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(TypeValidator.INVALID_CAST, types.get(0));
  }

  @Test
  public void testFILEOVERVIEW_JSDOC() {
    Assert.assertEquals("fileoverviewTags", DiagnosticGroups.FILEOVERVIEW_JSDOC.getName());
    List<DiagnosticType> types = DiagnosticGroups.FILEOVERVIEW_JSDOC.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(RhinoErrorReporter.EXTRA_FILEOVERVIEW, types.get(0));
  }

  @Test
  public void testSTRICT_MODULE_DEP_CHECK() {
    Assert.assertEquals("strictModuleDepCheck", DiagnosticGroups.STRICT_MODULE_DEP_CHECK.getName());
    List<DiagnosticType> types = DiagnosticGroups.STRICT_MODULE_DEP_CHECK.getTypes();
    Assert.assertEquals(2, types.size());
    Assert.assertEquals(VarCheck.STRICT_MODULE_DEP_ERROR, types.get(0));
    Assert.assertEquals(CheckGlobalNames.STRICT_MODULE_DEP_QNAME, types.get(1));
  }

  @Test
  public void testEXTERNS_VALIDATION() {
    Assert.assertEquals("externsValidation", DiagnosticGroups.EXTERNS_VALIDATION.getName());
    List<DiagnosticType> types = DiagnosticGroups.EXTERNS_VALIDATION.getTypes();
    Assert.assertEquals(2, types.size());
    Assert.assertEquals(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, types.get(0));
    Assert.assertEquals(VarCheck.UNDEFINED_EXTERN_VAR_ERROR, types.get(1));
  }

  @Test
  public void testAMBIGUOUS_FUNCTION_DECL() {
    Assert.assertEquals("ambiguousFunctionDecl", DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL.getName());
    List<DiagnosticType> types = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL, types.get(0));
  }

  @Test
  public void testUNKNOWN_DEFINES() {
    Assert.assertEquals("unknownDefines", DiagnosticGroups.UNKNOWN_DEFINES.getName());
    List<DiagnosticType> types = DiagnosticGroups.UNKNOWN_DEFINES.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(ProcessDefines.UNKNOWN_DEFINE_WARNING, types.get(0));
  }

  @Test
  public void testTWEAKS() {
    Assert.assertEquals("tweakValidation", DiagnosticGroups.TWEAKS.getName());
    List<DiagnosticType> types = DiagnosticGroups.TWEAKS.getTypes();
    Assert.assertEquals(3, types.size());
    Assert.assertEquals(ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING, types.get(0));
    Assert.assertEquals(ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING, types.get(1));
    Assert.assertEquals(ProcessTweaks.UNKNOWN_TWEAK_WARNING, types.get(2));
  }

  @Test
  public void testMISSING_PROPERTIES() {
    Assert.assertEquals("missingProperties", DiagnosticGroups.MISSING_PROPERTIES.getName());
    List<DiagnosticType> types = DiagnosticGroups.MISSING_PROPERTIES.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(TypeCheck.INEXISTENT_PROPERTY, types.get(0));
  }

  @Test
  public void testINTERNET_EXPLORER_CHECKS() {
    Assert.assertEquals("internetExplorerChecks", DiagnosticGroups.INTERNET_EXPLORER_CHECKS.getName());
    List<DiagnosticType> types = DiagnosticGroups.INTERNET_EXPLORER_CHECKS.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(RhinoErrorReporter.TRAILING_COMMA, types.get(0));
  }

  @Test
  public void testUNDEFINED_VARIABLES() {
    Assert.assertEquals("undefinedVars", DiagnosticGroups.UNDEFINED_VARIABLES.getName());
    List<DiagnosticType> types = DiagnosticGroups.UNDEFINED_VARIABLES.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(VarCheck.UNDEFINED_VAR_ERROR, types.get(0));
  }

  @Test
  public void testCHECK_REGEXP() {
    Assert.assertEquals("checkRegExp", DiagnosticGroups.CHECK_REGEXP.getName());
    List<DiagnosticType> types = DiagnosticGroups.CHECK_REGEXP.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(CheckRegExp.REGEXP_REFERENCE, types.get(0));
  }

  @Test
  public void testCHECK_TYPES() {
    Assert.assertEquals("checkTypes", DiagnosticGroups.CHECK_TYPES.getName());
    List<DiagnosticType> types = DiagnosticGroups.CHECK_TYPES.getTypes();
    Assert.assertEquals(2, types.size());
    Assert.assertEquals(TypeValidator.ALL_DIAGNOSTICS, types.subList(0, 1));
    Assert.assertEquals(TypeCheck.ALL_DIAGNOSTICS, types.subList(1, 2));
  }

  @Test
  public void testCHECK_VARIABLES() {
    Assert.assertEquals("checkVars", DiagnosticGroups.CHECK_VARIABLES.getName());
    List<DiagnosticType> types = DiagnosticGroups.CHECK_VARIABLES.getTypes();
    Assert.assertEquals(2, types.size());
    Assert.assertEquals(VarCheck.UNDEFINED_VAR_ERROR, types.get(0));
    Assert.assertEquals(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, types.get(1));
  }

  @Test
  public void testCHECK_USELESS_CODE() {
    Assert.assertEquals("uselessCode", DiagnosticGroups.CHECK_USELESS_CODE.getName());
    List<DiagnosticType> types = DiagnosticGroups.CHECK_USELESS_CODE.getTypes();
    Assert.assertEquals(2, types.size());
    Assert.assertEquals(CheckSideEffects.USELESS_CODE_ERROR, types.get(0));
    Assert.assertEquals(CheckUnreachableCode.UNREACHABLE_CODE, types.get(1));
  }
  
  @Test
  public void testTYPE_INVALIDATION() {
    Assert.assertEquals("typeInvalidation", DiagnosticGroups.TYPE_INVALIDATION.getName());
    List<DiagnosticType> types = DiagnosticGroups.TYPE_INVALIDATION.getTypes();
    Assert.assertEquals(1, types.size());
    Assert.assertEquals(DisambiguateProperties.Warnings.INVALIDATION, types.get(0));
  }

  @Test
  public void testSetWarningLevels() {
    // to be implemented
  }
}