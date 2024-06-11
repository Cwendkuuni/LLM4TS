import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    // Test registering a single diagnostic type
    String name1 = "singleType";
    DiagnosticGroup singleTypeGroup = new DiagnosticGroup(name1, CheckGlobalThis.GLOBAL_THIS);
    diagnosticGroups.registerGroup(name1, singleTypeGroup);
    assertNotNull(registeredGroups.get(name1));

    // Test registering multiple diagnostic types
    String name2 = "multipleTypes";
    DiagnosticGroup multipleTypesGroup = new DiagnosticGroup(name2,
        CheckAccessControls.DEPRECATED_NAME, CheckAccessControls.DEPRECATED_NAME_REASON);
    diagnosticGroups.registerGroup(name2, multipleTypesGroup.getDiagnosticTypes());
    assertNotNull(registeredGroups.get(name2));

    // Test registering a group with multiple sub-groups
    String name3 = "groupWithSubGroups";
    DiagnosticGroup groupWithSubGroups = new DiagnosticGroup(name3,
        new DiagnosticGroup[] { DEPRECATED, VISIBILITY });
    diagnosticGroups.registerGroup(name3, groupWithSubGroups);
    assertNotNull(registeredGroups.get(name3));
    assertNotNull(registeredGroups.get(name3).getDiagnosticGroups());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    String name = "visibility";
    DiagnosticGroup visibilityGroup = diagnosticGroups.forName(name);
    assertNotNull(visibilityGroup);
    assertNotNull(visibilityGroup.getDiagnosticTypes());
  }
}