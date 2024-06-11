import static com.google.javascript.jscomp.DiagnosticGroups.*;
import static com.google.javascript.jscomp.Checker.Type.ERROR;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class DiagnosticGroupsTest {

  private static Checker compiler;

  @BeforeClass
  public static void setup() {
    compiler = new Checker();
  }

  @Test
  public void testRegisteredGroups() {
    Map<String, DiagnosticGroup> groups = getInstance().getRegisteredGroups();
    List<String> registeredGroups = Arrays.asList(DIAGNOSTIC_GROUP_NAMES.split(", "));

    assertEquals(registeredGroups.size(), groups.size());
    for (String groupName : registeredGroups) {
      DiagnosticGroup group = groups.get(groupName);
      assertEquals(groupName, group.getName());
    }
  }

  @Test
  public void testGetDiagnosticGroup() {
    DiagnosticGroup deprecatedGroup = forName("deprecated");
    DiagnosticGroup accessControlsGroup = forName("accessControls");

    assertEquals("deprecated", deprecatedGroup.getName());
    assertEquals("accessControls", accessControlsGroup.getName());

    assertEquals(Arrays.asList(DEPRECATED, VISIBILITY),
        accessControlsGroup.getGroups());
  }

  @Test
  public void testInvalidGroup() {
    try {
      forName("nonexistentGroup");
      fail();
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testSettingWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("accessControls", "checkTypes");

    getInstance().setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    assertEquals(CheckLevel.WARNING, options.getWarningLevel(ACCESS_CONTROLS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(CHECK_TYPES));
  }

  @Test
  public void testSettingErrorLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("checkTypes");

    getInstance().setWarningLevels(options, diagnosticGroups, CheckLevel.ERROR);

    assertEquals(CheckLevel.ERROR, options.getWarningLevel(CHECK_TYPES));
  }

  @Test
  public void testSettingWarningLevelForInvalidDiagnosticGroup() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("nonexistentGroup");

    getInstance().setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    assertEquals(CheckLevel.UNKNOWN, options.getWarningLevel("nonexistentGroup"));
  }

  private static DiagnosticGroups getInstance() {
    return DiagnosticGroups.getInstance();
  }
}