import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupWithStringAndDiagnosticGroup() {
    DiagnosticGroup group = new DiagnosticGroup();
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", group);
    assertEquals(group, result);
  }

  @Test
  public void testRegisterGroupWithStringAndDiagnosticTypeArray() {
    DiagnosticType[] types = { new DiagnosticType(), new DiagnosticType() };
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", types);
    assertEquals("testGroup", result.getName());
    assertTrue(result.getTypes().contains(types[0]));
    assertTrue(result.getTypes().contains(types[1]));
  }

  @Test
  public void testRegisterGroupWithStringAndDiagnosticGroupArray() {
    DiagnosticGroup[] groups = { new DiagnosticGroup(), new DiagnosticGroup() };
    DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", groups);
    assertEquals("testGroup", result.getName());
    assertTrue(result.getGroups().contains(groups[0]));
    assertTrue(result.getGroups().contains(groups[1]));
  }

  @Test
  public void testGetRegisteredGroups() {
    // TODO: Write test case to check if the map returned by getRegisteredGroups() contains all the registered groups
  }

  @Test
  public void testForName() {
    // TODO: Write test case to check if the correct diagnostic group is returned for a given group name
  }

  @Test
  public void testSetWarningLevels() {
    // TODO: Write test case to check if the warning levels are correctly set for the CompilerOptions given a list of diagnostic group names
  }

  @Test
  public void testGLOBAL_THIS() {
    // TODO: Write test case to check if the GLOBAL_THIS diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testDEPRECATED() {
    // TODO: Write test case to check if the DEPRECATED diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testVISIBILITY() {
    // TODO: Write test case to check if the VISIBILITY diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testCONSTANT_PROPERTY() {
    // TODO: Write test case to check if the CONSTANT_PROPERTY diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testNON_STANDARD_JSDOC() {
    // TODO: Write test case to check if the NON_STANDARD_JSDOC diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testACCESS_CONTROLS() {
    // TODO: Write test case to check if the ACCESS_CONTROLS diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testINVALID_CASTS() {
    // TODO: Write test case to check if the INVALID_CASTS diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testFILEOVERVIEW_JSDOC() {
    // TODO: Write test case to check if the FILEOVERVIEW_JSDOC diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testSTRICT_MODULE_DEP_CHECK() {
    // TODO: Write test case to check if the STRICT_MODULE_DEP_CHECK diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testEXTERNS_VALIDATION() {
    // TODO: Write test case to check if the EXTERNS_VALIDATION diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testAMBIGUOUS_FUNCTION_DECL() {
    // TODO: Write test case to check if the AMBIGUOUS_FUNCTION_DECL diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testUNKNOWN_DEFINES() {
    // TODO: Write test case to check if the UNKNOWN_DEFINES diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testTWEAKS() {
    // TODO: Write test case to check if the TWEAKS diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testMISSING_PROPERTIES() {
    // TODO: Write test case to check if the MISSING_PROPERTIES diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testINTERNET_EXPLORER_CHECKS() {
    // TODO: Write test case to check if the INTERNET_EXPLORER_CHECKS diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testUNDEFINED_VARIABLES() {
    // TODO: Write test case to check if the UNDEFINED_VARIABLES diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testCHECK_REGEXP() {
    // TODO: Write test case to check if the CHECK_REGEXP diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testCHECK_TYPES() {
    // TODO: Write test case to check if the CHECK_TYPES diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testCHECK_VARIABLES() {
    // TODO: Write test case to check if the CHECK_VARIABLES diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testCHECK_USELESS_CODE() {
    // TODO: Write test case to check if the CHECK_USELESS_CODE diagnostic group is correctly registered and contains the correct diagnostic types
  }

  @Test
  public void testTYPE_INVALIDATION() {
    // TODO: Write test case to check if the TYPE_INVALIDATION diagnostic group is correctly registered and contains the correct diagnostic types
  }
}