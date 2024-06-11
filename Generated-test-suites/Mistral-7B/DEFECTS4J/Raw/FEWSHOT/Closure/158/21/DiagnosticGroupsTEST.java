package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class DiagnosticGroupsTest {

  private static Map<String, DiagnosticGroup> diagnosticGroups;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups().getRegisteredGroups();
  }

  @Test
  public void testGetRegisteredGroups() {
    assertThat(diagnosticGroups).isNotNull();
  }

  @Test
  public void testForName() {
    DiagnosticGroup globalThis = diagnosticGroups.get("globalThis");
    DiagnosticGroup deprecated = diagnosticGroups.get("deprecated");
    DiagnosticGroup visibility = diagnosticGroups.get("visibility");
    DiagnosticGroup constantProperty = diagnosticGroups.get("constantProperty");
    DiagnosticGroup nonStandardJsDoc = diagnosticGroups.get("nonStandardJsDocs");
    DiagnosticGroup accessControls = diagnosticGroups.get("accessControls");
    DiagnosticGroup invalidCasts = diagnosticGroups.get("invalidCasts");
    DiagnosticGroup fileoverviewJsdoc = diagnosticGroups.get("fileoverviewTags");
    DiagnosticGroup strictModuleDepCheck = diagnosticGroups.get("strictModuleDepCheck");
    DiagnosticGroup externsValidation = diagnosticGroups.get("externsValidation");
    DiagnosticGroup ambiguousFunctionDecl = diagnosticGroups.get("ambiguousFunctionDecl");
    DiagnosticGroup unknownDefines = diagnosticGroups.get("unknownDefines");
    DiagnosticGroup tweaks = diagnosticGroups.get("tweakValidation");
    DiagnosticGroup missingProperties = diagnosticGroups.get("missingProperties");
    DiagnosticGroup internetExplorerChecks = diagnosticGroups.get("internetExplorerChecks");
    DiagnosticGroup undefinedVariables = diagnosticGroups.get("undefinedVars");
    DiagnosticGroup checkRegExp = diagnosticGroups.get("checkRegExp");
    DiagnosticGroup checkTypes = diagnosticGroups.get("checkTypes");
    DiagnosticGroup checkVariables = diagnosticGroups.get("checkVars");
    DiagnosticGroup checkUselessCode = diagnosticGroups.get("uselessCode");
    DiagnosticGroup typeInvalidation = diagnosticGroups.get("typeInvalidation");

    assertNotNull(globalThis);
    assertNotNull(deprecated);
    assertNotNull(visibility);
    assertNotNull(constantProperty);
    assertNotNull(nonStandardJsDoc);
    assertNotNull(accessControls);
    assertNotNull(invalidCasts);
    assertNotNull(fileoverviewJsdoc);
    assertNotNull(strictModuleDepCheck);
    assertNotNull(externsValidation);
    assertNotNull(ambiguousFunctionDecl);
    assertNotNull(unknownDefines);
    assertNotNull(tweaks);
    assertNotNull(missingProperties);
    assertNotNull(internetExplorerChecks);
    assertNotNull(undefinedVariables);
    assertNotNull(checkRegExp);
    assertNotNull(checkTypes);
    assertNotNull(checkVariables);
    assertNotNull(checkUselessCode);
    assertNotNull(typeInvalidation);
  }

  @Test
  public void testSetWarningLevels() {
    // Assuming that CompilerOptions and CheckLevel are available
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableList.of("checkTypes", "checkVars");
    new DiagnosticGroups().setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);

    DiagnosticGroup checkTypes = diagnosticGroups.get("checkTypes");
    DiagnosticGroup checkVars = diagnosticGroups.get("checkVars");

    assertNotNull(checkTypes);
    assertNotNull(checkVars);

    // Checking warning levels
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(checkTypes));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(checkVars));
  }
}