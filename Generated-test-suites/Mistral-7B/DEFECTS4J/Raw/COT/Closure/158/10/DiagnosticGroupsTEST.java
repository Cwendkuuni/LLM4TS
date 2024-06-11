import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.common.collect.ImmutableMap;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("ACCESS_CONTROLS");
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    try {
      diagnosticGroups.registerGroup(null, DiagnosticType.class);
    } catch (NullPointerException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testRegisterGroupWithEmptyName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    try {
      diagnosticGroups.registerGroup("", DiagnosticType.class);
    } catch (IllegalArgumentException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testRegisterGroupWithExistingName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("ACCESS_CONTROLS", DiagnosticType.class);
    try {
      diagnosticGroups.registerGroup("ACCESS_CONTROLS", DiagnosticType.class);
    } catch (IllegalArgumentException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableMap.of(
        "ACCESS_CONTROLS", "ignore").keySet().stream().collect(Collectors.toList());
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    assertNotNull(options.getWarningLevel("ACCESS_CONTROLS"));
  }

  @Test
  public void testSetWarningLevelsWithNullOptions() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupsList = ImmutableMap.of(
        "ACCESS_CONTROLS", "ignore").keySet().stream().collect(Collectors.toList());
    try {
      diagnosticGroups.setWarningLevels(null, diagnosticGroupsList, CheckLevel.WARNING);
    } catch (NullPointerException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testSetWarningLevelsWithNullDiagnosticGroupsList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    try {
      diagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING);
    } catch (NullPointerException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testSetWarningLevelsWithEmptyDiagnosticGroupsList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = Collections.emptyList();
    try {
      diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    } catch (IllegalArgumentException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testSetWarningLevelsWithInvalidDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableMap.of("INVALID_GROUP", "ignore").keySet().stream().collect(Collectors.toList());
    try {
      diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    } catch (IllegalArgumentException e) {
      assertNotNull(e.getMessage());
    }
  }
}