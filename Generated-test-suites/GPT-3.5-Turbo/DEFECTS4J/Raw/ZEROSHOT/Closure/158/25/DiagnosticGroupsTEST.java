import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertNotNull(diagnosticGroups.getRegisteredGroups());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("globalThis");
    assertNotNull(group);
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup");
    assertNotNull(group);
  }
}