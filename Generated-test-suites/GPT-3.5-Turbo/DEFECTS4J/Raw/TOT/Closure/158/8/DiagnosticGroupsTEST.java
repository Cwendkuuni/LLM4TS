import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup_StringDiagnosticGroup() {
    DiagnosticGroup group = new DiagnosticGroup();
    assertNotNull("Group should not be null after registering", DiagnosticGroups.registerGroup("testGroup", group));
    assertEquals("Group should have been registered with the given name", group, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testRegisterGroup_StringDiagnosticTypeArray() {
    DiagnosticType type = new DiagnosticType();
    assertNotNull("Group should not be null after registering", DiagnosticGroups.registerGroup("testGroup", type));
    assertEquals("Group should have been registered with the given name", type, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testRegisterGroup_StringDiagnosticGroupArray() {
    DiagnosticGroup group1 = new DiagnosticGroup();
    DiagnosticGroup group2 = new DiagnosticGroup();
    assertNotNull("Group should not be null after registering", DiagnosticGroups.registerGroup("testGroup", group1, group2));
    assertEquals("Group should have been registered with the given name", group1, DiagnosticGroups.forName("testGroup"));
    assertEquals("Group should have been registered with the given name", group2, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    assertNotNull("Registered groups should not be null", registeredGroups);
    assertEquals("Registered groups should have the correct size", 18, registeredGroups.size());
    assertTrue("Registered groups should contain GLOBAL_THIS group", registeredGroups.containsKey("globalThis"));
    assertTrue("Registered groups should contain DEPRECATED group", registeredGroups.containsKey("deprecated"));
    assertTrue("Registered groups should contain VISIBILITY group", registeredGroups.containsKey("visibility"));
    assertTrue("Registered groups should contain CONSTANT_PROPERTY group", registeredGroups.containsKey("constantProperty"));
    assertTrue("Registered groups should contain NON_STANDARD_JSDOC group", registeredGroups.containsKey("nonStandardJsDocs"));
    assertTrue("Registered groups should contain ACCESS_CONTROLS group", registeredGroups.containsKey("accessControls"));
    assertTrue("Registered groups should contain INVALID_CASTS group", registeredGroups.containsKey("invalidCasts"));
    assertTrue("Registered groups should contain FILEOVERVIEW_JSDOC group", registeredGroups.containsKey("fileoverviewTags"));
    assertTrue("Registered groups should contain STRICT_MODULE_DEP_CHECK group", registeredGroups.containsKey("strictModuleDepCheck"));
    assertTrue("Registered groups should contain EXTERNS_VALIDATION group", registeredGroups.containsKey("externsValidation"));
    assertTrue("Registered groups should contain AMBIGUOUS_FUNCTION_DECL group", registeredGroups.containsKey("ambiguousFunctionDecl"));
    assertTrue("Registered groups should contain UNKNOWN_DEFINES group", registeredGroups.containsKey("unknownDefines"));
    assertTrue("Registered groups should contain TWEAKS group", registeredGroups.containsKey("tweakValidation"));
    assertTrue("Registered groups should contain MISSING_PROPERTIES group", registeredGroups.containsKey("missingProperties"));
    assertTrue("Registered groups should contain INTERNET_EXPLORER_CHECKS group", registeredGroups.containsKey("internetExplorerChecks"));
    assertTrue("Registered groups should contain UNDEFINED_VARIABLES group", registeredGroups.containsKey("undefinedVars"));
    assertTrue("Registered groups should contain CHECK_REGEXP group", registeredGroups.containsKey("checkRegExp"));
    assertTrue("Registered groups should contain CHECK_TYPES group", registeredGroups.containsKey("checkTypes"));
    assertTrue("Registered groups should contain CHECK_VARIABLES group", registeredGroups.containsKey("checkVars"));
    assertTrue("Registered groups should contain CHECK_USELESS_CODE group", registeredGroups.containsKey("uselessCode"));
    assertTrue("Registered groups should contain TYPE_INVALIDATION group", registeredGroups.containsKey("typeInvalidation"));
  }

  @Test
  public void testForName() {
    DiagnosticGroup group = new DiagnosticGroup();
    DiagnosticGroups.registerGroup("testGroup", group);
    assertEquals("Group should be found by the given name", group, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("testGroup");
    CheckLevel level = CheckLevel.WARNING;
    DiagnosticGroup group = new DiagnosticGroup();
    DiagnosticGroups.registerGroup("testGroup", group);
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    assertEquals("Options should have the correct warning level for the given group", level, options.getWarningLevel(group));
  }
}