package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckerFactory.CheckerFactoryWithTypes;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilationState;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Diagnostics.Type;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Test for DiagnosticGroups class.
 */
public class DiagnosticGroupsTest extends CompilerTestBase {

  private static Compiler compiler;
  private static DiagnosticGroups diagnosticGroups;

  @BeforeClass
  public static void setup() {
    compiler = new Compiler();
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);

    List<String> diagnosticGroupNames = Arrays.asList(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "checkVars", "constantProperty", "deprecated", "externsValidation",
        "fileoverviewTags", "globalThis", "internetExplorerChecks",
        "invalidCasts", "missingProperties", "nonStandardJsDocs",
        "strictModuleDepCheck", "typeInvalidation", "undefinedVars",
        "uselessCode", "visibility");

    assertEquals(diagnosticGroupNames.size(), registeredGroups.size());

    for (String name : diagnosticGroupNames) {
      DiagnosticGroup group = diagnosticGroups.forName(name);
      assertNotNull(group);
    }
  }

  @Test
  public void testWarningLevels() {
    CompilerOptions options = new CompilerOptions();

    List<String> diagnosticGroups = Lists.newArrayList(
        "accessControls", "checkTypes", "checkVars", "uselessCode");

    DiagnosticGroup[] warningGroups = new DiagnosticGroup[] {
        diagnosticGroups.get(0), diagnosticGroups.get(1),
        diagnosticGroups.get(2), diagnosticGroups.get(3)};

    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    List<DiagnosticType> warningTypes = Arrays.asList(
        TypeCheck.ALL_DIAGNOSTICS.get(0),
        CheckSideEffects.USELESS_CODE_ERROR.get(0),
        TypeValidator.ALL_DIAGNOSTICS.get(0),
        VarCheck.UNDEFINED_VAR_ERROR.get(0));

    for (DiagnosticType warningType : warningTypes) {
      assertEquals(CheckLevel.WARNING.toString(),
          options.getWarningLevel(warningType).level.toString());
    }

    // Check that non-registered group does not affect the warning level.
    options.setWarningLevel(new DiagnosticGroup("unexistingGroup"), CheckLevel.ERROR);

    DiagnosticType nonRegisteredWarningType = new DiagnosticType("unexistingType") {};
    assertEquals(CheckLevel.OFF.toString(),
        options.getWarningLevel(nonRegisteredWarningType).level.toString());
  }
}