import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroup;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(21, registeredGroups.size());
    // Add some assertions for specific groups here, for example:
    DiagnosticGroup accessControlsGroup = registeredGroups.get("accessControls");
    assertNotNull(accessControlsGroup);
    assertEquals(9, accessControlsGroup.getDiagnosticTypes().size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup globalThisGroup = diagnosticGroups.forName("GLOBAL_THIS");
    assertNotNull(globalThisGroup);
    assertEquals(1, globalThisGroup.getDiagnosticTypes().size());
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsToEnable = ImmutableList.of("accessControls", "checkTypes");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToEnable, CheckLevel.WARNING);
    assertTrue(options.hasWarning(DiagnosticGroup.ACCESS_CONTROLS, DiagnosticType.DEPRECATED_PROP));
    assertTrue(options.hasWarning(DiagnosticGroup.CHECK_TYPES, DiagnosticType.TYPE_MISMATCH));
  }
}