package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticTypes;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(23, registeredGroups.size());

    // Check that all registered groups are present
    List<String> groupNames = ImmutableList.of(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "constantProperty", "deprecated", "externsValidation", "fileoverviewTags",
        "globalThis", "invalidCasts", "missingProperties", "nonStandardJsDocs",
        "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "visibility",
        "internetExplorerChecks", "tweakValidation", "uselessCode"
    );

    for (String groupName : groupNames) {
      DiagnosticGroup group = diagnosticGroups.forName(groupName);
      checkNotNull(group, "Missing diagnostic group: " + groupName);
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test known groups
    assertEquals(DEPRECATED, diagnosticGroups.forName("deprecated"));
    assertEquals(VISIBILITY, diagnosticGroups.forName("visibility"));
    assertEquals(CONSTANT_PROPERTY, diagnosticGroups.forName("constantProperty"));
    // ... test all groups

    // Test unknown group
    assertNull(diagnosticGroups.forName("unknownGroup"));
  }
}