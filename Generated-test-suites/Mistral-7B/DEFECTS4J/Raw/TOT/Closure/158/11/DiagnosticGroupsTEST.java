import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;

public class DiagnosticGroupsTest {

 @Test
 public void testRegisterAndGetDiagnosticGroups() {
 DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

 // Test registering a diagnostic group with a name and diagnostic types.
 DiagnosticGroup globalThisGroup = diagnosticGroups.registerGroup("globalThis", DiagnosticGroups.CheckGlobalThis.GLOBAL_THIS);
 assertNotNull(globalThisGroup);

 // Test getting the registered diagnostic groups.
 Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
 assertNotNull(registeredGroups);
 assertNotNull(registeredGroups.get("globalThis"));
 }

 @Test
 public void testRegisterGroupWithMultipleTypes() {
 DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

 // Test registering a diagnostic group with multiple diagnostic types.
 DiagnosticGroup deprecatedGroup = diagnosticGroups.registerGroup("deprecated",
 DiagnosticGroups.CheckAccessControls.DEPRECATED_NAME,
 DiagnosticGroups.CheckAccessControls.DEPRECATED_NAME_REASON,
 DiagnosticGroups.CheckAccessControls.DEPRECATED_PROP,
 DiagnosticGroups.CheckAccessControls.DEPRECATED_PROP_REASON,
 DiagnosticGroups.CheckAccessControls.DEPRECATED_CLASS,
 DiagnosticGroups.CheckAccessControls.DEPRECATED_CLASS_REASON);
 assertNotNull(deprecatedGroup);

 // Test getting the registered diagnostic groups.
 Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
 assertNotNull(registeredGroups);
 assertNotNull(registeredGroups.get("deprecated"));
 }

 @Test
 public void testRegisterGroupWithOtherGroups() {
 DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

 // Test registering a diagnostic group with other diagnostic groups.
 DiagnosticGroup accessControlsGroup = diagnosticGroups.registerGroup("accessControls",
 DiagnosticGroups.DEPRECATED,
 DiagnosticGroups.VISIBILITY);
 assertNotNull(accessControlsGroup);

 // Test getting the registered diagnostic groups.
 Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
 assertNotNull(registeredGroups);
 assertNotNull(registeredGroups.get("accessControls"));
 assertNotNull(registeredGroups.get("accessControls").get("deprecated"));
 assertNotNull(registeredGroups.get("accessControls").get("visibility"));
 }
}