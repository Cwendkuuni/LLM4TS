import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testAddOptionWithNoArgument() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);

 assertTrue(options.hasOption("a"));
 assertFalse(options.hasOption("b"));
 assertNull(options.getOption("b"));
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "Description for option a");
 options.addOption(option);

 assertTrue(options.hasOption("a"));
 assertNull(options.getOption("b"));
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("alias"));
 assertNull(options.getOption("b"));
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("groupName", "Description for group");
 Option option1 = new Option("g1", "group1", false, "Description for group1");
 Option option2 = new Option("g2", "group2", false, "Description for group2");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 options.addOptionGroup(optionGroup);

 assertTrue(options.hasOptionGroup("groupName"));
 assertTrue(options.hasOption("g1"));
 assertTrue(options.hasOption("g2"));
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Description for option a");
 options.addOption(option);
 options.addOption(option);
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 Option option1 = new Option("r1", "required1", true, "Description for required option 1");
 Option option2 = new Option("r2", "required2", true, "Description for required option 2");
 options.addOptionGroup(new OptionGroup("group", "group name")).addOption(option1).addOption(option2);

 List<Option> requiredOptions = options.getRequiredOptions();
 assertTrue(requiredOptions.contains(option1));
 assertTrue(requiredOptions.contains(option2));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("long", "longOption1", false, "Description for long option 1");
 Option option2 = new Option("short", "shortOption2", false, "Description for short option 2");
 Option option3 = new Option("longOption", "longOption", false, "Description for long option");
 options.addOption(option1).addOption(option2).addOption(option3);

 List<String> matchingOptions = options.getMatchingOptions("long");
 assertTrue(matchingOptions.contains("longOption"));
 assertTrue(matchingOptions.contains("longOption1"));
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option1 = new Option("a", "alias1", false, "Description for option a1");
 Option option2 = new Option("a", "alias2", false, "Description for option a2");
 options.addOption(option1).addOption(option2);

 Option option = options.getOption("a");
 assertTrue(option == option1 || option == option2);
 }
}