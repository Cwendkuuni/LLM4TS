import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 // Basic test cases for the addOption method
 @Test
 public void testAddOptionWithShortNameAndNoArgument() {
 Options options = new Options();
 options.addOption("a", "option-a", false, "Option A description");
 Assert.assertTrue(options.hasOption("a"));
 }

 @Test
 public void testAddOptionWithShortNameAndArgument() {
 Options options = new Options();
 options.addOption("b", "option-b", true, "Option B description");
 Assert.assertTrue(options.hasOption("b"));
 }

 @Test
 public void testAddOptionWithLongNameAndNoArgument() {
 Options options = new Options();
 options.addOption("c", "option-c", false, "Option C description");
 options.addOption("--option-c", "option-c-long", false, "Option C description");
 Assert.assertTrue(options.hasOption("--option-c"));
 }

 @Test
 public void testAddOptionWithLongNameAndArgument() {
 Options options = new Options();
 options.addOption("d", "option-d", false, "Option D description");
 options.addOption("--option-d", "option-d-long", true, "Option D description");
 Assert.assertTrue(options.hasOption("--option-d"));
 }

 // Test for addOptionGroup method
 @Test
 public void testAddOptionGroupWithSingleOption() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Group 1", true, "Description of group1");
 Option option = new Option("g", "option-g", true, "Option G description");
 group.addOption(option);
 options.addOptionGroup(group);
 Assert.assertTrue(options.hasOption("g"));
 }

 @Test
 public void testAddOptionGroupWithMultipleOptions() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group2", "Group 2", true, "Description of group2");
 Option option1 = new Option("h", "option-h1", true, "Option H1 description");
 Option option2 = new Option("h", "option-h2", true, "Option H2 description");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 Assert.assertTrue(options.hasOption("h"));
 }

 // Test for getOption method
 @Test
 public void testGetOptionWithShortName() {
 Options options = new Options();
 Option option = new Option("i", "option-i", true, "Option I description");
 options.addOption(option);
 Assert.assertEquals(option, options.getOption("i"));
 }

 @Test
 public void testGetOptionWithLongName() {
 Options options = new Options();
 Option option = new Option("--option-j", "option-j", true, "Option J description");
 options.addOption(option);
 Assert.assertEquals(option, options.getOption("--option-j"));
 }

 // Test for getMatchingOptions method
 @Test
 public void testGetMatchingOptionsWithPartialName() {
 Options options = new Options();
 Option option1 = new Option("--option-k", "option-k1", true, "Option K1 description");
 Option option2 = new Option("--option-k", "option-k2", true, "Option K2 description");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("--option-k");
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertEquals("--option-k1", matchingOptions.get(0));
 Assert.assertEquals("--option-k2", matchingOptions.get(1));
 }

 // Test for hasOption method
 @Test
 public void testHasOptionWithShortName() {
 Options options = new Options();
 Option option = new Option("e", "option-e", true, "Option E description");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("e"));
 }

 @Test
 public void testHasOptionWithLongName() {
 Options options = new Options();
 Option option = new Option("--option-f", "option-f", true, "Option F description");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("--option-f"));
 }

 @Test
 public void testHasOptionWithInvalidName() {
 Options options = new Options();
 Assert.assertFalse(options.hasOption("z"));
 }

 // Test for getRequiredOptions method
 @Test
 public void testGetRequiredOptionsWithSingleOption() {
 Options options = new Options();
 Option option = new Option("r", "option-r", true, "Option R description");
 options.addOption(option);
 List<Option> requiredOptions = options.getRequiredOptions();
 Assert.assertEquals(1, requiredOptions.size());
 Assert.assertEquals(option, requiredOptions.get(0));
 }

 @Test
 public void testGetRequiredOptionsWithMultipleOptions() {
 Options options = new Options();
 Option option1 = new Option("s", "option-s1", true, "Option S1 description");
 Option option2 = new Option("s", "option-s2", true, "Option S2 description");
 options.addOption(option1);
 options.addOption(option2);
 options.addOptionGroup(new OptionGroup("group3", "Group 3", false, "Description of group3"){
 {addOption(option1); addOption(option2);}
 });
 List<Option> requiredOptions = options.getRequiredOptions();
 Assert.assertEquals(2, requiredOptions.size());
 Assert.assertEquals(option1, requiredOptions.get(0));
 Assert.assertEquals(option2, requiredOptions.get(1));
 }

 // Test for parsing options with no arguments
 @Test
 public void testParseWithNoArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 CommandLine cmd = options.parse(new String[]{"--a", "--b"});
 Assert.assertNull(cmd.getArgs());
 }

 // Test for parsing options with required arguments
 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredArguments() throws ParseException {
 Options options = new Options();
 options.addOption("c", true, "Option C with argument");
 CommandLine cmd = options.parse(new String[]{"--c"});
 }

 // Test for parsing options with optional arguments
 @Test
 public void testParseWithOptionalArguments() throws ParseException {
 Options options = new Options();
 options.addOption("d", true, "Option D with argument");
 CommandLine cmd = options.parse(new String[]{"--d", "value"});
 Assert.assertEquals(new String[]{"value"}, cmd.getArgs());
 }

 // Test for parsing options with long and short names
 @Test
 public void testParseWithLongAndShortNames() throws ParseException {
 Options options = new Options();
 options.addOption("e", true, "Option E with argument");
 options.addOption("--e", true, "Option E with long name");
 CommandLine cmd = options.parse(new String[]{"--e", "value"});
 Assert.assertEquals(new String[]{"value"}, cmd.getArgs());
 }
}