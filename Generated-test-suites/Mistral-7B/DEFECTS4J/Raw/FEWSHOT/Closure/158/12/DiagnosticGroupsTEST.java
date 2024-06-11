package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ParserConfig;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(23, registeredGroups.size());

    List<String> expectedGroups = ImmutableList.of(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "constantProperty", "deprecated", "externsValidation", "fileoverviewTags",
        "globalThis", "invalidCasts", "missingProperties", "nonStandardJsDocs",
        "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "unknownDefines",
        "uselessCode", "visibility"
    );

    for (String name : registeredGroups.keySet()) {
      assertNotNull(registeredGroups.get(name));
      assertEquals(name, registeredGroups.get(name).getName());
      assertEquals(true, expectedGroups.contains(name));
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    for (DiagnosticGroup group : diagnosticGroups.getRegisteredGroups().values()) {
      String name = group.getName();
      DiagnosticGroup retrievedGroup = diagnosticGroups.forName(name);
      checkNotNull(retrievedGroup);
      assertEquals(group, retrievedGroup);
    }
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    ParserConfig config = new ParserConfig();

    List<String> diagnosticGroupsToSet = ImmutableList.of(
        "checkTypes", "uselessCode"
    );

    diagnosticGroups.setWarningLevels(config, diagnosticGroupsToSet, CheckLevel.WARNING);

    for (DiagnosticGroup group : diagnosticGroups.getRegisteredGroups().values()) {
      CheckLevel warningLevel = config.getWarningLevel(group);
      if (diagnosticGroupsToSet.contains(group.getName())) {
        assertEquals(CheckLevel.WARNING, warningLevel);
      } else {
        assertEquals(CheckLevel.ERROR, warningLevel);
      }
    }
  }
}