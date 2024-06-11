import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckerFactory.CheckerName;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetGroups() {
    DiagnosticGroup globalThisGroup = DiagnosticGroups.registerGroup("globalThis", DiagnosticType.GLOBAL_THIS);
    DiagnosticGroup deprecatedGroup = DiagnosticGroups.registerGroup("deprecated",
        DiagnosticType.DEPRECATED_NAME,
        DiagnosticType.DEPRECATED_NAME_REASON,
        DiagnosticType.DEPRECATED_PROP,
        DiagnosticType.DEPRECATED_PROP_REASON,
        DiagnosticType.DEPRECATED_CLASS,
        DiagnosticType.DEPRECATED_CLASS_REASON);
    DiagnosticGroup visibilityGroup = DiagnosticGroups.registerGroup("visibility",
        DiagnosticType.BAD_PRIVATE_GLOBAL_ACCESS,
        DiagnosticType.BAD_PRIVATE_PROPERTY_ACCESS,
        DiagnosticType.BAD_PROTECTED_PROPERTY_ACCESS,
        DiagnosticType.PRIVATE_OVERRIDE,
        DiagnosticType.VISIBILITY_MISMATCH);

    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();

    assertNotNull(registeredGroups);
    assertEquals(3, registeredGroups.size());

    DiagnosticGroup globalThis = registeredGroups.get("globalThis");
    assertEquals(globalThisGroup, globalThis);

    DiagnosticGroup deprecated = registeredGroups.get("deprecated");
    assertEquals(deprecatedGroup, deprecated);

    DiagnosticGroup visibility = registeredGroups.get("visibility");
    assertEquals(visibilityGroup, visibility);
  }

  @Test
  public void testGetDiagnosticGroupByName() {
    DiagnosticGroup globalThisGroup = DiagnosticGroups.registerGroup("globalThis", DiagnosticType.GLOBAL_THIS);
    DiagnosticGroup deprecatedGroup = DiagnosticGroups.registerGroup("deprecated",
        DiagnosticType.DEPRECATED_NAME,
        DiagnosticType.DEPRECATED_NAME_REASON,
        DiagnosticType.DEPRECATED_PROP,
        DiagnosticType.DEPRECATED_PROP_REASON,
        DiagnosticType.DEPRECATED_CLASS,
        DiagnosticType.DEPRECATED_CLASS_REASON);
    DiagnosticGroup visibilityGroup = DiagnosticGroups.registerGroup("visibility",
        DiagnosticType.BAD_PRIVATE_GLOBAL_ACCESS,
        DiagnosticType.BAD_PRIVATE_PROPERTY_ACCESS,
        DiagnosticType.BAD_PROTECTED_PROPERTY_ACCESS,
        DiagnosticType.PRIVATE_OVERRIDE,
        DiagnosticType.VISIBILITY_MISMATCH);

    DiagnosticGroup globalThis = DiagnosticGroups.forName("globalThis");
    DiagnosticGroup deprecated = DiagnosticGroups.forName("deprecated");
    DiagnosticGroup visibility = DiagnosticGroups.forName("visibility");

    assertNotNull(globalThis);
    assertEquals(globalThisGroup, globalThis);

    assertNotNull(deprecated);
    assertEquals(deprecatedGroup, deprecated);

    assertNotNull(visibility);
    assertEquals(visibilityGroup, visibility);
  }

  @Test
  public void testDisplayedGroups() {
    String displayedGroups = DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES;

    String[] groupNames = displayedGroups.split(", ");

    assertEquals(21, groupNames.length);

    for (String groupName : groupNames) {
      DiagnosticGroup group = DiagnosticGroups.forName(groupName);
      assertNotNull(group);
    }
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroup accessControlsGroup = DiagnosticGroups.registerGroup("accessControls",
        DiagnosticType.DEPRECATED_NAME,
        DiagnosticType.DEPRECATED_NAME_REASON,
        DiagnosticType.DEPRECATED_PROP,
        DiagnosticType.DEPRECATED_PROP_REASON,
        DiagnosticType.DEPRECATED_CLASS,
        DiagnosticType.DEPRECATED_CLASS_REASON,
        DiagnosticType.BAD_PRIVATE_GLOBAL_ACCESS,
        DiagnosticType.BAD_PRIVATE_PROPERTY_ACCESS,
        DiagnosticType.BAD_PROTECTED_PROPERTY_ACCESS,
        DiagnosticType.PRIVATE_OVERRIDE,
        DiagnosticType.VISIBILITY_MISMATCH);

    DiagnosticGroup accessControls = DiagnosticGroups.forName("accessControls");
    assertEquals(accessControlsGroup, accessControls);
  }

  @Test
  public void testRegisterGroupWithDiagnosticGroups() {
    DiagnosticGroup accessControlsGroup = DiagnosticGroups.registerGroup("accessControls",
        DiagnosticType.DEPRECATED_NAME,
        DiagnosticType.DEPRECATED_NAME_REASON,
        DiagnosticType.DEPRECATED_PROP,
        DiagnosticType.DEPRECATED_PROP_REASON,
        DiagnosticType.DEPRECATED_CLASS,
        DiagnosticType.DEPRECATED_CLASS_REASON);

    DiagnosticGroup deprecatedGroup = DiagnosticGroups.registerGroup("deprecated",
        DiagnosticType.DEPRECATED_NAME,
        DiagnosticType.DEPRECATED_NAME_REASON,
        DiagnosticType.DEPRECATED_PROP,
        DiagnosticType.DEPRECATED_PROP_REASON,
        DiagnosticType.DEPRECATED_CLASS,
        DiagnosticType.DEPRECATED_CLASS_REASON);

    DiagnosticGroup accessControls = DiagnosticGroups.registerGroup("accessControls", accessControlsGroup, deprecatedGroup);

    DiagnosticGroup accessControls2 = DiagnosticGroups.forName("accessControls");
    assertEquals(accessControlsGroup, accessControls2);
  }
}