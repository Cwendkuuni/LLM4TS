import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

   @Test
   public void testAddOptionGroup() {
       // Test case 1: Valid OptionGroup
       Option opt1 = new Option("a", "apple", false, "option a");
       Option opt2 = new Option("b", "banana", false, "option b");
       OptionGroup group = new OptionGroup();
       group.addOption(opt1).addOption(opt2);

       Options options = new Options();
       options.addOptionGroup(group);

       assertEquals(2, options.getOptions().size());
       assertTrue(options.hasOption("a"));
       assertTrue(options.hasOption("b"));
       assertTrue(options.getOptionGroup(opt1).equals(group));
       assertTrue(options.getOptionGroup(opt2).equals(group));

       // Test case 2: Null OptionGroup
       try {
           options.addOptionGroup(null);
           fail("Null OptionGroup should throw IllegalArgumentException");
       } catch (IllegalArgumentException e) {
           assertEquals("OptionGroup must not be null", e.getMessage());
       }
   }

   @Test
   public void testAddOption() {
       // Test case 1: Valid option
       Option option = new Option("t", "test", false, "test option");
       Options options = new Options();
       options.addOption(option);

       assertEquals(1, options.getOptions().size());
       assertTrue(options.hasOption("t"));
       assertTrue(options.hasOption("test"));

       // Test case 2: Option with required and conflicting options
       Option option2 = new Option("o", "option", true, "option with argument");
       option2.setRequired(true);
       option2.addOption("o2", "option2", true, "second option");

       options.addOption(option2);
       assertEquals(2, options.getOptions().size());
       assertTrue(options.hasOption("o"));
       assertTrue(options.hasOption("option"));
       assertTrue(options.hasOption("o2"));
       assertTrue(options.hasOption("option2"));

       // Test case 3: Option with conflicting options only
       Option option3 = new Option("c", "conflict", false, "conflicting option");
       option3.addOption("c2", "conflict2", false, "second conflicting option");

       options.addOption(option3);
       assertEquals(3, options.getOptions().size());
       assertTrue(options.hasOption("c"));
       assertTrue(options.hasOption("conflict"));
       assertTrue(options.hasOption("c2"));
       assertTrue(options.hasOption("conflict2"));

       // Test case 4: Option with null key
       Option option4 = new Option(null, "nullKey", false, "option with null key");

       try {
           options.addOption(option4);
           fail("Null key option should throw IllegalArgumentException");
       } catch (IllegalArgumentException e) {
           assertEquals("Option key must not be null", e.getMessage());
       }
   }

   @Test
   public void testGetOption() {
       Option option1 = new Option("o", "option", false, "option");
       Option option2 = new Option("t", "test", true, "option with argument");
       Options options = new Options();
       options.addOption(option1);
       options.addOption(option2);

       // Test case 1: Get option with short name
       Option result1 = options.getOption("o");
       assertNotNull(result1);
       assertTrue(result1.equals(option1));

       // Test case 2: Get option with long name
       Option result2 = options.getOption("option");
       assertNotNull(result2);
       assertTrue(result2.equals(option1));

       // Test case 3: Get option with short name and argument
       Option result3 = options.getOption("t");
       assertNotNull(result3);
       assertTrue(result3.equals(option2));

       // Test case 4: Get option with long name and argument
       Option result4 = options.getOption("test");
       assertNotNull(result4);
       assertTrue(result4.equals(option2));

       // Test case 5: Get option with unknown name
       Option result5 = options.getOption("unknown");
       assertNull(result5);
   }

   @Test
   public void testGetMatchingOptions() {
       Option option1 = new Option("o", "option", false, "option");
       Option option2 = new Option("t", "test", true, "option with argument");
       Options options = new Options();
       options.addOption(option1);
       options.addOption(option2);

       // Test case 1: Partial name with short name match
       List<String> result1 = options.getMatchingOptions("o");
       assertNotNull(result1);
       assertEquals(1, result1.size());
       assertTrue(result1.contains("option"));

       // Test case 2: Partial name with long name match
       List<String> result2 = options.getMatchingOptions("opt");
       assertNotNull(result2);
       assertEquals(1, result2.size());
       assertTrue(result2.contains("option"));

       // Test case 3: Partial name with multiple matches
       List<String> result3 = options.getMatchingOptions("t");
       assertNotNull(result3);
       assertEquals(2, result3.size());
       assertTrue(result3.contains("test"));
       assertTrue(result3.contains("t"));

       // Test case 4: Partial name with no match
       List<String> result4 = options.getMatchingOptions("unknown");
       assertNotNull(result4);
       assertEquals(0, result4.size());
   }

   @Test
   public void testHasOption() {
       Option option1 = new Option("o", "option", false, "option");
       Option option2 = new Option("t", "test", true, "option with argument");
       Options options = new Options();
       options.addOption(option1);
       options.addOption(option2);

       // Test case 1: Option exists with short name
       assertTrue(options.hasOption("o"));

       // Test case 2: Option exists with long name
       assertTrue(options.hasOption("option"));

       // Test case 3: Option exists with short name and argument
       assertTrue(options.hasOption("t"));

       // Test case 4: Option exists with long name and argument
       assertTrue(options.hasOption("test"));

       // Test case 5: Option doesn't exist
       assertFalse(options.hasOption("unknown"));
   }

   @Test
   public void testHasLongOption() {
       Option option1 = new Option("o", "option", false, "option");
       Option option2 = new Option("t", "test", true, "option with argument");
       Options options = new Options();
       options.addOption(option1);
       options.addOption(option2);

       // Test case 1: Long option exists
       assertTrue(options.hasLongOption("option"));

       // Test case 2: Long option doesn't exist
       assertFalse(options.hasLongOption("unknown"));
   }

   @Test
   public void testHasShortOption() {
       Option option1 = new Option("o", "option", false, "option");
       Option option2 = new Option("t", "test", true, "option with argument");
       Options options = new Options();
       options.addOption(option1);
       options.addOption(option2);

       // Test case 1: Short option exists
       assertTrue(options.hasShortOption("o"));

       // Test case 2: Short option doesn't exist
       assertFalse(options.hasShortOption("unknown"));
   }

   @Test
   public void testGetOptionGroup() {
       OptionGroup group = new OptionGroup();
       Option option1 = new Option("o", "option", false, "option1");
       Option option2 = new Option("t", "test", false, "option2");
       group.addOption(option1).addOption(option2);

       Options options = new Options();
       options.addOptionGroup(group);

       // Test case 1: Option belongs to OptionGroup
       Option result1 = options.getOptionGroup(option1).getOptions().get(0);
       assertNotNull(result1);
       assertTrue(result1.equals(option1));

       // Test case 2: Option doesn't belong to OptionGroup
       Option result2 = options.getOptionGroup(new Option("unknown", "unknown", false, "unknown")).getOptions().get(0);
       assertNull(result2);
   }
}