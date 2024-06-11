import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

   @Test
   public void testAddOption() {
      Options options = new Options();
      Option option1 = new Option("a", "apple", false, "Description1");
      options.addOption(option1);
      assertTrue(options.hasOption("a"));
      assertTrue(options.hasLongOption("apple"));
      assertFalse(options.hasShortOption("b"));
   }

   @Test
   public void testAddOptionWithArg() {
      Options options = new Options();
      Option option2 = new Option("b", true, "Description2");
      options.addOption(option2);
      assertTrue(options.hasOption("b"));
      assertFalse(options.hasLongOption("banana"));
      assertTrue(options.hasShortOption("b"));
   }

   @Test
   public void testAddOptionWithLongOpt() {
      Options options = new Options();
      Option option3 = new Option("c", "cat", false, "Description3");
      options.addOption(option3);
      assertTrue(options.hasOption("c"));
      assertTrue(options.hasLongOption("cat"));
      assertFalse(options.hasShortOption("c"));
   }

   @Test
   public void testAddOptionWithLongOptAndArg() {
      Options options = new Options();
      Option option4 = new Option("d", "dog", true, "Description4");
      options.addOption(option4);
      assertTrue(options.hasOption("d"));
      assertTrue(options.hasLongOption("dog"));
      assertFalse(options.hasShortOption("d"));
   }

   @Test
   public void testAddOptionGroup() {
      Options options = new Options();
      Option optionA = new Option("a", "apple", false, "DescriptionA");
      Option optionB = new Option("b", "banana", true, "DescriptionB");
      Option optionC = new Option("c", "cherry", false, "DescriptionC");
      OptionGroup group = new OptionGroup();
      group.addOption(optionA);
      group.addOption(optionB);
      group.addOption(optionC);
      options.addOptionGroup(group);
      assertTrue(options.hasOption("a"));
      assertTrue(options.hasLongOption("apple"));
      assertTrue(options.hasOption("b"));
      assertTrue(options.hasLongOption("banana"));
      assertTrue(options.hasOption("c"));
      assertTrue(options.hasLongOption("cherry"));
      assertFalse(options.hasShortOption("d"));
   }
   
   @Test
   public void testGetOption() {
      Options options = new Options();
      Option option1 = new Option("a", "apple", false, "Description1");
      options.addOption(option1);
      Option option2 = new Option("b", true, "Description2");
      options.addOption(option2);
      Option option3 = new Option("c", "cat", false, "Description3");
      options.addOption(option3);
      Option option4 = new Option("d", "dog", true, "Description4");
      options.addOption(option4);
      assertEquals(option1, options.getOption("a"));
      assertEquals(option2, options.getOption("b"));
      assertEquals(option3, options.getOption("cat"));
      assertEquals(option4, options.getOption("dog"));
   }
   
   @Test
   public void testGetMatchingOptions() {
      Options options = new Options();
      Option option1 = new Option("a", "apple", false, "Description1");
      options.addOption(option1);
      Option option2 = new Option("b", true, "Description2");
      options.addOption(option2);
      Option option3 = new Option("c", "cat", false, "Description3");
      options.addOption(option3);
      Option option4 = new Option("d", "dog", true, "Description4");
      options.addOption(option4);
      assertEquals(3, options.getMatchingOptions("a").size());
      assertTrue(options.getMatchingOptions("a").contains("apple"));
      assertEquals(1, options.getMatchingOptions("cat").size());
      assertTrue(options.getMatchingOptions("cat").contains("cat"));
      assertEquals(2, options.getMatchingOptions("d").size());
      assertTrue(options.getMatchingOptions("d").contains("dog"));
   }
}