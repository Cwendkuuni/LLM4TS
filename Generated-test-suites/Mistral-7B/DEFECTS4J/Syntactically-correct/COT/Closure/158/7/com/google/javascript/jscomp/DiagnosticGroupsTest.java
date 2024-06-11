import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertEquals(22, groups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("ACCESS_CONTROLS");
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupString() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticGroup group = new DiagnosticGroup(name, new DiagnosticType[0]);
    diagnosticGroups.registerGroup(name, group);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertEquals(group, registeredGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupStringNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, new DiagnosticType[0]);
  }

  @Test
  public void testRegisterGroupDiagnosticTypeArray() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticType[] types = new DiagnosticType[1];
    types[0] = new DiagnosticType("Test Type");
    diagnosticGroups.registerGroup(name, types);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertEquals(1, registeredGroup.getDiagnosticTypes().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupDiagnosticTypeArrayNullTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    diagnosticGroups.registerGroup(name, (DiagnosticType[]) null);
  }

  @Test
  public void testRegisterGroupDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticGroup group = new DiagnosticGroup(name, new DiagnosticType[0]);
    diagnosticGroups.registerGroup(name, group);
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertEquals(group, registeredGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupDiagnosticGroupNullGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    diagnosticGroups.registerGroup(name, null);
  }
}