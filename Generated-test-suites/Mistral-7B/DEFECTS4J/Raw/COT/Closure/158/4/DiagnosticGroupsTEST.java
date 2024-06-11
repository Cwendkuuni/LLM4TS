import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import java.util.List;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals("Size should match number of registered groups.", 16, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull("ForName should return DEPRECATED group.", deprecated);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupStringDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup diagnosticGroup = diagnosticGroups.registerGroup("testGroup", DIAGNOSTIC_TYPE_WARNING);
    assertNotNull("RegisterGroup should return created diagnostic group.", diagnosticGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupStringInvalidDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("testGroup", DIAGNOSTIC_TYPE_ERROR);
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup testGroup = new DiagnosticGroup("testGroup");
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", testGroup);
    assertEquals("Registered group should be the same as passed group.", testGroup, registeredGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupStringNullDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("testGroup", null);
  }
}