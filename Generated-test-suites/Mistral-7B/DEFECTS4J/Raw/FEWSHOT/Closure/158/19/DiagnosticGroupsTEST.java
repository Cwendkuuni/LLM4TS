package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedGroupNames = ImmutableList.of("accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes", "checkVars", "constantProperty", "deprecated", "externsValidation", "fileoverviewTags", "globalThis", "invalidCasts", "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "uselessCode", "visibility");

    for (String name : expectedGroupNames) {
      DiagnosticGroup group = registeredGroups.get(name);
      assertNotNull(group);
    }
  }

  @Test
  public void testGroupsAndTheirMembers() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = accessControls.forName("deprecated");
    DiagnosticGroup visibility = accessControls.forName("visibility");

    List<DiagnosticType> expectedMembersOfAccessControls = ImmutableList.of(
        deprecated,
        visibility);

    List<DiagnosticType> membersOfAccessControls = getMembersOfGroup(accessControls);
    assertTrue(membersOfAccessControls.equals(expectedMembersOfAccessControls));

    List<DiagnosticType> expectedMembersOfDeprecated = ImmutableList.of();
    List<DiagnosticType> membersOfDeprecated = getMembersOfGroup(deprecated);
    assertTrue(membersOfDeprecated.isEmpty());

    List<DiagnosticType> expectedMembersOfVisibility = ImmutableList.of(
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH);
    List<DiagnosticType> membersOfVisibility = getMembersOfGroup(visibility);
    assertTrue(membersOfVisibility.equals(expectedMembersOfVisibility));
  }

  private List<DiagnosticType> getMembersOfGroup(DiagnosticGroup group) {
    return ImmutableList.copyOf(group.getDiagnostics());
  }
}