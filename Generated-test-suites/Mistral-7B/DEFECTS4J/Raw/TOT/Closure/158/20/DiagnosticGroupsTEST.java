import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup newGroup = DiagnosticGroups.registerGroup("newGroup");
    assertNotNull(newGroup);
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    assertNotNull(registeredGroups);
    assertNotNull(registeredGroups.get("deprecated"));
    assertNotNull(registeredGroups.get("accessControls"));

    DiagnosticGroup newGroup = DiagnosticGroups.registerGroup("newGroup");
    registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups.get("newGroup"));
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;

    assertNotNull(diagnosticGroups.forName("deprecated"));
    assertNotNull(diagnosticGroups.forName("accessControls"));

    DiagnosticGroup newGroup = DiagnosticGroups.registerGroup("newGroup");
    assertNotNull(diagnosticGroups.forName("newGroup"));
  }
}