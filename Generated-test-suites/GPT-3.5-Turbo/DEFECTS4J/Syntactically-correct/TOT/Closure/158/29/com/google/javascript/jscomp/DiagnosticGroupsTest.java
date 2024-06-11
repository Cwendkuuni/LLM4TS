import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

   @Test
   public void testRegisterGroup() {
      // Test case for registerGroup(String name, DiagnosticGroup group)
      DiagnosticGroup group = new DiagnosticGroup("testGroup", DiagnosticType.ERROR);
      assertEquals(group, DiagnosticGroups.registerGroup("testGroup", group));

      // Test case for registerGroup(String name, DiagnosticType ... types)
      DiagnosticGroup group2 = DiagnosticGroups.registerGroup("testGroup2",
              DiagnosticType.WARNING, DiagnosticType.ERROR);

      assertEquals(group2, DiagnosticGroups.forName("testGroup2"));
   }

   @Test
   public void testGetRegisteredGroups() {
      DiagnosticGroup group = new DiagnosticGroup("testGroup", DiagnosticType.WARNING, DiagnosticType.ERROR);
      DiagnosticGroups.registerGroup("testGroup", group);
  
      Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
      assertNotNull(registeredGroups);
      assertTrue(registeredGroups.containsKey("testGroup"));
      assertEquals(group, registeredGroups.get("testGroup"));
   }

   @Test
   public void testForName() {
      DiagnosticGroup group = new DiagnosticGroup("testGroup", DiagnosticType.WARNING);
      DiagnosticGroups.registerGroup("testGroup", group);
  
      assertEquals(group, DiagnosticGroups.forName("testGroup"));
      assertNull(DiagnosticGroups.forName("unknownGroup"));
   }

   @Test
   public void testSetWarningLevels() {
      CompilerOptions options = new CompilerOptions();
      DiagnosticGroup group1 = DiagnosticGroups.registerGroup("testGroup1", DiagnosticType.WARNING);
      DiagnosticGroup group2 = DiagnosticGroups.registerGroup("testGroup2", DiagnosticType.ERROR);
      List<String> diagnosticGroups = new ArrayList<>();
      diagnosticGroups.add("testGroup1");
      diagnosticGroups.add("testGroup2");
  
      DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.ERROR);
  
      assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
      assertEquals(CheckLevel.ERROR, options.getWarningLevel(group2));
   }

   @Test
   public void testConstructor() {
      DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
      assertNotNull(diagnosticGroups);
   }

   @Test
   public void testGlobalThis() {
      DiagnosticGroup group = DiagnosticGroups.GLOBAL_THIS;
      assertNotNull(group);
      assertTrue(group.contains(CheckGlobalThis.GLOBAL_THIS));
   }

   // Add more test cases for other DiagnosticGroups...
}