package com.example.test.diagnosticgroups;

import com.google.javascript.jscomp.DiagnosticGroups;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> expectedGroups = new HashMap<>();
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    expectedGroups.put("group1", group1);
    diagnosticGroups.registerGroup("group1", group1);
    Map<String, DiagnosticGroup> actualGroups = diagnosticGroups.getRegisteredGroups();
    Assertions.assertEquals(expectedGroups, actualGroups);
  }

  @Test
  public void testGetRegisteredGroupsEmpty() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> expectedGroups = new HashMap<>();
    Map<String, DiagnosticGroup> actualGroups = diagnosticGroups.getRegisteredGroups();
    Assertions.assertEquals(expectedGroups, actualGroups);
  }

  @Test
  public void testGetRegisteredGroupsDuplicateKey() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group1");
    diagnosticGroups.registerGroup("group1", group1);
    diagnosticGroups.registerGroup("group1", group2);
    Map<String, DiagnosticGroup> expectedGroups = new HashMap<>();
    expectedGroups.put("group1", group1);
    Map<String, DiagnosticGroup> actualGroups = diagnosticGroups.getRegisteredGroups();
    Assertions.assertEquals(expectedGroups, actualGroups);
    Assertions.assertEquals(group1, actualGroups.get("group1"));
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup expectedGroup = new DiagnosticGroup("group1");
    diagnosticGroups.registerGroup("group1", expectedGroup);
    DiagnosticGroup actualGroup = diagnosticGroups.forName("group1");
    Assertions.assertEquals(expectedGroup, actualGroup);
  }

  @Test
  public void testForNameNonExistentGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Assert.assertNull(diagnosticGroups.forName("nonExistentGroup"));
  }

  @Test
  public void testForNameExistingButNotRegisteredGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Assert.assertNull(diagnosticGroups.forName("nonExistentButRegisteredGroup"));
  }

  @Test
  public void testRegisterGroupNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Assert.assertThrows(NullPointerException.class, () -> diagnosticGroups.registerGroup(null, new DiagnosticGroup("group1")));
  }

  @Test
  public void testRegisterGroupNullGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Assert.assertThrows(NullPointerException.class, () -> diagnosticGroups.registerGroup("group1", null));
  }

  @Test
  public void testRegisterGroupDuplicateKey() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    diagnosticGroups.registerGroup("group1", group1);
    Assert.assertThrows(IllegalArgumentException.class, () -> diagnosticGroups.registerGroup("group1", new DiagnosticGroup("group2")));
  }
}