import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup_withNameAndGroup_shouldReturnRegisteredGroup() {
    // Setup
    DiagnosticGroup groupMock = mock(DiagnosticGroup.class);
    when(groupMock.getName()).thenReturn("testGroup");
    String groupName = "testGroup";
    
    // Action
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(groupName, groupMock);
    
    // Assert
    assertNotNull(registeredGroup);
    assertEquals(groupMock, registeredGroup);
  }

  @Test
  public void testRegisterGroup_withNameAndTypes_shouldReturnGroupWithTypes() {
    // Setup
    DiagnosticType typeMock = mock(DiagnosticType.class);
    DiagnosticType[] types = { typeMock };
    String groupName = "testGroup";
    
    // Action
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(groupName, types);
    
    // Assert
    assertNotNull(registeredGroup);
    assertEquals(groupName, registeredGroup.getName());
    assertEquals(types.length, registeredGroup.getTypes().size());
    assertTrue(registeredGroup.getTypes().contains(typeMock));
  }

  @Test
  public void testRegisterGroup_withNameAndGroups_shouldReturnGroupWithNestedGroups() {
    // Setup
    DiagnosticGroup groupMock = mock(DiagnosticGroup.class);
    DiagnosticGroup[] groups = { groupMock };
    String groupName = "testGroup";
    
    // Action
    DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup(groupName, groups);
    
    // Assert
    assertNotNull(registeredGroup);
    assertEquals(groupName, registeredGroup.getName());
    assertEquals(groups.length, registeredGroup.getGroups().size());
    assertTrue(registeredGroup.getGroups().contains(groupMock));
  }

  @Test
  public void testGetRegisteredGroups_shouldReturnImmutableMapOfGroups() {
    // Setup
    DiagnosticGroup groupMock = mock(DiagnosticGroup.class);
    String groupName = "testGroup";
    DiagnosticGroups.registerGroup(groupName, groupMock);
    
    // Action
    Map<String, DiagnosticGroup> registeredGroups = new DiagnosticGroups().getRegisteredGroups();
    
    // Assert
    assertNotNull(registeredGroups);
    assertEquals(1, registeredGroups.size());
    assertTrue(registeredGroups.containsKey(groupName));
    assertEquals(groupMock, registeredGroups.get(groupName));
  }

  @Test
  public void testForName_withExistingGroupName_shouldReturnRegisteredGroup() {
    // Setup
    DiagnosticGroup groupMock = mock(DiagnosticGroup.class);
    String groupName = "testGroup";
    DiagnosticGroups.registerGroup(groupName, groupMock);
    
    // Action
    DiagnosticGroup registeredGroup = new DiagnosticGroups().forName(groupName);
    
    // Assert
    assertNotNull(registeredGroup);
    assertEquals(groupMock, registeredGroup);
  }

  @Test
  public void testForName_withNonExistentGroupName_shouldReturnNull() {
    // Setup
    String groupName = "nonExistentGroup";
    
    // Action
    DiagnosticGroup registeredGroup = new DiagnosticGroups().forName(groupName);
    
    // Assert
    assertNull(registeredGroup);
  }

  @Test
  public void testSetWarningLevels_withOptionsAndDiagnosticGroupsAndLevel_shouldSetWarningLevels() {
    // Setup
    CompilerOptions optionsMock = mock(CompilerOptions.class);
    CheckLevel level = CheckLevel.WARNING;
    List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
    DiagnosticGroup groupMock1 = mock(DiagnosticGroup.class);
    DiagnosticGroup groupMock2 = mock(DiagnosticGroup.class);
    DiagnosticGroups diagnosticGroupsMock = mock(DiagnosticGroups.class);
    when(diagnosticGroupsMock.forName("testGroup1")).thenReturn(groupMock1);
    when(diagnosticGroupsMock.forName("testGroup2")).thenReturn(groupMock2);
    
    // Action
    diagnosticGroupsMock.setWarningLevels(optionsMock, diagnosticGroups, level);
    
    // Assert
    ArgumentCaptor<DiagnosticGroup> diagnosticGroupCaptor = ArgumentCaptor.forClass(DiagnosticGroup.class);
    ArgumentCaptor<CheckLevel> checkLevelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    Mockito.verify(optionsMock, Mockito.times(2)).setWarningLevel(diagnosticGroupCaptor.capture(), checkLevelCaptor.capture());
    
    List<DiagnosticGroup> capturedGroups = diagnosticGroupCaptor.getAllValues();
    List<CheckLevel> capturedLevels = checkLevelCaptor.getAllValues();
    
    assertNotNull(capturedGroups);
    assertEquals(2, capturedGroups.size());
    assertTrue(capturedGroups.contains(groupMock1));
    assertTrue(capturedGroups.contains(groupMock2));
    
    assertNotNull(capturedLevels);
    assertEquals(2, capturedLevels.size());
    assertTrue(capturedLevels.contains(level));
  }
}