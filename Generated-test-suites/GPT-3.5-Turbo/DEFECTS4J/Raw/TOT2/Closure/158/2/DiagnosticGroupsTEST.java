import static org.junit.Assert.*;
import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupWithDiagnosticGroup() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("test", group);
    assertEquals(group, result);
  }

  @Test
  public void testRegisterGroupWithNameAndTypes() {
    DiagnosticType type1 = new DiagnosticType("Type1");
    DiagnosticType type2 = new DiagnosticType("Type2");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("test", type1, type2);
    assertNotNull(result);
    assertEquals("test", result.getName());
    assertTrue(result.getTypes().contains(type1));
    assertTrue(result.getTypes().contains(type2));
  }

  @Test
  public void testRegisterGroupWithNameAndGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("Group1");
    DiagnosticGroup group2 = new DiagnosticGroup("Group2");
    DiagnosticGroup result = DiagnosticGroups.registerGroup("test", group1, group2);
    assertNotNull(result);
    assertEquals("test", result.getName());
    assertTrue(result.getGroups().contains(group1));
    assertTrue(result.getGroups().contains(group2));
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> groups = DiagnosticGroups.getRegisteredGroups();
    assertNotNull(groups);
    assertEquals(0, groups.size());
  }

  @Test
  public void testForNameWithExistingName() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroups.registerGroup("test", group);
    DiagnosticGroup result = DiagnosticGroups.forName("test");
    assertEquals(group, result);
  }

  @Test
  public void testForNameWithNonExistingName() {
    DiagnosticGroup result = DiagnosticGroups.forName("nonExistentGroup");
    assertNull(result);
  }
}