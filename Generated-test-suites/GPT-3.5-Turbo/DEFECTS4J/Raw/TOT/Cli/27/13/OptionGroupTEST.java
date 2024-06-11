
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

   @Test
   public void testAddOption() {
      OptionGroup group = new OptionGroup();
      Option option = new Option("a", "add", false, "Add option");
      
      OptionGroup result = group.addOption(option);
      
      assertTrue(group.getOptions().contains(option));
      assertSame(group, result);
   }
   
   @Test
   public void testGetNames() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", false, "Add option");
      Option option2 = new Option("r", "remove", false, "Remove option");
      group.addOption(option1);
      group.addOption(option2);
      
      Collection<String> names = group.getNames();
      
      assertTrue(names.contains("a"));
      assertTrue(names.contains("r"));
   }
   
   @Test
   public void testGetOptions() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", false, "Add option");
      Option option2 = new Option("r", "remove", false, "Remove option");
      group.addOption(option1);
      group.addOption(option2);
      
      Collection<Option> options = group.getOptions();
      
      assertTrue(options.contains(option1));
      assertTrue(options.contains(option2));
   }
   
   @Test
   public void testSetSelected() throws AlreadySelectedException {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", false, "Add option");
      Option option2 = new Option("r", "remove", false, "Remove option");
      group.addOption(option1);
      group.addOption(option2);
      
      group.setSelected(option1);
      
      assertEquals("a", group.getSelected());
   }
   
   @Test(expected = AlreadySelectedException.class)
   public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", false, "Add option");
      Option option2 = new Option("r", "remove", false, "Remove option");
      group.addOption(option1);
      group.addOption(option2);
      
      group.setSelected(option1);
      group.setSelected(option2);
   }
   
   @Test
   public void testGetSelected() throws AlreadySelectedException {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", false, "Add option");
      Option option2 = new Option("r", "remove", false, "Remove option");
      group.addOption(option1);
      group.addOption(option2);
      
      group.setSelected(option1);
      
      assertEquals("a", group.getSelected());
   }
   
   @Test
   public void testSetRequired() {
      OptionGroup group = new OptionGroup();
      
      group.setRequired(true);
      
      assertTrue(group.isRequired());
   }
   
   @Test
   public void testToString() {
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "add", false, "Add option");
      Option option2 = new Option("r", "remove", false, "Remove option");
      group.addOption(option1);
      group.addOption(option2);
      
      String result = group.toString();
      
      assertEquals("[--add Add option, --remove Remove option]", result);
   }
}
