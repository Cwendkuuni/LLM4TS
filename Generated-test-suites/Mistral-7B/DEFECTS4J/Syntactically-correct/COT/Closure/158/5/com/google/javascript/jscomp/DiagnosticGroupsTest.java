import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

 private static DiagnosticGroups diagnosticGroups;

 @BeforeClass
 public static void setup() {
 diagnosticGroups = new DiagnosticGroups();
 }

 @Test
 public void testRegisterGroup() {
 // Test registering a group with a diagnostic type
 DiagnosticType diagnosticType = new DiagnosticType("TestType");
 DiagnosticGroup group = diagnosticGroups.registerGroup("TestGroup", diagnosticType);
 assertNotNull(group);
 assertTrue(group.containsDiagnosticType(diagnosticType));

 // Test registering a group with multiple diagnostic types
 List<DiagnosticType> diagnosticTypes = ImmutableList.of(new DiagnosticType("Type1"), new DiagnosticType("Type2"));
 group = diagnosticGroups.registerGroup("TestGroup2", diagnosticTypes.toArray(new DiagnosticType[0]));
 assertNotNull(group);
 for (DiagnosticType diagnosticType1 : diagnosticTypes) {
 assertTrue(group.containsDiagnosticType(diagnosticType1));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterGroupWithNullName() {
 diagnosticGroups.registerGroup(null, new DiagnosticType("TestType"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterGroupWithEmptyName() {
 diagnosticGroups.registerGroup("", new DiagnosticType("TestType"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterGroupWithNullDiagnosticType() {
 diagnosticGroups.registerGroup("TestGroup3", null);
 }

 @Test
 public void testGetRegisteredGroups() {
 Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
 assertTrue(registeredGroups.size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForNameWithNullName() {
 diagnosticGroups.forName(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForNameWithEmptyName() {
 diagnosticGroups.forName("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForNameWithUnknownName() {
 diagnosticGroups.forName("UnknownName");
 }

 @Test
 public void testDiagnosticGroupsInit() {
 DiagnosticGroup globalThis = diagnosticGroups.forName("globalThis");
 DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
 DiagnosticGroup visibility = diagnosticGroups.forName("visibility");
 // Add more assertions here based on the available diagnostic groups
 }
}