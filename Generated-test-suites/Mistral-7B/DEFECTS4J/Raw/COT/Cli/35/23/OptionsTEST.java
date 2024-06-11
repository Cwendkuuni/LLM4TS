import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Test
 public void testAddOption() {
 options = new Options();
 options.addOption("a", "option-a", false, "Description for option a");
 Option optionA = options.getOption("a");
 Assert.assertEquals("option-a", optionA.getLongOpt());
 Assert.assertFalse(optionA.hasArg());
 }

 @Test
 public void testAddOptionWithArgument() {
 options = new Options();
 options.addOption("b", true, "Description for option b");
 Option optionB = options.getOption("b");
 Assert.assertEquals("b", optionB.getLongOpt());
 Assert.assertTrue(optionB.hasArg());
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 options = new Options();
 options.addOption("c", "option-c", false, "Description for option c");
 options.addOption("d", "option-d", false, "Description for option d");
 Option optionC = options.getOption("c");
 Option optionD = options.getOption("d");
 Assert.assertEquals("c", optionC.getKey());
 Assert.assertEquals("option-c", optionC.getLongOpt());
 Assert.assertFalse(optionC.hasArg());
 Assert.assertEquals("d", optionD.getKey());
 Assert.assertEquals("option-d", optionD.getLongOpt());
 Assert.assertFalse(optionD.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 options = new Options();
 OptionGroup group = new OptionGroup("group", "Option group");
 options.addOptionGroup(group);
 group.setRequired(true);
 Option optionE = new Option("e", "option-e", true, "Description for option e");
 group.addOption(optionE);
 Option optionF = new Option("f", "option-f", false, "Description for option f");
 group.addOption(optionF);
 options.parse("");
 List<Option> parsedOptions = options.getOptions();
 Assert.assertEquals(2, parsedOptions.size());
 Assert.assertTrue(parsedOptions.get(0) instanceof OptionGroup);
 OptionGroup parsedGroup = (OptionGroup) parsedOptions.get(0);
 Option parsedOptionE = parsedGroup.findOption("e");
 Option parsedOptionF = parsedGroup.findOption("f");
 Assert.assertEquals(optionE, parsedOptionE);
 Assert.assertEquals(optionF, parsedOptionF);
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 options = new Options();
 options.addOption("g", "option-g", false, "Description for option g");
 options.addOption("g", "option-g", false, "Description for option g");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupTwice() {
 options = new Options();
 OptionGroup group = new OptionGroup("group", "Option group");
 options.addOptionGroup(group);
 try {
 options.addOptionGroup(group);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() {
 options = new Options();
 Option optionH = new Option("h", "option-h", true, "Description for option h");
 options.addOption(optionH);
 options.parse("");
 }

 @Test
 public void testParseWithShortAndLongName() {
 options = new Options();
 options.addOption("i", "option-i", false, "Description for option i");
 options.addOption("j", "option-j", false, "Description for option j");
 options.parse(new String[]{"-i", "-j"});
 List<Option> parsedOptions = options.getOptions();
 Assert.assertEquals(2, parsedOptions.size());
 }

 @Test
 public void testParseWithLongName() {
 options = new Options();
 options.addOption("k", "option-k", false, "Description for option k");
 options.addOption("l", "option-l", false, "Description for option l");
 options.parse(new String[]{"--option-k"});
 List<Option> parsedOptions = options.getOptions();
 Assert.assertEquals(1, parsedOptions.size());
 Option parsedOptionK = parsedOptions.get(0);
 Assert.assertEquals("option-k", parsedOptionK.getLongOpt());
 }

 @Test
 public void testParseWithShortAndLongNameAndArgument() {
 options = new Options();
 options.addOption("m", "option-m", true, "Description for option m");
 options.parse(new String[]{"-m", "value"});
 List<Option> parsedOptions = options.getOptions();
 Assert.assertEquals(1, parsedOptions.size());
 Option parsedOptionM = parsedOptions.get(0);
 Assert.assertEquals("option-m", parsedOptionM.getLongOpt());
 Assert.assertEquals("value", parsedOptionM.getValue());
 }

 @Test
 public void testParseWithValueGivenForOptionWithoutArgument() {
 options = new Options();
 options.addOption("n", "option-n", false, "Description for option n");
 try {
 options.parse(new String[]{"-n", "value"});
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() {
 options = new Options();
 options.parse(new String[]{"-o"});
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() {
 options = new Options();
 options.parse(new String[]{"--invalid-option"});
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOptionWithArgument() {
 options = new Options();
 Option invalidOption = new Option("p", "invalid-option", true, "Description for invalid option");
 options.addOption(invalidOption);
 options.parse(new String[]{"--invalid-option", "value"});
 }
}