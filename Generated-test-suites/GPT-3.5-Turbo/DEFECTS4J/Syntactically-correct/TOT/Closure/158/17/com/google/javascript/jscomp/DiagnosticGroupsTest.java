import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DiagnosticGroupsTest {
  
  private DiagnosticGroups diagnosticGroups;
  
  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }
  
  @Test
  public void testRegisterGroup_withNameAndGroup_shouldReturnGroup() {
    String name = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup(name, DiagnosticType.WARNING);
    
    DiagnosticGroup result = diagnosticGroups.registerGroup(name, group);
    
    assertEquals(group, result);
  }
  
  @Test
  public void testRegisterGroup_withNameAndTypes_shouldReturnGroup() {
    String name = "testGroup";
    DiagnosticType type = new DiagnosticType("testType");
    
    DiagnosticGroup result = diagnosticGroups.registerGroup(name, type);
    
    assertEquals(type, result.getTypes().get(0));
  }
  
  @Test
  public void testGetRegisteredGroups_shouldReturnAllRegisteredGroups() {
    // Prepare
    String name1 = "testGroup1";
    DiagnosticType type1 = new DiagnosticType("testType1");
    DiagnosticGroup group1 = new DiagnosticGroup(name1, type1);
    
    String name2 = "testGroup2";
    DiagnosticType type2 = new DiagnosticType("testType2");
    DiagnosticGroup group2 = new DiagnosticGroup(name2, type2);
    
    diagnosticGroups.registerGroup(name1, group1);
    diagnosticGroups.registerGroup(name2, group2);
    
    // Execute
    Map<String, DiagnosticGroup> result = diagnosticGroups.getRegisteredGroups();
    
    // Assert
    assertEquals(2, result.size());
    assertEquals(group1, result.get(name1));
    assertEquals(group2, result.get(name2));
  }
  
  @Test
  public void testForName_withRegisteredGroupName_shouldReturnGroup() {
    // Prepare
    String name = "testGroup";
    DiagnosticType type = new DiagnosticType("testType");
    DiagnosticGroup group = new DiagnosticGroup(name, type);
    
    diagnosticGroups.registerGroup(name, group);
    
    // Execute
    DiagnosticGroup result = diagnosticGroups.forName(name);
    
    // Assert
    assertEquals(group, result);
  }
  
  @Test
  public void testForName_withUnregisteredGroupName_shouldReturnNull() {
    // Prepare
    String name = "unregisteredGroup";
    
    // Execute
    DiagnosticGroup result = diagnosticGroups.forName(name);
    
    // Assert
    assertNull(result);
  }
  
  @Test
  public void testSetWarningLevels_withExistingDiagnosticGroups_shouldSetWarningLevels() {
    // Prepare
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = List.of("testGroup1", "testGroup2");
    CheckLevel level = CheckLevel.WARNING;
    
    String name1 = "testGroup1";
    DiagnosticType type1 = new DiagnosticType("testType1");
    DiagnosticGroup group1 = new DiagnosticGroup(name1, type1);
    
    String name2 = "testGroup2";
    DiagnosticType type2 = new DiagnosticType("testType2");
    DiagnosticGroup group2 = new DiagnosticGroup(name2, type2);
    
    diagnosticGroups.registerGroup(name1, group1);
    diagnosticGroups.registerGroup(name2, group2);
    
    // Execute
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
    
    // Assert
    assertEquals(level, options.getWarningLevel(group1));
    assertEquals(level, options.getWarningLevel(group2));
  }
  
  @Test(expected = NullPointerException.class)
  public void testSetWarningLevels_withUnregisteredDiagnosticGroup_shouldThrowNullPointerException() {
    // Prepare
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = List.of("unregisteredGroup");
    CheckLevel level = CheckLevel.WARNING;
    
    // Execute
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
  }
}