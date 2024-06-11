import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);

 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescription() {
 Options options = createOptionsUnderTest();
 Option option = new Option('o', null, true, null);
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithLongOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("outputFile", "of", true, "Output file path");
 options.addOption(option);

 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithShortAndLongOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "outputFile", true, "Output file path");
 options.addOption(option);

 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("group1", "Group 1");
 options.addOptionGroup(group);

 Assert.assertEquals(0, options.getOptions().size());
 Assert.assertEquals(1, options.getOptionGroups().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithNullDescription() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup(null, "Group 1");
 options.addOptionGroup(group);
 }

 @Test
 public void testAddMultipleOptions() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("o", "output1", true, "Output file path 1");
 Option option2 = new Option("o", "output2", true, "Output file path 2");
 options.addOption(option1);
 options.addOption(option2);

 Assert.assertEquals(2, options.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateShortOption() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("o", "output", true, "Output file path 1");
 Option option2 = new Option("o", "output", true, "Output file path 2");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test
 public void testAddDuplicateLongOption() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("output1", "o1", true, "Output file path 1");
 Option option2 = new Option("output2", "o1", true, "Output file path 2");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullOptionName() {
 Options options = createOptionsUnderTest();
 Option option = new Option(null, "output", true, "Output file path");
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullKey() {
 Options options = createOptionsUnderTest();
 Option option = new Option("output", null, true, "Output file path");
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "output", true, "Output file path {0}");
 options.addOption(option);
 options.parse("--output,test.txt");

 Assert.assertEquals("test.txt", options.getValue("output"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidValue() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "output", true, "Output file path {0}");
 options.addOption(option);options.parse("--output,123");
 }

 @Test
 public void testParseWithDefaultValues() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "output", true, "Output file path {0}");
 options.addOption(option);
 options.parse("--output");

 Assert.assertNull(options.getValue("output"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() {
 Options options = createOptionsUnderTest();Option option = new Option("o", "output", true, "Output file path {0}");options.addOption(option);options.parse("--invalid-option");
 }

 @Test
 public void testParseWithMultipleOptions() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "output", true, "Output file path {0}");
 options.addOption(option);
 options.parse("--output,file1.txt --output,file2.txt");

 Assert.assertEquals("file1.txt", options.getValue("output"));
 }

 @Test
 public void testParseWithLongOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("longOption", "longOpt", false, "This is a long option");
 options.addOption(option);
 options.parse("--longOpt");

 Assert.assertTrue(options.isSet("longOpt"));
 }

 @Test
 public void testParseWithRequiredOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("r", "required", true, "Required option", true);
 options.addOption(option);
 options.parse("--required");

 Assert.assertTrue(options.isSet("required"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingRequiredOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("r", "required", true, "Required option", true);
 options.addOption(option);
 options.parse("--");
 }

 @Test
 public void testGetOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "output", true, "Output file path {0}");
 options.addOption(option);
 options.parse("--output,test.txt");

 Option obtainedOption = options.getOption("output");

 Assert.assertEquals(option, obtainedOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionWithUnknownOption() {
 Options options = createOptionsUnderTest();
 options.parse("--unknown-option");
 options.getOption("unknown-option");
 }

 @Test
 public void testGetOptions() {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("o1", "option1", true, "Option 1");
 Option option2 = new Option("o2", "option2", true, "Option 2");
 options.addOption(option1);
 options.addOption(option2);
 options.parse("--option1,test1 --option2,test2");

 List<Option> optionsList = options.getOptions();

 Assert.assertEquals(2, optionsList.size());
 Assert.assertEquals(option1, optionsList.get(0));
 Assert.assertEquals(option2, optionsList.get(1));
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptionsUnderTest();
 Option option = new Option("r", "required", true, "Required option", true);
 options.addOption(option);
 options.parse("--required");

 List<Option> requiredOptions = options.getRequiredOptions();

 Assert.assertEquals(1, requiredOptions.size());
 Assert.assertEquals(option, requiredOptions.get(0));
 }
}