import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(25, registeredGroups.size());

    assertNotNull(registeredGroups.get("accessControls"));
    assertNotNull(registeredGroups.get("ambiguousFunctionDecl"));
    assertNotNull(registeredGroups.get("checkRegExp"));
    // Add more assertions for all registered groups
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);

    DiagnosticGroup unknown = diagnosticGroups.forName("unknown");
    assertNull(unknown);
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String customGroupName = "customGroup";
    DiagnosticGroup customGroup = new DiagnosticGroup(customGroupName, new DiagnosticType[0]);
    diagnosticGroups.registerGroup(customGroupName, customGroup);

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(26, registeredGroups.size());
    assertNotNull(registeredGroups.get(customGroupName));
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterNullGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, new DiagnosticType[0]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterEmptyGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("", new DiagnosticType[0]);
  }
}