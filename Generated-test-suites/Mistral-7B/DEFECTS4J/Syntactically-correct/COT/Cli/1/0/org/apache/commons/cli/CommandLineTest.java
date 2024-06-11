import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 private static final String TEST_OPTION_A = "a";
 private static final String TEST_OPTION_B = "b";
 private static final String TEST_OPTION_C = "c";
 private static final String TEST_ARG = "testArg";

 @Test
 public void testHasOption() {
 CommandLine commandLine = getDefaultCommandLine();

 // test for non-existent option
 Assert.assertFalse(commandLine.hasOption(TEST_OPTION_A));

 // test for existent option
 CommandLine commandLineWithOption = getCommandLineWithOption(TEST_OPTION_A);
 Assert.assertTrue(commandLineWithOption.hasOption(TEST_OPTION_A));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLineWithOption = getCommandLineWithOption(TEST_OPTION_A);

 // test for option without value
 Assert.assertNull(commandLineWithOption.getOptionValue(TEST_OPTION_A));

 // test for option with value
 CommandLine commandLineWithOptionValue = getCommandLineWithOptionValue(TEST_OPTION_A, TEST_ARG);
 Assert.assertEquals(TEST_ARG, commandLineWithOptionValue.getOptionValue(TEST_OPTION_A));
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLineWithOptionValues = getCommandLineWithOptionValues(TEST_OPTION_A, TEST_ARG, "testArg2");
 String[] optionValues = commandLineWithOptionValues.getOptionValues(TEST_OPTION_A);
 Assert.assertArrayEquals(new String[] {TEST_ARG, "testArg2"}, optionValues);
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLineWithArgs = getCommandLineWithArgs(new String[] {"testArg1", "testArg2"});
 String[] args = commandLineWithArgs.getArgs();
 Assert.assertArrayEquals(new String[] {"testArg1", "testArg2"}, args);
 }

 @Test(expected = ParseException.class)
 public void testParsingInvalidArgument() {
 String[] incorrectArgs = new String[] {"--incorrectArgument"};
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options, incorrectArgs, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParsingMissingRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", "arg", true, "an argument");
 options.addOption(option);
 String[] incorrectArgs = new String[] {};
 try {
 parser.parse(options, incorrectArgs, false);
 } catch (ParseException e) {
 // expected
 Assert.assertEquals("Missing required option 'arg'", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParsingInvalidOptionValue() {
 Options options = new Options();
 Option option = new Option("a", "arg", true, "an argument");
 options.addOption(option);
 String[] incorrectArgs = new String[] {"--arg=invalidValue"};
 try {
 parser.parse(options, incorrectArgs, false);
 } catch (ParseException e) {
 // expected
 }
 }

 private CommandLine getDefaultCommandLine() {
 CommandLine commandLine = new CommandLine();
 return commandLine;
}

 private CommandLine getCommandLineWithOption(String option) {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(getDefaultCommandLineOptions().toArray(new String[0]), new String[] {option}, false);
 return commandLine;
}

 private CommandLine getCommandLineWithOptionValue(String option, String value) {
 CommandLine commandLine = new CommandLine();
 Option optionToAdd = new Option(option, null, true, null);
 commandLine.parse(getDefaultCommandLineOptions().toArray(new String[0]), new String[] {option, value}, false);
 return commandLine;
}

 private CommandLine getCommandLineWithOptionValues(String option, String... values) {
 CommandLine commandLine = new CommandLine();
 Option optionToAdd = new Option(option, null, true, null);
 commandLine.parse(getDefaultCommandLineOptions().toArray(new String[0]), new String[] {option, String.join(" ", values)}, false);
 return commandLine;
}

 private CommandLine getCommandLineWithArgs(String[] args) {
 CommandLine commandLine = new CommandLine();
 CommandLineParser parser = new DefaultParser();
 Options options = getDefaultCommandLineOptions();
 try {
 parser.parse(options.toArray(new String[0]), args, false);
 } catch (ParseException e) {
 // should not reach here
 }
 return commandLine;
}

 private Options getDefaultCommandLineOptions() {
 Options options = new Options();
 return options;
}

 private CommandLineParser parser = new DefaultParser();
}