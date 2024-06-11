import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class DiagnosticGroupsTest {

  @Test
  public void testDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test getRegisteredGroups method
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(20, registeredGroups.size()); // Number of registered groups

    // Test forName method
    DiagnosticGroup globalThis = diagnosticGroups.forName("globalThis");
    assertNotNull(globalThis);

    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertNotNull(accessControls);

    DiagnosticGroup checkTypes = diagnosticGroups.forName("checkTypes");
    assertNotNull(checkTypes);

    // Test registerGroup method with DiagnosticType arguments
    DiagnosticGroup customGroup1 = diagnosticGroups.registerGroup("customGroup1",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    assertNotNull(customGroup1);

    // Test registerGroup method with DiagnosticGroup arguments
    DiagnosticGroup customGroup2 = diagnosticGroups.registerGroup("customGroup2",
        accessControls, checkTypes);
    assertNotNull(customGroup2);

    // Test DIAGNOSTIC_GROUP_NAMES constant
    String diagnosticGroupName = "customGroup3";
    String expectedDiagnosticGroupName = "customGroup3";
    String diagnosticGroupNameInConstants = "customGroup3"; // Should be the same
    assertEquals(expectedDiagnosticGroupName, diagnosticGroupName);
    assertEquals(diagnosticGroupNameInConstants, diagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(",")[diagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(",").length - 1]);
  }
}