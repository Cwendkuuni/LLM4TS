import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.CompilerOptions;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    // Test registering a group with a diagnostic type
    DiagnosticGroup deprecatedGroup = diagnosticGroups.registerGroup("deprecated",
        DiagnosticType.DEPRECATED_NAME);
    assertNotNull(deprecatedGroup);
    options.setWarningLevel(deprecatedGroup, CheckLevel.WARNING);

    // Test registering a group with multiple diagnostic types
    DiagnosticGroup accessControlsGroup = diagnosticGroups.registerGroup("accessControls",
        DiagnosticType.DEPRECATED_NAME, DiagnosticType.VISIBILITY);
    assertNotNull(accessControlsGroup);
    options.setWarningLevel(accessControlsGroup, CheckLevel.QUIRK);

    // Test getting registered groups
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertNotNull(registeredGroups.get("deprecated"));
    assertNotNull(registeredGroups.get("accessControls"));
  }

  @Test
  public void testSettingWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    // Test setting warning levels for multiple groups
    diagnosticGroups.setWarningLevels(options, Arrays.asList("deprecated", "accessControls"), CheckLevel.ERROR);
    assertNotNull(options.getWarningLevel("deprecated"));
    assertNotNull(options.getWarningLevel("accessControls"));
  }
}