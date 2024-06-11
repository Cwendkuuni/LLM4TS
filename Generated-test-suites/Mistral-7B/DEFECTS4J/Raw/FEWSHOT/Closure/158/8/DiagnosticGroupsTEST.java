package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    ImmutableMap<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedGroupNames = Lists.newArrayList(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes", "checkVars", "constantProperty",
        "deprecated", "externsValidation", "fileoverviewTags", "globalThis", "internetExplorerChecks", "invalidCasts",
        "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation", "undefinedVars",
        "uselessCode", "visibility");

    assertEquals(expectedGroupNames.size(), registeredGroups.size());

    for (String groupName : expectedGroupNames) {
      DiagnosticGroup group = registeredGroups.get(groupName);
      checkNotNull(group, "Missing diagnostic group for name: " + groupName);
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup visibility = diagnosticGroups.forName("visibility");

    assertEquals(AccessControls.class, accessControls.getClass());
    assertEquals(Deprecated.class, deprecated.getClass());
    assertEquals(Visibility.class, visibility.getClass());
  }

  @Test
  public void testInvalidGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    assertNull(diagnosticGroups.forName("nonExistentGroupName"));
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String newGroupName = "newGroupName";
    DiagnosticGroup newGroup = new DiagnosticGroup(newGroupName, ImmutableList.of());

    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(newGroupName, newGroup);

    assertEquals(newGroupName, registeredGroup.getName());
    assertEquals(newGroup, registeredGroup);

    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(newGroupName);
    assertEquals(newGroup, retrievedGroup);
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String newGroupName = "newGroupName";
    DiagnosticType newType1 = new DiagnosticType("newType1");
    DiagnosticType newType2 = new DiagnosticType("newType2");

    DiagnosticGroup newGroup = new DiagnosticGroup(newGroupName, ImmutableList.of(newType1, newType2));

    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(newGroupName, newType1, newType2);

    assertEquals(newGroupName, registeredGroup.getName());
    assertEquals(newGroup, registeredGroup);

    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(newGroupName);
    assertEquals(newGroup, retrievedGroup);

    assertEquals(newType1, retrievedGroup.getDiagnosticTypes().get(0));
    assertEquals(newType2, retrievedGroup.getDiagnosticTypes().get(1));
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String newGroupName = "newGroupName";
    DiagnosticGroup newGroup1 = new DiagnosticGroup("newGroup1", ImmutableList.of());
    DiagnosticGroup newGroup2 = new DiagnosticGroup("newGroup2", ImmutableList.of());

    DiagnosticGroup newGroup = new DiagnosticGroup(newGroupName, ImmutableList.of(newGroup1, newGroup2));

    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(newGroupName, newGroup1, newGroup2);

    assertEquals(newGroupName, registeredGroup.getName());
    assertEquals(newGroup, registeredGroup);

    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(newGroupName);
    assertEquals(newGroup, retrievedGroup);

    assertEquals(newGroup1, retrievedGroup.getChildGroups().get(0));
    assertEquals(newGroup2, retrievedGroup.getChildGroups().get(1));
  }
}