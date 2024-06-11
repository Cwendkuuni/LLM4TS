import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup_withNameAndGroup_shouldRegisterGroup() {
    // Arrange
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroupName";
    DiagnosticGroup group = new DiagnosticGroup();

    // Act
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(name, group);

    // Assert
    assertEquals(group, registeredGroup);
  }

  @Test
  public void testRegisterGroup_withNameAndTypes_shouldRegisterGroup() {
    // Arrange
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroupName";
    DiagnosticType[] types = { new DiagnosticType(1), new DiagnosticType(2) };

    // Act
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(name, types);

    // Assert
    assertNotNull(registeredGroup);
    assertEquals(name, registeredGroup.getName());
    assertArrayEquals(types, registeredGroup.getTypes());
  }

  @Test
  public void testRegisterGroup_withNameAndGroups_shouldRegisterGroup() {
    // Arrange
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroupName";
    DiagnosticGroup group1 = new DiagnosticGroup();
    DiagnosticGroup group2 = new DiagnosticGroup();

    // Act
    DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(name, group1, group2);

    // Assert
    assertNotNull(registeredGroup);
    assertEquals(name, registeredGroup.getName());
    assertArrayEquals(new DiagnosticGroup[] { group1, group2 }, registeredGroup.getGroups());
  }

  @Test
  public void testGetRegisteredGroups_shouldReturnCopyOfRegisteredGroups() {
    // Arrange
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup("group1", new DiagnosticGroup());
    diagnosticGroups.registerGroup("group2", new DiagnosticGroup());

    // Act
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    // Assert
    assertNotNull(registeredGroups);
    assertEquals(2, registeredGroups.size());
    assertFalse(registeredGroups.containsKey("group1"));
    assertFalse(registeredGroups.containsKey("group2"));
  }

  @Test
  public void testForName_withExistingGroupName_shouldReturnGroup() {
    // Arrange
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "testGroupName";
    DiagnosticGroup group = new DiagnosticGroup();
    diagnosticGroups.registerGroup(name, group);

    // Act
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);

    // Assert
    assertEquals(group, registeredGroup);
  }

  @Test
  public void testForName_withNonExistingGroupName_shouldReturnNull() {
    // Arrange
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String name = "nonExistingGroup";

    // Act
    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);

    // Assert
    assertNull(registeredGroup);
  }

  @Test
  public void testSetWarningLevels_withValidOptionsAndGroups_shouldSetWarningLevels() {
    // Arrange
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> groups = Arrays.asList("group1", "group2");
    CheckLevel level = CheckLevel.WARNING;

    diagnosticGroups.registerGroup("group1", new DiagnosticGroup());
    diagnosticGroups.registerGroup("group2", new DiagnosticGroup());

    // Act
    diagnosticGroups.setWarningLevels(options, groups, level);

    // Assert
    assertEquals(level, options.getWarningLevel("group1"));
    assertEquals(level, options.getWarningLevel("group2"));
  }
}