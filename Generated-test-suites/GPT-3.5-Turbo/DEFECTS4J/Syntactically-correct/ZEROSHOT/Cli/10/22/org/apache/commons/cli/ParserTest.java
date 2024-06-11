import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

  @Test
  public void testParse() throws ParseException {
    Options options = new Options();
    Option option = new Option("a", "option");
    option.setRequired(true);
    options.addOption(option);

    String[] arguments = {"-a", "value"};

    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };

    CommandLine cmd = parser.parse(options, arguments);
    assert(cmd.hasOption("option"));
    assert(cmd.getOptionValue("option").equals("value"));
  }

  @Test(expected=MissingOptionException.class)
  public void testMissingOption() throws ParseException {
    Options options = new Options();
    Option option = new Option("a", "option");
    option.setRequired(true);
    options.addOption(option);

    String[] arguments = {};

    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };

    CommandLine cmd = parser.parse(options, arguments);
  }

  @Test(expected=UnrecognizedOptionException.class)
  public void testUnrecognizedOption() throws ParseException {
    Options options = new Options();
    Option option = new Option("a", "option");
    options.addOption(option);

    String[] arguments = {"-b"};

    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };

    CommandLine cmd = parser.parse(options, arguments);
  }

  @Test(expected=MissingArgumentException.class)
  public void testMissingArgument() throws ParseException {
    Options options = new Options();
    Option option = new Option("a", "option");
    option.setArgs(1);
    options.addOption(option);

    String[] arguments = {"-a"};

    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };

    CommandLine cmd = parser.parse(options, arguments);
  }
}