import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParserTest {

  private class MockParser extends Parser {

    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
      return arguments;
    }

    @Override
    public void processArgs(Option opt, ListIterator<String> iter) throws ParseException {
      // Do nothing for mocking purposes
    }

    @Override
    protected void processOption(String arg, ListIterator<String> iter) throws ParseException {
      // Do nothing for mocking purposes
    }
  }

  @Test
  public void testParse_withNullOptionsAndArguments_shouldReturnEmptyCommandLine() throws ParseException {
    Parser parser = new MockParser();
    CommandLine result = parser.parse(null, null);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getOptions().isEmpty());
    Assert.assertTrue(result.getArgList().isEmpty());
  }

  @Test
  public void testParse_withEmptyOptionsAndArguments_shouldReturnEmptyCommandLine() throws ParseException {
    Parser parser = new MockParser();
    CommandLine result = parser.parse(new Options(), new String[0]);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getOptions().isEmpty());
    Assert.assertTrue(result.getArgList().isEmpty());
  }

  @Test
  public void testParse_withOptionsAndNonOptionArguments_shouldReturnCommandLineWithNonOptionArguments() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "");

    Parser parser = new MockParser();
    CommandLine result = parser.parse(options, new String[] {"arg1", "arg2"});
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getOptions().isEmpty());
    Assert.assertEquals(2, result.getArgList().size());
    Assert.assertEquals("arg1", result.getArgList().get(0));
    Assert.assertEquals("arg2", result.getArgList().get(1));
  }

  @Test(expected = UnrecognizedOptionException.class)
  public void testParse_withOptionsAndInvalidOptionArgument_shouldThrowUnrecognizedOptionException() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "");

    Parser parser = new MockParser();
    parser.parse(options, new String[] {"-b"});
  }

  @Test(expected = MissingArgumentException.class)
  public void testParse_withOptionsAndMissingArgument_shouldThrowMissingArgumentException() throws ParseException {
    Options options = new Options();
    options.addOption(Option.builder("a").hasArg().build());

    Parser parser = new MockParser();
    parser.parse(options, new String[] {"-a"});
  }

  @Test
  public void testParse_withOptionsAndOptionWithArgument_shouldReturnCommandLineWithValueForOption() throws ParseException {
    Options options = new Options();
    options.addOption(Option.builder("a").hasArg().build());

    Parser parser = new MockParser();
    CommandLine result = parser.parse(options, new String[] {"-a", "value"});
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getOptions().hasOption("a"));
    Assert.assertEquals("value", result.getOptionValue("a"));
    Assert.assertTrue(result.getArgList().isEmpty());
  }

  @Test(expected = MissingOptionException.class)
  public void testCheckRequiredOptions_withMissingRequiredOptions_shouldThrowMissingOptionException() throws MissingOptionException {
    Options options = new Options();
    options.addOption("a", false, "");
    options.addOption("b", false, "");
    options.getOption("a").setRequired(true);
    options.getOption("b").setRequired(true);

    Parser parser = new MockParser();
    parser.setOptions(options);
    parser.checkRequiredOptions();
  }

  @Test
  public void testCheckRequiredOptions_withAllRequiredOptionsPresent_shouldNotThrowMissingOptionException() throws MissingOptionException {
    Options options = new Options();
    options.addOption("a", false, "");
    options.addOption("b", false, "");
    options.getOption("a").setRequired(true);
    options.getOption("b").setRequired(true);

    Parser parser = new MockParser();
    parser.setOptions(options);
    parser.checkRequiredOptions();
  }
}