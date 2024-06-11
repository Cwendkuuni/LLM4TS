import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.CheckerFactory.Checker;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Register groups
    String group1Name = "group1";
    DiagnosticGroup group1 = new DiagnosticGroup(group1Name, Checker.UNDEFINED_VAR_CHECKER);
    diagnosticGroups.registerGroup(group1Name, group1);

    String group2Name = "group2";
    DiagnosticGroup group2 = new DiagnosticGroup(group2Name, Checker.TYPE_CHECKER);
    diagnosticGroups.registerGroup(group2Name, group2);

    // Get registered groups
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);

    DiagnosticGroup group1Retrieved = registeredGroups.get(group1Name);
    assertNotNull(group1Retrieved);
    assertNotNull(group1Retrieved.getChecker());
    assertNotNull(group1Retrieved.getName());
    assertEquals(group1Name, group1Retrieved.getName());

    DiagnosticGroup group2Retrieved = registeredGroups.get(group2Name);
    assertNotNull(group2Retrieved);
    assertNotNull(group2Retrieved.getChecker());
    assertNotNull(group2Retrieved.getName());
    assertEquals(group2Name, group2Retrieved.getName());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Register groups
    String group1Name = "group1";
    DiagnosticGroup group1 = new DiagnosticGroup(group1Name, Checker.UNDEFINED_VAR_CHECKER);
    diagnosticGroups.registerGroup(group1Name, group1);

    String group2Name = "group2";
    DiagnosticGroup group2 = new DiagnosticGroup(group2Name, Checker.TYPE_CHECKER);
    diagnosticGroups.registerGroup(group2Name, group2);

    // Get group by name
    DiagnosticGroup retrievedGroup1 = diagnosticGroups.forName(group1Name);
    assertNotNull(retrievedGroup1);
    assertNotNull(retrievedGroup1.getChecker());
    assertNotNull(retrievedGroup1.getName());
    assertEquals(group1Name, retrievedGroup1.getName());

    DiagnosticGroup retrievedGroup2 = diagnosticGroups.forName(group2Name);
    assertNotNull(retrievedGroup2);
    assertNotNull(retrievedGroup2.getChecker());
    assertNotNull(retrievedGroup2.getName());
    assertEquals(group2Name, retrievedGroup2.getName());

    // Get null if group not registered
    DiagnosticGroup nullGroup = diagnosticGroups.forName("unknownGroup");
    assertNull(nullGroup);
  }

  @Test
  public void testGetRegisteredGroupsImmutable() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Register groups
    String group1Name = "group1";
    DiagnosticGroup group1 = new DiagnosticGroup(group1Name, Checker.UNDEFINED_VAR_CHECKER);
    diagnosticGroups.registerGroup(group1Name, group1);

    String group2Name = "group2";
    DiagnosticGroup group2 = new DiagnosticGroup(group2Name, Checker.TYPE_CHECKER);
    diagnosticGroups.registerGroup(group2Name, group2);

    // Get registered groups as an immutable map
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    ImmutableMap<String, DiagnosticGroup> immutableRegisteredGroups = (ImmutableMap<String, DiagnosticGroup>) registeredGroups;

    // Check that the map is immutable
    try {
      immutableRegisteredGroups.put("newGroup", new DiagnosticGroup("newGroup", Checker.UNDEFINED_VAR_CHECKER));
      fail("Should not be able to modify the map");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testDiagnosticGroupNameConstants() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test constants
    assertNotNull(DiagnosticGroups.GLOBAL_THIS);
    assertNotNull(DiagnosticGroups.DEPRECATED);
    assertNotNull(DiagnosticGroups.VISIBILITY);
    // ... and so on for all the constants
  }

  @Test
  public void testDiagnosticGroupNamesString() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test DIAGNOSTIC_GROUP_NAMES constant as a string
    String diagnosticGroupsNames = DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES;
    String[] diagnosticGroupsNameArray = diagnosticGroupsNames.split(",");

    // Check that the number of elements in the array matches the number of constants
    assertEquals(22, diagnosticGroupsNameArray.length);

    // Check that each element in the array corresponds to a constant
    for (String diagnosticGroupName : diagnosticGroupsNameArray) {
      DiagnosticGroup diagnosticGroup = diagnosticGroups.forName(diagnosticGroupName);
      assertNotNull(diagnosticGroup);
    }
  }
}