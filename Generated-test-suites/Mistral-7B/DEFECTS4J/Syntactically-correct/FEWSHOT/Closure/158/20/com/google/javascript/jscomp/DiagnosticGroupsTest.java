package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();

    assertNotNull(groups);
    assertTrue(groups.size() > 0);

    for (String name : DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(",")) {
      DiagnosticGroup group = diagnosticGroups.forName(name);
      assertNotNull(group);
    }
  }
}