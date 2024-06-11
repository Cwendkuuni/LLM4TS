import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    assertEquals(21, registeredGroups.size());

    // Check that all registered groups are present in the map
    List<String> groupNames = JavaCollections.asList(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp",
        "checkTypes", "constantProperty", "deprecated", "externsValidation",
        "fileoverviewTags", "globalThis", "internetExplorerChecks",
        "invalidCasts", "missingProperties", "nonStandardJsDocs",
        "strictModuleDepCheck", "typeInvalidation", "undefinedVars",
        "uselessCode", "visibility");

    for (String groupName : groupNames) {
      assertTrue(registeredGroups.containsKey(groupName));
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecatedGroup);
    assertEquals("deprecated", deprecatedGroup.getName());

    DiagnosticGroup invalidCastsGroup = diagnosticGroups.forName("invalidCasts");
    assertNotNull(invalidCastsGroup);
    assertEquals("invalidCasts", invalidCastsGroup.getName());

    DiagnosticGroup unknownGroup = diagnosticGroups.forName("unknownGroup");
    assertNull(unknownGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }
}