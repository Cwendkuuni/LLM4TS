import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup1() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup1");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup1", group);
    assertEquals(group, result);
  }
  
  @Test
  public void testRegisterGroup2() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup2", new DiagnosticType());
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup2", group);
    assertEquals(group, result);
  }
  
  @Test
  public void testRegisterGroup3() {
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup3");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup4");
    DiagnosticGroup group = new DiagnosticGroup("testGroup3", group1, group2);
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup3", group);
    assertEquals(group, result);
  }
  
  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> result = diagnosticGroups.getRegisteredGroups();
    assertEquals(0, result.size());
  }
  
  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup result = diagnosticGroups.forName("testGroup5");
    assertEquals(null, result);
  }
  
  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("testGroup6");
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.forName("testGroup6")));
  }
  
  @Test
  public void testGLOBAL_THIS() {
    assertEquals("globalThis", DiagnosticGroups.GLOBAL_THIS.getName());
  }
  
  @Test
  public void testDEPRECATED() {
    List<DiagnosticType> types = DiagnosticGroups.DEPRECATED.getTypes();
    assertEquals(6, types.size());
  }
  
  @Test
  public void testVISIBILITY() {
    List<DiagnosticType> types = DiagnosticGroups.VISIBILITY.getTypes();
    assertEquals(5, types.size());
  }
  
  @Test
  public void testCONSTANT_PROPERTY() {
    List<DiagnosticType> types = DiagnosticGroups.CONSTANT_PROPERTY.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testNON_STANDARD_JSDOC() {
    List<DiagnosticType> types = DiagnosticGroups.NON_STANDARD_JSDOC.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testACCESS_CONTROLS() {
    List<DiagnosticType> types = DiagnosticGroups.ACCESS_CONTROLS.getTypes();
    assertEquals(11, types.size());
  }
  
  @Test
  public void testINVALID_CASTS() {
    List<DiagnosticType> types = DiagnosticGroups.INVALID_CASTS.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testFILEOVERVIEW_JSDOC() {
    List<DiagnosticType> types = DiagnosticGroups.FILEOVERVIEW_JSDOC.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testSTRICT_MODULE_DEP_CHECK() {
    List<DiagnosticType> types = DiagnosticGroups.STRICT_MODULE_DEP_CHECK.getTypes();
    assertEquals(2, types.size());
  }
  
  @Test
  public void testEXTERNS_VALIDATION() {
    List<DiagnosticType> types = DiagnosticGroups.EXTERNS_VALIDATION.getTypes();
    assertEquals(2, types.size());
  }
  
  @Test
  public void testAMBIGUOUS_FUNCTION_DECL() {
    List<DiagnosticType> types = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testUNKNOWN_DEFINES() {
    List<DiagnosticType> types = DiagnosticGroups.UNKNOWN_DEFINES.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testTWEAKS() {
    List<DiagnosticType> types = DiagnosticGroups.TWEAKS.getTypes();
    assertEquals(3, types.size());
  }
  
  @Test
  public void testMISSING_PROPERTIES() {
    List<DiagnosticType> types = DiagnosticGroups.MISSING_PROPERTIES.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testINTERNET_EXPLORER_CHECKS() {
    List<DiagnosticType> types = DiagnosticGroups.INTERNET_EXPLORER_CHECKS.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testUNDEFINED_VARIABLES() {
    List<DiagnosticType> types = DiagnosticGroups.UNDEFINED_VARIABLES.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testCHECK_REGEXP() {
    List<DiagnosticType> types = DiagnosticGroups.CHECK_REGEXP.getTypes();
    assertEquals(1, types.size());
  }
  
  @Test
  public void testCHECK_TYPES() {
    List<DiagnosticType> types = DiagnosticGroups.CHECK_TYPES.getTypes();
    assertEquals(4, types.size());
  }
  
  @Test
  public void testCHECK_VARIABLES() {
    List<DiagnosticType> types = DiagnosticGroups.CHECK_VARIABLES.getTypes();
    assertEquals(2, types.size());
  }
  
  @Test
  public void testCHECK_USELESS_CODE() {
    List<DiagnosticType> types = DiagnosticGroups.CHECK_USELESS_CODE.getTypes();
    assertEquals(2, types.size());
  }
  
  @Test
  public void testTYPE_INVALIDATION() {
    List<DiagnosticType> types = DiagnosticGroups.TYPE_INVALIDATION.getTypes();
    assertEquals(1, types.size());
  }
}