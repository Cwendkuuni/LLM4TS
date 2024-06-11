package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> diagnosticGroupNames = List.of("accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes", "checkVars", "constantProperty", "deprecated", "externsValidation", "fileoverviewTags", "globalThis", "invalidCasts", "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "unknownDefines", "uselessCode", "visibility");

    for (String name : diagnosticGroupNames) {
      DiagnosticGroup group = registeredGroups.get(name);
      assertNotNull(group, "Missing registered diagnostic group: " + name);
      assertTrue(group instanceof DiagnosticGroup, "Invalid registered diagnostic group class: " + name);
    }
  }

  @Test
  public void testGetGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecatedGroup, "Missing diagnostic group: deprecated");
  }

  @Test
  public void testSetWarningLevels() {
    // Set up test environment
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    List<String> diagnosticGroupsToSet = List.of("checkTypes", "checkVars");
    DiagnosticGroups.WarningLevel level = DiagnosticGroups.WarningLevel.WARNING;

    // Test method call
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToSet, level);

    // Assert warning levels have been set correctly
    for (String diagnosticGroupName : diagnosticGroupsToSet) {
      DiagnosticGroup group = diagnosticGroups.forName(diagnosticGroupName);
      assertTrue(options.getWarningLevel(group) == level, "Warning level not set correctly for group: " + diagnosticGroupName);
    }
  }
}