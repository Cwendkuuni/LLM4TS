package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertEquals(24, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNull() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupStringDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticType deprecatedName = new DiagnosticType("deprecatedName");
    DiagnosticGroup deprecated = diagnosticGroups.registerGroup("deprecated", deprecatedName);
    assertNotNull(deprecated);
    assertEquals("deprecated", deprecated.getName());
    assertEquals(deprecatedName, deprecated.getDiagnostics().get(0));
  }

  @Test
  public void testRegisterGroupStringDiagnosticTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticType deprecatedName = new DiagnosticType("deprecatedName");
    DiagnosticType deprecatedNameReason = new DiagnosticType("deprecatedNameReason");
    DiagnosticGroup deprecated = diagnosticGroups.registerGroup(
        "deprecated", deprecatedName, deprecatedNameReason);
    assertNotNull(deprecated);
    assertEquals("deprecated", deprecated.getName());
    assertEquals(2, deprecated.getDiagnostics().size());
    assertEquals(deprecatedName, deprecated.getDiagnostics().get(0));
    assertEquals(deprecatedNameReason, deprecated.getDiagnostics().get(1));
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = new DiagnosticGroup("deprecated", new DiagnosticType[0]);
    diagnosticGroups.registerGroup("deprecated", deprecated);
    assertNotNull(diagnosticGroups.forName("deprecated"));
    assertEquals(deprecated, diagnosticGroups.forName("deprecated"));
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = new DiagnosticGroup("deprecated", new DiagnosticGroup[0]);
    DiagnosticGroup accessControls = new DiagnosticGroup("accessControls", new DiagnosticGroup[0]);
    diagnosticGroups.registerGroup("deprecated", deprecated);
    diagnosticGroups.registerGroup("accessControls", accessControls);
    assertNotNull(diagnosticGroups.forName("deprecated"));
    assertEquals(deprecated, diagnosticGroups.forName("deprecated"));
    assertNotNull(diagnosticGroups.forName("accessControls"));
    assertEquals(accessControls, diagnosticGroups.forName("accessControls"));
  }

  @Test
  public void testDiagnosticGroupNameList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupsList = List.of("deprecated", "accessControls");
    diagnosticGroups.setWarningLevels(new CompilerOptions(), diagnosticGroupsList, CheckLevel.WARNING);
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertTrue(deprecated.getWarningLevel() >= CheckLevel.WARNING);
    assertTrue(accessControls.getWarningLevel() >= CheckLevel.WARNING);
  }

  @Test
  public void testDiagnosticGroupNameNull() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupsList = List.of((String) null);
    try {
      diagnosticGroups.setWarningLevels(new CompilerOptions(), diagnosticGroupsList, CheckLevel.WARNING);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }
}