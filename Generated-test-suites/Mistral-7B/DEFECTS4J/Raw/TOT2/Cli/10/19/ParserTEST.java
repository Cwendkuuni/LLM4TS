import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setup() {
 parser = new DefaultParser();
 options = new Options();
 }

 /**
 * Test parsing a simple command line with no required options or properties
 */
 @Test
 public void testSimpleParse() throws ParseException {
 options.addOption("a", "optionA", false, "Option A");

 String[] args = new String[]{"test", "--optionA"};
 CommandLine cmd = parser.parse(options, args, null);

 Assert.assertEquals("test", cmd.getArgs().get(0));
 Assert.assertTrue(cmd.hasOption("optionA"));
 }

 /**
 * Test parsing a command line with required options
 */
 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOptions() throws ParseException {
 options.addOption("a", "optionA", true, "Option A");

 String[] args = new String[]{"test"};
 CommandLine cmd = parser.parse(options, args, null);
 }

 /**
 * Test parsing a command line with multiple values for an option
 */
 @Test
 public void testOptionWithMultipleValues() throws ParseException {
 options.addOption("a", "optionA", true, "Option A");
 options.addOption("b", "optionB", true, "Option B");

 String[] args = new String[]{"test", "--optionA", "value1", "--optionA", "value2", "--optionB", "value3"};
 CommandLine cmd = parser.parse(options, args, null);

 List<String> optionAValues = cmd.getOptionValues("optionA");
 List<String> optionBValues = cmd.getOptionValues("optionB");

 Assert.assertEquals(Arrays.asList("value1", "value2"), optionAValues);
 Assert.assertEquals(Arrays.asList("value3"), optionBValues);
 }

 /**
 * Test parsing a command line with options in a group
 */
 @Test
 public void testOptionInGroup() throws ParseException {
 OptionGroup group = new OptionGroup("groupName", "Group Name");
 group.addOption("a", "optionA", false, "Option A in Group");
 group.addOption("b", "optionB", false, "Option B in Group");
 options.addOptionGroup(group);

 String[] args = new String[]{"test", "--groupName", "--optionA", "--optionB"};
 CommandLine cmd = parser.parse(options, args, null);

 Option optionA = cmd.getOption("optionA");
 Option optionB = cmd.getOption("optionB");

 Assert.assertTrue(optionA.isSelected());
 Assert.assertTrue(optionB.isSelected());
 }

 /**
 * Test parsing a command line with properties
 */
 @Test
 public void testParseWithProperties() throws ParseException {
 options.addOption("a", "optionA", false, "Option A");
 options.addOptions("p", "property:", true, "Property:");

 String[] args = new String[]{"test", "--property:key=value"};
 CommandLine cmd = parser.parse(options, args, null);

 Properties props = cmd.getParsedLongOptions();
 Assert.assertEquals("value", props.getProperty("key"));
 }

 /**
 * Test parsing a command line with a single non-option argument
 */
 @Test
 public void testSingleArg() throws ParseException {
 String[] args = new String[]{"test", "argument"};
 CommandLine cmd = parser.parse(options, args, null);

 Assert.assertEquals("argument", cmd.getArgs().get(0));
 Assert.assertFalse(cmd.hasOptions());
 }

 /**
 * Test parsing a command line with multiple arguments
 */
 @Test
 public void testMultipleArgs() throws ParseException {
 String[] args = new String[]{"test", "arg1", "arg2", "arg3"};
 CommandLine cmd = parser.parse(options, args, null);

 Assert.assertEquals(Arrays.asList("arg1", "arg2", "arg3"), cmd.getArgs());
 Assert.assertFalse(cmd.hasOptions());
 }

 /**
 * Test parsing a command line with a single non-option argument and an option
 */
 @Test
 public void testSingleArgWithOption() throws ParseException {
 String[] args = new String[]{"test", "--optionA"};
 CommandLine cmd = parser.parse(options, args, null);

 Assert.assertFalse(cmd.getArgs().isEmpty());
 Assert.assertTrue(cmd.hasOption("optionA"));
 }
}