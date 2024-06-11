import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DiagnosticGroupsTest {
  
  private DiagnosticGroups diagnosticGroups;
  
  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }
  
  @Test
  public void testGetRegisteredGroups() {
    assertNotNull(diagnosticGroups.getRegisteredGroups());
  }
  
  @Test
  public void testForName() {
    assertNotNull(diagnosticGroups.forName("globalThis"));
  }
  
  @Test
  public void testRegisterGroup() {
    String name = "testGroup";
    DiagnosticType type = new DiagnosticType();
    DiagnosticGroup group = new DiagnosticGroup(name, type);
    assertEquals(group, diagnosticGroups.registerGroup(name, type));
  }
  
  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("globalThis", "deprecated");
    CheckLevel level = CheckLevel.WARNING;
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    assertEquals(level, options.getWarningLevel(diagnosticGroups.forName("globalThis")));
    assertEquals(level, options.getWarningLevel(diagnosticGroups.forName("deprecated")));
  }

}