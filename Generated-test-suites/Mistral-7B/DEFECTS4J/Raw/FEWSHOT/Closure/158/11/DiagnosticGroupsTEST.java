package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    List<String> groupNames = Lists.newArrayList(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "constantProperty", "deprecated", "externsValidation", "fileoverviewTags",
        "globalThis", "invalidCasts", "missingProperties", "nonStandardJsDocs",
        "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "unknownDefines",
        "uselessCode", "visibility");

    for (String name : groupNames) {
      DiagnosticGroup group = registeredGroups.get(name);
      checkNotNull(group, "Missing registered group: " + name);
    }
  }

  @Test
  public void testGetGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");

    assertEquals(AccessControlsDiagnosticGroup.class, accessControls.getClass());
    assertEquals(DeprecatedDiagnosticGroup.class, deprecated.getClass());
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "myGroup";

    DiagnosticGroup group = new DiagnosticGroup(name, ImmutableList.of());
    diagnosticGroups.registerGroup(name, group);

    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertEquals(group, registeredGroup);
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "myGroup";

    DiagnosticType type1 = new DiagnosticType("MyType1");
    DiagnosticType type2 = new DiagnosticType("MyType2");

    DiagnosticGroup group = new DiagnosticGroup(name, ImmutableList.of(type1, type2));
    diagnosticGroups.registerGroup(name, group);

    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertEquals(group, registeredGroup);
    assertEquals(2, registeredGroup.getDiagnostics().size());
    assertEquals(type1, registeredGroup.getDiagnostics().get(0));
    assertEquals(type2, registeredGroup.getDiagnostics().get(1));
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "myGroup";

    DiagnosticGroup group1 = new DiagnosticGroup("group1", ImmutableList.of());
    DiagnosticGroup group2 = new DiagnosticGroup("group2", ImmutableList.of());

    DiagnosticGroup myGroup = new DiagnosticGroup(name, ImmutableList.of(group1, group2));
    diagnosticGroups.registerGroup(name, myGroup);

    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertEquals(myGroup, registeredGroup);
    assertEquals(2, registeredGroup.getGroups().size());
    assertEquals(group1, registeredGroup.getGroups().get(0));
    assertEquals(group2, registeredGroup.getGroups().get(1));
  }
}