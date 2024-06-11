package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroupsArePresent() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> groupsToCheck = List.of(
        "accessControls",
        "ambiguousFunctionDecl",
        "checkRegExp",
        "checkTypes",
        "constantProperty",
        "deprecated",
        "externsValidation",
        "fileoverviewTags",
        "globalThis",
        "internetExplorerChecks",
        "invalidCasts",
        "missingProperties",
        "nonStandardJsDocs",
        "strictModuleDepCheck",
        "typeInvalidation",
        "unknownDefines",
        "uselessCode",
        "visibility"
    );

    for (String groupName : groupsToCheck) {
      DiagnosticGroup group = diagnosticGroups.forName(groupName);
      assertNotNull(group, "Diagnostic group [" + groupName + "] is not registered.");
      assertTrue(registeredGroups.containsKey(groupName),
          "Diagnostic group [" + groupName + "] is not in the registered groups map.");
    }
  }
}