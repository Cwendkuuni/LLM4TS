import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(groups);
  }
  
  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("accessControls");
    assertNotNull(group);
  }
  
  @Test
  public void testRegisterGroupWithGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("test", group1, group2);
    assertEquals(group1, registeredGroup.getContainedGroups().get(0));
    assertEquals(group2, registeredGroup.getContainedGroups().get(1));
  }
  
  @Test
  public void testRegisterGroupWithNameAndTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticType type2 = new DiagnosticType("type2");
    DiagnosticGroup group = diagnosticGroups.registerGroup("test", type1, type2);
    assertEquals(type1, group.getTypes().get(0));
    assertEquals(type2, group.getTypes().get(1));
  }
  
  @Test
  public void testRegisterGroupWithNameAndGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticType type1 = new DiagnosticType("type1");
    DiagnosticType type2 = new DiagnosticType("type2");
    DiagnosticGroup group1 = new DiagnosticGroup("group1", type1);
    DiagnosticGroup group2 = new DiagnosticGroup("group2", type2);
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("test", group1, group2);
    assertEquals(group1, registeredGroup.getContainedGroups().get(0));
    assertEquals(group2, registeredGroup.getContainedGroups().get(1));
  }
  
  @Test
  public void testConstantProperty() {
    assertNotNull(DiagnosticGroups.CONSTANT_PROPERTY);
  }
  
  @Test
  public void testNonStandardJsDocs() {
    assertNotNull(DiagnosticGroups.NON_STANDARD_JSDOC);
  }
  
  @Test
  public void testAccessControls() {
    assertNotNull(DiagnosticGroups.ACCESS_CONTROLS);
  }
  
  @Test
  public void testDeprecated() {
    assertNotNull(DiagnosticGroups.DEPRECATED);
  }
  
  @Test
  public void testVisibility() {
    assertNotNull(DiagnosticGroups.VISIBILITY);
  }
  
  @Test
  public void testInvalidCasts() {
    assertNotNull(DiagnosticGroups.INVALID_CASTS);
  }
  
  @Test
  public void testFileoverviewJsDoc() {
    assertNotNull(DiagnosticGroups.FILEOVERVIEW_JSDOC);
  }
  
  @Test
  public void testStrictModuleDepCheck() {
    assertNotNull(DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
  }
  
  @Test
  public void testExternsValidation() {
    assertNotNull(DiagnosticGroups.EXTERNS_VALIDATION);
  }
  
  @Test
  public void testAmbiguousFunctionDecl() {
    assertNotNull(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
  }
  
  @Test
  public void testUnknownDefines() {
    assertNotNull(DiagnosticGroups.UNKNOWN_DEFINES);
  }
  
  @Test
  public void testTweaks() {
    assertNotNull(DiagnosticGroups.TWEAKS);
  }
  
  @Test
  public void testMissingProperties() {
    assertNotNull(DiagnosticGroups.MISSING_PROPERTIES);
  }
  
  @Test
  public void testInternetExplorerChecks() {
    assertNotNull(DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
  }
  
  @Test
  public void testUndefinedVariables() {
    assertNotNull(DiagnosticGroups.UNDEFINED_VARIABLES);
  }
  
  @Test
  public void testCheckRegExp() {
    assertNotNull(DiagnosticGroups.CHECK_REGEXP);
  }
  
  @Test
  public void testCheckTypes() {
    assertNotNull(DiagnosticGroups.CHECK_TYPES);
  }
  
  @Test
  public void testCheckVariables() {
    assertNotNull(DiagnosticGroups.CHECK_VARIABLES);
  }
  
  @Test
  public void testCheckUselessCode() {
    assertNotNull(DiagnosticGroups.CHECK_USELESS_CODE);
  }
  
  @Test
  public void testTypeInvalidation() {
    assertNotNull(DiagnosticGroups.TYPE_INVALIDATION);
  }
}