import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Test
 public void testAddOption() {
 options = new Options();
 options.addOption("o", "output", false, "Output file path");
 Option option = options.getOption("o");
 assertEquals("o", option.getKey());
 assertEquals("output", option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testAddOptionWithArgument() {
 options = new Options();
 options.addOption("i", "input", true, "Input file path");
 Option option = options.getOption("i");
 assertEquals("i", option.getKey());
 assertEquals("input", option.getLongOpt());
 assertTrue(option.hasArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testAddRequiredOption() {
 options = new Options();
 options.addOption("r", "required", true, "Required option");
 Option option = options.getOption("r");
 assertEquals("r", option.getKey());
 assertEquals(null, option.getLongOpt());
 assertTrue(option.hasArg());
 assertTrue(option.isRequired());
 }

 @Test
 public void testAddOptionGroup() {
 options = new Options();
 OptionGroup group = new OptionGroup("g", "Group");
 group.setRequired(false);
 Option option = new Option("g", "groupOption", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 Option groupOption = options.getOption("g");
 assertNotNull(groupOption);
 assertEquals("g", groupOption.getKey());
 assertEquals("group", groupOption.getLongOpt());
 assertFalse(groupOption.hasArg());
 assertFalse(groupOption.isRequired());
 }

 @Test
 public void testAddRequiredOptionGroup() {
 options = new Options();
 OptionGroup group = new OptionGroup("g", "Group");
 group.setRequired(true);
 Option option = new Option("g", "groupOption", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 Option groupOption = options.getOption("g");
 assertNotNull(groupOption);
 assertEquals("g", groupOption.getKey());
 assertEquals(null, groupOption.getLongOpt());
 assertFalse(groupOption.hasArg());
 assertTrue(groupOption.isRequired());
 }

 @Test
 public void testGetOptions() {
 options = new Options();
 options.addOption("o", "output", false, "Output file path");
 options.addOption("i", "input", true, "Input file path");
 List<Option> optionsList = options.getOptions();
 assertEquals(2, optionsList.size());
 }

 @Test
 public void testGetRequiredOptions() {
 options = new Options();
 options.addOption("r", "required", true, "Required option");
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 }

 @Test
 public void testGetOptionGroups() {
 options = new Options();
 OptionGroup group = new OptionGroup("g", "Group");
 Option option = new Option("g", "groupOption", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 Collection<OptionGroup> optionGroups = options.getOptionGroups();
 assertEquals(1, optionGroups.size());
 }

 @Test
 public void testParse() {
 options = new Options();
 options.addOption("o", "output", false, "Output file path");
 Parser parser = new Parser();
 String[] args = {"-o", "output.txt"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 fail("ParseException was thrown!");
 }
 Option option = options.getOption("o");
 assertEquals("output.txt", option.getValue());
 }

 @Test
 public void testParseWithUnknownOption() {
 options = new Options();
 options.addOption("o", "output", false, "Output file path");
 Parser parser = new Parser();
 String[] args = {"-x", "output.txt"};
 try {
 parser.parse(options, args);
 fail("ParseException was not thrown!");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithRequiredOptionMissing() {
 options = new Options();
 options.addOption("r", "required", true, "Required option");
 Parser parser = new Parser();
 try {
 parser.parse(options, new String[0]);
 fail("ParseException was not thrown!");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithInvalidOptionArgument() {
 options = new Options();
 options.addOption("i", "input", true, "Input file path");
 Parser parser = new Parser();
 try {
 parser.parse(options, new String[]{"-i", "invalid"});
 fail("ParseException was not thrown!");
 } catch (ParseException e) {
 assertEquals("Invalid value for option: -i:invalid", e.getMessage());
 }
 }

 @Test
 public void testParseWithMixedShortAndLongOptions() {
 options = new Options();
 options.addOption("o", "output", false, "Output file path");
 options.addOption("h", "help", false, "Print this help");
 options.addOption("i", "input", true, "Input file path");
 Parser parser = new Parser();
 String[] args = {"-o", "output.txt", "-h", "--help"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 fail("ParseException was thrown!");
 }
 Option option = options.getOption("o");
 assertEquals("output.txt", option.getValue());
 assertTrue(options.getOptions().get(1).isParsed());
 assertTrue(options.getOptions().get(2).isParsed());
 }

 @Test
 public void testParseWithOptionGroup() {
 options = new Options();
 OptionGroup group = new OptionGroup("g", "Group");
 Option option = new Option("g", "groupOption", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 options.addOption("o", "output", false, "Output file path");
 Parser parser = new Parser();
 String[] args = {"--group", "--groupOption"};
 try {
 parser.parse(options, args);
 } catch (ParseException e) {
 fail("ParseException was thrown!");
 }
 Option groupOption = options.getOption("g");
 Option groupOptionValue = groupOption.getValue();
 assertEquals(groupOptionValue, "groupOption");
 }
}