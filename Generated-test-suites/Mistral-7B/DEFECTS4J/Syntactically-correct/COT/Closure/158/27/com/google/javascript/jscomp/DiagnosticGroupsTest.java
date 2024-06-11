import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertEquals(20, groups.size());
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
  public void testRegisterGroupStringDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticType diagnosticType = new DiagnosticType(1, "Test Diagnostic", null);
    DiagnosticGroup group = diagnosticGroups.registerGroup(name, diagnosticType);
    assertNotNull(group);
    assertEquals(name, group.getName());
    assertEquals(1, group.getDiagnostics().size());
    assertEquals(diagnosticType, group.getDiagnostics().get(0));
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticGroup group = new DiagnosticGroup(name, ImmutableMap.of());
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(name, group);
    assertSame(group, registeredGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupDuplicateName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticGroup group1 = diagnosticGroups.registerGroup(name, new DiagnosticType(1, "Test Diagnostic 1", null));
    diagnosticGroups.registerGroup(name, new DiagnosticType(2, "Test Diagnostic 2", null));
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroupArray() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticGroup group1 = new DiagnosticGroup(name1, ImmutableMap.of());
    DiagnosticGroup group2 = new DiagnosticGroup(name2, ImmutableMap.of());
    DiagnosticGroup[] groups = {group1, group2};
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(name, groups);
    assertSame(groups, registeredGroup.getGroups());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupDuplicateGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticGroup group1 = new DiagnosticGroup(name1, ImmutableMap.of());
    DiagnosticGroup group2 = new DiagnosticGroup(name, ImmutableMap.of());
    diagnosticGroups.registerGroup(name1, group1);
    diagnosticGroups.registerGroup(name, group2);
  }
}