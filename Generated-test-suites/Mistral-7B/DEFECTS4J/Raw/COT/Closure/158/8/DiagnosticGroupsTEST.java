import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(22, registeredGroups.size());
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
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP", new DiagnosticType[0]);
    assertNotNull(group);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, new DiagnosticType[0]);
  }

  @Test
  public void testRegisterGroupDiagnosticTypeArray() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP", new DiagnosticType[0]);
    assertNotNull(group);
  }

  @Test
  public void testRegisterGroupDiagnosticGroupArray() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("GROUP1", new DiagnosticType[0]);
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("GROUP2", new DiagnosticGroup[0]);
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP", group1, group2);
    assertNotNull(group);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupNullGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("TEST_GROUP", new DiagnosticGroup[0]);
  }

  @Test
  public void testToString() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    for (Map.Entry<String, DiagnosticGroup> entry : registeredGroups.entrySet()) {
      System.out.println(entry.getValue().getName());
    }
  }

}