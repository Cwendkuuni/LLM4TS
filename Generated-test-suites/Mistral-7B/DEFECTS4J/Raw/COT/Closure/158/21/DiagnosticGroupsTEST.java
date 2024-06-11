import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticTypes;
import org.junit.Test;

public class DiagnosticGroupsTest {

 private DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

 @Test
 public void testRegisterAndGetGroupByName() {
 DiagnosticGroup globalThisGroup = diagnosticGroups.registerGroup("globalThis", DiagnosticTypes.GLOBAL_THIS);
 DiagnosticGroup deprecatedGroup = diagnosticGroups.registerGroup("deprecated",
 DiagnosticTypes.DEPRECATED_NAME, DiagnosticTypes.DEPRECATED_NAME_REASON,
 DiagnosticTypes.DEPRECATED_PROP, DiagnosticTypes.DEPRECATED_PROP_REASON,
 DiagnosticTypes.DEPRECATED_CLASS, DiagnosticTypes.DEPRECATED_CLASS_REASON);

 DiagnosticGroup groupByName = diagnosticGroups.forName("deprecated");
 assertNotNull(groupByName);
 assertEquals(deprecatedGroup, groupByName);
 }

 @Test
 public void testGetRegisteredGroups() {
 Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
 DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");

 assertNotNull(registeredGroups);
 assertNotNull(registeredGroups.get("deprecated"));
 assertEquals(deprecatedGroup, registeredGroups.get("deprecated"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullGroupByName() {
 DiagnosticGroup nullGroup = diagnosticGroups.forName(null);
 assertNull(nullGroup);
 }

 @Test
 public void testRegisterMultipleGroups() {
 DiagnosticGroup accessControlsGroup = diagnosticGroups.registerGroup("accessControls",
 DEPRECATED, VISIBILITY);
 DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
 DiagnosticGroup visibilityGroup = diagnosticGroups.forName("visibility");

 assertNotNull(accessControlsGroup);
 assertNotNull(deprecatedGroup);
 assertNotNull(visibilityGroup);
 assertEquals(2, accessControlsGroup.getSubGroups().size());
 assertEquals(deprecatedGroup, accessControlsGroup.getSubGroups().get(0));
 assertEquals(visibilityGroup, accessControlsGroup.getSubGroups().get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterDuplicateGroup() {
 DiagnosticGroup accessControlsGroup = diagnosticGroups.registerGroup("accessControls",
 DEPRECATED, VISIBILITY);
 diagnosticGroups.registerGroup("accessControls", DEPRECATED, VISIBILITY);
 }
}