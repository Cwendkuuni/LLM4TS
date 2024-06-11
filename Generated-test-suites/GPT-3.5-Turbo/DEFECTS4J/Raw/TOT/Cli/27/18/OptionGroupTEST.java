import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

   // Test for addOption method
   @Test
   public void testAddOption() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      Option option2 = new Option("d", "delete", false, "Delete option");
      
      group.addOption(option1);
      group.addOption(option2);
      
      assertTrue(group.getOptions().contains(option1));
      assertTrue(group.getOptions().contains(option2));
   }

   // Test for getNames method
   @Test
   public void testGetNames() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      Option option2 = new Option("d", "delete", false, "Delete option");
      
      group.addOption(option1);
      group.addOption(option2);
      
      assertTrue(group.getNames().contains("a"));
      assertTrue(group.getNames().contains("d"));
   }

   // Test for getOptions method
   @Test
   public void testGetOptions() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      Option option2 = new Option("d", "delete", false, "Delete option");
      
      group.addOption(option1);
      group.addOption(option2);
      
      assertTrue(group.getOptions().size() == 2);
   }

   // Test for setSelected method when an option has not been selected yet
   @Test
   public void testSetSelectedNoOptionSelected() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      
      group.addOption(option1);
      
      group.setSelected(null);
      
      assertNull(group.getSelected());
   }

   // Test for setSelected method when an option is selected for the first time
   @Test
   public void testSetSelectedFirstTime() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      
      group.addOption(option1);
      
      group.setSelected(option1);
      
      assertEquals("a", group.getSelected());
   }

   // Test for setSelected method when a different option is selected while another option is already selected
   @Test(expected = AlreadySelectedException.class)
   public void testSetSelectedDifferentOptionSelected() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      Option option2 = new Option("d", "delete", false, "Delete option");
      
      group.addOption(option1);
      group.addOption(option2);
      
      group.setSelected(option1);
      group.setSelected(option2);
   }

   // Test for getSelected method when no option has been selected
   @Test
   public void testGetSelectedNoOptionSelected() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      
      group.addOption(option1);
      
      assertNull(group.getSelected());
   }

   // Test for getSelected method when an option has been selected
   @Test
   public void testGetSelectedOptionSelected() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      
      group.addOption(option1);
      
      group.setSelected(option1);
      
      assertEquals("a", group.getSelected());
   }

   // Test for setRequired method
   @Test
   public void testSetRequired() {
      OptionGroup group = new OptionGroup();
      
      assertFalse(group.isRequired());
      
      group.setRequired(true);
      
      assertTrue(group.isRequired());
   }

   // Test for isRequired method
   @Test
   public void testIsRequired() {
      OptionGroup group = new OptionGroup();
      
      assertFalse(group.isRequired());
      
      group.setRequired(true);
      
      assertTrue(group.isRequired());
   }

   // Test for toString method when there are multiple options in the group
   @Test
   public void testToStringMultipleOptions() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      Option option2 = new Option("d", "delete", false, "Delete option");
      
      group.addOption(option1);
      group.addOption(option2);
      
      String expected = "[-a Add option, -d Delete option]";
      
      assertEquals(expected, group.toString());
   }

   // Test for toString method when there is a single option in the group
   @Test
   public void testToStringSingleOption() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", true, "Add option");
      
      group.addOption(option1);
      
      String expected = "[-a Add option]";
      
      assertEquals(expected, group.toString());
   }

}