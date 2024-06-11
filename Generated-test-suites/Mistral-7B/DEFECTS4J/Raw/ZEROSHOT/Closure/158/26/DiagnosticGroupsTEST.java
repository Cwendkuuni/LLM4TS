import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import junit.framework.Assert;

public class DiagnosticGroupsTest {

  private static DiagnosticGroups diagnosticGroups;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    Assert.assertNotNull(registeredGroups);
    ImmutableMap<String, DiagnosticGroup> expectedGroups = ImmutableMap.of(
        "accessControls", ACCESS_CONTROLS,
        "ambiguousFunctionDecl", AMBIGUOUS_FUNCTION_DECL,
        "checkRegExp", CHECK_REGEXP,
        "checkTypes", CHECK_TYPES,
        "constantProperty", CONSTANT_PROPERTY,
        "deprecated", DEPRECATED,
        "externsValidation", EXTERNS_VALIDATION,
        "fileoverviewTags", FILEOVERVIEW_JSDOC,
        "globalThis", GLOBAL_THIS,
        "invalidCasts", INVALID_CASTS,
        "missingProperties", MISSING_PROPERTIES,
        "nonStandardJsDocs", NON_STANDARD_JSDOC,
        "strictModuleDepCheck", STRICT_MODULE_DEP_CHECK,
        "typeInvalidation", TYPE_INVALIDATION,
        "uselessCode", CHECK_USELESS_CODE,
        "visibility", VISIBILITY,
        "internetExplorerChecks", INTERNET_EXPLORER_CHECKS,
        "unknownDefines", UNKNOWN_DEFINES,
        "tweakValidation", TWEAKS
    );
    assertEquals(expectedGroups, registeredGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup unknown = diagnosticGroups.forName("unknown");

    assertNotNull(accessControls);
    assertNotNull(deprecated);
    assertNull(unknown);
  }

  @Test
  public void testRegisterGroup() {
    String newGroupName = "testGroup";
    DiagnosticGroup newGroup = new DiagnosticGroup(newGroupName, ImmutableList.of());

    diagnosticGroups.registerGroup(newGroupName, newGroup);

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    Assert.assertNotNull(registeredGroups.get(newGroupName));
  }

  @Test(expected = IllegalStateException.class)
  public void testRegisterGroupTwice() {
    String duplicateGroupName = "accessControls";
    diagnosticGroups.registerGroup(duplicateGroupName, ImmutableList.of());

    try {
      diagnosticGroups.registerGroup(duplicateGroupName, ImmutableList.of());
    } catch (IllegalStateException e) {
      assertEquals("A diagnostic group with the name " + duplicateGroupName
          + " is already registered.", e.getMessage());
      throw e;
    }
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Lists.newArrayList(ACCESS_CONTROLS.getName());
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    assertEquals(CheckLevel.WARNING, options.getWarningLevel(ACCESS_CONTROLS));
  }

  @Test
  public void testSetWarningLevelsEmptyList() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Lists.newArrayList();
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.ERROR);

    for (DiagnosticGroup group : diagnosticGroups.getRegisteredGroups().values()) {
      assertEquals(CheckLevel.DEFAULT, options.getWarningLevel(group));
    }
  }
}