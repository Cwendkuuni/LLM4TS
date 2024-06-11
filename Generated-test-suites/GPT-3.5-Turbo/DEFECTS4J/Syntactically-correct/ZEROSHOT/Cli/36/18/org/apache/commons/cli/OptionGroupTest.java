import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

   @Test
   public void testAddOption() {
      OptionGroup group = new OptionGroup();
      Option option = new Option("t", "test", false, "A test option");
      OptionGroup result = group.addOption(option);
      assertEquals(group, result);
   }
   
   @Test
   public void testGetNames() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "option1", false, "Option 1");
      Option option2 = new Option("b", "option2", false, "Option 2");
      group.addOption(option1);
      group.addOption(option2);
      Collection<String> names = group.getNames();
      assertTrue(names.contains("a"));
      assertTrue(names.contains("b"));
   }
   
   @Test
   public void testGetOptions() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "option1", false, "Option 1");
      Option option2 = new Option("b", "option2", false, "Option 2");
      group.addOption(option1);
      group.addOption(option2);
      Collection<Option> options = group.getOptions();
      assertTrue(options.contains(option1));
      assertTrue(options.contains(option2));
   }
   
   @Test
   public void testSetSelected() throws AlreadySelectedException {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "option1", false, "Option 1");
      Option option2 = new Option("b", "option2", false, "Option 2");
      group.addOption(option1);
      group.addOption(option2);
      
      // Selecting option1 should not throw an exception
      group.setSelected(option1);
      assertEquals(option1.getKey(), group.getSelected());
      
      // Selecting option2 should throw an exception since an option from this group has already been selected
      try {
         group.setSelected(option2);
      } catch (AlreadySelectedException e) {
         assertTrue(e.getGroup() == group);
         assertTrue(e.getOption() == option2);
      }
   }
   
   @Test
   public void testGetSelected() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "option1", false, "Option 1");
      Option option2 = new Option("b", "option2", false, "Option 2");
      group.addOption(option1);
      group.addOption(option2);
      
      // No option has been selected yet
      assertNull(group.getSelected());
      
      // Select option1
      group.setSelected(option1);
      assertEquals(option1.getKey(), group.getSelected());
   }
   
   @Test
   public void testSetRequired() {
      OptionGroup group = new OptionGroup();
      
      // By default, the group is not required
      assertFalse(group.isRequired());
      
      // Set the group as required
      group.setRequired(true);
      assertTrue(group.isRequired());
   }
   
   @Test
   public void testToString() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "option1", false, "Option 1");
      Option option2 = new Option("b", "option2", false, "Option 2");
      group.addOption(option1);
      group.addOption(option2);
      
      String expected = "[-a Option 1, -b Option 2]";
      
      assertEquals(expected, group.toString());
   }
   
}