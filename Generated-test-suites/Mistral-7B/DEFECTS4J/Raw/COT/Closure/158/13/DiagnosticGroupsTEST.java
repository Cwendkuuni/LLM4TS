import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import java.util.List;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(22, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForNameWithUnknownName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName("unknownName");
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "myGroup";
    DiagnosticGroup group = new DiagnosticGroup(name, new DiagnosticType[] {});
    diagnosticGroups.registerGroup(name, group);
    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(name);
    assertEquals(group, retrievedGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupWithNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(null, new DiagnosticType[] {});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("", new DiagnosticType[] {});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("myGroup", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("myGroup", null, (DiagnosticType[]) null);
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "myGroup";
    DiagnosticType type1 = new DiagnosticType();
    DiagnosticType type2 = new DiagnosticType();
    DiagnosticGroup group = new DiagnosticGroup(name, new DiagnosticType[] { type1, type2 });
    diagnosticGroups.registerGroup(name, type1, type2);
    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(name);
    DiagnosticGroup expectedGroup = new DiagnosticGroup(name, new DiagnosticType[] { type1, type2 });
    assertEquals(expectedGroup, retrievedGroup);
  }

  @Test
  public void testRegisterGroupWithMultipleTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "myGroup";
    DiagnosticType type1 = new DiagnosticType();
    DiagnosticType type2 = new DiagnosticType();
    DiagnosticType type3 = new DiagnosticType();
    DiagnosticGroup group = new DiagnosticGroup(name, new DiagnosticType[] { type1, type2, type3 });
    diagnosticGroups.registerGroup(name, type1, type2, type3);
    DiagnosticGroup retrievedGroup = diagnosticGroups.forName(name);
    DiagnosticGroup expectedGroup = new DiagnosticGroup(name, new DiagnosticType[] { type1, type2, type3 });
    assertEquals(expectedGroup, retrievedGroup);
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = null;
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
  }

  @Test(expected = NullPointerException.class)
  public void testSetWarningLevelsWithNullOptions() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupsList = null;
    diagnosticGroups.setWarningLevels(null, diagnosticGroupsList, CheckLevel.WARNING);
  }

  @Test(expected = NullPointerException.class)
  public void testSetWarningLevelsWithNullDiagnosticGroupsList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING);
  }

  @Test
  public void testSetWarningLevelsWithValidArguments() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = Arrays.asList("accessControls", "checkTypes");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(ACCESS_CONTROLS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(CHECK_TYPES));
  }

  @Test
  public void testSetWarningLevelsWithMultipleGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = Arrays.asList("accessControls", "checkTypes", "checkVars");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.ERROR);
    assertEquals(CheckLevel.ERROR, options.getWarningLevel(ACCESS_CONTROLS));
    assertEquals(CheckLevel.ERROR, options.getWarningLevel(CHECK_TYPES));
    assertEquals(CheckLevel.ERROR, options.getWarningLevel(CHECK_VARS));
  }
}