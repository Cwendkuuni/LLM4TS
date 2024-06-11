package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CompilerOptions;

@RunWith(JUnit4.class)
public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedGroups = ImmutableList.of(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp",
        "checkTypes", "constantProperty", "deprecated",
        "externsValidation", "fileoverviewTags", "globalThis",
        "internetExplorerChecks", "invalidCasts", "missingProperties",
        "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation",
        "undefinedVars", "uselessCode", "visibility"
    );

    assertThat(registeredGroups.keySet()).containsExactlyElementsIn(expectedGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup unused = diagnosticGroups.forName("unused");

    assertNull(unused);
    assertThat(accessControls).isNotNull();
    assertThat(deprecated).isNotNull();
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String name = "testGroup";
    DiagnosticType diagnosticType = new DiagnosticType() {};
    DiagnosticGroup group = new DiagnosticGroup(name, diagnosticType);

    diagnosticGroups.registerGroup(name, diagnosticType);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);

    Preconditions.checkNotNull(registeredGroup, "Registered group should not be null.");
    Preconditions.checkState(registeredGroup == group, "Registered group should be the same instance.");
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableList.of("checkTypes", "checkVars");

    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.QUIRK);

    DiagnosticGroup checkTypes = diagnosticGroups.forName("checkTypes");
    DiagnosticGroup checkVars = diagnosticGroups.forName("checkVars");

    Preconditions.checkState(options.getWarningLevel(checkTypes) == CheckLevel.QUIRK,
        "CheckTypes warning level should be set to QUIRK.");
    Preconditions.checkState(options.getWarningLevel(checkVars) == CheckLevel.QUIRK,
        "CheckVars warning level should be set to QUIRK.");
  }
}