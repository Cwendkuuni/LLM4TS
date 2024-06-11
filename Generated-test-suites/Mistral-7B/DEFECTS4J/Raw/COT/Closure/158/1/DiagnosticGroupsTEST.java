import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.common.collect.ImmutableMap;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertEquals(21, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupStringDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroup";
    DiagnosticType diagnosticType = new DiagnosticType("Test Diagnostic", 1, null);
    DiagnosticGroup group = diagnosticGroups.registerGroup(name, diagnosticType);
    assertNotNull(group);
    assertEquals(name, group.getName());
    assertEquals(1, group.getDiagnosticTypes().size());
    assertEquals(diagnosticType, group.getDiagnosticTypes().get(0));
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup(name, null);
    diagnosticGroups.registerGroup(name, group);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertNotNull(registeredGroup);
    assertEquals(name, registeredGroup.getName());
    assertSame(group, registeredGroup);
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroupArray() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup(name, null);
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup2", null);
    diagnosticGroups.registerGroup(name, group, group2);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertNotNull(registeredGroup);
    assertEquals(name, registeredGroup.getName());
    assertSame(group, registeredGroup);
    DiagnosticGroup registeredGroup2 = diagnosticGroups.forName("testGroup2");
    assertNotNull(registeredGroup2);
    assertEquals("testGroup2", registeredGroup2.getName());
    assertSame(group2, registeredGroup2);
  }

  @Test
  public void testRegisterGroupTwice() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup(name, null);
    diagnosticGroups.registerGroup(name, group);
    try {
      diagnosticGroups.registerGroup(name, group);
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("already registered"));
    }
  }
}