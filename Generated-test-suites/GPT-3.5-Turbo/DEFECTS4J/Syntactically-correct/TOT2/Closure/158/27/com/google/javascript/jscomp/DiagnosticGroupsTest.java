import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupWithStringDiagnosticGroup() {
    // Test the registerGroup method with String and DiagnosticGroup parameters
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("test", group);
    assertEquals(group, result);
  }

  @Test
  public void testRegisterGroupWithStringDiagnosticTypeArray() {
    // Test the registerGroup method with String and DiagnosticType array parameters
    DiagnosticType type = new DiagnosticType("TEST_TYPE");
    DiagnosticGroup group = new DiagnosticGroup("testGroup", type);
    DiagnosticGroup result = DiagnosticGroups.registerGroup("test", type);
    assertEquals(group, result);
  }

  @Test
  public void testRegisterGroupWithStringDiagnosticGroupArray() {
    // Test the registerGroup method with String and DiagnosticGroup array parameters
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    DiagnosticGroup combinedGroup = new DiagnosticGroup("combinedGroup", group1, group2);
    DiagnosticGroup result = DiagnosticGroups.registerGroup("test", group1, group2);
    assertEquals(combinedGroup, result);
  }

  @Test
  public void testGetRegisteredGroups() {
    // Test the getRegisteredGroups method to ensure it returns the correct map
    assertEquals(DiagnosticGroups.getRegisteredGroups(), DiagnosticGroups.groupsByName);
  }

  @Test
  public void testForName() {
    // Test the forName method to find a registered diagnostic group by name
    String name = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup(name);
    DiagnosticGroups.registerGroup(name, group);
    assertEquals(group, DiagnosticGroups.forName(name));
  }
}