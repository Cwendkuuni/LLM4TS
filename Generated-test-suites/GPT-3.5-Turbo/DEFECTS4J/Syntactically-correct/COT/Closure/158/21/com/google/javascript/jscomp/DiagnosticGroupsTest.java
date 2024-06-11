import org.junit.Test;

public class DiagnosticGroupsTest {
  
  DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
  
  @Test
  public void testRegisterGroup(){
    String name = "testGroup";
    DiagnosticType type = new DiagnosticType();
    DiagnosticGroup group = diagnosticGroups.registerGroup(name, type);
    assertEquals(group, diagnosticGroups.getRegisteredGroups().get(name));
  }
  
  @Test
  public void testForName(){
    String name = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup();
    diagnosticGroups.forName(name, group);
    assertEquals(group, diagnosticGroups.forChild(name));
  }
  
  @Test
  public void testGetRegisteredGroups(){
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(groups);
    assertEquals(groupsByName, groups);
  }
  
  @Test
  public void testSetWarningLevels(){
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = new ArrayList<String>();
    diagnosticGroups.add("testGroup");
    CheckLevel level = CheckLevel.WARNING;
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    assertEquals(level, options.getWarningLevel(diagnosticGroups.forName("testGroup")));
  }
  
  // Edge cases and exception handling scenarios:
  // - Empty diagnosticGroups list for setWarningLevels()
  // - Null diagnosticGroup name for forName() method
  
  @Test
  public void testSetWarningLevels_EmptyList(){
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = new ArrayList<String>();
    CheckLevel level = CheckLevel.WARNING;
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    // No exception should be thrown
  }
  
  @Test
  public void testForName_NullName(){
    String name = null;
    DiagnosticGroup group = diagnosticGroups.forChild(name);
    assertNull(group);
  }
  
}