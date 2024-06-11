import static org.junit.Assert.*;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.ImmutableMap.of;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticTypes;

import org.junit.Test;

public class DiagnosticGroupsTest {

 private DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

 @Test
 public void testGetRegisteredGroups() {
  Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
  Map<String, DiagnosticGroup> expectedGroups = newHashMap();

  expectedGroups.put("accessControls", ACCESS_CONTROLS);
  expectedGroups.put("ambiguousFunctionDecl", AMBIGUOUS_FUNCTION_DECL);
  expectedGroups.put("checkRegExp", CHECK_REGEXP);
  expectedGroups.put("checkTypes", CHECK_TYPES);
  expectedGroups.put("constantProperty", CONSTANT_PROPERTY);
  expectedGroups.put("externsValidation", EXTERNS_VALIDATION);
  expectedGroups.put("fileoverviewTags", FILEOVERVIEW_JSDOC);
  expectedGroups.put("globalThis", GLOBAL_THIS);
  expectedGroups.put("invalidCasts", INVALID_CASTS);
  expectedGroups.put("missingProperties", MISSING_PROPERTIES);
  expectedGroups.put("nonStandardJsDocs", NON_STANDARD_JSDOC);
  expectedGroups.put("strictModuleDepCheck", STRICT_MODULE_DEP_CHECK);
  expectedGroups.put("typeInvalidation", TYPE_INVALIDATION);
  expectedGroups.put("undefinedVars", UNDEFINED_VARIABLES);
  expectedGroups.put("visibility", VISIBILITY);

  assertEquals(expectedGroups, registeredGroups);
 }

 @Test
 public void testFindGroupByName() {
  DiagnosticGroup ambiguousFunctionDecl = diagnosticGroups.forName("ambiguousFunctionDecl");
  DiagnosticGroup constantProperty = diagnosticGroups.forName("constantProperty");

  assertNotNull(ambiguousFunctionDecl);
  assertNotNull(constantProperty);
 }

 @Test(expected = NullPointerException.class)
 public void testFindNullGroupByName() {
  diagnosticGroups.forName(null);
 }

 @Test
 public void testRegisterGroup() {
  String customGroupName = "customGroup";
  DiagnosticGroup customGroup = new DiagnosticGroup(customGroupName, DiagnosticTypes.WARNING);

  diagnosticGroups.registerGroup(customGroupName, customGroup);

  DiagnosticGroup registeredGroup = diagnosticGroups.forName(customGroupName);
  assertEquals(customGroup, registeredGroup);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterNullGroup() {
  diagnosticGroups.registerGroup(null, DiagnosticTypes.WARNING);
 }

 @Test
 public void testRegisterGroupWithTypes() {
  String customGroupName1 = "customGroup1";
  DiagnosticType customType1 = DiagnosticTypes.WARNING;
  String customGroupName2 = "customGroup2";
  DiagnosticType customType2 = DiagnosticTypes.ERROR;

  DiagnosticGroup customGroup1 = diagnosticGroups.registerGroup(customGroupName1, customType1);
  DiagnosticGroup customGroup2 = diagnosticGroups.registerGroup(customGroupName2, customType2);

  DiagnosticGroup registeredGroup1 = diagnosticGroups.forName(customGroupName1);
  DiagnosticGroup registeredGroup2 = diagnosticGroups.forName(customGroupName2);

  assertEquals(customGroup1, registeredGroup1);
  assertEquals(customGroup2, registeredGroup2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterGroupWithNullTypes() {
  String customGroupName = "customGroup";
  DiagnosticType[] nullTypes = null;

  diagnosticGroups.registerGroup(customGroupName, nullTypes);
 }

 @Test
 public void testRegisterGroupWithGroups() {
  String parentGroupName = "parentGroup";
  DiagnosticGroup parentGroup = new DiagnosticGroup(parentGroupName, DiagnosticTypes.WARNING);
  String childGroupName1 = "childGroup1";
  DiagnosticGroup childGroup1 = new DiagnosticGroup(childGroupName1, DiagnosticTypes.WARNING);
  String childGroupName2 = "childGroup2";
  DiagnosticGroup childGroup2 = new DiagnosticGroup(childGroupName2, DiagnosticTypes.ERROR);

  diagnosticGroups.registerGroup(parentGroupName, parentGroup);
  diagnosticGroups.registerGroup(parentGroupName, childGroup1);
  diagnosticGroups.registerGroup(parentGroupName, childGroup2);

  DiagnosticGroup registeredParentGroup = diagnosticGroups.forName(parentGroupName);
  DiagnosticGroup registeredChildGroup1 = diagnosticGroups.forName(childGroupName1);
  DiagnosticGroup registeredChildGroup2 = diagnosticGroups.forName(childGroupName2);

  assertEquals(parentGroup, registeredParentGroup);
  assertEquals(childGroup1, registeredChildGroup1);
  assertEquals(childGroup2, registeredChildGroup2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterGroupWithNullGroup() {
  String parentGroupName = "parentGroup";
  DiagnosticGroup nullGroup = null;

  diagnosticGroups.registerGroup(parentGroupName, nullGroup);
 }
}