import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CompilerOptions;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    DiagnosticGroups actualGroups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedGroups = List.of("accessControls", "ambiguousFunctionDecl",
        "checkRegExp", "checkTypes", "checkVars", "constantProperty", "deprecated",
        "externsValidation", "fileoverviewTags", "globalThis", "invalidCasts",
        "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck",
        "typeInvalidation", "undefinedVars", "uselessCode", "visibility");

    assertTrue(actualGroups.size() == expectedGroups.size());

    for (String expectedGroupName : expectedGroups) {
      DiagnosticGroup actualGroup = actualGroups.get(expectedGroupName);
      assertNotNull(actualGroup);
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup actualAccessControlsGroup = diagnosticGroups.forName("accessControls");
    DiagnosticGroup actualDeprecatedGroup = diagnosticGroups.forName("deprecated");

    assertNotNull(actualAccessControlsGroup);
    assertNotNull(actualDeprecatedGroup);

    assertTrue(actualAccessControlsGroup instanceof DiagnosticGroup);
    assertTrue(actualDeprecatedGroup instanceof DiagnosticGroup);

    DiagnosticType actualDeprecatedType = null;
    for (DiagnosticType diagnosticType : actualDeprecatedGroup.getDiagnostics()) {
      if (diagnosticType.getGroupId().equals("DEPRECATED_NAME")) {
        actualDeprecatedType = diagnosticType;
        break;
      }
    }

    assertNotNull(actualDeprecatedType);
  }
}