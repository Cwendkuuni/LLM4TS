import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGS_WITH_REQUIRED_OPTION = {"-a", "--b", "arg1"};
 private static final String[] ARGS_WITH_OPTIONAL_OPTION = {"-c", "value", "--d"};
 private static final String[] ARGS_WITH_LONG_OPTION = {"--option-long", "arg"};
 private static final String[] ARGS_WITH_DOUBLE_DASH = {"--"};
 private static final String[] ARGS_WITH_INVALID_OPTION = {"--invalid"};

 @Test
 public void testParseWithRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("a", "a", true, "An option requiring an argument");
 options.addRequiredOption("b", "b", true, "Another required option");

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_REQUIRED_OPTION);

 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("arg1", cmd.getOptionValue("a"));
 assertEquals("arg1", cmd.getOptionValue("b"));
 }

 @Test
 public void testParseWithOptionalOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("c", "c", false, "An optional option");

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_OPTIONAL_OPTION);

 assertTrue(cmd.hasOption("c"));
 assertNull(cmd.getOptionValue("c"));
 }

 @Test
 public void testParseWithLongOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option-long", "long", true, "An option with a long name");

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_LONG_OPTION);

 assertTrue(cmd.hasOption("option-long"));
 assertEquals("arg", cmd.getOptionValue("option-long"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingRequiredArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "a", true, "An option requiring an argument");

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0]);
 fail("MissingArgumentException expected but not thrown");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseUnknownOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_INVALID_OPTION);
 fail("UnrecognizedOptionException expected but not thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseDoubleDash() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();

 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS_WITH_DOUBLE_DASH);
 fail("ParseException expected but not thrown");
 }
}