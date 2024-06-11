import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    Assert.assertNotNull(registeredGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
    Assert.assertNotNull(deprecatedGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForNameUnknownName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName("unknownGroupName");
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String groupName = "newGroupName";
    DiagnosticType diagnosticType = new DiagnosticType("newDiagnosticType");
    DiagnosticGroup newGroup = new DiagnosticGroup(groupName, diagnosticType);
    diagnosticGroups.registerGroup(groupName, diagnosticType);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(groupName);
    Assert.assertEquals(newGroup, registeredGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, new DiagnosticType("newDiagnosticType"));
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupNullDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("newGroupName", null);
  }

  @Test
  public void testRegisterGroupMultipleTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String groupName = "newGroupName";
    DiagnosticType diagnosticType1 = new DiagnosticType("newDiagnosticType1");
    DiagnosticType diagnosticType2 = new DiagnosticType("newDiagnosticType2");
    DiagnosticGroup newGroup = new DiagnosticGroup(groupName, diagnosticType1, diagnosticType2);
    diagnosticGroups.registerGroup(groupName, diagnosticType1, diagnosticType2);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(groupName);
    Assert.assertEquals(newGroup, registeredGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupDuplicateName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String groupName = "existingGroupName";
    diagnosticGroups.registerGroup(groupName, new DiagnosticType("newDiagnosticType"));
    diagnosticGroups.registerGroup(groupName, new DiagnosticType("newDiagnosticType2"));
  }

  @Test
  public void testRegisterGroupEmptyList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup newGroup = new DiagnosticGroup("emptyGroupName");
    diagnosticGroups.registerGroup("emptyGroupName", newGroup);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName("emptyGroupName");
    Assert.assertEquals(newGroup, registeredGroup);
  }

  @Test
  public void testRegisterGroupNullGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String groupName = "newGroupName";
    diagnosticGroups.registerGroup(groupName, null);
    Assert.fail("Expected NullPointerException");
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableList.of("ACCESS_CONTROLS", "INVALID_CASTS");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    DiagnosticGroup accessControlsGroup = diagnosticGroups.forName("ACCESS_CONTROLS");
    DiagnosticGroup invalidCastsGroup = diagnosticGroups.forName("INVALID_CASTS");
    Assert.assertEquals(CheckLevel.WARNING, options.getWarningLevel(accessControlsGroup));
    Assert.assertEquals(CheckLevel.WARNING, options.getWarningLevel(invalidCastsGroup));
  }

  @Test
  public void testSetWarningLevelsNullOptions() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupsList = ImmutableList.of("ACCESS_CONTROLS", "INVALID_CASTS");
    diagnosticGroups.setWarningLevels(null, diagnosticGroupsList, CheckLevel.ERROR);
    Assert.fail("Expected NullPointerException");
  }

  @Test
  public void testSetWarningLevelsNullDiagnosticGroupsList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING);
    Assert.fail("Expected IllegalArgumentException");
  }

  @Test
  public void testSetWarningLevelsEmptyList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, ImmutableList.of(), CheckLevel.WARNING);
  }

  @Test
  public void testSetWarningLevelsUnknownDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableList.of("unknownDiagnosticGroup");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    Assert.assertEquals(CheckLevel.DEFAULT, options.getWarningLevel(diagnosticGroups.forName("unknownDiagnosticGroup")));
  }
}