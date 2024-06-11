package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    List<String> groupNames = Lists.newArrayList(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "checkVars", "constantProperty", "deprecated", "externsValidation",
        "fileoverviewTags", "globalThis", "internetExplorerChecks", "invalidCasts",
        "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck",
        "typeInvalidation", "undefinedVars", "uselessCode", "visibility"
    );
    checkState(registeredGroups.size() == groupNames.size(), "Unexpected number of registered diagnostic groups");
    for (String name : groupNames) {
      DiagnosticGroup group = diagnosticGroups.forName(name);
      checkState(group != null, "No diagnostic group for name: " + name);
      assertEquals(name, group.getName());
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup visibility = diagnosticGroups.forName("visibility");
    DiagnosticGroup unknown = diagnosticGroups.forName("unknown");

    assertNull(unknown);
    assertEquals("accessControls", accessControls.getName());
    assertEquals("deprecated", deprecated.getName());
    assertEquals("visibility", visibility.getName());
  }
}