import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "option", false, "Description of option");
 options.addOption(option);
 Option parsedOption = options.getOption("o");
 assertNotNull(parsedOption);
 assertFalse(parsedOption.hasArg());
 assertEquals("Description of option", parsedOption.getDescription());
 }

 @Test
 public void testAddOptionWithArg() {
 Option option = new Option("o", "option", true, "Description of option");
 options.addOption(option);
 Option parsedOption = options.getOption("o");
 assertNotNull(parsedOption);
 assertTrue(parsedOption.hasArg());
 assertEquals("Description of option", parsedOption.getDescription());
 }

 @Test
 public void testAddOptionWithLongName() {
 Option option = new Option("optionLong", "longOption", false, "Description of long option");
 options.addOption(option);
 Option parsedOption = options.getOption("optionLong");
 assertNotNull(parsedOption);
 assertFalse(parsedOption.hasArg());
 assertEquals("Description of long option", parsedOption.getDescription());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("group", "Group description");
 Option option = new Option("o", "option", false, "Description of option");
 optionGroup.addOption(option);
 options.addOptionGroup(optionGroup);
 Option parsedOption = options.getOption("o");
 assertNotNull(parsedOption);
 assertFalse(parsedOption.hasArg());
 assertEquals("Description of option", parsedOption.getDescription());
 OptionGroup parsedOptionGroup = options.getOptionGroup("group");
 assertNotNull(parsedOptionGroup);
 assertEquals("Group description", parsedOptionGroup.getDescription());
 }

 @Test
 public void testHasOption() {
 Option option = new Option("o", "option", false, "Description of option");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testHasLongOption() {
 Option option = new Option("o", "option", false, "Description of option");
 options.addOption(option);
 assertTrue(options.hasLongOption("option"));
 }

 @Test
 public void testHasShortOption() {
 Option option = new Option("o", "option", false, "Description of option");
 options.addOption(option);
 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("o1", "option1", false, "Description of option1");
 Option option2 = new Option("o2", "option2", false, "Description of option2");
 Option option3 = new Option("o3", "option3", false, "Description of option3");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> optionsList = options.getMatchingOptions("o");
 assertEquals(3, optionsList.size());
 List<String> optionsList2 = options.getMatchingOptions("o1");
 assertEquals(1, optionsList2.size());
 }

 @Test
 public void testParse() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"-o", "value", "-l", "valueLong"});
 Option optionO = options.getOption("o");
 Option optionL = options.getOption("l");
 assertNotNull(cmd.getOptionValues("o"));
 assertNotNull(cmd.getOptionValues("l"));
 assertEquals(1, cmd.getOptionValues("o").length);
 assertEquals("value", cmd.getOptionValues("o")[0]);
 assertEquals(1, cmd.getOptionValues("l").length);
 assertEquals("valueLong", cmd.getOptionValues("l")[0]);
 assertNotNull(optionO);
 assertFalse(optionO.hasArg());
 assertNotNull(optionL);
 assertFalse(optionL.hasArg());
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"-i", "value"});
 assertNull(cmd.getOptionValues("i"));
 }
}