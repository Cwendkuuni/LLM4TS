import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.ParserTestingUtils;
import com.google.javascript.jscomp.test.TestSourceFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    List<String> diagnosticGroupNames = ParserTestingUtils.asList(DIAGNOSTIC_GROUP_NAMES);
    for (String groupName : diagnosticGroupNames) {
      assertTrue(registeredGroups.containsKey(groupName));
    }
  }

  @org.junit.Test
  public void testFindGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup expectedAccessControls = ACCESS_CONTROLS;
    assertSame(expectedAccessControls, accessControls);
  }

  @org.junit.Test
  public void testSetWarningLevels() throws IOException {
    String sourceCode = "function test() {}\n";
    TestSourceFile sourceFile = new TestSourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupsToSet = ParserTestingUtils.asList("checkTypes", "checkVars");
    CheckLevel checkTypeLevel = CheckLevel.WARNING;
    CheckLevel checkVarLevel = CheckLevel.ERROR;

    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToSet, checkTypeLevel);
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToSet, checkVarLevel);

    Compiler compiler = new Compiler();
    Diagnostics diagnostics = compiler.check(sourceFile, options);

    int checkTypesCount = 0;
    int checkVarsCount = 0;
    for (Diagnostics.Message message : diagnostics.getMessages()) {
      DiagnosticGroup diagnosticGroup = diagnosticGroups.forName(message.group);
      if (diagnosticGroup == CHECK_TYPES) {
        checkTypesCount++;
      } else if (diagnosticGroup == CHECK_VARS) {
        checkVarsCount++;
      }
    }

    // Since the source code doesn't contain any issues, the number of messages for each group should match their respective warning levels.
    assertTrue(checkTypesCount > 0 && checkTypesCount <= diagnostics.size());
    assertTrue(checkVarsCount > 0 && checkVarsCount <= diagnostics.size());

    // The number of messages for groups not set should be zero.
    DiagnosticGroup unusedGroup = diagnosticGroups.forName("unusedGroup");
    int unusedGroupCount = 0;
    for (Diagnostics.Message message : diagnostics.getMessages()) {
      if (message.group.equals(unusedGroup.getName())) {
        unusedGroupCount++;
      }
    }

    assertTrue(unusedGroupCount == 0);
  }
}