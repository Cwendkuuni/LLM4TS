import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  private DiagnosticGroups diagnosticGroups;

  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisterGroup_withNameAndGroup_shouldRegisterGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    diagnosticGroups.registerGroup("groupName", group);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertTrue(registeredGroups.containsKey("groupName"));
    assertEquals(group, registeredGroups.get("groupName"));
  }

  @Test
  public void testRegisterGroup_withNameAndTypes_shouldRegisterGroup() {
    DiagnosticType type1 = new DiagnosticType("testType1");
    DiagnosticType type2 = new DiagnosticType("testType2");
    DiagnosticGroup group = diagnosticGroups.registerGroup("groupName", type1, type2);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertTrue(registeredGroups.containsKey("groupName"));
    assertEquals(group, registeredGroups.get("groupName"));
    assertEquals(2, group.getTypes().size());
    assertTrue(group.getTypes().contains(type1));
    assertTrue(group.getTypes().contains(type2));
  }

  @Test
  public void testRegisterGroup_withNameAndGroups_shouldRegisterGroup() {
    DiagnosticGroup nestedGroup1 = new DiagnosticGroup("nestedGroup1");
    DiagnosticGroup nestedGroup2 = new DiagnosticGroup("nestedGroup2");
    DiagnosticGroup group = diagnosticGroups.registerGroup("groupName", nestedGroup1, nestedGroup2);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertTrue(registeredGroups.containsKey("groupName"));
    assertEquals(group, registeredGroups.get("groupName"));
    assertEquals(2, group.getGroups().size());
    assertTrue(group.getGroups().contains(nestedGroup1));
    assertTrue(group.getGroups().contains(nestedGroup2));
  }

  @Test
  public void testForName_withExistingGroupName_shouldReturnGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    diagnosticGroups.registerGroup("groupName", group);
    DiagnosticGroup foundGroup = diagnosticGroups.forName("groupName");
    assertEquals(group, foundGroup);
  }

  @Test
  public void testForName_withNonExistingGroupName_shouldReturnNull() {
    DiagnosticGroup foundGroup = diagnosticGroups.forName("nonExistingGroupName");
    assertNull(foundGroup);
  }

  @Test
  public void testSetWarningLevels_withExistingDiagnosticGroups_shouldSetWarningLevels() {
    String groupName1 = "groupName1";
    String groupName2 = "groupName2";
    DiagnosticGroup group1 = new DiagnosticGroup(groupName1);
    DiagnosticGroup group2 = new DiagnosticGroup(groupName2);
    diagnosticGroups.registerGroup(groupName1, group1);
    diagnosticGroups.registerGroup(groupName2, group2);
    List<String> diagnosticGroups = Arrays.asList(groupName1, groupName2);
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group2));
  }

  @Test(expected = NullPointerException.class)
  public void testSetWarningLevels_withNonExistingDiagnosticGroup_shouldThrowNullPointerException() {
    String nonExistingGroupName = "nonExistingGroupName";
    List<String> diagnosticGroups = Arrays.asList(nonExistingGroupName);
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
  }

}