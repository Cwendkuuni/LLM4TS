import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(23, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup globalThis = diagnosticGroups.forName("globalThis");
    assertNotNull(globalThis);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupStringDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.registerGroup("deprecated", DEPRECATED_NAME);
    assertNotNull(deprecated);
  }

  @Test
  public void testRegisterGroupStringDiagnosticTypeString() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.registerGroup("deprecated", DEPRECATED_NAME, DEPRECATED_NAME_REASON);
    assertNotNull(deprecated);
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup visibility = new DiagnosticGroup("visibility");
    DiagnosticGroup accessControls = diagnosticGroups.registerGroup("accessControls", visibility);
    assertEquals(visibility, accessControls);
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroupArray() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = new DiagnosticGroup("deprecated");
    DiagnosticGroup visibility = new DiagnosticGroup("visibility");
    DiagnosticGroup accessControls = diagnosticGroups.registerGroup("accessControls", deprecated, visibility);
    assertEquals(deprecated, accessControls.getDiagnosticTypes()[0]);
    assertEquals(visibility, accessControls.getDiagnosticTypes()[1]);
  }
}