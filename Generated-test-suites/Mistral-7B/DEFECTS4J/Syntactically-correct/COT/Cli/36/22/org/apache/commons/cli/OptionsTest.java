import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptions() {
 return new Options();
 }

 @Test
 public void testAddOptionShort() {
 Options options = createOptions();
 Option option = new Option("a", "alias", false, "Short option with alias");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testAddOptionShortWithArg() {
 Options options = createOptions();
 Option option = new Option("b", "boolean", true, "Short option with boolean value");
 options.addOption(option);
 assertTrue(options.hasOption("b"));
 }

 @Test
 public void testAddOptionLong() {
 Options options = createOptions();
 Option option = new Option("c", "custom", false, "Long option with custom name");
 options.addOption(option);
 assertTrue(options.hasOption("c"));
 }

 @Test
 public void testAddOptionLongWithArg() {
 Options options = createOptions();
 Option option = new Option("d", "data", true, "Long option with data value");
 options.addOption(option);
 assertTrue(options.hasOption("d"));
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("g", "Group");
 options.addOptionGroup(group);
 assertTrue(options.hasOptionGroup("g"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();
 Option opt1 = new Option("a", null, false, "Option 1");
 Option opt2 = new Option("b", null, false, "Option 2");
 Option opt3 = new Option("c", null, false, "Option 3");
 Option opt4 = new Option("d", null, false, "Option 4");
 Option opt5 = new Option("e", null, false, "Option 5");
 Option opt6 = new Option("a", null, false, "Option A");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 options.addOption(opt4);
 options.addOption(opt5);
 options.addOption(opt6);
 List<String> matchingOptions = options.getMatchingOptions("a");
 assertEquals(2, matchingOptions.size());
 assertEquals("a", matchingOptions.get(0));
 assertEquals("a", matchingOptions.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = createOptions();
 Option option = new Option("a", null, false, "Option A");
 options.addOption(option);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescription() {
 Options options = createOptions();
 Option option = new Option(null, null, false, null);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortOption() {
 Options options = createOptions();
 Option option1 = new Option("a", null, false, "Option A");
 Option option2 = new Option("a", null, false, "Option A");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongOption() {
 Options options = createOptions();
 Option option1 = new Option(null, "longOption", false, "Option A");
 Option option2 = new Option(null, "longOption", false, "Option A");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test
 public void testToString() {
 Options options = createOptions();
 options.addOption(new Option("a", null, false, "Option A"));
 options.addOption(new Option("b", null, false, "Option B"));
 options.addOption(new Option("c", null, false, "Option C"));
 assertEquals("[ Options: [ short {a=Option A, b=Option B, c=Option C} ] [ long {} ]]", options.toString());
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptions();
 options.addOptionGroup(new OptionGroup("g", "Group"));
 Option option1 = new Option("a", null, true, "Required option A");
 Option option2 = new Option("b", null, true, "Required option B");
 options.addOption(option1);
 options.addOption(option2);
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(2, requiredOptions.size());
 assertTrue(requiredOptions.contains(option1));
 assertTrue(requiredOptions.contains(option2));
 }

 @Test
 public void testGetOptions() {
 Options options = createOptions();
 options.addOption(new Option("a", null, false, "Option A"));
 options.addOption(new Option("b", null, false, "Option B"));
 List<Option> optionsList = options.getOptions();
 assertEquals(2, optionsList.size());
 assertTrue(optionsList.contains(options.getOption("a")));
 assertTrue(optionsList.contains(options.getOption("b")));
 }

 @Test
 public void testGetOption() {
 Options options = createOptions();
 Option option = new Option("a", null, false, "Option A");
 options.addOption(option);
 Option retrievedOption = options.getOption("a");
 assertEquals(option, retrievedOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionWithNullOptionName() {
 Options options = createOptions();
 options.getOption(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetOptionWithNonExistentOption() {
 Options options = createOptions();
 Option option = new Option("x", null, false, "Option X");
 options.addOption(option);
 options.getOption("y");
 }
}