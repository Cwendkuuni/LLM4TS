package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    assertTrue(registeredGroups.size() > 0);
    assertNotNull(registeredGroups.get("accessControls"));
    assertNotNull(registeredGroups.get("ambiguousFunctionDecl"));
    // Add more assertions for other groups as needed
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
    DiagnosticGroup unknown = diagnosticGroups.forName("unknown");
    assertNull(unknown);
  }

  // Add more test cases for other methods in DiagnosticGroups class
}