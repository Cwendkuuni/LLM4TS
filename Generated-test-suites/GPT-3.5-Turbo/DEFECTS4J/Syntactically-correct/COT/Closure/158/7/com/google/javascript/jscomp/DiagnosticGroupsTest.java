import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticGroups;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {
  
  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroupName", group);
    assertEquals(group, registeredGroup);
  }
  
  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroups.registerGroup("testGroupName", group);
    Map<String, DiagnosticGroup> registeredGroups = new DiagnosticGroups().getRegisteredGroups();
    assertTrue(registeredGroups.containsValue(group));
  }
  
  @Test 
  public void testForName() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroups.registerGroup("testGroupName", group);
    DiagnosticGroup registeredGroup = new DiagnosticGroups().forName("testGroupName");
    assertEquals(group, registeredGroup);
  }
  
  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("test1", "test2");
    CheckLevel level = CheckLevel.WARNING;
    new DiagnosticGroups().setWarningLevels(options, diagnosticGroups, level);
    assertEquals(level, options.getWarningLevel("test1"));
    assertEquals(level, options.getWarningLevel("test2"));
  }
  
  @Test
  public void testGlobalsThis() {
    DiagnosticGroup globalsThisGroup = DiagnosticGroups.GLOBAL_THIS;
    assertNotNull(globalsThisGroup);
  }
  
  @Test
  public void testDeprecated() {
    DiagnosticGroup deprecatedGroup = DiagnosticGroups.DEPRECATED;
    assertNotNull(deprecatedGroup);
  }
  
  @Test
  public void testVisibility() {
    DiagnosticGroup visibilityGroup = DiagnosticGroups.VISIBILITY;
    assertNotNull(visibilityGroup);
  }
  
  @Test
  public void testConstantProperty() {
    DiagnosticGroup constantPropertyGroup = DiagnosticGroups.CONSTANT_PROPERTY;
    assertNotNull(constantPropertyGroup);
  }
  
  @Test
  public void testNonStandardJsDocs() {
    DiagnosticGroup nonStandardJsDocsGroup = DiagnosticGroups.NON_STANDARD_JSDOC;
    assertNotNull(nonStandardJsDocsGroup);
  }
  
  @Test
  public void testAccessControls() {
    DiagnosticGroup accessControlsGroup = DiagnosticGroups.ACCESS_CONTROLS;
    assertNotNull(accessControlsGroup);
  }
  
  @Test
  public void testInvalidCasts() {
    DiagnosticGroup invalidCastsGroup = DiagnosticGroups.INVALID_CASTS;
    assertNotNull(invalidCastsGroup);
  }
  
  @Test
  public void testFileoverviewJsDoc() {
    DiagnosticGroup fileoverviewJsDocGroup = DiagnosticGroups.FILEOVERVIEW_JSDOC;
    assertNotNull(fileoverviewJsDocGroup);
  }
  
  @Test
  public void testStrictModuleDepCheck() {
    DiagnosticGroup strictModuleDepCheckGroup = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
    assertNotNull(strictModuleDepCheckGroup);
  }
  
  @Test
  public void testExternsValidation() {
    DiagnosticGroup externsValidationGroup = DiagnosticGroups.EXTERNS_VALIDATION;
    assertNotNull(externsValidationGroup);
  }
  
  @Test
  public void testAmbiguousFunctionDecl() {
    DiagnosticGroup ambiguousFunctionDeclGroup = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
    assertNotNull(ambiguousFunctionDeclGroup);
  }
  
  @Test
  public void testUnknownDefines() {
    DiagnosticGroup unknownDefinesGroup = DiagnosticGroups.UNKNOWN_DEFINES;
    assertNotNull(unknownDefinesGroup);
  }
  
  @Test
  public void testTweaks() {
    DiagnosticGroup tweaksGroup = DiagnosticGroups.TWEAKS;
    assertNotNull(tweaksGroup);
  }
  
  @Test
  public void testMissingProperties() {
    DiagnosticGroup missingPropertiesGroup = DiagnosticGroups.MISSING_PROPERTIES;
    assertNotNull(missingPropertiesGroup);
  }
  
  @Test
  public void testInternetExplorerChecks() {
    DiagnosticGroup internetExplorerChecksGroup = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
    assertNotNull(internetExplorerChecksGroup);
  }
  
  @Test
  public void testUndefinedVariables() {
    DiagnosticGroup undefinedVariablesGroup = DiagnosticGroups.UNDEFINED_VARIABLES;
    assertNotNull(undefinedVariablesGroup);
  }
  
  @Test
  public void testCheckRegExp() {
    DiagnosticGroup checkRegExpGroup = DiagnosticGroups.CHECK_REGEXP;
    assertNotNull(checkRegExpGroup);
  }
  
  @Test
  public void testCheckTypes() {
    DiagnosticGroup checkTypesGroup = DiagnosticGroups.CHECK_TYPES;
    assertNotNull(checkTypesGroup);
  }
  
  @Test
  public void testCheckVariables() {
    DiagnosticGroup checkVariablesGroup = DiagnosticGroups.CHECK_VARIABLES;
    assertNotNull(checkVariablesGroup);
  }
  
  @Test
  public void testCheckUselessCode() {
    DiagnosticGroup checkUselessCodeGroup = DiagnosticGroups.CHECK_USELESS_CODE;
    assertNotNull(checkUselessCodeGroup);
  }
  
  @Test
  public void testTypeInvalidation() {
    DiagnosticGroup typeInvalidationGroup = DiagnosticGroups.TYPE_INVALIDATION;
    assertNotNull(typeInvalidationGroup);
  }
}