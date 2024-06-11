import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup1() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup1",
        DiagnosticType.TYPE1, DiagnosticType.TYPE2);
    assertNotNull(group);
    assertEquals("testGroup1", group.getName());
    assertTrue(group.contains(DiagnosticType.TYPE1));
    assertTrue(group.contains(DiagnosticType.TYPE2));
  }

  @Test
  public void testRegisterGroup2() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup2",
        DiagnosticType.TYPE3, DiagnosticType.TYPE4);
    assertNotNull(group);
    assertEquals("testGroup2", group.getName());
    assertTrue(group.contains(DiagnosticType.TYPE3));
    assertTrue(group.contains(DiagnosticType.TYPE4));
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertEquals(0, diagnosticGroups.getRegisteredGroups().size());

    DiagnosticGroups.registerGroup("testGroup3",
        DiagnosticType.TYPE5, DiagnosticType.TYPE6);
    assertEquals(1, diagnosticGroups.getRegisteredGroups().size());
    assertTrue(diagnosticGroups.getRegisteredGroups().containsKey("testGroup3"));

    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup4",
        DiagnosticType.TYPE7, DiagnosticType.TYPE8);
    assertEquals(2, diagnosticGroups.getRegisteredGroups().size());
    assertTrue(diagnosticGroups.getRegisteredGroups().containsKey("testGroup3"));
    assertTrue(diagnosticGroups.getRegisteredGroups().containsKey("testGroup4"));
    assertTrue(diagnosticGroups.getRegisteredGroups().containsValue(group));
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("testGroup5",
        DiagnosticType.TYPE9, DiagnosticType.TYPE10);
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("testGroup6",
        DiagnosticType.TYPE11, DiagnosticType.TYPE12);

    assertEquals(group1, diagnosticGroups.forName("testGroup5"));
    assertEquals(group2, diagnosticGroups.forName("testGroup6"));
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> groups = new ArrayList<String>();
    groups.add("testGroup7");
    groups.add("testGroup8");
    diagnosticGroups.setWarningLevels(options, groups, CheckLevel.WARNING);

    assertTrue(options.getWarningLevel(forName("testGroup7")) == CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(forName("testGroup8")) == CheckLevel.WARNING);
  }
}