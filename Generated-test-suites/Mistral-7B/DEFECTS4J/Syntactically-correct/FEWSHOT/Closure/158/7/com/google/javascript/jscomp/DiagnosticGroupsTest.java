package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = new DiagnosticGroup("test", new DiagnosticType[] {});
    diagnosticGroups.registerGroup("test", group);
    DiagnosticGroup retrievedGroup = diagnosticGroups.forName("test");
    assertNotNull(retrievedGroup);
    assertNotNull(retrievedGroup.getName().equals("test"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, new DiagnosticType[] {});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupNullGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("test", null);
  }

  @Test
  public void testRegisterGroupTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("test",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    DiagnosticType[] types = group.getTypes();
    assertNotNull(types);
    assertNotNull(types.length > 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupEmptyTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("test", new DiagnosticType[0]);
  }

  @Test
  public void testRegisterGroupGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = new DiagnosticGroup("test", new DiagnosticGroup[] {});
    diagnosticGroups.registerGroup("test", group);
    DiagnosticGroup retrievedGroup = diagnosticGroups.forName("test");
    assertNotNull(retrievedGroup);
    assertNotNull(retrievedGroup.getGroups());
    assertNotNull(retrievedGroup.getGroups().length > 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupEmptyGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("test", new DiagnosticGroup[0]);
  }

  @Test
  public void testDiagnosticGroupNames() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupNames = ImmutableMap.copyOf(diagnosticGroups.getRegisteredGroups())
        .keySet()
        .stream()
        .collect(ImmutableMap.toImmutableList())
        .get(0);
    String diagnosticGroupName = diagnosticGroupNames.get(0);
    assertNotNull(diagnosticGroupName);
    assertEquals(DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES, diagnosticGroupName);
  }
}