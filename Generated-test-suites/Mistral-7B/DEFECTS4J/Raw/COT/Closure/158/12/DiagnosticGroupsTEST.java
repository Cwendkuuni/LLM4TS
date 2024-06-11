import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import org.junit.Test;

public class DiagnosticGroupsTest {

 private DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

 @Test
 public void testGetRegisteredGroups() {
 Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
 assertNotNull(registeredGroups);
 }

 @Test
 public void testForName() {
 DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
 assertNotNull(deprecated);
 }

 @Test(expected = NullPointerException.class)
 public void testForNameNullName() {
 diagnosticGroups.forName(null);
 }

 @Test
 public void testRegisterGroupStringDiagnosticType() {
 DiagnosticGroup constantProperty = diagnosticGroups.registerGroup("constantProperty",
 DiagnosticType.CONST_PROPERTY_REASSIGNED_VALUE);
 DiagnosticGroup registeredConstantProperty = diagnosticGroups.forName("constantProperty");
 assertNotNull(registeredConstantProperty);
 assertTrue(registeredConstantProperty.contains(DiagnosticType.CONST_PROPERTY_REASSIGNED_VALUE));
 }

 @Test
 public void testRegisterGroupStringDiagnosticGroup() {
 DiagnosticGroup accessControls = diagnosticGroups.registerGroup("accessControls");
 DiagnosticGroup registeredAccessControls = diagnosticGroups.forName("accessControls");
 assertNotNull(registeredAccessControls);
 assertTrue(registeredAccessControls.contains(DiagnosticType.DEPRECATED_NAME));
 }

 @Test
 public void testRegisterGroupStringDiagnosticGroups() {
 DiagnosticGroup accessControls = diagnosticGroups.registerGroup("accessControls",
 DiagnosticGroup.DEPRECATED, DiagnosticGroup.VISIBILITY);
 DiagnosticGroup registeredAccessControls = diagnosticGroups.forName("accessControls");
 assertNotNull(registeredAccessControls);
 assertTrue(registeredAccessControls.contains(DiagnosticGroup.DEPRECATED));
 assertTrue(registeredAccessControls.contains(DiagnosticGroup.VISIBILITY));
 }

 @Test(expected = NullPointerException.class)
 public void testRegisterGroupNullName() {
 diagnosticGroups.registerGroup(null, DiagnosticType.DEPRECATED_NAME);
 }

 @Test(expected = NullPointerException.class)
 public void testRegisterGroupNullGroup() {
 diagnosticGroups.registerGroup("accessControls", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterGroupDuplicateName() {
 diagnosticGroups.registerGroup("accessControls", DiagnosticType.DEPRECATED_NAME);
 diagnosticGroups.registerGroup("accessControls", DiagnosticType.DEPRECATED_NAME);
 }

 @Test
 public void testDiagnosticGroupNames() {
 String diagnosticGroupNames = diagnosticGroups.DIAGNOSTIC_GROUP_NAMES;
 String[] groupNamesArray = diagnosticGroupNames.split(", ");
 for (String groupName : groupNamesArray) {
 DiagnosticGroup group = diagnosticGroups.forName(groupName);
 assertNotNull(group);
 }
 }
}