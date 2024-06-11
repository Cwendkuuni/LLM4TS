
package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Tests for DiagnosticGroups class
 */
public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> expectedGroups = Maps.newHashMap();
    expectedGroups.put("GLOBAL_THIS", DiagnosticGroups.GLOBAL_THIS);
    // Add other expected groups here...

    Map<String, DiagnosticGroup> actualGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(expectedGroups, actualGroups);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, new DiagnosticGroup("name", new DiagnosticType[0]));
  }

  // Add test cases for other methods and edge cases here...

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableList.of("ACCESS_CONTROLS");

    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.QUIRKS);
    assertEquals(CheckLevel.QUIRKS, options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
  }

  // Add more test cases as needed...
}