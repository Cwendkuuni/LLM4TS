package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    // Check that all registered diagnostic groups are present
    List<String> expectedGroups = List.of("accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "checkVars", "constantProperty", "deprecated", "externsValidation", "fileoverviewTags", "globalThis",
        "invalidCasts", "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation",
        "undefinedVars", "unknownDefines", "uselessCode", "visibility");
    assertThat(registeredGroups.keySet()).containsExactlyElementsIn(expectedGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup visibility = diagnosticGroups.forName("visibility");
    DiagnosticGroup unknownGroup = diagnosticGroups.forName("unknownGroup");

    // Check that the known diagnostic groups are not null
    assertThat(accessControls).isNotNull();
    assertThat(deprecated).isNotNull();
    assertThat(visibility).isNotNull();

    // Check that the unknown diagnostic group is null
    assertNull(unknownGroup);
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String myGroup = "myGroup";

    DiagnosticGroup myGroupDiagnostic = new DiagnosticGroup(myGroup, ImmutableMap.of());
    diagnosticGroups.registerGroup(myGroup, myGroupDiagnostic);

    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(myGroup);
    assertThat(retrievedGroup).isSameInstanceAs(myGroupDiagnostic);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, ImmutableMap.of());
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupNullDiagnostic() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("myGroup", null);
  }

  @Test
  public void testRegisterGroupType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String myGroup = "myGroup";
    DiagnosticType myDiagnostic = new DiagnosticType("MyDiagnostic");

    DiagnosticGroup myGroupDiagnostic = new DiagnosticGroup(myGroup, ImmutableMap.of(myDiagnostic.getTypeName(), myDiagnostic));
    diagnosticGroups.registerGroup(myGroup, myGroupDiagnostic);

    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(myGroup);
    assertThat(retrievedGroup.getDiagnostics().get(myDiagnostic.getTypeName())).isSameInstanceAs(myDiagnostic);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupNullDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String myGroup = "myGroup";
    DiagnosticType myDiagnostic = null;

    DiagnosticGroup myGroupDiagnostic = new DiagnosticGroup(myGroup, ImmutableMap.of());
    diagnosticGroups.registerGroup(myGroup, myGroupDiagnostic, myDiagnostic);
  }
}