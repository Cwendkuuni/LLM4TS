import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetDiagnosticGroup() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", new DiagnosticType[] {});
    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    DiagnosticGroup retrievedGroup = registeredGroups.get("testGroup");
    assertEquals(group, retrievedGroup);
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroupWithTypes",
        DiagnosticType.DEPRECATED, DiagnosticType.INEXISTENT_PROPERTY);
    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    DiagnosticGroup retrievedGroup = registeredGroups.get("testGroupWithTypes");
    assertEquals(2, retrievedGroup.getDiagnosticTypes().size());
    assertEquals(DiagnosticType.DEPRECATED, retrievedGroup.getDiagnosticTypes().get(0));
    assertEquals(DiagnosticType.INEXISTENT_PROPERTY, retrievedGroup.getDiagnosticTypes().get(1));
  }

  @Test
  public void testRegisterGroupWithOtherGroups() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", new DiagnosticType[] {});
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", new DiagnosticGroup[] { group1 });
    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    DiagnosticGroup retrievedGroup2 = registeredGroups.get("group2");
    assertEquals(1, retrievedGroup2.getSubGroups().size());
    DiagnosticGroup retrievedGroup1 = retrievedGroup2.getSubGroups().get(0);
    assertEquals(group1, retrievedGroup1);
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups.registerGroup("group1", new DiagnosticType[] {});
    DiagnosticGroups.registerGroup("group2", new DiagnosticType[] {});
    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    assertEquals(2, registeredGroups.size());
  }

  @Test
  public void testFindDiagnosticGroupByName() {
    DiagnosticGroups.registerGroup("group1", new DiagnosticType[] {});
    DiagnosticGroups.registerGroup("group2", new DiagnosticType[] {});
    DiagnosticGroup group = DiagnosticGroups.forName("group1");
    assertEquals("group1", group.getName());
  }

  @Test(expected = NullPointerException.class)
  public void testFindNullDiagnosticGroupByName() {
    DiagnosticGroups.forName(null);
  }
}