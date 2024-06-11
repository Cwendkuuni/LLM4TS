package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.CompilerOptions;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedGroups = ImmutableList.of(
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
        "undefinedVars",
        "uselessCode",
        "visibility"
    );

    assertThat(registeredGroups.keySet()).containsExactlyElementsIn(expectedGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup globalThisGroup = diagnosticGroups.forName("globalThis");
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");

    assertNull(diagnosticGroups.forName("unknown"));

    assertThat(globalThisGroup).isNotNull();
    assertThat(deprecatedGroup).isNotNull();
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    List<String> diagnosticGroupsToEnable = ImmutableList.of(
        "deprecated",
        "checkTypes",
        "checkVars"
    );

    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToEnable, CheckLevel.WARNING);

    List<DiagnosticGroup> enabledGroups = ImmutableList.of(
        diagnosticGroups.forName("deprecated"),
        diagnosticGroups.forName("checkTypes"),
        diagnosticGroups.forName("checkVars")
    );

    for (DiagnosticGroup group : enabledGroups) {
      assertThat(options.getWarningLevel(group)).isEqualTo(CheckLevel.WARNING);
    }
  }
}