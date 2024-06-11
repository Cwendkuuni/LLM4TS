import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    
    DiagnosticGroups.registerGroup("name1", group1);
    DiagnosticGroups.registerGroup("name2", group2);
    
    assertEquals(group1, DiagnosticGroups.forName("name1"));
    assertEquals(group2, DiagnosticGroups.forName("name2"));
    assertNull(DiagnosticGroups.forName("invalidName"));
  }
  
  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    
    DiagnosticGroups.registerGroup("name1", group1);
    DiagnosticGroups.registerGroup("name2", group2);
    
    assertEquals(2, DiagnosticGroups.getRegisteredGroups().size());
    assertTrue(DiagnosticGroups.getRegisteredGroups().containsKey("name1"));
    assertTrue(DiagnosticGroups.getRegisteredGroups().containsKey("name2"));
    assertFalse(DiagnosticGroups.getRegisteredGroups().containsKey("invalidName"));
  }
  
  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroup group1 = new DiagnosticGroup("group1");
    DiagnosticGroup group2 = new DiagnosticGroup("group2");
    
    DiagnosticGroups.registerGroup("name1", group1);
    DiagnosticGroups.registerGroup("name2", group2);
    
    DiagnosticGroups.setWarningLevels(options, Arrays.asList("name1"), CheckLevel.WARNING);
    
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
    assertEquals(CheckLevel.OFF, options.getWarningLevel(group2));
  }

}