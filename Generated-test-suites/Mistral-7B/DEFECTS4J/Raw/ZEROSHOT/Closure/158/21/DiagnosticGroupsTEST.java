import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    assertEquals(21, registeredGroups.size());

    // Check that all registered groups are present
    List<String> groupNames = JavaUtil.asList("accessControls", "ambiguousFunctionDecl",
        "checkRegExp", "checkTypes", "constantProperty", "deprecated", "externsValidation",
        "fileoverviewTags", "globalThis", "internetExplorerChecks", "invalidCasts",
        "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation",
        "unusedVariables", "unknownDefines", "uselessCode", "visibility");

    for (String name : groupNames) {
      assertTrue(registeredGroups.containsKey(name));
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup accessControls = forName(diagnosticGroups, "accessControls");
    assertNotNull(accessControls);
    assertEquals("accessControls", accessControls.getName());

    DiagnosticGroup nonStandardJsDocs = forName(diagnosticGroups, "nonStandardJsDocs");
    assertNotNull(nonStandardJsDocs);
    assertEquals("nonStandardJsDocs", nonStandardJsDocs.getName());

    assertNull(forName(diagnosticGroups, "unknownGroup"));
  }
}