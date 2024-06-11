
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;
import org.junit.Test;
import java.util.List;

public class DiagnosticGroupsTest {

  @Test
  public void testDiagnosticGroupsConstructor() {
    new DiagnosticGroups();
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertFalse(registeredGroups.isEmpty());
    // Add assertions for empty map
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("GLOBAL_THIS");
    assertNotNull(group);
    // Add assertions for null group
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = new DiagnosticGroup("testGroup", null);
    diagnosticGroups.registerGroup("testGroup", group);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName("testGroup");
    assertEquals(group, registeredGroup);
    // Add assertions for existing group

    diagnosticGroups = new DiagnosticGroups();
    assertThrows(IllegalArgumentException.class, () -> diagnosticGroups.registerGroup("GLOBAL_THIS", new DiagnosticGroup("testGroup", null)));
    // Add assertions for group registration failure
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("testGroup", CheckAccessControls.DEPRECATED_NAME);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName("testGroup");
    assertArrayEquals(new DiagnosticType[] { CheckAccessControls.DEPRECATED_NAME }, registeredGroup.getDiagnosticTypes());
    // Add assertions for types

    diagnosticGroups = new DiagnosticGroups();
    assertThrows(IllegalArgumentException.class, () -> diagnosticGroups.registerGroup("testGroup", new DiagnosticType[] {}));
    // Add assertions for empty types array
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecatedGroup = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup visibilityGroup = DiagnosticGroups.VISIBILITY;
    diagnosticGroups.registerGroup("testGroup", deprecatedGroup, visibilityGroup);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName("testGroup");
    assertSame(deprecatedGroup, registeredGroup.getParent());
    assertSame(visibilityGroup, registeredGroup.getParent().getParent());
    // Add assertions for groups

    diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = new DiagnosticGroup("group1", null);
    DiagnosticGroup group2 = new DiagnosticGroup("group2", null);
    assertThrows(IllegalArgumentException.class, () -> diagnosticGroups.registerGroup("testGroup", group1, group2, null));
    // Add assertions for null parent group
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = Arrays.asList("DEPRECATED", "VISIBILITY");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.DEPRECATED));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.VISIBILITY));
    // Add assertions for warning levels

    diagnosticGroups = new DiagnosticGroups();
    assertThrows(IllegalArgumentException.class, () -> diagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING));
    // Add assertions for null diagnosticGroupsList

    diagnosticGroups = new DiagnosticGroups();
    assertThrows(IllegalArgumentException.class, () -> diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, null));
    // Add assertions for null options

    diagnosticGroups = new DiagnosticGroups();
    assertThrows(IllegalArgumentException.class, () -> diagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup"), CheckLevel.WARNING));
    // Add assertions for non-existent diagnostic group
  }

  @Test
  public void testGetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, Arrays.asList("DEPRECATED", "VISIBILITY"), CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, diagnosticGroups.getWarningLevel(options, "DEPRECATED"));
    assertEquals(CheckLevel.WARNING, diagnosticGroups.getWarningLevel(options, "VISIBILITY"));
    // Add assertions for warning levels

    diagnosticGroups = new DiagnosticGroups();
    assertNull(diagnosticGroups.getWarningLevel(options, "testGroup"));
    // Add assertions for non-existent diagnostic group
  }

  @Test
  public void testMultipleInitializations() {
    DiagnosticGroups diagnosticGroups1 = new DiagnosticGroups();
    DiagnosticGroups diagnosticGroups2 = new DiagnosticGroups();
    diagnosticGroups1.registerGroup("DEPRECATED", CheckAccessControls.DEPRECATED_NAME);
    diagnosticGroups2.registerGroup("DEPRECATED", CheckAccessControls.DEPRECATED_NAME);
    assertEquals(diagnosticGroups1.forName("DEPRECATED"), diagnosticGroups2.forName("DEPRECATED"));
    // Add assertions for same group in multiple initializations
  }
}
