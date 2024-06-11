package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Unit tests for DiagnosticGroups class.
 */
public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test registering single diagnostic type
    DiagnosticGroup deprecatedGroup = diagnosticGroups.registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME);
    assertEquals("deprecated", deprecatedGroup.getName());

    // Test registering multiple diagnostic types
    DiagnosticGroup accessControlsGroup = diagnosticGroups.registerGroup("accessControls",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    assertEquals("accessControls", accessControlsGroup.getName());

    // Test registering group with nested groups
    DiagnosticGroup visibilityGroup = diagnosticGroups.registerGroup("visibility",
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH);
    assertEquals("visibility", visibilityGroup.getName());

    // Test getting registered diagnostic groups
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(11, registeredGroups.size());

    List<String> diagnosticGroupsNames = Lists.newArrayList(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "checkVars", "constantProperty", "deprecated", "externsValidation",
        "fileoverviewTags", "globalThis", "invalidCasts", "missingProperties",
        "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation",
        "undefinedVars", "uselessCode", "visibility");
    for (String name : diagnosticGroupsNames) {
      DiagnosticGroup group = registeredGroups.get(name);
      assertNotNull(group);
      assertEquals(name, group.getName());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, CheckAccessControls.DEPRECATED_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("", CheckAccessControls.DEPRECATED_NAME);
  }

  @Test
  public void testFindGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControlsGroup = diagnosticGroups.registerGroup("accessControls",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);

    DiagnosticGroup foundGroup = diagnosticGroups.forName("accessControls");
    assertEquals(accessControlsGroup, foundGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testFindNullGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testFindUnknownGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertNull(diagnosticGroups.forName("unknownGroup"));
  }
}