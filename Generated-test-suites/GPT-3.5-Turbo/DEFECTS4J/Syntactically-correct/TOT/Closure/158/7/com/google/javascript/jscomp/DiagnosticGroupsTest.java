import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

public class DiagnosticGroupsTest {
  
  private DiagnosticGroups diagnosticGroups;
  
  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }
  
  @Test
  public void testRegisterGroupWithStringAndDiagnosticGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    diagnosticGroups.registerGroup("test", group);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertTrue(registeredGroups.containsKey("test"));
    assertEquals(group, registeredGroups.get("test"));
  }
  
  @Test
  public void testRegisterGroupWithStringAndDiagnosticTypes() {
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticType type2 = new DiagnosticType("type2");
    diagnosticGroups.registerGroup("test", type1, type2);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertTrue(registeredGroups.containsKey("test"));
    DiagnosticGroup group = registeredGroups.get("test");
    assertTrue(group.containsType(type1));
    assertTrue(group.containsType(type2));
  }
  
  @Test
  public void testRegisterGroupWithStringAndDiagnosticGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    diagnosticGroups.registerGroup("test", group1, group2);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertTrue(registeredGroups.containsKey("test"));
    DiagnosticGroup group = registeredGroups.get("test");
    assertTrue(group.containsGroup(group1));
    assertTrue(group.containsGroup(group2));
  }
  
  @Test
  public void testForName() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    diagnosticGroups.registerGroup("test", group);
    assertEquals(group, diagnosticGroups.forName("test"));
  }
  
  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup"), CheckLevel.WARNING);
    Map<DiagnosticGroup, CheckLevel> warningLevels = options.getWarningLevels();
    assertTrue(warningLevels.containsKey(diagnosticGroups.forName("testGroup")));
    assertEquals(CheckLevel.WARNING, warningLevels.get(diagnosticGroups.forName("testGroup")));
  }
}