import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;
import com.google.javascript.jscomp.CompilerOptions;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    // Test registration of diagnostic groups
    assertEquals(36, diagnosticGroups.getRegisteredGroups().size());

    // Test retrieval of registered diagnostic groups by name
    DiagnosticGroup globalThis = diagnosticGroups.forName("globalThis");
    assertNotNull(globalThis);
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertNotNull(accessControls);

    // Test setting warning levels for registered diagnostic groups
    List<String> diagnosticGroupsToEnable = ImmutableList.of("deprecated", "accessControls");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToEnable, CheckLevel.WARNING);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testNullDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    String nullGroupName = null;
    diagnosticGroups.setWarningLevels(options, ImmutableList.of(nullGroupName), CheckLevel.WARNING);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testInvalidDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    String invalidGroupName = "nonExistentGroup";
    diagnosticGroups.setWarningLevels(options, ImmutableList.of(invalidGroupName), CheckLevel.WARNING);
  }
}