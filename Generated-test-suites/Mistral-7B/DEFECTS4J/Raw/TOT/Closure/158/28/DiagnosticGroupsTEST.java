import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Compiler;

import java.io.IOException;
import java.util.List;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();

    assertEquals(28, groups.size()); // Check the number of registered groups

    // Test some specific groups
    DiagnosticGroup deprecatedGroup = groups.get("deprecated");
    assertTrue(deprecatedGroup instanceof DiagnosticGroup);
    List<DiagnosticType> deprecatedTypes = deprecatedGroup.getTypes();
    assertEquals(6, deprecatedTypes.size()); // Check the number of types in the group

    DiagnosticGroup checkTypesGroup = groups.get("checkTypes");
    assertTrue(checkTypesGroup instanceof DiagnosticGroup);
    assertTrue(checkTypesGroup instanceof DiagnosticGroupWithTypes);
  }

  @org.junit.Test
  public void testSettingWarningLevels() throws IOException {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    List<String> diagnosticGroupsToEnable = Arrays.asList(
        "accessControls", "checkRegExp", "checkTypes", "checkVars");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToEnable, CheckLevel.WARNING);

    assertTrue(options.getWarningLevel(ACCESS_CONTROLS) == CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(CHECK_REGEXP) == CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(CHECK_TYPES) == CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(CHECK_VARS) == CheckLevel.WARNING);
  }
}