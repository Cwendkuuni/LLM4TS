import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGUMENTS = {"--option1", "value1", "-o", "option2", "--option3=value3", "--", "arg1", "arg2"};
 private static final String[] REQUIRED_OPTIONS = {"--option1", "--option3"};

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("option2", false, "Option 2");
 options.addOption("option3", true, "Option 3")
 .requiredOption()
 .longOpt("long-option3")
 .valueSeparator('=');

 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGUMENTS, null, false);

 assertEquals(3, cmd.getNumOptions());
 assertTrue(cmd.hasOption("option1"));
 assertEquals("value1", cmd.getOptionValue("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertFalse(cmd.hasOption("option2", false)); // option2 is not a value option
 assertTrue(cmd.hasOption("option3"));
 assertEquals("value3", cmd.getOptionValue("option3"));

 // test for required options
 assertEquals(2, REQUIRED_OPTIONS.length);
 for (String option : REQUIRED_OPTIONS) {
 assertTrue(cmd.hasOption(option));
 }
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Option 1")
 .requiredOption();
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGUMENTS, null, false);
 fail("Missing required option exception not thrown");
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Option 1")
 .valueSeparator('=');
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGUMENTS, null, false);
 String option1 = cmd.getOptionValue("option1");
 assertEquals(0, option1.length()); // no argument provided

 options.addOption("option2", true, "Option 2")
 .requiredOption()
 .valueSeparator('=');
 cmd = parser.parse(options, ARGUMENTS, null, false);
 String option2 = cmd.getOptionValue("option2");
 assertEquals("", option2); // no argument provided

 options.addOption("option3", true, "Option 3")
 .valueSeparator('=')
 .longOpt("long-option3")
 .requiredOption()
 .argName("argument");
 cmd = parser.parse(options, ARGUMENTS, null, false);
 option3 = cmd.getOptionValue("option3");
 assertEquals("", option3); // no argument provided

 options.clearOptions();
 options.addOption("option1", true, "Option 1")
 .valueSeparator('=');
 options.addOption("option2", true, "Option 2")
 .valueSeparator('=');
 cmd = parser.parse(options, ARGUMENTS, null, false);
 option1 = cmd.getOptionValue("option1");
 assertEquals("arg1", option1); // first argument is used as value
 option2 = cmd.getOptionValue("option2");
 assertEquals("arg2", option2); // second argument is used as value
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 Parser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGUMENTS, null, false);
 String unknownOption = "--unknown";
 parser.parse(options, new String[]{unknownOption}, null, false);
 fail("Unrecognized option exception not thrown");
 }
}