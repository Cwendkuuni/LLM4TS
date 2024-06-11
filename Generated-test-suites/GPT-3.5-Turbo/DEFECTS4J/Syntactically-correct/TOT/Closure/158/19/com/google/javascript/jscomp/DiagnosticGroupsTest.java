import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.common.collect.ImmutableMap;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    // Test Case 1: Registering a group with a name and single diagnostic type
    DiagnosticGroup testGroup1 = DiagnosticGroups.registerGroup("testGroup1",
        CheckAccessControls.DEPRECATED_NAME);
    assertEquals("testGroup1", testGroup1.getName());
    assertTrue(testGroup1.getTypes().contains(CheckAccessControls.DEPRECATED_NAME));

    // Test Case 2: Registering a group with a name and multiple diagnostic types
    DiagnosticGroup testGroup2 = DiagnosticGroups.registerGroup("testGroup2",
        CheckAccessControls.DEPRECATED_NAME, CheckAccessControls.DEPRECATED_PROP);
    assertEquals("testGroup2", testGroup2.getName());
    assertTrue(testGroup2.getTypes().contains(CheckAccessControls.DEPRECATED_NAME));
    assertTrue(testGroup2.getTypes().contains(CheckAccessControls.DEPRECATED_PROP));

    // Test Case 3: Registering a group with a name and other existing groups
    DiagnosticGroup testGroup3 = DiagnosticGroups.registerGroup("testGroup3",
        testGroup1, testGroup2);
    assertEquals("testGroup3", testGroup3.getName());
    assertTrue(testGroup3.getTypes().contains(CheckAccessControls.DEPRECATED_NAME));
    assertTrue(testGroup3.getTypes().contains(CheckAccessControls.DEPRECATED_PROP));
    assertTrue(testGroup3.getTypes().contains(CheckAccessControls.DEPRECATED_CLASS));
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertFalse(registeredGroups.isEmpty());
    assertTrue(registeredGroups.containsKey("testGroup1"));
    assertTrue(registeredGroups.containsKey("testGroup2"));
    assertTrue(registeredGroups.containsKey("testGroup3"));
  }

  @Test
  public void testForName() {
    DiagnosticGroup group1 = DiagnosticGroups.forName("testGroup1");
    assertNotNull(group1);
    assertEquals("testGroup1", group1.getName());
    assertTrue(group1.getTypes().contains(CheckAccessControls.DEPRECATED_NAME));

    DiagnosticGroup group2 = DiagnosticGroups.forName("testGroup2");
    assertNotNull(group2);
    assertEquals("testGroup2", group2.getName());
    assertTrue(group2.getTypes().contains(CheckAccessControls.DEPRECATED_NAME));
    assertTrue(group2.getTypes().contains(CheckAccessControls.DEPRECATED_PROP));

    DiagnosticGroup group3 = DiagnosticGroups.forName("testGroup3");
    assertNotNull(group3);
    assertEquals("testGroup3", group3.getName());
    assertTrue(group3.getTypes().contains(CheckAccessControls.DEPRECATED_NAME));
    assertTrue(group3.getTypes().contains(CheckAccessControls.DEPRECATED_PROP));
    assertTrue(group3.getTypes().contains(CheckAccessControls.DEPRECATED_CLASS));
  }

  @Test
  public void testSetWarningLevels() {
    // Test Case 1: Setting warning levels for existing diagnostic groups
    CompilerOptions options = new CompilerOptions();
    String[] diagnosticGroups = {"testGroup1", "testGroup2"};
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("testGroup1")));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("testGroup2")));

    // Test Case 2: Setting warning levels for non-existing diagnostic groups
    CompilerOptions options2 = new CompilerOptions();
    String[] diagnosticGroups2 = {"nonExistingGroup"};
    try {
      DiagnosticGroups.setWarningLevels(options2, diagnosticGroups2, CheckLevel.WARNING);
      fail("Expected exception for non-existing diagnostic group");
    } catch (NullPointerException e) {
      // Expected exception
    }
  }
}