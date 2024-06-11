import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setup() {
 parser = new DefaultParser();
 options = new Options();
 }

 @Test
 public void testSetAndGetOptions() {
 options.addOption("o", "option", false, "an option");
 parser.setOptions(options);
 assertEquals(options, parser.getOptions());
 }

 @Test
 public void testGetRequiredOptions() {
 options.addRequiredOption("r", "requiredOption", false, "a required option");
 parser.setOptions(options);
 List<Option> requiredOptions = parser.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals("r", requiredOptions.get(0).getKey());
 }

 @Test(expected = ParseException.class)
 public void testParseWithEmptyArguments() {
 CommandLine cmd = parser.parse(options, new String[0]);
 fail("Expected ParseException when parsing with empty arguments.");
 }

 @Test
 public void testParseWithValidArguments() {
 String[] arguments = {"-o", "arg1", "--option2", "arg3"};
 CommandLine cmd = null;
 try {
 cmd = parser.parse(options, arguments);
 } catch (ParseException e) {
 fail("ParseException occurred while parsing valid arguments.");
 }

 assertEquals(4, cmd.getArgs().size());
 assertEquals("-o", cmd.getArg(0));
 assertEquals("arg1", cmd.getArg(1));
 assertEquals("--option2", cmd.getArg(2));
 assertEquals("arg3", cmd.getArg(3));

 assertTrue(cmd.hasOption("o"));
 assertTrue(cmd.hasOption("option2"));
 assertFalse(cmd.hasOption("invalidOption"));
 }

 @Test
 public void testParseWithProperties() {
 Properties properties = new Properties();
 properties.setProperty("option.arg", "value");
 CommandLine cmd = null;
 try {
 cmd = parser.parse(options, new String[0], properties, false);
 } catch (ParseException e) {
 fail("ParseException occurred while parsing properties.");
 }

 assertTrue(cmd.getOptions().hasOption("option"));
 assertEquals(1, cmd.getOptions().getOption("option").getValues().length);
 assertEquals("value", cmd.getOptions().getOption("option").getValueForProcessing());
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArguments() {
 String[] arguments = {"--", "-o", "--invalidOption", "-"};
 CommandLine cmd = null;
 try {
 cmd = parser.parse(options, arguments, null, false);
 } catch (ParseException e) {
 // expected
 }

 assertEquals(0, cmd.getArgs().size());
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingRequiredOptions() {
 options.addRequiredOption("r", "requiredOption", false, "a required option");
 String[] arguments = {"-o", "arg1", "--option2", "arg3"};
 CommandLine cmd = null;
 try {
 cmd = parser.parse(options, arguments, null, false);
 } catch (ParseException e) {
 fail("ParseException occurred while parsing valid arguments.");
 }

 assertTrue(cmd.hasOption("o"));
 assertTrue(cmd.hasOption("option2"));
 assertFalse(cmd.hasOption("r"));

 try {
 parser.checkRequiredOptions();
 fail("Expected MissingOptionException when required options are missing.");
 } catch (MissingOptionException e) {
 // expected
 }
 }

 @Test
 public void testProcessOptions() {
 options.addOption("o", "option", false, "an option");
 String[] arguments = {"-o", "arg1", "--option2", "arg3"};
 CommandLine cmd = parser.parse(options, arguments, null, false);
 List<Option> optionsList = cmd.getOptions();
 Option option1 = optionsList.get(0);
 Option option2 = optionsList.get(1);

 assertEquals("arg1", option1.getValueForProcessing());
 assertEquals("arg3", option2.getValueForProcessing());
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingRequiredArgument() {
 options.addOption("r", "requiredOption", true, "a required argument");
 String[] arguments = {"-o", "arg1", "--option2"};
 CommandLine cmd = null;
 try {
 cmd = parser.parse(options, arguments, null, false);
 } catch (ParseException e) {
 fail("ParseException occurred while parsing valid arguments.");
 }

 assertTrue(cmd.hasOption("o"));
 assertTrue(cmd.hasOption("option2"));
 assertFalse(cmd.hasOption("r"));

 try {
 parser.checkRequiredOptions();
 fail("Expected MissingArgumentException when required arguments are missing.");
 } catch (MissingArgumentException e) {
 // expected
 }
 }

}