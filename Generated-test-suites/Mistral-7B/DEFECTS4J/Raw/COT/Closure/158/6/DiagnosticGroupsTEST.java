import static org.junit.Assert.*;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Checker.Type;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertEquals("size", 22, groups.size());
    // Add assertions for each group name and instance check here.
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("ACCESS_CONTROLS");
    assertNotNull(group);
    // Add assertions for specific group properties here.
  }

  @Test
  public void testRegisterGroupWithSingleType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticType type = Checker.error(Type.TYPE_ERROR, "Test Error");
    DiagnosticGroup group = diagnosticGroups.registerGroup(name, type);
    assertNotNull(group);
    assertEquals(name, group.getName());
    assertEquals(1, group.getDiagnostics().size());
    // Add assertions for specific group properties and diagnostics here.
  }

  @Test
  public void testRegisterGroupWithMultipleTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticType type1 = Checker.error(Type.TYPE_ERROR, "Test Error 1");
    DiagnosticType type2 = Checker.error(Type.TYPE_ERROR, "Test Error 2");
    DiagnosticGroup group = diagnosticGroups.registerGroup(name, type1, type2);
    assertNotNull(group);
    assertEquals(name, group.getName());
    assertEquals(2, group.getDiagnostics().size());
    // Add assertions for specific group properties and diagnostics here.
  }

  @Test
  public void testRegisterGroupWithMultipleGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    DiagnosticGroup group1 = new DiagnosticGroup("Group1", new String[]{"Diag1"});
    DiagnosticGroup group2 = new DiagnosticGroup("Group2", new String[]{"Diag2"});
    DiagnosticGroup group = diagnosticGroups.registerGroup(name, group1, group2);
    assertNotNull(group);
    assertEquals(name, group.getName());
    assertEquals(group1, group.getChildGroups().get(0));
    assertEquals(group2, group.getChildGroups().get(1));
    // Add assertions for specific group properties and child groups here.
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, new DiagnosticType[]{}, (DiagnosticGroup[]) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("", new DiagnosticType[]{}, (DiagnosticGroup[]) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    diagnosticGroups.registerGroup(name, null, new DiagnosticGroup[0]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyGroupArray() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "TEST_GROUP";
    diagnosticGroups.registerGroup(name, new DiagnosticType[0], (DiagnosticGroup[]) null);
  }

  @Test
  public void testRegisteredGroupsList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> registeredGroupsList = newArrayList("ACCESS_CONTROLS", "DEPRECATED", "VISIBILITY");
    DiagnosticGroups expectedDiagnosticGroups = new DiagnosticGroups();
    expectedDiagnosticGroups.registerGroup("ACCESS_CONTROLS", DEPRECATED, VISIBILITY);
    expectedDiagnosticGroups.registerGroup("DEPRECATED", CheckAccessControls.DEPRECATED_NAME, CheckAccessControls.DEPRECATED_NAME_REASON, CheckAccessControls.DEPRECATED_PROP, CheckAccessControls.DEPRECATED_PROP_REASON, CheckAccessControls.DEPRECATED_CLASS, CheckAccessControls.DEPRECATED_CLASS_REASON);
    expectedDiagnosticGroups.registerGroup("VISIBILITY", CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS, CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS, CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS, CheckAccessControls.PRIVATE_OVERRIDE, CheckAccessControls.VISIBILITY_MISMATCH);

    diagnosticGroups.setWarningLevels(new CompilerOptions(), registeredGroupsList, Checker.WarningLevel.WARNING);

    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    DiagnosticGroup accessControls = groups.get("ACCESS_CONTROLS");
    DiagnosticGroup deprecated = groups.get("DEPRECATED");
    DiagnosticGroup visibility = groups.get("VISIBILITY");

    assertEquals(expectedDiagnosticGroups.getRegisteredGroups().get(0).getName(), accessControls.getName());
    assertEquals(expectedDiagnosticGroups.getRegisteredGroups().get(1).getName(), deprecated.getName());
    assertEquals(expectedDiagnosticGroups.getRegisteredGroups().get(2).getName(), visibility.getName());
  }
}