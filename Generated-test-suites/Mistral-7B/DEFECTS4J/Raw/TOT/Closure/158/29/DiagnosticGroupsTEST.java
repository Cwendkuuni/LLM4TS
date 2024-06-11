import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;
import static com.google.javascript.jscomp.CompilerOptions.WarningLevel.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.SourceFile;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetGroups() {
    DiagnosticGroup globalThisGroup = registerGroup("globalThis", CheckGlobalThis.GLOBAL_THIS);
    DiagnosticGroup deprecatedGroup = registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    DiagnosticGroup visibilityGroup = registerGroup("visibility",
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH);

    Map<String, DiagnosticGroup> groups = getRegisteredGroups();

    assertEquals(3, groups.size());
    assertEquals(globalThisGroup, groups.get("globalThis"));
    assertEquals(deprecatedGroup, groups.get("deprecated"));
    assertEquals(visibilityGroup, groups.get("visibility"));
  }

  @Test
  public void testFindGroupByName() {
    DiagnosticGroup globalThisGroup = registerGroup("globalThis", CheckGlobalThis.GLOBAL_THIS);
    DiagnosticGroup deprecatedGroup = registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    DiagnosticGroup visibilityGroup = registerGroup("visibility",
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH);

    DiagnosticGroup foundGroup = forName("deprecated");
    assertEquals(deprecatedGroup, foundGroup);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = asList("deprecated", "visibility");

    DiagnosticGroups diagnosticGroupsInstance = new DiagnosticGroups();
    diagnosticGroupsInstance.setWarningLevels(options, diagnosticGroups, WARNING);

    DiagnosticGroup deprecatedGroup = forName("deprecated");
    DiagnosticGroup visibilityGroup = forName("visibility");

    assertEquals(WARNING, options.getWarningLevel(deprecatedGroup));
    assertEquals(WARNING, options.getWarningLevel(visibilityGroup));
    assertEquals(ERROR, options.getWarningLevel(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testCompileWithWarningLevels() {
    SourceFile input = new SourceFile("Test.java", "class Test {}\n");
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(ACCESS_CONTROLS, ERROR);

    List<String> diagnosticGroups = asList("deprecated", "visibility");
    diagnosticGroupsInstance.setWarningLevels(options, diagnosticGroups, WARNING);

    DiagnosticType diagnostic = compiler.compile(input, options).getFirstDiagnostic();

    assertNull(diagnostic);
  }

  @Test
  public void testCompileWithErrorLevel() {
    SourceFile input = new SourceFile("Test.java", "class Test {}\n");
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(GLOBAL_THIS, ERROR);

    List<String> diagnosticGroups = asList("deprecated", "visibility");
    diagnosticGroupsInstance.setWarningLevels(options, diagnosticGroups, WARNING);

    DiagnosticType diagnostic = compiler.compile(input, options).getFirstDiagnostic();

    assertNotNull(diagnostic);
  }
}