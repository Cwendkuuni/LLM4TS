package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> groupNames = Lists.newArrayList(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp",
        "checkTypes", "checkVars", "constantProperty", "deprecated",
        "externsValidation", "fileoverviewTags", "globalThis",
        "internetExplorerChecks", "invalidCasts", "missingProperties",
        "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation",
        "undefinedVars", "uselessCode", "visibility"
    );

    for (String groupName : groupNames) {
      DiagnosticGroup group = registeredGroups.get(groupName);
      checkNotNull(group, "Missing diagnostic group: " + groupName);
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertEquals(AccessControls.class, accessControls.getClass());

    DiagnosticGroup checkTypes = diagnosticGroups.forName("checkTypes");
    assertEquals(CheckTypes.class, checkTypes.getClass());

    DiagnosticGroup unknown = diagnosticGroups.forName("unknown");
    assertNull(unknown);
  }
}