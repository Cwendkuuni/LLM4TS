import org.junit.Assert;
import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    // Test if registerGroup correctly adds the group to groupsByName map and returns the correct group.
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroups.registerGroup("testGroup", group);
    
    Assert.assertEquals(group, DiagnosticGroups.getRegisteredGroups().
                        get("testGroup"));
  }

  @Test
  public void testRegisterGroupWithTypes() {
    // Test if registerGroup correctly adds the group with diagnostic types to groupsByName map and returns the correct group.
    DiagnosticType type1 = new DiagnosticType("testType1");
    DiagnosticType type2 = new DiagnosticType("testType2");
    
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", type1, type2);
    
    Assert.assertEquals(group, DiagnosticGroups.getRegisteredGroups().
                        get("testGroup"));
    Assert.assertTrue(group.getTypes().contains(type1));
    Assert.assertTrue(group.getTypes().contains(type2));
  }

  @Test
  public void testRegisterGroupWithGroups() {
    // Test if registerGroup correctly adds the group with diagnostic groups to groupsByName map and returns the correct group.
    DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
    DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
    
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", group1, group2);
    
    Assert.assertEquals(group, DiagnosticGroups.getRegisteredGroups().
                        get("testGroup"));
    Assert.assertTrue(group.getGroups().contains(group1));
    Assert.assertTrue(group.getGroups().contains(group2));
  }

  @Test
  public void testGetRegisteredGroups() {
    // Test if getRegisteredGroups returns a copy of the groupsByName map.
    DiagnosticGroups.getRegisteredGroups().put("testGroup", new DiagnosticGroup("testGroup"));
    
    Assert.assertNull(DiagnosticGroups.getRegisteredGroups().get("testGroup"));
  }

  @Test
  public void testForName() {
    // Test if forName returns the correct diagnostic group for a given name.
    DiagnosticGroup group = new DiagnosticGroup("testGroup");
    DiagnosticGroups.registerGroup("testGroup", group);
    
    Assert.assertEquals(group, DiagnosticGroups.forName("testGroup"));
  }

  @Test
  public void testSetWarningLevels() {
    // Test if setWarningLevels correctly sets the warning levels for the given diagnostic groups.
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("testGroup1");
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("testGroup2");
    options.setWarningLevel(group1, CheckLevel.WARNING);
    
    DiagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup1", "testGroup2"), CheckLevel.ERROR);
    
    Assert.assertEquals(CheckLevel.ERROR, options.getWarningLevel(group1));
    Assert.assertEquals(CheckLevel.ERROR, options.getWarningLevel(group2));
  }

  @Test(expected = NullPointerException.class)
  public void testSetWarningLevelsWithUnknownGroup() {
    // Test if setWarningLevels throws NullPointerException when an unknown diagnostic group is provided.
    CompilerOptions options = new CompilerOptions();
    
    DiagnosticGroups.setWarningLevels(options, Arrays.asList("testGroup"), CheckLevel.ERROR);
  }
}