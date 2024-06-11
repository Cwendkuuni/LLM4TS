package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    ImmutableMap<String, DiagnosticGroup> groups = DiagnosticGroups.getRegisteredGroups();
    List<String> expectedGroups = ImmutableList.of("accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes", "checkVars", "constantProperty", "deprecated", "externsValidation", "fileoverviewTags", "globalThis", "invalidCasts", "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "uselessCode", "visibility");
    assertTrue(groups.keySet().containsAll(expectedGroups));
  }

  @Test
  public void testGetGroupByName() {
    DiagnosticGroup accessControls = DiagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = DiagnosticGroups.forName("deprecated");
    assertNotNull(accessControls);
    assertNotNull(deprecated);
  }

  @Test
  public void testGlobalThisGroup() {
    DiagnosticGroup globalThisGroup = DiagnosticGroups.GLOBAL_THIS;
    assertNotNull(globalThisGroup);
    assertTrue(globalThisGroup.getName().equals("globalThis"));
  }

  @Test
  public void testDeprecatedGroup() {
    DiagnosticGroup deprecatedGroup = DiagnosticGroups.DEPRECATED;
    assertNotNull(deprecatedGroup);
    assertTrue(deprecatedGroup.getName().equals("deprecated"));

    List<DiagnosticType> expectedTypes = ImmutableList.of(CheckAccessControls.DEPRECATED_NAME, CheckAccessControls.DEPRECATED_NAME_REASON, CheckAccessControls.DEPRECATED_PROP, CheckAccessControls.DEPRECATED_PROP_REASON, CheckAccessControls.DEPRECATED_CLASS, CheckAccessControls.DEPRECATED_CLASS_REASON);
    assertTrue(deprecatedGroup.getDiagnosticTypes().containsAll(expectedTypes));
  }

  @Test
  public void testVisibilityGroup() {
    DiagnosticGroup visibilityGroup = DiagnosticGroups.VISIBILITY;
    assertNotNull(visibilityGroup);
    assertTrue(visibilityGroup.getName().equals("visibility"));

    List<DiagnosticGroup> expectedGroups = ImmutableList.of(DiagnosticGroups.DEPRECATED);
    assertTrue(visibilityGroup.getSubGroups().containsAll(expectedGroups));
  }

  // Add more test cases for other groups if necessary
}