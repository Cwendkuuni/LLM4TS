import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertEquals(diagnosticGroups.getRegisteredGroups(), DiagnosticGroups.groupsByName);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertEquals(diagnosticGroups.forName("globalThis"), DiagnosticGroups.GLOBAL_THIS);
    assertEquals(diagnosticGroups.forName("deprecated"), DiagnosticGroups.DEPRECATED);
    assertEquals(diagnosticGroups.forName("visibility"), DiagnosticGroups.VISIBILITY);
    assertEquals(diagnosticGroups.forName("constantProperty"), DiagnosticGroups.CONSTANT_PROPERTY);
    assertEquals(diagnosticGroups.forName("nonStandardJsDocs"), DiagnosticGroups.NON_STANDARD_JSDOC);
    assertEquals(diagnosticGroups.forName("accessControls"), DiagnosticGroups.ACCESS_CONTROLS);
    assertEquals(diagnosticGroups.forName("invalidCasts"), DiagnosticGroups.INVALID_CASTS);
    assertEquals(diagnosticGroups.forName("fileoverviewTags"), DiagnosticGroups.FILEOVERVIEW_JSDOC);
    assertEquals(diagnosticGroups.forName("strictModuleDepCheck"), DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
    assertEquals(diagnosticGroups.forName("externsValidation"), DiagnosticGroups.EXTERNS_VALIDATION);
    assertEquals(diagnosticGroups.forName("ambiguousFunctionDecl"), DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
    assertEquals(diagnosticGroups.forName("unknownDefines"), DiagnosticGroups.UNKNOWN_DEFINES);
    assertEquals(diagnosticGroups.forName("tweakValidation"), DiagnosticGroups.TWEAKS);
    assertEquals(diagnosticGroups.forName("missingProperties"), DiagnosticGroups.MISSING_PROPERTIES);
    assertEquals(diagnosticGroups.forName("internetExplorerChecks"), DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
    assertEquals(diagnosticGroups.forName("undefinedVars"), DiagnosticGroups.UNDEFINED_VARIABLES);
    assertEquals(diagnosticGroups.forName("checkRegExp"), DiagnosticGroups.CHECK_REGEXP);
    assertEquals(diagnosticGroups.forName("checkTypes"), DiagnosticGroups.CHECK_TYPES);
    assertEquals(diagnosticGroups.forName("checkVars"), DiagnosticGroups.CHECK_VARIABLES);
    assertEquals(diagnosticGroups.forName("uselessCode"), DiagnosticGroups.CHECK_USELESS_CODE);
    assertEquals(diagnosticGroups.forName("typeInvalidation"), DiagnosticGroups.TYPE_INVALIDATION);
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> groupNames = Arrays.asList("accessControls", "deprecated");
    diagnosticGroups.setWarningLevels(options, groupNames, CheckLevel.WARNING);
    assertEquals(options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS), CheckLevel.WARNING);
    assertEquals(options.getWarningLevel(DiagnosticGroups.DEPRECATED), CheckLevel.WARNING);
    assertEquals(options.getWarningLevel(DiagnosticGroups.VISIBILITY), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.CONSTANT_PROPERTY), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.NON_STANDARD_JSDOC), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.INVALID_CASTS), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.FILEOVERVIEW_JSDOC), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.STRICT_MODULE_DEP_CHECK), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.EXTERNS_VALIDATION), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.UNKNOWN_DEFINES), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.TWEAKS), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.MISSING_PROPERTIES), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.INTERNET_EXPLORER_CHECKS), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.UNDEFINED_VARIABLES), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.CHECK_REGEXP), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.CHECK_TYPES), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.CHECK_VARIABLES), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.CHECK_USELESS_CODE), CheckLevel.OFF);
    assertEquals(options.getWarningLevel(DiagnosticGroups.TYPE_INVALIDATION), CheckLevel.OFF);
  }

}