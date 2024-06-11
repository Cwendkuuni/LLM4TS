import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;

public class DiagnosticGroupsTest {
  
  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertNotNull(diagnosticGroups.getRegisteredGroups());
  }
  
  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertNotNull(diagnosticGroups.forName("accessControls"));
    assertNotNull(diagnosticGroups.forName("ambiguousFunctionDecl"));
    assertNotNull(diagnosticGroups.forName("checkRegExp"));
    assertNotNull(diagnosticGroups.forName("checkTypes"));
    assertNotNull(diagnosticGroups.forName("checkVars"));
    assertNotNull(diagnosticGroups.forName("constantProperty"));
    assertNotNull(diagnosticGroups.forName("deprecated"));
    assertNotNull(diagnosticGroups.forName("externsValidation"));
    assertNotNull(diagnosticGroups.forName("fileoverviewTags"));
    assertNotNull(diagnosticGroups.forName("globalThis"));
    assertNotNull(diagnosticGroups.forName("internetExplorerChecks"));
    assertNotNull(diagnosticGroups.forName("invalidCasts"));
    assertNotNull(diagnosticGroups.forName("missingProperties"));
    assertNotNull(diagnosticGroups.forName("nonStandardJsDocs"));
    assertNotNull(diagnosticGroups.forName("strictModuleDepCheck"));
    assertNotNull(diagnosticGroups.forName("typeInvalidation"));
    assertNotNull(diagnosticGroups.forName("undefinedVars"));
    assertNotNull(diagnosticGroups.forName("unknownDefines"));
    assertNotNull(diagnosticGroups.forName("uselessCode"));
    assertNotNull(diagnosticGroups.forName("visibility"));
  }
  
  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("testGroup1");
    assertNotNull(group1);
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("testGroup2");
    assertNotNull(group2);
  }
  
  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.setWarningLevels(options, DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(","), CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.GLOBAL_THIS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.DEPRECATED));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.VISIBILITY));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CONSTANT_PROPERTY));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.NON_STANDARD_JSDOC));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.INVALID_CASTS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.FILEOVERVIEW_JSDOC));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.STRICT_MODULE_DEP_CHECK));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.EXTERNS_VALIDATION));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.UNKNOWN_DEFINES));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.TWEAKS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.MISSING_PROPERTIES));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.INTERNET_EXPLORER_CHECKS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.UNDEFINED_VARIABLES));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_REGEXP));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_TYPES));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_VARIABLES));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_USELESS_CODE));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.TYPE_INVALIDATION));
  }
}