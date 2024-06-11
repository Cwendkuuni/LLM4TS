import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Properties;

public class ParserTest {

  @Test
  public void testParse_WhenNoOptionsAndNoArguments_ThenReturnEmptyCommandLine() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();
    String[] arguments = {};

    CommandLine result = parser.parse(options, arguments);

    assertTrue(result.getOptions().isEmpty());
    assertTrue(result.getArgList().isEmpty());
  }

  @Test
  public void testParse_WhenOptionWithNoArgument_ThenReturnCommandLineWithOption() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();
    options.addOption(new Option("a", false, "no argument"));
    options.addOption(new Option("b", false, "no argument"));

    String[] arguments = {"-a"};

    CommandLine result = parser.parse(options, arguments);

    assertTrue(result.hasOption("a"));
    assertFalse(result.hasOption("b"));
  }

  @Test
  public void testParse_WhenOptionWithArgument_ThenReturnCommandLineWithOptionAndArgument() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();
    options.addOption(new Option("a", true, "with argument"));

    String[] arguments = {"-a", "argument"};

    CommandLine result = parser.parse(options, arguments);

    assertTrue(result.hasOption("a"));
    assertEquals("argument", result.getOptionValue("a"));
  }

  @Test
  public void testParse_WhenOptionWithArgumentMissing_ThenThrowMissingArgumentException() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();
    options.addOption(new Option("a", true, "with argument"));

    String[] arguments = {"-a"};

    try {
      parser.parse(options, arguments);
      fail("MissingArgumentException is expected.");
    }
    catch (MissingArgumentException e) {
      assertEquals("Missing argument for option: a", e.getMessage());
    }
  }

  @Test
  public void testParse_WhenOptionNotRecognized_ThenThrowUnrecognizedOptionException() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();
    options.addOption(new Option("a", false, "no argument"));

    String[] arguments = {"-b"};

    try {
      parser.parse(options, arguments);
      fail("UnrecognizedOptionException is expected.");
    }
    catch (UnrecognizedOptionException e) {
      assertEquals("Unrecognized option: -b", e.getMessage());
    }
  }

  @Test
  public void testParse_WhenStopAtNonOptionTrueAndUnknownOption_ThenReturnCommandLineWithRemainingArguments() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();
    options.addOption(new Option("a", false, "no argument"));

    String[] arguments = {"-a", "-b"};

    CommandLine result = parser.parse(options, arguments, true);

    assertTrue(result.hasOption("a"));
    assertFalse(result.hasOption("b"));
    assertEquals("-b", result.getArgList().get(0));
  }

  @Test
  public void testParse_WhenNoOptionsAndStopAtNonOptionTrue_ThenReturnCommandLineWithArguments() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();

    String[] arguments = {"argument1", "argument2"};

    CommandLine result = parser.parse(options, arguments, true);

    assertTrue(result.getOptions().isEmpty());
    assertEquals("argument1", result.getArgList().get(0));
    assertEquals("argument2", result.getArgList().get(1));
  }

  @Test
  public void testParse_WhenOptionWithArgumentFromProperties_ThenReturnCommandLineWithOptionAndArgument() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();
    options.addOption(new Option("a", true, "with argument"));

    Properties properties = new Properties();
    properties.setProperty("a", "argument");

    String[] arguments = {};

    CommandLine result = parser.parse(options, arguments, properties);

    assertTrue(result.hasOption("a"));
    assertEquals("argument", result.getOptionValue("a"));
  }

  @Test
  public void testParse_WhenRequiredOptionMissing_ThenThrowMissingOptionException() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };

    Options options = new Options();
    options.addOption(new Option("a", true, "required option"));

    String[] arguments = {};

    try {
      parser.parse(options, arguments);
      fail("MissingOptionException is expected.");
    }
    catch (MissingOptionException e) {
      assertEquals("Missing required options: a", e.getMessage());
    }
  }
}