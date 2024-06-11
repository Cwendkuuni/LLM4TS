import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.parse.Config;
import com.google.javascript.jscomp.errors.ErrorReporter;
import com.google.javascript.jscomp.node.RootNode;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagGroups = new DiagnosticGroups();
    DiagnosticGroup globalThisGroup = diagGroups.GLOBAL_THIS;
    DiagnosticGroup deprecatedGroup = diagGroups.DEPRECATED;
    DiagnosticGroup visibilityGroup = diagGroups.VISIBILITY;
    DiagnosticGroup constantPropertyGroup = diagGroups.CONSTANT_PROPERTY;
    // Add more assertions for other registered groups if needed

    assertNotNull(globalThisGroup);
    assertNotNull(deprecatedGroup);
    assertNotNull(visibilityGroup);
    assertNotNull(constantPropertyGroup);
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagGroups.getRegisteredGroups();

    assertEquals(21, registeredGroups.size()); // 21 groups in the class

    // Check some groups
    DiagnosticGroup globalThisGroup = registeredGroups.get("globalThis");
    DiagnosticGroup deprecatedGroup = registeredGroups.get("deprecated");
    DiagnosticGroup visibilityGroup = registeredGroups.get("visibility");
    DiagnosticGroup constantPropertyGroup = registeredGroups.get("constantProperty");

    assertNotNull(globalThisGroup);
    assertNotNull(deprecatedGroup);
    assertNotNull(visibilityGroup);
    assertNotNull(constantPropertyGroup);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagGroups = new DiagnosticGroups();

    DiagnosticGroup globalThisGroup = diagGroups.forName("globalThis");
    DiagnosticGroup deprecatedGroup = diagGroups.forName("deprecated");
    DiagnosticGroup visibilityGroup = diagGroups.forName("visibility");
    DiagnosticGroup constantPropertyGroup = diagGroups.forName("constantProperty");

    assertNotNull(globalThisGroup);
    assertNotNull(deprecatedGroup);
    assertNotNull(visibilityGroup);
    assertNotNull(constantPropertyGroup);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions(new Config());
    DiagnosticGroups diagGroups = new DiagnosticGroups();
    List<String> diagnosticGroups = asList("ACCESS_CONTROLS", "INVALID_CASTS");

    diagGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagGroups.ACCESS_CONTROLS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagGroups.INVALID_CASTS));
  }

  @Test
  public void testCompileWithWarningLevels() {
    String sourceCode = "function foo() {}";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new Config());
    ErrorReporter errorReporter = new ErrorReporter() {};
    RootNode node = parser(sourceCode);

    diagGroups.setWarningLevels(options, asList("INVALID_CASTS"), CheckLevel.ERROR);

    compiler.setOptions(options);
    compiler.setErrorReporter(errorReporter);
    compiler.compile(node, null, null);

    // Check if the expected errors were reported
    // based on the set warning levels
    // Add error checks here for your specific test cases
  }
}