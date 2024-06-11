package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    assertNotNull(registeredGroups);

    // Perform some random checks on the registered groups to ensure they exist
    assertNotNull(registeredGroups.get("accessControls"));
    assertNotNull(registeredGroups.get("invalidCasts"));
    assertNotNull(registeredGroups.get("fileoverviewTags"));
    // Add more checks as needed
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup invalidCasts = diagnosticGroups.forName("invalidCasts");
    DiagnosticGroup fileoverviewTags = diagnosticGroups.forName("fileoverviewTags");

    assertNotNull(accessControls);
    assertNotNull(invalidCasts);
    assertNotNull(fileoverviewTags);

    // Add more tests as needed
  }

  @Test
  public void testRegisteredGroupsNames() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String diagnosticGroupNames = diagnosticGroups.DIAGNOSTIC_GROUP_NAMES;

    String[] groupNames = diagnosticGroupNames.split(",");

    for (String groupName : groupNames) {
      DiagnosticGroup group = diagnosticGroups.forName(groupName.trim());
      assertNotNull(group);
    }
  }
}