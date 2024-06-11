import org.junit.*;

public class DiagnosticGroupsTest {

  // Test registerGroup(String name, DiagnosticGroup group) method
  @Test
  public void testRegisterGroupWithGroup() {
    DiagnosticGroups groups = new DiagnosticGroups();
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup result = groups.registerGroup("testName", group);

    Assert.assertEquals(group, result);
    Assert.assertEquals(group, groups.forName("testName"));
  }

  // Test registerGroup(String name, DiagnosticType ... types) method
  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroups groups = new DiagnosticGroups();
    DiagnosticGroup result = groups.registerGroup("testName",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON);

    Assert.assertNotNull(result);
    Assert.assertEquals(result, groups.forName("testName"));
  }

  // Test registerGroup(String name, DiagnosticGroup ... groups) method
  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroups groups = new DiagnosticGroups();
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    DiagnosticGroup result = groups.registerGroup("testName", group1, group2);

    Assert.assertEquals(result, groups.forName("testName"));
    Assert.assertEquals(group1, groups.forName("testName").getGroups().get(0));
    Assert.assertEquals(group2, groups.forName("testName").getGroups().get(1));
  }

  // Test getRegisteredGroups() method
  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups groups = new DiagnosticGroups();
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    groups.registerGroup("name1", group1);
    groups.registerGroup("name2", group2);

    Assert.assertNotNull(groups.getRegisteredGroups());
    Assert.assertEquals(group1, groups.getRegisteredGroups().get("name1"));
    Assert.assertEquals(group2, groups.getRegisteredGroups().get("name2"));
  }

  // Test forName(String name) method
  @Test
  public void testForName() {
    DiagnosticGroups groups = new DiagnosticGroups();
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    groups.registerGroup("testName", group);

    Assert.assertNotNull(groups.forName("testName"));
    Assert.assertEquals(group, groups.forName("testName"));
  }

  // Test setWarningLevels(CompilerOptions options, List<String> diagnosticGroups, CheckLevel level) method
  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups groups = new DiagnosticGroups();
    groups.registerGroup("testName",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON);
    groups.registerGroup("testName2",
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON);

    groups.setWarningLevels(options, Arrays.asList("testName", "testName2"), CheckLevel.WARNING);
    CheckLevel level1 = options.getWarningLevel(groups.forName("testName"));
    CheckLevel level2 = options.getWarningLevel(groups.forName("testName2"));

    Assert.assertEquals(CheckLevel.WARNING, level1);
    Assert.assertEquals(CheckLevel.WARNING, level2);
  }
}