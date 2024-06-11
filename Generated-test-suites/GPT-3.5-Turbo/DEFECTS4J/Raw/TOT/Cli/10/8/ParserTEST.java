import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

  private Parser parser;

  @Before
  public void setUp() throws Exception {
    parser = new DefaultParser();
  }

  @After
  public void tearDown() throws Exception {
    parser = null;
  }

  @Test
  public void testParse_NullOptionsAndArguments_ParseExceptionThrown() {
    try {
      parser.parse(null, null);
      Assert.fail("Expected ParseException");
    } catch (ParseException e) {
      // Success
      Assert.assertTrue(e instanceof ParseException);
    }
  }

  @Test
  public void testParse_MissingRequiredOption_MissingOptionExceptionThrown() {
    Options options = new Options();
    Option option = new Option("f", "file", true, "input file");
    option.setRequired(true);
    options.addOption(option);

    try {
      parser.parse(options, null);
      Assert.fail("Expected MissingOptionException");
    } catch (MissingOptionException e) {
      // Success
      Assert.assertTrue(e instanceof MissingOptionException);
    }
  }

  @Test
  public void testParse_ValidOptionsAndArguments_CommandLineReturned() throws ParseException {
    Options options = new Options();
    Option option = new Option("f", "file", true, "input file");
    options.addOption(option);

    String[] args = {"--file", "input.txt"};

    CommandLine cmd = parser.parse(options, args);

    Assert.assertTrue(cmd.hasOption("f"));
    Assert.assertEquals("input.txt", cmd.getOptionValue("f"));
  }

  @Test
  public void testProcessArgs_ValidOptionWithSingleArgument_OptionProcessed() throws ParseException {
    Option option = new Option("f", "file", true, "input file");
    ListIterator<String> iterator = Arrays.asList("input.txt").listIterator();

    parser.processArgs(option, iterator);

    Assert.assertTrue(option.hasArg());
    Assert.assertEquals("input.txt", option.getValue());
  }

  @Test
  public void testProcessArgs_ValidOptionWithMultipleArguments_OptionProcessed() throws ParseException {
    Option option = new Option("n", "numbers", true, "list of numbers");
    ListIterator<String> iterator = Arrays.asList("1", "2", "3").listIterator();

    parser.processArgs(option, iterator);

    Assert.assertTrue(option.hasArgs());
    Assert.assertArrayEquals(new String[]{"1", "2", "3"}, option.getValues());
  }

  @Test
  public void testProcessOption_ValidOption_OptionProcessed() throws ParseException {
    String arg = "--file";
    ListIterator<String> iterator = Arrays.asList("input.txt").listIterator();

    parser.processOption(arg, iterator);

    CommandLine cmd = parser.getCmd();

    Assert.assertTrue(cmd.hasOption("file"));
    Assert.assertEquals("input.txt", cmd.getOptionValue("file"));
  }

  @Test
  public void testProcessOption_UnrecognizedOption_UnrecognizedOptionExceptionThrown() {
    String arg = "-u";
    ListIterator<String> iterator = Arrays.asList("value").listIterator();

    try {
      parser.processOption(arg, iterator);
      Assert.fail("Expected UnrecognizedOptionException");
    } catch (UnrecognizedOptionException e) {
      // Success
      Assert.assertTrue(e instanceof UnrecognizedOptionException);
    }
  }

  @Test
  public void testProcessOption_RequiredOption_OptionAddedToRequiredOptionsList() throws ParseException {
    String arg = "--file";
    ListIterator<String> iterator = Arrays.asList("input.txt").listIterator();

    Option option = new Option("f", "file", true, "input file");
    option.setRequired(true);
    options.addOption(option);

    parser.processOption(arg, iterator);

    List<Option> requiredOptions = parser.getRequiredOptions();
    Assert.assertTrue(requiredOptions.contains(option));
    Assert.assertEquals(1, requiredOptions.size());
  }
}