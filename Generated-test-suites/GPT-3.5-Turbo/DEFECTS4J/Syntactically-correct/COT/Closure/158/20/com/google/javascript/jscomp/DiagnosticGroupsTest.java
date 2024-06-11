import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {
  
  private static DiagnosticGroups groups;
  
  @BeforeClass
  public static void setUp() {
    groups = new DiagnosticGroups();
  }
  
  @Test
  public void testGetRegisteredGroups() {
    assertNotNull(groups.getRegisteredGroups());
  }
  
  @Test
  public void testForName() {
    assertNotNull(groups.forName("accessControls"));
    assertNull(groups.forName("invalidGroup"));
  }
  
  // Edge cases and exception handling scenarios
  @Test
  public void testRegisterGroup_types() {
    assertNotNull(DiagnosticGroups.registerGroup("testGroup1"));
    assertNotNull(DiagnosticGroups.registerGroup("testGroup2", DiagnosticGroups.VISIBILITY));
  }
  
  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = new ArrayList<String>();
    diagnosticGroups.add("accessControls");
    diagnosticGroups.add("invalidGroup");
    groups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(groups.forName("accessControls")) == CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(groups.forName("invalidGroup")) == null);
  }

}