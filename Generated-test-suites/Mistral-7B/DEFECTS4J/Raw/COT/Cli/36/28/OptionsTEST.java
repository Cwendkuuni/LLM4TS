import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option option = new Option("a", "alias", false, "Option A");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("a"));
 Assert.assertFalse(options.hasOption("b"));
 }

 @Test
 public void testAddOptionWithArgument() {
 Option option = new Option("b", "bool-val", true, "Option B with boolean value");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("b"));
 }

 @Test
 public void testAddOptionWithLongName() {
 Option option = new Option("l", "long-alias", false, "Option A with long alias");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("l"));
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group1", "My group", true, "This is my group");
 Option option = new Option("g", null, false, "Option in group", group);
 options.addOptionGroup(group);
 options.addOption(option);
 Assert.assertTrue(options.hasOption("g"));
 Assert.assertEquals(group, options.getOptionGroup(option));
 }

 @Test
 public void testGetOptions() {
 List<Option> optionsList = options.getOptions();
 Assert.assertFalse(optionsList.isEmpty());
 }

 @Test
 public void testGetRequiredOptions() {
 List<Option> requiredOptions = options.getRequiredOptions();
 Assert.assertEquals(0, requiredOptions.size());
 }

 @Test
 public void testGetMatchingOptions() {
 List<String> options = this.options.getMatchingOptions("o");
 Assert.assertEquals(0, options.size());
 }

 @Test(expected = ParseException.class)
 public void testParseError() {
 try {
 options.parse("");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseErrorWithInvalidOption() {
 try {
 options.parse(new String[]{"-x"});
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseErrorWithTwoInvalidOptions() {
 try {
 options.parse(new String[]{"-x", "-y"});
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseSuccess() throws ParseException {
 String[] args = {"-a", "valueA", "--long-alias", "valueB"};
 options.parse(args);
 Option optionA = options.getOption("a");
 Assert.assertFalse(optionA.hasArg());
 Assert.assertEquals("valueA", optionA.getValue());
 Option optionB = options.getOption("l");
 Assert.assertTrue(optionB.hasArg());
 Assert.assertEquals("valueB", optionB.getValue());
 }

 @Test
 public void testParseSuccessWithOptionGroup() throws ParseException {
 OptionGroup group = new OptionGroup("group1", "My group", true, "This is my group");
 Option option = new Option("g", null, false, "Option in group", group);
 options.addOptionGroup(group);
 options.addOption(option);
 String[] args = {"--group1", "-g"};
 options.parse(args);
 Option groupOption = options.getOption("g");
 Assert.assertEquals(group, options.getOptionGroup(groupOption));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortOption() {
 Option option1 = new Option("a", "alias", false, "Option A");
 options.addOption(option1);
 Option option2 = new Option("a", "alias2", false, "Option A2");
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongOption() {
 Option option1 = new Option("l", "long-alias", false, "Option A with long alias");
 options.addOption(option1);
 Option option2 = new Option("l", "long-alias2", false, "Option A2 with long alias");
 options.addOption(option2);
 }
}