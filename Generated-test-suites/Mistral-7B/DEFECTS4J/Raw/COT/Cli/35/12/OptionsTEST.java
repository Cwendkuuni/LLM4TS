import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptions() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 // Test adding an option with a short name
 options.addOption("a", "option-a", false, "Option A");

 // Test getting the option
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("option-a", optionA.getLongOpt());
 Assert.assertFalse(optionA.hasArg());

 // Test adding an option with a long name
 options.addOption("option-b", "long-option-b", true, "Long Option B");
 Option optionB = options.getOption("option-b");
 Assert.assertNotNull(optionB);
 Assert.assertEquals("long-option-b", optionB.getLongOpt());
 Assert.assertTrue(optionB.hasArg());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortOption() {
 Options options = createOptions();
 options.addOption("a", "option-a", false, "Option A");
 options.addOption("a", "option-a", false, "Option A (duplicate)");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongOption() {
 Options options = createOptions();
 options.addOption("option-a", "option-a", false, "Option A");
 options.addOption("option-a", "option-a", false, "Option A (duplicate)");
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("group", "Group");
 group.setRequired(true);
 options.addOptionGroup(group);

 // Test getting the OptionGroup
 OptionGroup optionGroup = options.getOptionGroup("group");
 Assert.assertEquals("group", optionGroup.getName());
 Assert.assertTrue(optionGroup.isRequired());

 // Test adding an option to the OptionGroup
 Option option = new Option("option", "option", false, "Option");
 option.setLongOpt("long-option");
 group.addOption(option);
 Option groupOption = options.getOption("group:option");
 Assert.assertEquals(option, groupOption);
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 Options options = createOptions();
 options.addOptionGroup(new OptionGroup("required-group", "Required Group", true, ""));
 options.parse(new String[]{"--not-required"});
 }

 @Test
 public void testParseWithMissingShortOption() {
 Options options = createOptions();
 options.addOption("a", "option-a", false, "Option A");
 CommandLine parsed = options.parse(new String[]{"--not-a"});
 Assert.assertFalse(parsed.hasOption("a"));
 }

 @Test
 public void testParseWithMissingLongOption() {
 Options options = createOptions();
 options.addOption("option-b", "long-option-b", true, "Long Option B");
 CommandLine parsed = options.parse(new String[]{"--not-long-option-b"});
 Assert.assertFalse(parsed.hasOption("option-b"));
 }

 @Test
 public void testParseWithValidOptions() throws ParseException {
 Options options = createOptions();
 options.addOption("a", "option-a", false, "Option A");
 options.addOption("option-b", "long-option-b", true, "Long Option B");
 CommandLine parsed = options.parse(new String[]{"-a", "--long-option-b=value"});
 Assert.assertTrue(parsed.hasOption("a"));
 Assert.assertTrue(parsed.hasOption("option-b"));
 Assert.assertNull(parsed.getOptionValue("a"));
 Assert.assertEquals("value", parsed.getOptionValue("option-b"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Options options = createOptions();
 options.addOption("a", "option-a", false, "Option A");
 CommandLine parsed = options.parse(new String[]{"--invalid"});
 }
}