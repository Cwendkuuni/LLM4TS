import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class OptionsTest {

   @Test
   public void testAddOptionGroup() {
       Options options = new Options();
       OptionGroup group1 = new OptionGroup();
       Option option1 = new Option("a", "test1", false, "Test option 1");
       Option option2 = new Option("b", "test2", false, "Test option 2");
 
       group1.addOption(option1);
       group1.addOption(option2);
 
       options.addOptionGroup(group1);
 
       assertTrue(options.getOptionGroups().contains(group1));
   }

   @Test
   public void testAddOption() {
       Options options = new Options();
       Option option1 = new Option("a", "option1", false, "Test option 1");
       options.addOption(option1);

       assertTrue(options.getOptions().contains(option1));
   }

   @Test
   public void testGetOption() {
       Options options = new Options();
       Option option1 = new Option("a", "option1", false, "Test option 1");
       options.addOption(option1);

       Option retrievedOption = options.getOption("a");
       assertEquals(option1, retrievedOption);
   }

   @Test
   public void testGetMatchingOptions() {
       Options options = new Options();
       Option option1 = new Option("test1", false, "Test option 1");
       Option option2 = new Option("test2", false, "Test option 2");
       options.addOption(option1);
       options.addOption(option2);

       List<String> matchingOptions = options.getMatchingOptions("test");
       assertTrue(matchingOptions.contains("test1"));
       assertTrue(matchingOptions.contains("test2"));
   }

   @Test
   public void testHasOption() {
       Options options = new Options();
       Option option1 = new Option("a", "option1", false, "Test option 1");
       options.addOption(option1);

       assertTrue(options.hasOption("a"));
       assertFalse(options.hasOption("b"));
   }

   @Test
   public void testHasLongOption() {
       Options options = new Options();
       Option option1 = new Option("a", "option1", false, "Test option 1");
       options.addOption(option1);

       assertTrue(options.hasLongOption("option1"));
       assertFalse(options.hasLongOption("option2"));
   }

   @Test
   public void testHasShortOption() {
       Options options = new Options();
       Option option1 = new Option("a", "option1", false, "Test option 1");
       options.addOption(option1);

       assertTrue(options.hasShortOption("a"));
       assertFalse(options.hasShortOption("b"));
   }

   @Test
   public void testGetOptionGroup() {
       Options options = new Options();
       OptionGroup group1 = new OptionGroup();
       Option option1 = new Option("a", "test1", false, "Test option 1");
       Option option2 = new Option("b", "test2", false, "Test option 2");

       group1.addOption(option1);
       group1.addOption(option2);

       options.addOptionGroup(group1);

       OptionGroup retrievedGroup = options.getOptionGroup(option1);
       assertEquals(group1, retrievedGroup);
   }
}