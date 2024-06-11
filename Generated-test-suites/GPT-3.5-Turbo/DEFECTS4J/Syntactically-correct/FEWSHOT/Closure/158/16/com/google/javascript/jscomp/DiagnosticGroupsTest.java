import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
  
  @Test
  public void testRegisterGroupWithObject() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", group);
    assertEquals(group, registeredGroup);
  }
  
  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticType type2 = new DiagnosticType("type2");
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", type1, type2);
    assertEquals(group, DiagnosticGroups.forName("testGroup"));
  }
  
  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticType type2 = new DiagnosticType("type2");
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", type1);
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", type2);
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", group1, group2);
    assertEquals(group, DiagnosticGroups.forName("testGroup"));
  }
  
  @Test
  public void testGetRegisteredGroups() {
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", type1);
    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    assertEquals(group1, registeredGroups.get("group1"));
  }
  
  @Test
  public void testForName() {
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", type1);
    DiagnosticGroup group = DiagnosticGroups.forName("group1");
    assertEquals(group1, group);
  }
  
  @Test
  public void testRegisterGroupWithNameAndGroup() {
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", type1);
    DiagnosticGroup group = DiagnosticGroups.registerGroup("group2", group1);
    assertEquals(group, DiagnosticGroups.forName("group2"));
  }
  
  @Test
  public void testSetWarningLevels() {
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", type1);
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = new ArrayList<>();
    diagnosticGroups.add("group1");
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
  }
  
  @Test(expected = NullPointerException.class)
  public void testSetWarningLevelsInvalidDiagnosticGroup() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = new ArrayList<>();
    diagnosticGroups.add("group1");
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
  }
}