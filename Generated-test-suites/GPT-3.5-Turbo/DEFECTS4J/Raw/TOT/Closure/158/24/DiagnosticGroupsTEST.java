import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    // Test case 1: registerGroup(String name, DiagnosticGroup group)
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", new DiagnosticType[]{});
    assertEquals(group1, DiagnosticGroups.forName("group1"));
    
    // Test case 2: registerGroup(String name, DiagnosticType... types)
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", new DiagnosticType[]{});
    assertEquals(group2, DiagnosticGroups.forName("group2"));
    
    // Test case 3: registerGroup(String name, DiagnosticGroup... groups)
    DiagnosticGroup group3 = DiagnosticGroups.registerGroup("group3", new DiagnosticGroup[]{});
    assertEquals(group3, DiagnosticGroups.forName("group3"));
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", new DiagnosticType[]{});
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", new DiagnosticType[]{});
    DiagnosticGroup group3 = DiagnosticGroups.registerGroup("group3", new DiagnosticGroup[]{});

    assertEquals(group1, DiagnosticGroups.getRegisteredGroups().get("group1"));
    assertEquals(group2, DiagnosticGroups.getRegisteredGroups().get("group2"));
    assertEquals(group3, DiagnosticGroups.getRegisteredGroups().get("group3"));
  }

  @Test
  public void testForname() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", new DiagnosticType[]{});
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", new DiagnosticType[]{});
    DiagnosticGroup group3 = DiagnosticGroups.registerGroup("group3", new DiagnosticGroup[]{});

    assertEquals(group1, DiagnosticGroups.forName("group1"));
    assertEquals(group2, DiagnosticGroups.forName("group2"));
    assertEquals(group3, DiagnosticGroups.forName("group3"));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("group1", "group2", "group3");

    // Test case 1: Setting warning levels for existing groups
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", new DiagnosticType[]{});
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", new DiagnosticType[]{});
    DiagnosticGroup group3 = DiagnosticGroups.registerGroup("group3", new DiagnosticGroup[]{});

    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group2));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group3));
    
    // Test case 2: Setting warning levels for non-existent group
    diagnosticGroups.add("invalidGroup");

    try {
      DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
      fail("Expected an exception to be thrown for non-existent group");
    } catch (NullPointerException e) {
      assertEquals("No warning class for name: invalidGroup", e.getMessage());
    }
  }

  @Test
  public void testConstants() {
    // Test case 1: GLOBAL_THIS
    assertEquals("globalThis", DiagnosticGroups.GLOBAL_THIS.getName());

    // Test case 2: DEPRECATED
    assertEquals("deprecated", DiagnosticGroups.DEPRECATED.getName());

    // Test case 3: VISIBILITY
    assertEquals("visibility", DiagnosticGroups.VISIBILITY.getName());
  }

  @Test
  public void testSpecificGroups() {
    // Test case 1: CONSTANT_PROPERTY
    assertEquals("constantProperty", DiagnosticGroups.CONSTANT_PROPERTY.getName());
    assertEquals(1, DiagnosticGroups.CONSTANT_PROPERTY.getTypes().length);

    // Test case 2: NON_STANDARD_JSDOC
    assertEquals("nonStandardJsDocs", DiagnosticGroups.NON_STANDARD_JSDOC.getName());
    assertEquals(1, DiagnosticGroups.NON_STANDARD_JSDOC.getTypes().length);

    // Test case 3: ACCESS_CONTROLS
    assertEquals("accessControls", DiagnosticGroups.ACCESS_CONTROLS.getName());
    assertEquals(2, DiagnosticGroups.ACCESS_CONTROLS.getTypes().length);

    // Test case 4: INVALID_CASTS
    assertEquals("invalidCasts", DiagnosticGroups.INVALID_CASTS.getName());
    assertEquals(1, DiagnosticGroups.INVALID_CASTS.getTypes().length);

    // Test case 5: FILEOVERVIEW_JSDOC
    assertEquals("fileoverviewTags", DiagnosticGroups.FILEOVERVIEW_JSDOC.getName());
    assertEquals(1, DiagnosticGroups.FILEOVERVIEW_JSDOC.getTypes().length);

    // Test case 6: STRICT_MODULE_DEP_CHECK
    assertEquals("strictModuleDepCheck", DiagnosticGroups.STRICT_MODULE_DEP_CHECK.getName());
    assertEquals(2, DiagnosticGroups.STRICT_MODULE_DEP_CHECK.getTypes().length);

    // Test case 7: EXTERNS_VALIDATION
    assertEquals("externsValidation", DiagnosticGroups.EXTERNS_VALIDATION.getName());
    assertEquals(2, DiagnosticGroups.EXTERNS_VALIDATION.getTypes().length);

    // Test case 8: AMBIGUOUS_FUNCTION_DECL
    assertEquals("ambiguousFunctionDecl", DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL.getName());
    assertEquals(1, DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL.getTypes().length);

    // Test case 9: UNKNOWN_DEFINES
    assertEquals("unknownDefines", DiagnosticGroups.UNKNOWN_DEFINES.getName());
    assertEquals(1, DiagnosticGroups.UNKNOWN_DEFINES.getTypes().length);

    // Test case 10: TWEAKS
    assertEquals("tweakValidation", DiagnosticGroups.TWEAKS.getName());
    assertEquals(3, DiagnosticGroups.TWEAKS.getTypes().length);

    // Test case 11: MISSING_PROPERTIES
    assertEquals("missingProperties", DiagnosticGroups.MISSING_PROPERTIES.getName());
    assertEquals(1, DiagnosticGroups.MISSING_PROPERTIES.getTypes().length);

    // Test case 12: INTERNET_EXPLORER_CHECKS
    assertEquals("internetExplorerChecks", DiagnosticGroups.INTERNET_EXPLORER_CHECKS.getName());
    assertEquals(1, DiagnosticGroups.INTERNET_EXPLORER_CHECKS.getTypes().length);

    // Test case 13: UNDEFINED_VARIABLES
    assertEquals("undefinedVars", DiagnosticGroups.UNDEFINED_VARIABLES.getName());
    assertEquals(1, DiagnosticGroups.UNDEFINED_VARIABLES.getTypes().length);

    // Test case 14: CHECK_REGEXP
    assertEquals("checkRegExp", DiagnosticGroups.CHECK_REGEXP.getName());
    assertEquals(1, DiagnosticGroups.CHECK_REGEXP.getTypes().length);

    // Test case 15: CHECK_TYPES
    assertEquals("checkTypes", DiagnosticGroups.CHECK_TYPES.getName());
    assertEquals(70, DiagnosticGroups.CHECK_TYPES.getTypes().length);

    // Test case 16: CHECK_VARIABLES
    assertEquals("checkVars", DiagnosticGroups.CHECK_VARIABLES.getName());
    assertEquals(2, DiagnosticGroups.CHECK_VARIABLES.getTypes().length);

    // Test case 17: CHECK_USELESS_CODE
    assertEquals("uselessCode", DiagnosticGroups.CHECK_USELESS_CODE.getName());
    assertEquals(2, DiagnosticGroups.CHECK_USELESS_CODE.getTypes().length);

    // Test case 18: TYPE_INVALIDATION
    assertEquals("typeInvalidation", DiagnosticGroups.TYPE_INVALIDATION.getName());
    assertEquals(1, DiagnosticGroups.TYPE_INVALIDATION.getTypes().length);
  }
}