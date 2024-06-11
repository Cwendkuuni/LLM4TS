import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupWithDiagnosticGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testName", group);
    assertEquals(group, registeredGroup);
  }

  @Test
  public void testRegisterGroupWithDiagnosticTypes() {
    DiagnosticType type = new DiagnosticType("testType");
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testName", type);
    assertEquals(1, group.getTypes().size());
    assertTrue(group.getTypes().contains(type));
  }

  @Test
  public void testRegisterGroupWithDiagnosticGroups() {
    DiagnosticGroup subgroup = new DiagnosticGroup("subgroup");
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testName", subgroup);
    assertEquals(1, group.getSubgroups().size());
    assertTrue(group.getSubgroups().contains(subgroup));
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagGroups = new DiagnosticGroups();
    assertEquals(0, diagGroups.getRegisteredGroups().size());
  }

  @Test
  public void testForName() {
    String groupName = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup(groupName);
    DiagnosticGroups.registerGroup(groupName, group);
    DiagnosticGroup retrievedGroup = new DiagnosticGroups().forName(groupName);
    assertEquals(group, retrievedGroup);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagGroups = new DiagnosticGroups();
    diagGroups.setWarningLevels(options, List.of("testGroup"), CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(new DiagnosticGroup("testGroup")));
  }
}