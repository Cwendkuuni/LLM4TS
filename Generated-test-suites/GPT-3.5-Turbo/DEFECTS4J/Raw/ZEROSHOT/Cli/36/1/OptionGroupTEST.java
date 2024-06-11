import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

   @Test
   public void testAddOption() {
      OptionGroup optionGroup = new OptionGroup();
      
      Option option1 = new Option("a", false, "Option A");
      Option result1 = optionGroup.addOption(option1);
      assertEquals(optionGroup.getOptions().contains(option1), true);
      assertEquals(result1, optionGroup);

      Option option2 = new Option("b", false, "Option B");
      Option result2 = optionGroup.addOption(option2);
      assertEquals(optionGroup.getOptions().contains(option2), true);
      assertEquals(result2, optionGroup);
   }
   
   @Test
   public void testGetNames() {
      OptionGroup optionGroup = new OptionGroup();
      
      Option option1 = new Option("a", false, "Option A");
      optionGroup.addOption(option1);

      Option option2 = new Option("b", false, "Option B");
      optionGroup.addOption(option2);
      
      assertEquals(optionGroup.getNames().contains("a"), true);
      assertEquals(optionGroup.getNames().contains("b"), true);
   }
   
   @Test
   public void testGetOptions() {
      OptionGroup optionGroup = new OptionGroup();
      
      Option option1 = new Option("a", false, "Option A");
      optionGroup.addOption(option1);

      Option option2 = new Option("b", false, "Option B");
      optionGroup.addOption(option2);
      
      assertEquals(optionGroup.getOptions().contains(option1), true);
      assertEquals(optionGroup.getOptions().contains(option2), true);
   }
   
   @Test
   public void testSetSelected() {
      OptionGroup optionGroup = new OptionGroup();
      
      Option option1 = new Option("a", false, "Option A");
      optionGroup.addOption(option1);

      Option option2 = new Option("b", false, "Option B");
      optionGroup.addOption(option2);
      
      assertEquals(optionGroup.getSelected(), null);
      
      optionGroup.setSelected(option1);
      assertEquals(optionGroup.getSelected(), "a");
      
      optionGroup.setSelected(option2);
      assertEquals(optionGroup.getSelected(), "b");
      
      try {
         optionGroup.setSelected(option1);
         fail("Exception not thrown");
      } catch (AlreadySelectedException e) {
         assertEquals(optionGroup.getSelected(), "b");
      }
   }
   
   @Test
   public void testGetSelected() {
      OptionGroup optionGroup = new OptionGroup();
      
      Option option1 = new Option("a", false, "Option A");
      optionGroup.addOption(option1);

      Option option2 = new Option("b", false, "Option B");
      optionGroup.addOption(option2);
      
      assertEquals(optionGroup.getSelected(), null);
      
      optionGroup.setSelected(option1);
      assertEquals(optionGroup.getSelected(), "a");
      
      optionGroup.setSelected(option2);
      assertEquals(optionGroup.getSelected(), "b");
   }
   
   @Test
   public void testSetRequired() {
      OptionGroup optionGroup = new OptionGroup();
      
      assertEquals(optionGroup.isRequired(), false);
      
      optionGroup.setRequired(true);
      assertEquals(optionGroup.isRequired(), true);
      
      optionGroup.setRequired(false);
      assertEquals(optionGroup.isRequired(), false);
   }
   
   @Test
   public void testToString() {
      OptionGroup optionGroup = new OptionGroup();
      
      Option option1 = new Option("a", false, "Option A");
      optionGroup.addOption(option1);

      Option option2 = new Option("b", false, "Option B");
      optionGroup.addOption(option2);
      
      assertEquals(optionGroup.toString(), "[-a Option A, -b Option B]");
   }
}