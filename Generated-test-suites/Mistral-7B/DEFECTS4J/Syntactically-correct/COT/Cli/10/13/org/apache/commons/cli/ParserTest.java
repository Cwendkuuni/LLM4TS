import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
 public void testParse() throws ParseException {
 String[] args = {"-a", "1", "--option", "value", "-b", "2"};
 CommandLine cmd = parser.parse(options, args);

 assertEquals("1", cmd.getOptionValue("a"));
 assertEquals("value", cmd.getOptionValue("option"));
 assertNull(cmd.getOptionValue("nonExistentOption"));
 assertEquals("2", cmd.getArg(0));
 assertEquals("", cmd.getArg(1));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingOption() throws ParseException {
 String[] args = {"-a", "1"};
 CommandLine cmd = parser.parse(options, args);
 fail("Missing required option");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 String[] args = {"--unknownOption", "value"};
 CommandLine cmd = parser.parse(options, args);
 fail("Unrecognized option: unknownOption");
 }

 @Test(expected = ParseException.class)
 public void testInvalidOptionValue() throws ParseException {
 options.addOption("a", true, "a");
 String[] args = {"-a"};
 CommandLine cmd = parser.parse(options, args);
 fail("Invalid option value for option: a");
 }

 @Test
 public void testProcessProperties() throws ParseException {
 Properties props = new Properties();
 props.setProperty("optionA", "valueA");
 props.setProperty("optionB.arg", "valueB");

 parser.parse(options, new String[0], props);

 assertEquals(2, options.getOptions().size());
 Option optionA = options.getOption("optionA");
 Option optionB = options.getOption("optionB");

 assertEquals("valueA", optionA.getValue());
 assertEquals(new String[]{"valueB"}, optionB.getValues());
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Option option = new Option("a", true, "a");
 option.setRequired(true);
 options.addOption(option);
 String[] args = {"--optionB"};
 CommandLine cmd = parser.parse(options, args);
 fail("Missing argument for option: a");
 }
}