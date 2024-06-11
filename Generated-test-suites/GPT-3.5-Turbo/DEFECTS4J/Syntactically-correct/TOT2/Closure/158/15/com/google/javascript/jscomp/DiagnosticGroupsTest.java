import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupFirstVariant() {
    DiagnosticGroup group = new DiagnosticGroup("TestGroup");
    DiagnosticGroup registered = DiagnosticGroups.registerGroup("TestGroup", group);
    assertEquals(group, registered);
  }

  @Test
  public void testRegisterGroupSecondVariant() {
    DiagnosticType type = new DiagnosticType("TestType");
    DiagnosticGroup group = DiagnosticGroups.registerGroup("TestGroup", type);
    assertNotNull(group);
    assertTrue(group.contains(type));
  }

  @Test
  public void testRegisterGroupThirdVariant() {
    DiagnosticGroup nestedGroup = new DiagnosticGroup("NestedGroup");
    DiagnosticGroup group = DiagnosticGroups.registerGroup("TestGroup", nestedGroup);
    assertNotNull(group);
    assertTrue(group.contains(nestedGroup));
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> groups = DiagnosticGroups.getRegisteredGroups();
    assertNotNull(groups);
    assertFalse(groups.isEmpty());
  }

  @Test
  public void testForName() {
    DiagnosticGroup group = new DiagnosticGroup("TestGroup");
    DiagnosticGroups.registerGroup("TestGroup", group);
    assertEquals(group, DiagnosticGroups.forName("TestGroup"));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("TestGroup1", "TestGroup2");
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    // Check if warning levels were properly set
  }
}