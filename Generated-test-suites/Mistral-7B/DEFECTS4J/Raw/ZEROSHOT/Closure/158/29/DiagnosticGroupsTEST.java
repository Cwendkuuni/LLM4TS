import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.parser.Parser;
import com.google.javascript.jscomp.parser.parserstats.ParserStats;

public class DiagnosticGroupsTest {

  private static Compiler compiler;
  private static CompilerOptions options;

  @BeforeClass
  public static void setup() {
    compiler = new Compiler();
    options = new CompilerOptions();
  }

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    List<String> groupNames = ImmutableList.of("accessControls", "ambiguousFunctionDecl", "checkRegExp",
        "checkTypes", "checkVars", "constantProperty", "deprecated", "externsValidation",
        "fileoverviewTags", "globalThis", "internetExplorerChecks", "invalidCasts", "missingProperties",
        "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "uselessCode",
        "visibility");
    for (String name : groupNames) {
      DiagnosticGroup group = registeredGroups.get(name);
      assertNotNull("Missing registered group: " + name, group);
    }
  }

  @Test
  public void testGetGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControlsGroup = diagnosticGroups.forName("accessControls");
    DiagnosticGroup checkTypesGroup = diagnosticGroups.forName("checkTypes");
    assertNotNull("Missing group by name: accessControls", accessControlsGroup);
    assertNotNull("Missing group by name: checkTypes", checkTypesGroup);
    assertTrue("accessControls and checkTypes are not the same: " + accessControlsGroup + " " + checkTypesGroup,
        accessControlsGroup != checkTypesGroup);
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupsToEnable = ImmutableList.of("checkTypes", "checkVars");
    DiagnosticType testDiagnosticType = CheckerFactory.getCheckersByType(DiagnosticType.class)[0];
    DiagnosticType checkTypesDiagnosticType = CheckerFactory.getCheckersByType(CheckTypes.class)[0];
    DiagnosticType checkVarsDiagnosticType = CheckerFactory.getCheckersByType(CheckVars.class)[0];

    options.setWarningLevel(Diagnostics.Type.ALL, CheckerFactory.getCheckersByType(Diagnostics.class)[0],
        Compiler.DiagnosticPair.Level.OFF);

    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToEnable, Compiler.DiagnosticPair.Level.WARNING);

    Parser parser = new Parser(options);
    String sourceCode = "function foo() {" + "var x = 1; " + "var x = 2; " + "}";
    ParserStats parserStats = parser.parse(sourceCode, "Test.java", null);
    Diagnostics diagnostics = compiler.getErrors();

    int numberOfCheckTypesDiagnostic = 0;
    int numberOfCheckVarsDiagnostic = 0;
    int numberOfTestDiagnostic = 0;

    for (DiagnosticType diagnostic : diagnostics.getDiagnostics()) {
      if (diagnostic.getCode() == checkTypesDiagnosticType.getCode()) {
        numberOfCheckTypesDiagnostic++;
      } else if (diagnostic.getCode() == checkVarsDiagnosticType.getCode()) {
        numberOfCheckVarsDiagnostic++;
      } else if (diagnostic.getCode() == testDiagnosticType.getCode()) {
        numberOfTestDiagnostic++;
      }
    }

    assertTrue("checkTypes diagnostic should be enabled", numberOfCheckTypesDiagnostic > 0);
    assertTrue("checkVars diagnostic should be enabled", numberOfCheckVarsDiagnostic > 0);
    assertTrue("test diagnostic should not be enabled", numberOfTestDiagnostic == 0);
  }
}