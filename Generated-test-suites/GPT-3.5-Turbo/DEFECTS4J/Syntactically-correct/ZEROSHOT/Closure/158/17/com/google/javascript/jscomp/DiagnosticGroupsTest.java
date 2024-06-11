import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  private DiagnosticGroups diagnosticGroups;

  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisterGroupWithNameAndGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup result = diagnosticGroups.registerGroup("testName", group);
    assertNotNull(result);
    assertEquals(group, result);
  }

  @Test
  public void testRegisterGroupWithNameAndTypes() {
    DiagnosticType[] types = new DiagnosticType[]{new DiagnosticType("testType")};
    DiagnosticGroup result = diagnosticGroups.registerGroup("testName", types);
    assertNotNull(result);
    assertEquals(types[0], result.getTypes().get(0));
  }

  @Test
  public void testRegisterGroupWithNameAndGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
    DiagnosticGroup result = diagnosticGroups.registerGroup("testName", group1, group2);
    assertNotNull(result);
    assertEquals(group1, result.getGroups().get(0));
    assertEquals(group2, result.getGroups().get(1));
  }

  @Test
  public void testGetRegisteredGroups() {
    assertEquals(0, diagnosticGroups.getRegisteredGroups().size());
  }

  @Test
  public void testForNameWithExistingGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    diagnosticGroups.registerGroup("testName", group);
    DiagnosticGroup result = diagnosticGroups.forName("testName");
    assertEquals(group, result);
  }

  @Test
  public void testForNameWithNonexistentGroup() {
    DiagnosticGroup result = diagnosticGroups.forName("nonexistentName");
    assertNull(result);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    String[] diagnosticGroups = new String[]{"group1", "group2"};
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel("group1"));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel("group2"));
  }

  @Test(expected = NullPointerException.class)
  public void testSetWarningLevelsWithNullGroup() {
    CompilerOptions options = new CompilerOptions();
    String[] diagnosticGroups = new String[]{"group1", null};
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
  }
}