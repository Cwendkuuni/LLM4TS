package com.example.junit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.TypeCheck;

public class DiagnosticGroupsTest {
  private DiagnosticGroups diagnosticGroups;

  @Before
  public void setup() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testConstructor() {
    // Basic test case for constructor
  }

  @Test
  public void testGetRegisteredGroups() {
    // Basic test case for getRegisteredGroups()
    Map<String, DiagnosticGroup> expectedGroups = ImmutableMap.of(
        "ACCESS_CONTROLS", ACCESS_CONTROLS,
        "INVALID_CASTS", INVALID_CASTS,
        "TYPE_INVALIDATION", TYPE_INVALIDATION);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(expectedGroups, registeredGroups);
  }

  @Test
  public void testForName_ValidName() {
    // Basic test case for forName() with a valid name
    DiagnosticGroup accessControls = diagnosticGroups.forName("ACCESS_CONTROLS");
    assertNotNull(accessControls);
  }

  @Test
  public void testForName_NullName() {
    // Edge case test for forName() with a null name
    assertNull(diagnosticGroups.forName(null));
  }

  @Test
  public void testForName_EmptyName() {
    // Edge case test for forName() with an empty name
    assertNull(diagnosticGroups.forName(""));
  }

  @Test
  public void testForName_InvalidCharacters() {
    // Edge case test for forName() with a name containing invalid characters
    assertNull(diagnosticGroups.forName("INVALID_GROUP@"));
  }

  @Test
  public void testForName_NonExistentGroup() {
    // Edge case test for forName() with a non-existent group name
    assertNull(diagnosticGroups.forName("UNKNOWN_GROUP"));
  }

  @Test
  public void testSetWarningLevels_NullOptions() {
    List<String> diagnosticGroups = Arrays.asList("ACCESS_CONTROLS", "INVALID_CASTS");
    DiagnosticGroups diagnosticGroupsInst = new DiagnosticGroups();
    try {
      diagnosticGroupsInst.setWarningLevels(null, diagnosticGroups, CheckLevel.WARNING);
    } catch (NullPointerException e) {
      // Expected since options is null
    }
  }

  @Test
  public void testSetWarningLevels_EmptyDiagnosticGroupsList() {
    // Edge case test for setWarningLevels() with an empty diagnosticGroups list
    CompilerOptions options = mock(CompilerOptions.class);
    DiagnosticGroups diagnosticGroupsInst = new DiagnosticGroups();
    try {
      diagnosticGroupsInst.setWarningLevels(options, Collections.emptyList(), CheckLevel.WARNING);
    } catch (IllegalArgumentException e) {
      // Expected since diagnosticGroups list is empty
    }
  }

  @Test
  public void testSetWarningLevels_InvalidGroupNames() {
    // Edge case test for setWarningLevels() with invalid group names
    CompilerOptions options = mock(CompilerOptions.class);
    List<String> diagnosticGroups = Arrays.asList("ACCESS_CONTROLS@", "INVALID_CASTS");
    DiagnosticGroups diagnosticGroupsInst = new DiagnosticGroups();
    try {
      diagnosticGroupsInst.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    } catch (IllegalArgumentException e) {
      // Expected since group names contain invalid characters
    }
  }

  @Test
  public void testSetWarningLevels_NullDiagnosticGroup() {
    // Edge case test for setWarningLevels() with a null diagnostic group
    CompilerOptions options = mock(CompilerOptions.class);
    List<String> diagnosticGroups = Arrays.asList("ACCESS_CONTROLS", null);
    DiagnosticGroups diagnosticGroupsInst = new DiagnosticGroups();
    try {
      diagnosticGroupsInst.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    } catch (NullPointerException e) {
      // Expected since diagnosticGroup is null
    }
  }

  @Test
  public void testSetWarningLevels_InvalidCheckLevel() {
    // Edge case test for setWarningLevels() with an invalid check level
    CompilerOptions options = mock(CompilerOptions.class);
    List<String> diagnosticGroups = Arrays.asList("ACCESS_CONTROLS", "INVALID_CASTS");
    DiagnosticGroups diagnosticGroupsInst = new DiagnosticGroups();
    try {
      diagnosticGroupsInst.setWarningLevels(options, diagnosticGroups, CheckLevel.ERROR);
    } catch (IllegalArgumentException e) {
      // Expected since CheckLevel is invalid
    }
  }
}