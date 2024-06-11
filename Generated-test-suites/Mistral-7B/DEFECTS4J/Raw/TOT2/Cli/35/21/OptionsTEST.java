import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 Assert.assertEquals("Output file path", options.getOptions().get(0).getLongOpt());
 }

 @Test
 public void testAddOptionWithDescription() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path. e.g. /path/to/output.txt");
 options.addOption(option);
 Assert.assertEquals("Output file path. e.g. /path/to/output.txt", options.getOptions().get(0).getDescription());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "Group");
 Option option = new Option("g", "groupOption", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 Assert.assertEquals(1, options.getOptionGroups().size());
 }

 @Test
 public void testAddRequiredOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 option.setRequired(true);
 options.addOption(option);
 try {
 options.parse("--");
 Assert.fail();
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testAddRequiredOptionValue() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 option.setRequired(true);
 options.addOption(option);
 try {
 options.parse("-o");
 Assert.fail();
 } catch (ParseException e) {
 // expected
 Assert.assertEquals("No value provided for option: output", e.getMessage());
 }
 }

 @Test
 public void testAddOptionalOptionValue() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 Options parsedOptions = options.parse("--output=test.txt");
 Assert.assertEquals("test.txt", parsedOptions.getOptionValue("output"));
 }

 @Test(expected = ParseException.class)
 public void testAddInvalidOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 option.setLongOpt("invalid");
 options.addOption(option);
 try {
 options.parse("--invalid");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 Option parsedOption = options.getOption("output");
 Assert.assertEquals(option, parsedOption);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("o", "output1", true, "Output file path1");
 Option option2 = new Option("o", "output2", true, "Output file path2");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("o");
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertTrue(matchingOptions.contains("output1"));
 Assert.assertTrue(matchingOptions.contains("output2"));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("output"));
 Assert.assertTrue(options.hasOption("o"));
 Assert.assertFalse(options.hasOption("invalid"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 Assert.assertTrue(options.hasLongOption("output"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 Assert.assertTrue(options.hasShortOption("o"));
 }
}