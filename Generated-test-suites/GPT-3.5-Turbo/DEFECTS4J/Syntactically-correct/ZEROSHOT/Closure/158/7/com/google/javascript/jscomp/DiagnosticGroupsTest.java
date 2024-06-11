import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckAccessControls;
import com.google.javascript.jscomp.TypeValidator;
import com.google.javascript.jscomp.TypeCheck;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CheckUnreachableCode;
import com.google.javascript.jscomp.DisambiguateProperties;
import com.google.javascript.jscomp.CheckRegExp;
import com.google.javascript.jscomp.CheckGlobalNames;
import com.google.javascript.jscomp.VarCheck;
import com.google.javascript.jscomp.ProcessDefines;
import com.google.javascript.jscomp.ProcessTweaks;

import java.util.Map;
import java.util.List;

public class DiagnosticGroupsTest {

  private DiagnosticGroups diagnosticGroups;

  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = diagnosticGroups.registerGroup("testingGroup");
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertTrue(registeredGroups.containsKey("testingGroup"));
    assertEquals(registeredGroups.get("testingGroup"), group);
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticType[] types = {new DiagnosticType("type1"), new DiagnosticType("type2")};
    DiagnosticGroup group = diagnosticGroups.registerGroup("testingGroupWithTypes", types);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertTrue(registeredGroups.containsKey("testingGroupWithTypes"));
    assertEquals(registeredGroups.get("testingGroupWithTypes"), group);
  }

  @Test
  public void testRegisterGroupWithGroups() {
    DiagnosticGroup[] groups = {new DiagnosticGroup("group1"), new DiagnosticGroup("group2")};
    DiagnosticGroup group = diagnosticGroups.registerGroup("testingGroupWithGroups", groups);
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertTrue(registeredGroups.containsKey("testingGroupWithGroups"));
    assertEquals(registeredGroups.get("testingGroupWithGroups"), group);
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertEquals(registeredGroups, DiagnosticGroups.groupsByName);
  }

  @Test
  public void testForName() {
    String name = "testingGroup";
    DiagnosticGroup group = new DiagnosticGroup(name);
    DiagnosticGroup expectedGroup = diagnosticGroups.forName(name);
    assertEquals(expectedGroup, group);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = List.of("group1", "group2");
    CheckLevel level = CheckLevel.ERROR;
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
  }
}