import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @After
 public void tearDown() {
 options = null;
 }

 /**
 * Test method for {@link Options#addOptionGroup(OptionGroup)}.
 */
 @Test
 public void testAddOptionGroup() {
 // create an Option group
 OptionGroup optionGroup = new OptionGroup("testGroup", "Test Option Group");

 // add the option group to the Options
 options.addOptionGroup(optionGroup);

 // check if the OptionGroup was added
 assertTrue(options.getOptionGroups().contains(optionGroup));
 }

 /**
 * Test method for {@link Options#getOptionGroups()}.
 */
 @Test
 public void testGetOptionGroups() {
 // add some OptionGroups
 options.addOptionGroup(new OptionGroup("testGroup1", "Test Option Group 1"));
 options.addOptionGroup(new OptionGroup("testGroup2", "Test Option Group 2"));

 // check the size and contents of the returned list
 assertEquals(2, options.getOptionGroups().size());
 }

 /**
 * Test method for {@link Options#addOption(String, String, boolean, String)}.
 */
 @Test
 public void testAddOption() {
 // add an option with short name only
 options.addOption("a", "option A");
 Option optionA = options.getOption("a");
 assertEquals("a", optionA.getKey());
 assertEquals("option A", optionA.getLongOpt());
 assertFalse(optionA.hasArg());

 // add an option with long name only
 options.addOption("--optionB", "option B");
 Option optionB = options.getOption("--optionB");
 assertEquals("optionB", optionB.getKey());
 assertEquals("optionB", optionB.getLongOpt());
 assertFalse(optionB.hasArg());

 // add an option with short and long names
 options.addOption("c", "option C", true, "option C description");
 Option optionC = options.getOption("c");
 assertEquals("c", optionC.getKey());
 assertEquals("optionC", optionC.getLongOpt());
 assertTrue(optionC.hasArg());
 }

 /**
 * Test method for {@link Options#getOptions()}.
 */
 @Test
 public void testGetOptions() {
 // add some options
 options.addOption("a", "option A");
 options.addOption("--optionB", "option B");
 options.addOption("c", "option C", true, "option C description");

 // check the size and contents of the returned list
 assertEquals(3, options.getOptions().size());
 }

 /**
 * Test method for {@link Options#getRequiredOptions()}.
 */
 @Test
 public void testGetRequiredOptions() {
 // add some required options
 options.addOption("a", true, "option A");
 options.addOption("--optionB", true, "option B");

 // check the size and contents of the returned list
 assertEquals(2, options.getRequiredOptions().size());
 }

 /**
 * Test method for {@link Options#getOption(String)}.
 */
 @Test
 public void testGetOption() {
 // add some options
 options.addOption("a", "option A");
 options.addOption("--optionB", "option B");
 options.addOption("c", "option C", true, "option C description");

 // test getting an existing option
 Option optionA = options.getOption("a");
 assertEquals("a", optionA.getKey());
 assertEquals("option A", optionA.getLongOpt());
 assertFalse(optionA.hasArg());

 // test getting a non-existent option returns null
 assertNull(options.getOption("z"));
 }

 /**
 * Test method for {@link Options#getMatchingOptions(String)}.
 */
 @Test
 public void testGetMatchingOptions() {
 // add some options
 options.addOption("--optionA", "option A");
 options.addOption("--optionB", "option B");
 options.addOption("--optionC", "option C");

 // test getting all matching options
 List<String> optionsList = options.getMatchingOptions("option");
 assertEquals(2, optionsList.size());
 assertEquals("--optionB", optionsList.get(0));
 assertEquals("--optionC", optionsList.get(1));

 // test getting no matching options
 optionsList = options.getMatchingOptions("z");
 assertTrue(optionsList.isEmpty());
 }

 /**
 * Test method for {@link Options#hasOption(String)}.
 */
 @Test
 public void testHasOption() {
 // add some options
 options.addOption("a", "option A");
 options.addOption("--optionB", "option B");
 options.addOption("c", "option C", true, "option C description");

 // test getting an existing option
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("--optionB"));
 assertTrue(options.hasOption("c"));

 // test getting a non-existent option returns false
 assertFalse(options.hasOption("z"));
 }

 /**
 * Test method for {@link Options#hasLongOption(String)}.
 */
 @Test
 public void testHasLongOption() {
 // add some options
 options.addOption("a", "option A");
 options.addOption("--optionB", "option B");
 options.addOption("c", "option C", true, "option C description");

 // test getting an existing long option
 assertTrue(options.hasLongOption("--optionB"));
 assertFalse(options.hasLongOption("--nonExistentOption"));
 }

 /**
 * Test method for {@link Options#hasShortOption(String)}.
 */
 @Test
 public void testHasShortOption() {
 // add some options
 options.addOption("a", "option A");
 options.addOption("--optionB", "option B");
 options.addOption("c", "option C", true, "option C description");

 // test getting an existing short option
 assertTrue(options.hasShortOption("a"));
 assertFalse(options.hasShortOption("z"));
 }

 /**
 * Test method for {@link Options#getOptionGroup(Option)}.
 */
 @Test
 public void testGetOptionGroup() {
 // create an OptionGroup and an Option
 OptionGroup optionGroup = new OptionGroup("testGroup", "Test Option Group");
 Option option = new Option("test", false, false, "Test option");
 option.setLongOpt("--test");
 optionGroup.addOption(option);

 // set the OptionGroup for the Option
 option.setOptionGroup(optionGroup);

 // test getting the OptionGroup for the Option
 OptionGroup optionGroupReturned = options.getOptionGroup(option);
 assertEquals(optionGroup, optionGroupReturned);
 }

 /**
 * Test method for {@link Options#toString()}.
 */
 @Test
 public void testToString() {
 // add some options
 options.addOption("a", "option A");
 options.addOption("--optionB", "option B");
 options.addOption("c", "option C", true, "option C description");

 // test toString method
 String expected = "[ Options: [ short {a= option A} ] [ long {--optionB=optionB} ] ]";
 assertEquals(expected, options.toString());
 }
}