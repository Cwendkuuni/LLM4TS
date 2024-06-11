import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals("size", 17, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("ACCESS_CONTROLS");
    assertNotNull("group is null", group);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupStringDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticType diagnosticType = new DiagnosticType(1, "TEST_DIAGNOSTIC");
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP", diagnosticType);
    assertNotNull("group is null", group);
    assertEquals("group name", "TEST_GROUP", group.getName());
    assertEquals("diagnostic type", diagnosticType, group.getDiagnostics().get(0));
  }

  @Test
  public void testRegisterGroupStringDiagnosticTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticType diagnosticType1 = new DiagnosticType(1, "TEST_DIAGNOSTIC1");
    DiagnosticType diagnosticType2 = new DiagnosticType(2, "TEST_DIAGNOSTIC2");
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP", diagnosticType1, diagnosticType2);
    assertNotNull("group is null", group);
    assertEquals("group name", "TEST_GROUP", group.getName());
    assertEquals("number of diagnostics", 2, group.getDiagnostics().size());
    assertEquals("first diagnostic", diagnosticType1, group.getDiagnostics().get(0));
    assertEquals("second diagnostic", diagnosticType2, group.getDiagnostics().get(1));
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = new DiagnosticGroup("GROUP1", ImmutableMap.of());
    DiagnosticGroup group2 = new DiagnosticGroup("GROUP2", ImmutableMap.of());
    DiagnosticGroup newGroup = diagnosticGroups.registerGroup("TEST_GROUP", group1, group2);
    assertNotNull("group is null", newGroup);
    assertEquals("group name", "TEST_GROUP", newGroup.getName());
    assertEquals("number of groups", 2, newGroup.getGroups().size());
    assertEquals("first group", group1, newGroup.getGroups().get(0));
    assertEquals("second group", group2, newGroup.getGroups().get(1));
  }

  @Test
  public void testRegisteredGroupsToString() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    String expected = "{accessControls, ambiguousFunctionDecl, checkRegExp, checkTypes, checkVars, constantProperty, deprecated, fileoverviewTags, globalThis, internetsExplorerChecks, invalidCasts, missingProperties, nonStandardJsDocs, strictModuleDepCheck, typeInvalidation, undefinedVars, uselessCode}";
    assertEquals("registered groups string", expected, diagnosticGroups.DIAGNOSTIC_GROUP_NAMES);
  }

  @Test
  public void testGetDiagnosticsForNamedGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("CHECK_TYPES");
    List<DiagnosticType> diagnostics = group.getDiagnostics();
    assertEquals("number of diagnostics", 2, diagnostics.size());
    assertEquals("first diagnostic", TypeValidator.ALL_DIAGNOSTICS, diagnostics.get(0));
    assertEquals("second diagnostic", TypeCheck.ALL_DIAGNOSTICS, diagnostics.get(1));
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsToSet = ImmutableList.of("CHECK_VARIABLES", "CHECK_USELESS_CODE");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToSet, CheckLevel.WARNING);
    assertEquals("warning level for CHECK_VARIABLES", CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_VARIABLES));
    assertEquals("warning level for CHECK_USELESS_CODE", CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_USELESS_CODE));
    assertEquals("warning level for other group", CheckLevel.ERROR, options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
  }
}