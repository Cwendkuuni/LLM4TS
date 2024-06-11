import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static com.google.common.collect.Lists.newArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import com.google.javascript.jscomp.CheckAccessControls;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckRegExp;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ProcessDefines;
import com.google.javascript.jscomp.ProcessTweaks;
import com.google.javascript.jscomp.TypeCheck;
import com.google.javascript.jscomp.TypeValidator;
import com.google.javascript.jscomp.VariableReferenceCheck;
import com.google.javascript.jscomp.VarCheck;

public class DiagnosticGroupsTest {

  private static Map<String, DiagnosticGroup> diagnosticGroups;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups().getRegisteredGroups();
  }

  @Test
  public void testGetRegisteredGroups() {
    assertNotNull(diagnosticGroups);
  }

  @Test
  public void testGetDiagnosticGroupByName() {
    assertNotNull(diagnosticGroups.get("accessControls"));
  }

  @Test
  public void testGetNullDiagnosticGroupByName() {
    assertNull(diagnosticGroups.get("unknownGroup"));
  }

  @Test
  public void testRegisterDiagnosticGroup() {
    DiagnosticGroup newGroup = new DiagnosticGroup("newGroup", null);
    String newGroupName = "newGroup";
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(newGroupName, newGroup);
    assertNotNull(registeredGroup);
    assertNotNull(diagnosticGroups.get(newGroupName));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterDiagnosticGroupNullName() {
    DiagnosticGroup newGroup = new DiagnosticGroup("newGroup", null);
    DiagnosticGroups.registerGroup(null, newGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterDiagnosticGroupNullGroup() {
    DiagnosticGroups.registerGroup("newGroup", null);
  }

  @Test
  public void testRegisterDiagnosticType() {
    DiagnosticType diagnosticType = new DiagnosticType("testDiagnosticType");
    DiagnosticGroup diagnosticGroup = DiagnosticGroups.registerGroup("testGroup", diagnosticType);
    assertNotNull(diagnosticGroup.getDiagnosticTypes());
    assertNotNull(diagnosticGroup.getDiagnosticTypes().get(0));
    assertNotNull(diagnosticGroup.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterDiagnosticTypeNullDiagnosticType() {
    DiagnosticGroup diagnosticGroup = DiagnosticGroups.registerGroup("testGroup", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterDiagnosticTypeNullGroupName() {
    DiagnosticType diagnosticType = new DiagnosticType("testDiagnosticType");
    DiagnosticGroups.registerGroup(null, diagnosticType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterMultipleDiagnosticTypesNullDiagnosticType() {
    DiagnosticGroup diagnosticGroup = new DiagnosticGroup("testGroup", null);
    DiagnosticType[] diagnosticTypes = new DiagnosticType[0];
    DiagnosticGroups.registerGroup("testGroup", diagnosticTypes);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterMultipleDiagnosticTypesNullGroupName() {
    DiagnosticType[] diagnosticTypes = new DiagnosticType[0];
    DiagnosticGroups.registerGroup(null, diagnosticTypes);
  }

  @Test
  public void testRegisterMultipleDiagnosticTypes() {
    DiagnosticType diagnosticType1 = new DiagnosticType("testDiagnosticType1");
    DiagnosticType diagnosticType2 = new DiagnosticType("testDiagnosticType2");
    DiagnosticGroup diagnosticGroup = new DiagnosticGroup("testGroup", null);
    DiagnosticGroups.registerGroup("testGroup", new DiagnosticType[] { diagnosticType1, diagnosticType2 });
    List<DiagnosticType> diagnosticTypes = diagnosticGroup.getDiagnosticTypes();
    assertNotNull(diagnosticTypes);
    assertNotNull(diagnosticTypes.get(0));
    assertNotNull(diagnosticTypes.get(1));
  }

  @Test
  public void testRegisterMultipleDiagnosticGroups() {
    DiagnosticType diagnosticType = new DiagnosticType("testDiagnosticType");
    DiagnosticGroup diagnosticGroup1 = new DiagnosticGroup("testGroup1", null);
    DiagnosticGroup diagnosticGroup2 = new DiagnosticGroup("testGroup2", null);
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", diagnosticGroup1, diagnosticGroup2);
    assertNotNull(registeredGroup);
    assertNotNull(diagnosticGroup1.getName());
    assertNotNull(diagnosticGroup2.getName());
    assertNotNull(diagnosticGroups.get("testGroup"));
    assertNotNull(diagnosticGroups.get("testGroup").getDiagnosticTypes());
    assertNotNull(diagnosticGroups.get("testGroup").getDiagnosticTypes().get(0));
    assertNotNull(diagnosticGroups.get("testGroup").getDiagnosticTypes().get(1));
  }
}