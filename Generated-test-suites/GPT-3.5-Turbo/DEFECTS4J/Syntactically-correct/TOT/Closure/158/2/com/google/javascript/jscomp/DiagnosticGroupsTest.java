import org.junit.Test;

public class DiagnosticGroupsTest {
  
  @Test
  public void testRegisterGroup_StringDiagnosticGroup() {
    // Typical use case: register a diagnostic group with a name and group
    DiagnosticGroup expectedGroup = new DiagnosticGroup("testGroup");
    
    DiagnosticGroup actualGroup = DiagnosticGroups.registerGroup("testName", expectedGroup);
    
    assertEquals(expectedGroup, actualGroup);
  }
  
  @Test
  public void testRegisterGroup_StringDiagnosticTypeArray() {
    // Typical use case: register a diagnostic group with a name and diagnostic types
    DiagnosticType[] expectedTypes = {new DiagnosticType("testType1"), new DiagnosticType("testType2")};
    
    DiagnosticGroup actualGroup = DiagnosticGroups.registerGroup("testName", expectedTypes);
    
    assertEquals(expectedTypes[0], actualGroup.getTypes().get(0));
    assertEquals(expectedTypes[1], actualGroup.getTypes().get(1));
  }
  
  @Test
  public void testRegisterGroup_StringDiagnosticGroupArray() {
    // Typical use case: register a diagnostic group with a name and subgroups
    DiagnosticGroup subgroup1 = new DiagnosticGroup("subgroup1");
    DiagnosticGroup subgroup2 = new DiagnosticGroup("subgroup2");
    DiagnosticGroup[] expectedGroups = {subgroup1, subgroup2};
    
    DiagnosticGroup actualGroup = DiagnosticGroups.registerGroup("testName", expectedGroups);
    
    assertEquals(subgroup1, actualGroup.getGroups().get(0));
    assertEquals(subgroup2, actualGroup.getGroups().get(1));
  }
  
  @Test
  public void testGetRegisteredGroups() {
    // Typical use case: get a map of all registered diagnostic groups
    DiagnosticGroup expectedGroup = new DiagnosticGroup("testGroup");
    DiagnosticGroups.registerGroup("testName", expectedGroup);
    
    Map<String, DiagnosticGroup> actualGroups = DiagnosticGroups.getRegisteredGroups();
    
    assertTrue(actualGroups.containsKey("testName"));
    assertEquals(expectedGroup, actualGroups.get("testName"));
  }
  
  @Test
  public void testForName() {
    // Typical use case: get a registered diagnostic group by name
    DiagnosticGroup expectedGroup = new DiagnosticGroup("testGroup");
    DiagnosticGroups.registerGroup("testName", expectedGroup);
    
    DiagnosticGroup actualGroup = DiagnosticGroups.forName("testName");
    
    assertEquals(expectedGroup, actualGroup);
  }
  
  @Test
  public void testSetWarningLevels() {
    // Typical use case: set warning levels for a list of diagnostic groups
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("group1", "group2");
    CheckLevel level = CheckLevel.WARNING;
    
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    DiagnosticGroups.registerGroup("group1", group1);
    DiagnosticGroups.registerGroup("group2", group2);
    
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    
    assertEquals(level, options.getWarningLevel(group1));
    assertEquals(level, options.getWarningLevel(group2));
  }
}