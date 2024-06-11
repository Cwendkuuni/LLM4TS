import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import com.google.common.collect.ImmutableMap;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    // Check that the registered groups are present in the map
    ImmutableMap<String, DiagnosticGroup> expectedGroups =
        ImmutableMap.of(
            "accessControls", ACCESS_CONTROLS,
            "ambiguousFunctionDecl", AMBIGUOUS_FUNCTION_DECL,
            "checkRegExp", CHECK_REGEXP,
            "checkTypes", CHECK_TYPES,
            "constantProperty", CONSTANT_PROPERTY,
            "externsValidation", EXTERNS_VALIDATION,
            "fileoverviewTags", FILEOVERVIEW_JSDOC,
            "globalThis", GLOBAL_THIS,
            "invalidCasts", INVALID_CASTS,
            "internetExplorerChecks", INTERNET_EXPLORER_CHECKS,
            "missingProperties", MISSING_PROPERTIES,
            "nonStandardJsDocs", NON_STANDARD_JSDOC,
            "strictModuleDepCheck", STRICT_MODULE_DEP_CHECK,
            "typeInvalidation", TYPE_INVALIDATION,
            "undefinedVars", UNDEFINED_VARIABLES,
            "visibility", VISIBILITY
        );

    assertEquals(expectedGroups, registeredGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup deprecated = forName(DiagnosticGroups.class, "deprecated");
    DiagnosticGroup visibility = forName(DiagnosticGroups.class, "visibility");

    assertNotNull(deprecated);
    assertNotNull(visibility);

    assertEquals("deprecated", deprecated.getName());
    assertEquals("visibility", visibility.getName());
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    forName(diagnosticGroups, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForNameInvalidName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    forName(diagnosticGroups, "InvalidName");
  }

  @Test(expected = IllegalStateException.class)
  public void testForNameUnknownDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    forName(diagnosticGroups, "UnknownGroup");
  }
}