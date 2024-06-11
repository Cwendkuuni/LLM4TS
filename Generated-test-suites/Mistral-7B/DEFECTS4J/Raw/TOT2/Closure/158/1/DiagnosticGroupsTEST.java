import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> groups = DiagnosticGroups.getRegisteredGroups();
    assertNotNull(groups);
  }

  @Test
  public void testForName() {
    DiagnosticGroup group = DiagnosticGroups.forName("VISIBILITY");
    assertNotNull(group);
  }

  // Add test cases for registering DiagnosticGroups
}