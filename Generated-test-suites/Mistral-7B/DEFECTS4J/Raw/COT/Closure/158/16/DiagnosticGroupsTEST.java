import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import java.util.List;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals("Size of registered groups should be 18", 18, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("ACCESS_CONTROLS");
    assertNotNull("Group for name ACCESS_CONTROLS should not be null", group);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test
  public void testRegisterGroupStringDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP",
        DiagnosticType.ERROR);
    assertNotNull("Group for name TEST_GROUP should not be null", group);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupStringDiagnosticTypeNullDiagnosticType() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("TEST_GROUP", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupStringArrayDiagnosticTypeInvalidSize() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("TEST_GROUP", new DiagnosticType[0]);
  }

  @Test
  public void testRegisterGroupStringDiagnosticTypeValid() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticType diagnosticType = DiagnosticType.ERROR;
    DiagnosticGroup group = diagnosticGroups.registerGroup("TEST_GROUP", diagnosticType);
    assertNotNull("Group for name TEST_GROUP should not be null", group);
    assertEquals("Group's diagnostic type should be the same as passed", diagnosticType, group.getDiagnosticType());
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("GROUP1", DiagnosticType.ERROR);
    DiagnosticGroup group2 = new DiagnosticGroup("GROUP2", DiagnosticType.WARNING);
    diagnosticGroups.registerGroup("GROUP2", group1, group2);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals("Size of registered groups should be 19", 19, registeredGroups.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupStringDiagnosticGroupNullDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("TEST_GROUP", (DiagnosticGroup[]) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupStringDiagnosticGroupInvalidGroupSize() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup[] diagnosticGroupsArray = new DiagnosticGroup[0];
    diagnosticGroups.registerGroup("TEST_GROUP", diagnosticGroupsArray);
  }

  @Test
  public void testRegisterGroupStringDiagnosticGroupValid() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("GROUP1", DiagnosticType.ERROR);
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("GROUP2", DiagnosticType.WARNING);
    diagnosticGroups.registerGroup("GROUP3", group1, group2);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    DiagnosticGroup group3 = registeredGroups.get("GROUP3");
    assertNotNull("Group for name GROUP3 should not be null", group3);
    assertEquals("Group3's diagnostic types should be the same as passed", DiagnosticType.ERROR, group3.getDiagnosticType());
    DiagnosticGroup group1InGroup3 = group3.getGroups().get(0);
    assertEquals("Group1 in Group3 should be the same as original group1", group1, group1InGroup3);
    DiagnosticGroup group2InGroup3 = group3.getGroups().get(1);
    assertEquals("Group2 in Group3 should be the same as original group2", group2, group2InGroup3);
  }
}