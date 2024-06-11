import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  @Test
  public void testDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test registering a diagnostic group with a single diagnostic type
    DiagnosticGroup constantPropertyGroup = diagnosticGroups.registerGroup(
        "constantProperty", DiagnosticType.CONST_PROPERTY_REASSIGNED_VALUE);
    assertNotNull(constantPropertyGroup);
    assertNull(constantPropertyGroup.getGroups());
    assertNull(constantPropertyGroup.getTypes());
    assertNull(constantPropertyGroup.getName());

    // Test registering a diagnostic group with multiple diagnostic types
    DiagnosticGroup accessControlsGroup = diagnosticGroups.registerGroup(
        "accessControls",
        DiagnosticType.DEPRECATED_NAME,
        DiagnosticType.DEPRECATED_NAME_REASON,
        DiagnosticType.DEPRECATED_PROP,
        DiagnosticType.DEPRECATED_PROP_REASON,
        DiagnosticType.DEPRECATED_CLASS,
        DiagnosticType.DEPRECATED_CLASS_REASON);
    assertNotNull(accessControlsGroup);
    List<DiagnosticType> accessControlsTypes = ImmutableList.copyOf(accessControlsGroup.getTypes());
    List<DiagnosticGroup> accessControlsGroups = ImmutableList.copyOf(accessControlsGroup.getGroups());
    assertNull(accessControlsGroup.getName());
    for (DiagnosticType diagnosticType : accessControlsTypes) {
      assertNotNull(diagnosticType);
    }
    for (DiagnosticGroup diagnosticGroup : accessControlsGroups) {
      assertNotNull(diagnosticGroup);
    }

    // Test getting registered diagnostic groups
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    DiagnosticGroup globalThisGroup = registeredGroups.get("globalThis");
    assertNotNull(globalThisGroup);
    DiagnosticGroup deprecatedGroup = registeredGroups.get("deprecated");
    assertNotNull(deprecatedGroup);

    // Test finding a diagnostic group by name
    DiagnosticGroup findGroupName = diagnosticGroups.forName("accessControls");
    assertNotNull(findGroupName);
    DiagnosticGroup findNonExistentGroupName = diagnosticGroups.forName("nonexistentgroup");
    assertNull(findNonExistentGroupName);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, DiagnosticType.DEPRECATED_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("name", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("name", null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("", DiagnosticType.DEPRECATED_NAME);
  }

  @Test(expected = IllegalStateException.class)
  public void testRegisterGroupWithDuplicateName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("name", DiagnosticType.DEPRECATED_NAME);
    diagnosticGroups.registerGroup("name", DiagnosticType.DEPRECATED_NAME_REASON);
  }
}