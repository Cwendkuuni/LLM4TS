import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(21, registeredGroups.size());
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
    DiagnosticGroup group = diagnosticGroups.registerGroup(name);
    assertNotNull(group);
    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(name);
    assertEquals(group, retrievedGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupDuplicateName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "ACCESS_CONTROLS";
    diagnosticGroups.registerGroup(name);
    diagnosticGroups.registerGroup(name);
  }

  @Test
  public void testRegisterGroupDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP_TYPE";
    DiagnosticType diagnosticType = new DiagnosticType("TEST_TYPE");
    DiagnosticGroup group = diagnosticGroups.registerGroup(name, diagnosticType);
    assertNotNull(group);
    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(name);
    assertEquals(group, retrievedGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupDiagnosticTypeNull() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP_TYPE_NULL";
    diagnosticGroups.registerGroup(name, null);
  }

  @Test
  public void testRegisterGroupDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP_GROUP";
    DiagnosticGroup group = new DiagnosticGroup("TEST_GROUP_GROUP");
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(name, group);
    assertNotNull(registeredGroup);
    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(name);
    assertEquals(group, retrievedGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupDiagnosticGroupNull() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP_GROUP_NULL";
    diagnosticGroups.registerGroup(name, null);
  }

}