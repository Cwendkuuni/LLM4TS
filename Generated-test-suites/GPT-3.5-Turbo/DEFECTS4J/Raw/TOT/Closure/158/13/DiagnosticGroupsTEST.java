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
  public void testRegisterGroup_withNameAndGroup_shouldRegisterGroup() {
    DiagnosticGroup group = new DiagnosticGroup();
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", group);
    assertEquals(group, registeredGroup);
  }

  @Test
  public void testRegisterGroup_withNameAndTypes_shouldRegisterGroup() {
    DiagnosticType[] types = new DiagnosticType[2];
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", types);
    assertNotNull(registeredGroup);
    assertEquals("testGroup", registeredGroup.getName());
    assertArrayEquals(types, registeredGroup.getTypes());
  }

  @Test
  public void testRegisterGroup_withNameAndGroups_shouldRegisterGroup() {
    DiagnosticGroup[] groups = new DiagnosticGroup[2];
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", groups);
    assertNotNull(registeredGroup);
    assertEquals("testGroup", registeredGroup.getName());
    assertArrayEquals(groups, registeredGroup.getSubgroups());
  }

  @Test
  public void testGetRegisteredGroups_shouldReturnRegisteredGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup();
    DiagnosticGroup group2 = new DiagnosticGroup();
    diagnosticGroups.registerGroup("group1", group1);
    diagnosticGroups.registerGroup("group2", group2);
    
    assertEquals(2, diagnosticGroups.getRegisteredGroups().size());
    assertTrue(diagnosticGroups.getRegisteredGroups().containsValue(group1));
    assertTrue(diagnosticGroups.getRegisteredGroups().containsValue(group2));
  }

  @Test
  public void testForName_withRegisteredName_shouldReturnDiagnosticGroup() {
    DiagnosticGroup group = new DiagnosticGroup();
    diagnosticGroups.registerGroup("testGroup", group);
    
    assertEquals(group, diagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testForName_withUnregisteredName_shouldReturnNull() {
    assertNull(diagnosticGroups.forName("unregisteredGroup"));
  }

  @Test
  public void testSetWarningLevels_withValidNameAndCheckLevel_shouldSetWarningLevel() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("group1", "group2");
    
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.forName("group1")));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.forName("group2")));
  }

  @Test(expected = NullPointerException.class)
  public void testSetWarningLevels_withInvalidName_shouldThrowNullPointerException() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("group1", "group2");
    
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroups.forName("group3")));
  }
}