
import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    String name = "testName";
    DiagnosticGroup group = new DiagnosticGroup(name);
    DiagnosticGroup result = DiagnosticGroups.registerGroup(name, group);
    assertEquals(result, group);

    DiagnosticType type = new DiagnosticType();
    DiagnosticGroup result2 = DiagnosticGroups.registerGroup(name, type);
    assertEquals(result2, new DiagnosticGroup(name, type));

    DiagnosticGroup[] groups = { new DiagnosticGroup("group1"), new DiagnosticGroup("group2") };
    DiagnosticGroup result3 = DiagnosticGroups.registerGroup(name, groups);
    assertEquals(result3, new DiagnosticGroup(name, groups));

    Map<String, DiagnosticGroup> expectedGroups = new HashMap<>();
    expectedGroups.put(name, group);
    assertEquals(DiagnosticGroups.getRegisteredGroups(), expectedGroups);
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> result = DiagnosticGroups.getRegisteredGroups();
    assertEquals(result, DiagnosticGroups.groupsByName);
  }

  @Test
  public void testForName() {
    String name = "testName";
    DiagnosticGroup group = new DiagnosticGroup(name);
    DiagnosticGroups.registerGroup(name, group);
    DiagnosticGroup result = DiagnosticGroups.forName(name);
    assertEquals(result, group);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
    CheckLevel level = CheckLevel.WARNING;
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
    DiagnosticGroups.registerGroup("testGroup1", group1);
    DiagnosticGroups.registerGroup("testGroup2", group2);
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    assertEquals(options.getWarningLevel(group1), level);
    assertEquals(options.getWarningLevel(group2), level);
  }
}
