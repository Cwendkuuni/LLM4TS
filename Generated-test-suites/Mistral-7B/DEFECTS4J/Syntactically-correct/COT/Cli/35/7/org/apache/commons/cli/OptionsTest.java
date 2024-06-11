import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 return new Options();
 }

 @Test
 public void testAddOptionShort() {
 Options options = createOptionsUnderTest();
 options.addOption('a', "optionA", false, "Option A description");
 assertEquals("Option A description", options.getOption("optionA").getDescription());
 }

 @Test
 public void testAddOptionShortWithArg() {
 Options options = createOptionsUnderTest();
 options.addOption('a', "optionA", true, "Option A description");
 assertTrue(options.getOption('a').hasArg());
 }

 @Test
 public void testAddOptionLong() {
 Options options = createOptionsUnderTest();
 options.addOption('a', "optionALong", false, "Option A description");
 options.addOption("optionB", "optionB", false, "Option B description");
 assertEquals("Option A description", options.getOption("optionALong").getDescription());
 assertEquals("Option B description", options.getOption("optionB").getDescription());
 }

 @Test
 public void testAddOptionLongWithArg() {
 Options options = createOptionsUnderTest();
 options.addOption('a', "optionALong", false, "Option A description");
 options.addOption("optionB", "optionB", true, "Option B description");
 assertFalse(options.getOption("optionB").hasArg());
 options.parse("--optionB value");
 assertEquals("value", options.getOptionValue("optionB"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOptionShort() {
 Options options = createOptionsUnderTest();
 options.addOption('a', "optionA", false, "Option A description");
 options.addOption('a', "optionB", false, "Option B description");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOptionLong() {
 Options options = createOptionsUnderTest();
 options.addOption("optionA", "optionA", false, "Option A description");
 options.addOption("optionA", "optionB", false, "Option B description");
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("testGroup", "Test group");
 options.addOptionGroup(group);
 assertTrue(options.hasOptionGroup("testGroup"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupDuplicate() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("testGroup", "Test group");
 options.addOptionGroup(group);
 options.addOptionGroup(group);
 }

 @Test
 public void testAddRequiredOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("option", "option", true, "Option description");
 options.addOption(option);
 List<Option> optionsList = options.getOptions();
 Option requiredOption = optionsList.get(0);
 assertTrue(requiredOption.isRequired());
 }

 @Test
 public void testAddRequiredOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("testGroup", "Test group");
 options.addOptionGroup(group);
 List<OptionGroup> optionGroups = options.getOptionGroups();
 OptionGroup requiredGroup = optionGroups.get(0);
 assertTrue(requiredGroup.isRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddRequiredOptionGroupTwice() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("testGroup", "Test group");
 options.addOptionGroup(group);
 options.addOptionGroup(group);
 }

 @Test
 public void testParseWithShortOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("option", "option", false, "Option description");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"-option"});
 assertTrue(cmd.hasOption("option"));
 }

 @Test
 public void testParseWithShortOptionArg() {
 Options options = createOptionsUnderTest();
 Option option = new Option("option", "option", true, "Option description");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"-option", "arg"});
 assertEquals("arg", cmd.getOptionValue("option"));
 }

 @Test
 public void testParseWithLongOption() {
 Options options = createOptionsUnderTest();
 Option option = new Option("option", "option", false, "Option description");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"--option"});
 assertTrue(cmd.hasOption("option"));
 }

 @Test
 public void testParseWithLongOptionArg() {
 Options options = createOptionsUnderTest();
 Option option = new Option("option", "option", true, "Option description");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"--option", "arg"});
 assertEquals("arg", cmd.getOptionValue("option"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnknownShortOption() {
 Options options = createOptionsUnderTest();
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"-unknown"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnknownLongOption() {
 Options options = createOptionsUnderTest();
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"--unknown"});
 }
}