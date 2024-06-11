import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  private static DiagnosticGroups diagnosticGroups;
  private static CompilerOptions options;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups();
    options = new CompilerOptions();
  }

  @Test
  public void testRegisterAndGetDiagnosticGroups() {
    DiagnosticGroup globalThisGroup = diagnosticGroups.registerGroup("globalThis", CheckGlobalThis.GLOBAL_THIS);
    assertEquals("globalThis", globalThisGroup.getName());

    DiagnosticGroup deprecatedGroup = diagnosticGroups.registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    assertEquals("deprecated", deprecatedGroup.getName());

    DiagnosticGroup visibilityGroup = diagnosticGroups.registerGroup("visibility",
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH);
    assertEquals("visibility", visibilityGroup.getName());

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(11, registeredGroups.size());
  }

  @Test
  public void testGetDiagnosticGroupByName() {
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecatedGroup);

    DiagnosticGroup unknownGroup = diagnosticGroups.forName("unknown");
    assertNull(unknownGroup);
  }

  @Test
  public void testSetWarningLevels() {
    List<String> diagnosticGroupsToSet = ImmutableList.of("deprecated", "visibility");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToSet, CheckLevel.WARNING);

    assertTrue(options.hasWarning(CheckAccessControls.DEPRECATED_NAME));
    assertTrue(options.hasWarning(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS));
  }
}