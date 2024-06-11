import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DiagnosticGroupsTest {

  private DiagnosticGroups diagnosticGroups;

  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertEquals(19, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroup globalThis = diagnosticGroups.forName("globalThis");
    assertNotNull(globalThis);
    assertEquals("globalThis", globalThis.getName());
  }

  @Test
  public void testRegisterGroupWithTypeArray() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
    assertNotNull(group);
    assertEquals("testGroup", group.getName());
    assertEquals(1, group.getTypes().length);
    assertEquals(CheckGlobalNames.STRICT_MODULE_DEP_QNAME, group.getTypes()[0]);
  }

  @Test
  public void testRegisterGroupWithGroupArray() {
    DiagnosticGroup globalThisGroup = DiagnosticGroups.forName("globalThis");
    DiagnosticGroup deprecatedGroup = DiagnosticGroups.forName("deprecated");

    DiagnosticGroup group = DiagnosticGroups.registerGroup("combinedGroup", globalThisGroup, deprecatedGroup);
    assertNotNull(group);
    assertEquals("combinedGroup", group.getName());
    assertEquals(2, group.getGroups().length);
    assertEquals(globalThisGroup, group.getGroups()[0]);
    assertEquals(deprecatedGroup, group.getGroups()[1]);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = List.of("globalThis", "deprecated", "ambiguousFunctionDecl");
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("globalThis")));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("deprecated")));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.forName("ambiguousFunctionDecl")));
  }

  // Add more test cases for edge cases and error scenarios

}