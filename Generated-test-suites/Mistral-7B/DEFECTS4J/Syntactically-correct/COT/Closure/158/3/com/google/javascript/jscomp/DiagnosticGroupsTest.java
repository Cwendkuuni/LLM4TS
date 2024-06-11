import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import org.junit.Test;

public class DiagnosticGroupsTest {

 private DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

 @Test
 public void testGetRegisteredGroups() {
 Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
 assertNotNull(groups);
 assertFalse(groups.isEmpty());
 }

 @Test
 public void testForName() {
 DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
 DiagnosticGroup visibility = diagnosticGroups.forName("visibility");
 assertNotNull(deprecated);
 assertNotNull(visibility);
 }

 @Test
 public void testRegisterGroupWithDiagnosticType() {
 DiagnosticGroup constantProperty = diagnosticGroups.registerGroup("constantProperty",
 DiagnosticType.CONST_PROPERTY_REASSIGNED_VALUE);
 assertNotNull(constantProperty);
 }

 @Test
 public void testRegisterGroupWithMultipleDiagnosticTypes() {
 DiagnosticGroup accessControls = diagnosticGroups.registerGroup("accessControls",
 DiagnosticType.DEPRECATED_NAME, DiagnosticType.DEPRECATED_NAME_REASON,
 DiagnosticType.DEPRECATED_PROP, DiagnosticType.DEPRECATED_PROP_REASON,
 DiagnosticType.DEPRECATED_CLASS, DiagnosticType.DEPRECATED_CLASS_REASON);
 assertNotNull(accessControls);
 }

 @Test
 public void testRegisterGroupWithDiagnosticGroup() {
 DiagnosticGroup visibility = new DiagnosticGroup("visibility");
 DiagnosticGroup accessControls = diagnosticGroups.registerGroup("accessControls", visibility);
 assertNotNull(accessControls);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNameWhenRegisteringGroup() {
 diagnosticGroups.registerGroup(null, "test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyNameWhenRegisteringGroup() {
 diagnosticGroups.registerGroup("", "test");
 }

 @Test
 public void testSetWarningLevels() {
 CompilerOptions options = new CompilerOptions();
 List<String> diagnosticGroupsNames = ImmutableList.of("accessControls", "checkTypes");
 diagnosticGroups.setWarningLevels(options, diagnosticGroupsNames, CheckLevel.QUIRKS);
 assertEquals(CheckLevel.QUIRKS, options.getWarningLevel(DiagnosticGroup.ACCESS_CONTROLS));
 assertEquals(CheckLevel.QUIRKS, options.getWarningLevel(DiagnosticGroup.CHECK_TYPES));
 }

}