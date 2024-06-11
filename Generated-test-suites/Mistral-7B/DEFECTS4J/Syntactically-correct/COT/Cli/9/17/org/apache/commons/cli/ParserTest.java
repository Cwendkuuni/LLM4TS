import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 parser = new DefaultParser();
 options = new Options();
 }

 @Test
 public void testParseWithValidArguments() throws ParseException {
 String[] arguments = new String[]{"-a", "1", "--optionB", "2"};
 CommandLine cmd = parser.parse(options, arguments);

 assertEquals("1", cmd.getOptionValue("a"));
 assertEquals("2", cmd.getOptionValue("optionB"));
 assertNull(cmd.getOptionValue("nonexistentoption"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 String[] arguments = new String[]{"--optionA"};
 assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnrecognizedOption() throws ParseException {
 String[] arguments = new String[]{"--unknownoption"};
 assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArgument() throws ParseException {
 Option option = new Option("a", "arg", true, "help");
 option.setArgName("NONE");
 options.addOption(option);
 String[] arguments = new String[]{"--a"};
 assertThrows(ParseException.class, () -> parser.parse(options, arguments));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidArgumentValue() throws ParseException {
 Option option = new Option("a", "arg", true, "help");
 option.setArgName("NONE");
 String[] arguments = new String[]{"--a", "invalidValue"};
 assertThrows(ParseException.class, () -> parser.parse(options, arguments));
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.put("myOption", "value");
 CommandLine cmd = parser.parse(options, new String[0], properties);
 assertEquals("value", cmd.getOptionValue("myOption"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Option option = new Option("a", "arg", true, "help");
 String[] arguments = new String[]{"--a", "value", "--optionB"};
 CommandLine cmd = parser.parse(options, arguments, true);
 assertEquals("value", cmd.getOptionValue("a"));
 assertNull(cmd.getOptionValue("optionB"));
 }
}