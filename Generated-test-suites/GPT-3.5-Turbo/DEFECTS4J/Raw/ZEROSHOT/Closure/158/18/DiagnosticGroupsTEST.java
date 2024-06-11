import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class DiagnosticGroupsTest {
  
  private DiagnosticGroups diagnosticGroups;
  
  @Before
  public void setUp() {
    diagnosticGroups = new DiagnosticGroups();
  }
  
  @Test
  public void testRegisterGroup_WithNameAndGroup_ReturnsGroup() {
    String name = "test";
    DiagnosticGroup group = new DiagnosticGroup(name);
    
    DiagnosticGroup result = diagnosticGroups.registerGroup(name, group);
    
    assertEquals(group, result);
  }
  
  @Test
  public void testRegisterGroup_WithNameAndTypes_ReturnsGroup() {
    String name = "test";
    DiagnosticType[] types = {mock(DiagnosticType.class)};
    
    DiagnosticGroup result = diagnosticGroups.registerGroup(name, types);
    
    assertEquals(name, result.getName());
    assertEquals(1, result.getTypes().size());
    assertEquals(types[0], result.getTypes().get(0));
  }
  
  @Test
  public void testRegisterGroup_WithNameAndGroups_ReturnsGroup() {
    String name = "test";
    DiagnosticGroup[] groups = {mock(DiagnosticGroup.class)};
    
    DiagnosticGroup result = diagnosticGroups.registerGroup(name, groups);
    
    assertEquals(name, result.getName());
    assertEquals(1, result.getGroups().size());
    assertEquals(groups[0], result.getGroups().get(0));
  }
  
  @Test
  public void testGetRegisteredGroups_ReturnsCorrectMap() {
    Map<String, DiagnosticGroup> expected = mock(Map.class);
    when(diagnosticGroups.getRegisteredGroups()).thenReturn(expected);
    
    Map<String, DiagnosticGroup> result = diagnosticGroups.getRegisteredGroups();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testForName_ReturnsGroup() {
    String name = "test";
    DiagnosticGroup expected = mock(DiagnosticGroup.class);
    when(diagnosticGroups.forName(name)).thenReturn(expected);
    
    DiagnosticGroup result = diagnosticGroups.forName(name);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testSetWarningLevels_SetsWarningLevels() {
    CompilerOptions options = mock(CompilerOptions.class);
    List<String> diagnosticGroups = mock(List.class);
    String name = "test";
    DiagnosticGroup group = mock(DiagnosticGroup.class);
    when(diagnosticGroups.get(0)).thenReturn(name);
    when(diagnosticGroups.size()).thenReturn(1);
    when(diagnosticGroups.forName(name)).thenReturn(group);
    
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    
    verify(options).setWarningLevel(group, CheckLevel.WARNING);
  }
}