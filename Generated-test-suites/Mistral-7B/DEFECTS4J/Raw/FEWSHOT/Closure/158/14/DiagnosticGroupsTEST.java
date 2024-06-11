package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class DiagnosticGroupsTest {

  private static DiagnosticGroups diagnosticGroups;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisteredGroups() {
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    List<String> groupNames = List.of(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp",
        "checkTypes", "checkVars", "constantProperty", "deprecated",
        "externsValidation", "fileoverviewTags", "globalThis",
        "invalidCasts", "missingProperties", "nonStandardJsDocs",
        "strictModuleDepCheck", "typeInvalidation", "undefinedVars",
        "unknownDefines", "uselessCode", "visibility"
    );

    for (String name : groupNames) {
      DiagnosticGroup group = diagnosticGroups.forName(name);
      assertNotNull(group, "Missing group named " + name);
      assertTrue(groups.containsKey(name), "Group named " + name + " not found");
    }
  }

  @Test
  public void testGlobalThis() {
    DiagnosticGroup globalThis = diagnosticGroups.GLOBAL_THIS;
    assertNotNull(globalThis, "GlobalThis group is null");
  }

  @Test
  public void testDeprecated() {
    DiagnosticGroup deprecated = diagnosticGroups.DEPRECATED;
    assertNotNull(deprecated, "Deprecated group is null");
  }

  @Test
  public void testVisibility() {
    DiagnosticGroup visibility = diagnosticGroups.VISIBILITY;
    assertNotNull(visibility, "Visibility group is null");
  }

  @Test
  public void testConstantProperty() {
    DiagnosticGroup constantProperty = diagnosticGroups.CONSTANT_PROPERTY;
    assertNotNull(constantProperty, "ConstantProperty group is null");
  }

  @Test
  public void testNonStandardJsDocs() {
    DiagnosticGroup nonStandardJsDocs = diagnosticGroups.NON_STANDARD_JSDOC;
    assertNotNull(nonStandardJsDocs, "NonStandardJsDocs group is null");
  }

  @Test
  public void testAccessControls() {
    DiagnosticGroup accessControls = diagnosticGroups.ACCESS_CONTROLS;
    assertNotNull(accessControls, "AccessControls group is null");
  }

  @Test
  public void testInvalidCasts() {
    DiagnosticGroup invalidCasts = diagnosticGroups.INVALID_CASTS;
    assertNotNull(invalidCasts, "InvalidCasts group is null");
  }

  @Test
  public void testFileoverviewJsdoc() {
    DiagnosticGroup fileoverviewJsdoc = diagnosticGroups.FILEOVERVIEW_JSDOC;
    assertNotNull(fileoverviewJsdoc, "FileoverviewJsdoc group is null");
  }

  @Test
  public void testStrictModuleDepCheck() {
    DiagnosticGroup strictModuleDepCheck = diagnosticGroups.STRICT_MODULE_DEP_CHECK;
    assertNotNull(strictModuleDepCheck, "StrictModuleDepCheck group is null");
  }

  @Test
  public void testExternsValidation() {
    DiagnosticGroup externsValidation = diagnosticGroups.EXTERNS_VALIDATION;
    assertNotNull(externsValidation, "ExternsValidation group is null");
  }

  @Test
  public void testAmbiguousFunctionDecl() {
    DiagnosticGroup ambiguousFunctionDecl = diagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
    assertNotNull(ambiguousFunctionDecl, "AmbiguousFunctionDecl group is null");
  }

  @Test
  public void testUnknownDefines() {
    DiagnosticGroup unknownDefines = diagnosticGroups.UNKNOWN_DEFINES;
    assertNotNull(unknownDefines, "UnknownDefines group is null");
  }

  @Test
  public void testTweaks() {
    DiagnosticGroup tweaks = diagnosticGroups.TWEAKS;
    assertNotNull(tweaks, "Tweaks group is null");
  }

  @Test
  public void testMissingProperties() {
    DiagnosticGroup missingProperties = diagnosticGroups.MISSING_PROPERTIES;
    assertNotNull(missingProperties, "MissingProperties group is null");
  }

  @Test
  public void testInternetExplorerChecks() {
    DiagnosticGroup internetExplorerChecks = diagnosticGroups.INTERNET_EXPLORER_CHECKS;
    assertNotNull(internetExplorerChecks, "InternetExplorerChecks group is null");
  }

  @Test
  public void testUndefinedVariables() {
    DiagnosticGroup undefinedVariables = diagnosticGroups.UNDDEFINED_VARIABLES;
    assertNotNull(undefinedVariables, "UndefinedVariables group is null");
  }

  @Test
  public void testCheckRegExp() {
    DiagnosticGroup checkRegExp = diagnosticGroups.CHECK_REGEXP;
    assertNotNull(checkRegExp, "CheckRegExp group is null");
  }

  @Test
  public void testCheckTypes() {
    DiagnosticGroup checkTypes = diagnosticGroups.CHECK_TYPES;
    assertNotNull(checkTypes, "CheckTypes group is null");
  }

  @Test
  public void testCheckVariables() {
    DiagnosticGroup checkVariables = diagnosticGroups.CHECK_VARIABLES;
    assertNotNull(checkVariables, "CheckVariables group is null");
  }

  @Test
  public void testCheckUseleesCode() {
    DiagnosticGroup checkUseleesCode = diagnosticGroups.CHECK_USELESS_CODE;
    assertNotNull(checkUseleesCode, "CheckUseleesCode group is null");
  }

  @Test
  public void testTypeInvalidation() {
    DiagnosticGroup typeInvalidation = diagnosticGroups.TYPE_INVALIDATION;
    assertNotNull(typeInvalidation, "TypeInvalidation group is null");
  }
}