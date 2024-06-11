import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
        new DiagnosticType());
    assertEquals("testGroup", group.getName());
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
        new DiagnosticType(), new DiagnosticType());
    assertEquals("testGroup", group.getName());
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroup innerGroup = DiagnosticGroups.registerGroup("innerGroup",
        new DiagnosticType());
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
        innerGroup);
    assertEquals("testGroup", group.getName());
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups groups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = groups.getRegisteredGroups();
    assertNotNull(registeredGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups groups = new DiagnosticGroups();
    DiagnosticGroup group = groups.forName("testGroup");
    assertNull(group);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups groups = new DiagnosticGroups();
    List<String> diagnosticGroups = Arrays.asList("testGroup");
    groups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    WarningLevel testGroupLevel = options.getWarningLevel(DiagnosticGroups.forName("testGroup"));
    assertEquals(CheckLevel.WARNING, testGroupLevel);
  }

  @Test
  public void testGlobalsThisGroup() {
    DiagnosticGroup group = DiagnosticGroups.GLOBAL_THIS;
    assertNotNull(group);
  }

  @Test
  public void testDeprecatedGroup() {
    DiagnosticGroup group = DiagnosticGroups.DEPRECATED;
    assertNotNull(group);
  }

  @Test
  public void testVisibilityGroup() {
    DiagnosticGroup group = DiagnosticGroups.VISIBILITY;
    assertNotNull(group);
  }

  @Test
  public void testConstantPropertyGroup() {
    DiagnosticGroup group = DiagnosticGroups.CONSTANT_PROPERTY;
    assertNotNull(group);
  }

  @Test
  public void testNonStandardJsDocsGroup() {
    DiagnosticGroup group = DiagnosticGroups.NON_STANDARD_JSDOC;
    assertNotNull(group);
  }

  @Test
  public void testAccessControlsGroup() {
    DiagnosticGroup group = DiagnosticGroups.ACCESS_CONTROLS;
    assertNotNull(group);
  }

  @Test
  public void testInvalidCastsGroup() {
    DiagnosticGroup group = DiagnosticGroups.INVALID_CASTS;
    assertNotNull(group);
  }

  @Test
  public void testFileoverviewJsDocGroup() {
    DiagnosticGroup group = DiagnosticGroups.FILEOVERVIEW_JSDOC;
    assertNotNull(group);
  }

  @Test
  public void testStrictModuleDepCheckGroup() {
    DiagnosticGroup group = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
    assertNotNull(group);
  }

  @Test
  public void testExternsValidationGroup() {
    DiagnosticGroup group = DiagnosticGroups.EXTERNS_VALIDATION;
    assertNotNull(group);
  }

  @Test
  public void testAmbiguousFunctionDeclGroup() {
    DiagnosticGroup group = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
    assertNotNull(group);
  }

  @Test
  public void testUnknownDefinesGroup() {
    DiagnosticGroup group = DiagnosticGroups.UNKNOWN_DEFINES;
    assertNotNull(group);
  }

  @Test
  public void testTweaksGroup() {
    DiagnosticGroup group = DiagnosticGroups.TWEAKS;
    assertNotNull(group);
  }

  @Test
  public void testMissingPropertiesGroup() {
    DiagnosticGroup group = DiagnosticGroups.MISSING_PROPERTIES;
    assertNotNull(group);
  }

  @Test
  public void testInternetExplorerChecksGroup() {
    DiagnosticGroup group = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
    assertNotNull(group);
  }

  @Test
  public void testUndefinedVariablesGroup() {
    DiagnosticGroup group = DiagnosticGroups.UNDEFINED_VARIABLES;
    assertNotNull(group);
  }

  @Test
  public void testCheckRegExpGroup() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_REGEXP;
    assertNotNull(group);
  }

  @Test
  public void testCheckTypesGroup() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_TYPES;
    assertNotNull(group);
  }

  @Test
  public void testCheckVariablesGroup() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_VARIABLES;
    assertNotNull(group);
  }

  @Test
  public void testCheckUselessCodeGroup() {
    DiagnosticGroup group = DiagnosticGroups.CHECK_USELESS_CODE;
    assertNotNull(group);
  }

  @Test
  public void testTypeInvalidationGroup() {
    DiagnosticGroup group = DiagnosticGroups.TYPE_INVALIDATION;
    assertNotNull(group);
  }
}