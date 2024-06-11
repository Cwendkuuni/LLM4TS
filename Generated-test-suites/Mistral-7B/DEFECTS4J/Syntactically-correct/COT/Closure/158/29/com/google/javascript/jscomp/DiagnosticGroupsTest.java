package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;

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
    DiagnosticGroup group = diagnosticGroups.forName("ACCESS_CONTROLS");
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForNameWithInvalidGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName("invalidGroupName");
  }

  @Test
  public void testRegisterGroupWithNameAndTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP",
        DiagnosticType.ERROR);
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, DiagnosticType.ERROR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("", DiagnosticType.ERROR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithGroupAlreadyRegistered() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("ACCESS_CONTROLS", DiagnosticType.ERROR);
    diagnosticGroups.registerGroup("ACCESS_CONTROLS", DiagnosticType.ERROR);
  }

  @Test
  public void testRegisterGroupWithNameAndDiagnosticTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP",
        DiagnosticType.ERROR, DiagnosticType.WARNING);
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupWithNullGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, DiagnosticType.ERROR,
        DiagnosticType.WARNING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyGroupNameAndTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("", DiagnosticType.ERROR,
        DiagnosticType.WARNING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithDuplicateDiagnosticTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("TEST_GROUP", DiagnosticType.ERROR,
        DiagnosticType.ERROR);
  }

  @Test
  public void testRegisterGroupWithNameAndGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP",
        DiagnosticGroup.GLOBAL_THIS);
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupWithNullGroupNameAndGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, DiagnosticGroup.GLOBAL_THIS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyGroupNameAndGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("", DiagnosticGroup.GLOBAL_THIS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithDuplicateGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("TEST_GROUP", DiagnosticGroup.GLOBAL_THIS);
    diagnosticGroups.registerGroup("TEST_GROUP", DiagnosticGroup.GLOBAL_THIS);
  }

}