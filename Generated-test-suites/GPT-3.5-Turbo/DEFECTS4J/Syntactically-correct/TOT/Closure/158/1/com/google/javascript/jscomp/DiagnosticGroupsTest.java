import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroup;

import java.util.Map;

public class DiagnosticGroupsTest {

  private DiagnosticGroups diagnosticGroups;

  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisterGroup() {
    // Typical use case
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("group1", DiagnosticGroup1.DIAGNOSTICS);
    Assert.assertNotNull(group1);

    // Edge case
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("group2");
    Assert.assertEquals(group2, DiagnosticGroup2.EMPTY_GROUP);

    // Error scenario
    try {
      diagnosticGroups.registerGroup(null, DiagnosticGroup3.DIAGNOSTICS);
      Assert.fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      // expected exception
      Assert.assertEquals("No null names allowed", e.getMessage());
    }
  }

  @Test
  public void testGetRegisteredGroups() {
    // Typical use case
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("group1", DiagnosticGroup1.DIAGNOSTICS);
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("group2", DiagnosticGroup2.DIAGNOSTICS);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    Assert.assertEquals(2, registeredGroups.size());
    Assert.assertEquals(group1, registeredGroups.get("group1"));
    Assert.assertEquals(group2, registeredGroups.get("group2"));

    // Edge case
    DiagnosticGroup group3 = diagnosticGroups.registerGroup("group3");
    registeredGroups = diagnosticGroups.getRegisteredGroups();
    Assert.assertEquals(3, registeredGroups.size());
    Assert.assertEquals(group1, registeredGroups.get("group1"));
    Assert.assertEquals(group2, registeredGroups.get("group2"));
    Assert.assertEquals(group3, registeredGroups.get("group3"));

    // Error scenario
    registeredGroups = diagnosticGroups.getRegisteredGroups();
    Assert.assertTrue(registeredGroups instanceof ImmutableMap);
  }

  @Test
  public void testForName() {
    // Typical use case
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("group1", DiagnosticGroup1.DIAGNOSTICS);
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("group2", DiagnosticGroup2.DIAGNOSTICS);
    Assert.assertEquals(group1, diagnosticGroups.forName("group1"));
    Assert.assertEquals(group2, diagnosticGroups.forName("group2"));

    // Edge case
    DiagnosticGroup group3 = diagnosticGroups.registerGroup("group3");
    Assert.assertEquals(group3, diagnosticGroups.forName("group3"));

    // Error scenario
    Assert.assertNull(diagnosticGroups.forName("nonExistentGroup"));
  }

  @Test
  public void testSetWarningLevels() {
    // Typical use case
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("group1", "group2", "group3");
    diagnosticGroups.registerGroup("group1", DiagnosticGroup1.DIAGNOSTICS);
    diagnosticGroups.registerGroup("group2", DiagnosticGroup2.DIAGNOSTICS);
    diagnosticGroups.registerGroup("group3", DiagnosticGroup3.DIAGNOSTICS);
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    // Assert warning level for each group

    // Edge case
    diagnosticGroups.registerGroup("group4");
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.ERROR);
    // Assert warning level for each group

    // Error scenario
    diagnosticGroups.setWarningLevels(options, Arrays.asList("nonExistentGroup"), CheckLevel.WARNING);
    // Assert no warning level set for non-existent group
  }

  private static class DiagnosticGroup1 {
    protected static final DiagnosticType DIAGNOSTIC1 =
        new DiagnosticType("DIAGNOSTIC1", CheckLevels.WARNING, "diagnostic 1");

    protected static final DiagnosticRegistry REGISTRY =
        new DiagnosticRegistry(ERROR1, ERROR2);

    protected static final DiagnosticGroup DIAGNOSTICS =
        new DiagnosticGroup("group1", REGISTRY);
  }

  private static class DiagnosticGroup2 {
    protected static final DiagnosticType DIAGNOSTIC3 =
        new DiagnosticType("DIAGNOSTIC3", CheckLevels.ERROR, "diagnostic 3");

    protected static final DiagnosticRegistry REGISTRY =
        new DiagnosticRegistry(ERROR3, ERROR4);

    protected static final DiagnosticGroup DIAGNOSTICS =
        new DiagnosticGroup("group2", REGISTRY);

    protected static final DiagnosticGroup EMPTY_GROUP =
        new DiagnosticGroup("group2");
  }

  private static class DiagnosticGroup3 {
    protected static final DiagnosticType DIAGNOSTIC5 =
        new DiagnosticType("DIAGNOSTIC5", CheckLevels.OFF, "diagnostic 5");

    protected static final DiagnosticRegistry REGISTRY =
        new DiagnosticRegistry(ERROR5, ERROR6);

    protected static final DiagnosticGroup DIAGNOSTICS =
        new DiagnosticGroup(null, REGISTRY);
  }

  private static class DiagnosticRegistry {
    protected final List<DiagnosticType> diagnostics;

    DiagnosticRegistry(DiagnosticType... diagnostics) {
      this.diagnostics = Arrays.asList(diagnostics);
    }
  }

  private static class CheckLevels {
    protected static final CheckLevel ERROR = CheckLevel.ERROR;
    protected static final CheckLevel WARNING = CheckLevel.WARNING;
    protected static final CheckLevel OFF = CheckLevel.OFF;
  }

  private static class DiagnosticType {
    protected final String name;
    protected final CheckLevel defaultLevel;
    protected final String description;

    DiagnosticType(String name, CheckLevel defaultLevel, String description) {
      this.name = name;
      this.defaultLevel = defaultLevel;
      this.description = description;
    }
  }

}