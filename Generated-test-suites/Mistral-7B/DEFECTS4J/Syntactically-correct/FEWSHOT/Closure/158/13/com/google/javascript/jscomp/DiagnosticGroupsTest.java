package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> expectedGroups = DiagnosticGroups.getRegisteredGroups();
    Map<String, DiagnosticGroup> actualGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(actualGroups);
    assertEquals(expectedGroups.size(), actualGroups.size());
    for (Map.Entry<String, DiagnosticGroup> entry : expectedGroups.entrySet()) {
      String groupName = entry.getKey();
      DiagnosticGroup expectedGroup = entry.getValue();
      DiagnosticGroup actualGroup = actualGroups.get(groupName);
      assertEquals(expectedGroup.getClass(), actualGroup.getClass());
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup expectedGlobalThis = DiagnosticGroups.GLOBAL_THIS;
    DiagnosticGroup actualGlobalThis = diagnosticGroups.forName("globalThis");
    assertEquals(expectedGlobalThis.getClass(), actualGlobalThis.getClass());
    DiagnosticGroup nullGroup = diagnosticGroups.forName(null);
    assertNull(nullGroup);
  }

  @Test
  public void testRegisterGroup_NameAndDiagnosticTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroup";
    DiagnosticType diagnosticType1 = new DiagnosticType("TestDiagnostic1");
    DiagnosticType diagnosticType2 = new DiagnosticType("TestDiagnostic2");
    DiagnosticGroup expectedGroup = new DiagnosticGroup(name, diagnosticType1, diagnosticType2);
    DiagnosticGroup actualGroup = diagnosticGroups.registerGroup(name, diagnosticType1, diagnosticType2);
    assertEquals(expectedGroup.getName(), actualGroup.getName());
    assertEquals(expectedGroup.getDiagnosticTypes().size(), actualGroup.getDiagnosticTypes().size());
    for (DiagnosticType diagnosticType : expectedGroup.getDiagnosticTypes()) {
      assertEquals(diagnosticType, actualGroup.getDiagnosticTypes().get(diagnosticType.getName()));
    }
  }

  @Test
  public void testRegisterGroup_NameAndDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroup";
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    DiagnosticGroup expectedGroup = new DiagnosticGroup(name, group1, group2);
    DiagnosticGroup actualGroup = diagnosticGroups.registerGroup(name, group1, group2);
    assertEquals(expectedGroup.getName(), actualGroup.getName());
    assertEquals(expectedGroup.getChildGroups().size(), actualGroup.getChildGroups().size());
    for (DiagnosticGroup childGroup : expectedGroup.getChildGroups()) {
      assertEquals(childGroup, actualGroup.getChildGroups().get(childGroup.getName()));
    }
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = null;
    CheckLevel level = CheckLevel.WARNING;
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, level);
    assertNull(options.getWarningLevel(DiagnosticGroups.GLOBAL_THIS));
    assertNull(options.getWarningLevel(DiagnosticGroups.DEPRECATED));
  }

  @Test
  public void testSetWarningLevels_ValidDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = Arrays.asList("accessControls", "invalidCasts");
    CheckLevel level = CheckLevel.ERROR;
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, level);
    assertNotNull(options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
    assertNotNull(options.getWarningLevel(DiagnosticGroups.INVALID_CASTS));
  }
}