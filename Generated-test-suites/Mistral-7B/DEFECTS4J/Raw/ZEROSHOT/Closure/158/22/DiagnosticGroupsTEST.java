import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertEquals(21, groups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertNotNull(deprecated);
    assertNotNull(accessControls);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupString() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", new DiagnosticType[0]);
    assertNotNull(group);
    assertEquals("testGroup", group.getName());
  }

  @Test
  public void testRegisterGroupStringDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", CheckAccessControls.DEPRECATED_NAME);
    assertNotNull(group);
    assertEquals("testGroup", group.getName());
  }

  @Test
  public void testRegisterGroupStringDiagnosticTypeArray() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup",
        CheckAccessControls.DEPRECATED_NAME, CheckAccessControls.DEPRECATED_NAME_REASON);
    assertNotNull(group);
    assertEquals("testGroup", group.getName());
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = new DiagnosticGroup("testGroup", new DiagnosticType[0]);
    diagnosticGroups.registerGroup("testGroup2", group);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName("testGroup2");
    assertSame(group, registeredGroup);
  }

  @Test
  public void testRegisterGroupAlreadyRegistered() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.registerGroup("deprecated", new DiagnosticType[0]);
    DiagnosticGroup deprecated2 = diagnosticGroups.registerGroup("deprecated", new DiagnosticType[0]);
    assertEquals(deprecated, deprecated2);
  }

  @Test
  public void testDiagnosticGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertEquals("accessControls", ACCESS_CONTROLS.getName());
    assertEquals("ambiguousFunctionDecl", AMBIGUOUS_FUNCTION_DECL.getName());
    // ... add assertions for all the other groups
  }
}