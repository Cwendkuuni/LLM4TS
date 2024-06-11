import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

  private Parser parser;

  @Before
  public void setup() {
    parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };
  }

  @Test
  public void testParse_WithValidOptionsAndArguments_ShouldReturnCommandLine() throws ParseException {
    Options options = new Options();
    options.addOption("f", "file", true, "input file");
    options.addOption("v", "verbose", false, "enable verbose mode");

    String[] arguments = {"-f", "input.txt", "-v"};

    CommandLine result = parser.parse(options, arguments);

    Assert.assertTrue(result.hasOption("f"));
    Assert.assertTrue(result.hasOption("verbose"));
    Assert.assertEquals("input.txt", result.getOptionValue("f"));
    Assert.assertFalse(result.hasOption("h"));
  }

  @Test(expected = ParseException.class)
  public void testParse_WithInvalidOption_ShouldThrowParseException() throws ParseException {
    Options options = new Options();
    options.addOption("f", "file", true, "input file");

    String[] arguments = {"-c", "file.txt"};

    parser.parse(options, arguments);
  }

  @Test
  public void testProcessArgs_WithSingleArgument_ShouldAddValueToOption() throws ParseException {
    Option option = new Option("f", "file", true, "input file");

    ListIterator iterator = Arrays.asList("file.txt").listIterator();

    parser.processArgs(option, iterator);

    Assert.assertEquals("file.txt", option.getValue());
  }

  @Test(expected = MissingArgumentException.class)
  public void testProcessArgs_WithMissingArgument_ShouldThrowMissingArgumentException() throws ParseException {
    Option option = new Option("f", "file", true, "input file");

    ListIterator iterator = Arrays.asList().listIterator();

    parser.processArgs(option, iterator);
  }

  @Test
  public void testProcessOption_WithValidOption_ShouldAddOptionToCommandLine() throws ParseException {
    Options options = new Options();
    options.addOption("f", "file", true, "input file");

    ListIterator iterator = Arrays.asList("-f", "file.txt").listIterator();

    parser.processOption("-f", iterator);

    CommandLine commandLine = parser.getCommandLine();

    Assert.assertTrue(commandLine.hasOption("f"));
    Assert.assertEquals("file.txt", commandLine.getOptionValue("f"));
  }

  @Test(expected = UnrecognizedOptionException.class)
  public void testProcessOption_WithInvalidOption_ShouldThrowUnrecognizedOptionException() throws ParseException {
    Options options = new Options();
    options.addOption("f", "file", true, "input file");

    ListIterator iterator = Arrays.asList("-c", "file.txt").listIterator();

    parser.processOption("-c", iterator);
  }

  @Test
  public void testCheckRequiredOptions_WithMissingRequiredOption_ShouldThrowMissingOptionException() {
    Options options = new Options();
    options.addOption(Option.builder("f").required().build());

    CommandLine commandLine = new CommandLine();
    commandLine.addOption(Option.builder("m").build());

    parser.setOptions(options);
    parser.setCommandLine(commandLine);

    try {
      parser.checkRequiredOptions();
      // Expecting MissingOptionException to be thrown
      Assert.fail("MissingOptionException not thrown");
    } catch (MissingOptionException e) {
      Assert.assertTrue(e.getMissingOptions().contains("f"));
    }
  }

  @Test
  public void testCheckRequiredOptions_WithAllRequiredOptions_ShouldNotThrowException() {
    Options options = new Options();
    options.addOption(Option.builder("f").required().build());

    CommandLine commandLine = new CommandLine();
    commandLine.addOption(Option.builder("f").build());

    parser.setOptions(options);
    parser.setCommandLine(commandLine);

    try {
      parser.checkRequiredOptions();
    } catch (MissingOptionException e) {
      // Expecting no exception to be thrown
      Assert.fail("Unexpected Exception thrown");
    }
  }

}