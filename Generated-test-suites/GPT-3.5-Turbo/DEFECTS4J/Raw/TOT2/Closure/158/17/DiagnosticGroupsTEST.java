import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupWithSingleType() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup", DiagnosticType.WARNING);
    assertNotNull(DiagnosticGroups.registerGroup("testGroup", group));
  }

  @Test
  public void testRegisterGroupWithMultipleTypes() {
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup1", DiagnosticType.WARNING);
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup2", DiagnosticType.ERROR);
    assertNotNull(DiagnosticGroups.registerGroup("testGroup", group1, group2));
  }

  @Test
  public void testGetRegisteredGroups() {
    assertNotNull(new DiagnosticGroups().getRegisteredGroups());
  }

  @Test
  public void testForName() {
    assertNotNull(new DiagnosticGroups().forName("testGroup"));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
    new DiagnosticGroups().setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(new DiagnosticGroup("testGroup1")));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(new DiagnosticGroup("testGroup2")));
  }

  @Test
  public void testInvalidWarningLevel() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("invalidGroup");
    try {
      new DiagnosticGroups().setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
      fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Expected exception
    }
  }
}