import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group1 = new DiagnosticGroup("group1", DiagnosticType.ERROR);
    DiagnosticGroup group2 = new DiagnosticGroup("group2", DiagnosticType.WARNING);
    
    DiagnosticGroups.registerGroup("Group1", group1);
    DiagnosticGroups.registerGroup("Group2", group2);
    
    Map<String, DiagnosticGroup> groups = DiagnosticGroups.getRegisteredGroups();
    
    assertEquals(group1, groups.get("Group1"));
    assertEquals(group2, groups.get("Group2"));
    assertNull(groups.get("Group3")); // Ensures that a non-registered group is not present in the map
  }

  @Test
  public void testForName() {
    DiagnosticGroup group1 = new DiagnosticGroup("group1", DiagnosticType.ERROR);
    DiagnosticGroup group2 = new DiagnosticGroup("group2", DiagnosticType.WARNING);
    
    DiagnosticGroups.registerGroup("Group1", group1);
    DiagnosticGroups.registerGroup("Group2", group2);
    
    assertEquals(group1, DiagnosticGroups.forName("Group1"));
    assertEquals(group2, DiagnosticGroups.forName("Group2"));
    assertNull(DiagnosticGroups.forName("Group3")); // Ensures that a non-registered group returns null
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups.registerGroup("Group1", new DiagnosticGroup("group1", DiagnosticType.ERROR));
    DiagnosticGroups.registerGroup("Group2", new DiagnosticGroup("group2", DiagnosticType.WARNING));
    
    assertEquals(CheckLevel.OFF, options.getWarningLevel("group1"));
    
    List<String> diagnosticGroups = Arrays.asList("Group1", "Group2");
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    assertEquals(CheckLevel.WARNING, options.getWarningLevel("group1"));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel("group2"));
  }

}