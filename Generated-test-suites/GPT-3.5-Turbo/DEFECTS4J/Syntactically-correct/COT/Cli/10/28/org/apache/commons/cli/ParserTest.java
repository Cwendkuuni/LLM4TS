
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testFlatten() {
    // Test when stopAtNonOption is false
    Options options = new Options();
    String[] arguments = {"-a", "--option", "value", "nonOption"};
    boolean stopAtNonOption = false;
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    assertArrayEquals(arguments, parser.flatten(options, arguments, stopAtNonOption));

    // Test when stopAtNonOption is true
    stopAtNonOption = true;
    String[] expectedArguments = {"-a", "--option"};
    assertArrayEquals(expectedArguments, parser.flatten(options, arguments, stopAtNonOption));
  }

  @Test
  public void testParse() throws ParseException {
    // Test when no arguments are provided
    Options options = new Options();
    String[] arguments = {};
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    CommandLine result = parser.parse(options, arguments);
    assertNotNull(result);
    assertEquals(0, result.getOptions().length);
    assertEquals(0, result.getArgs().length);

    // Test when option is provided
    Option option = new Option("a", "option", false, "example option");
    options.addOption(option);
    arguments = new String[]{"--option"};
    result = parser.parse(options, arguments);
    assertNotNull(result);
    assertTrue(result.hasOption("option"));
    assertEquals(0, result.getArgs().length);

    // Test when argument is provided
    arguments = new String[]{"argument"};
    result = parser.parse(options, arguments);
    assertNotNull(result);
    assertEquals(0, result.getOptions().length);
    assertEquals(1, result.getArgs().length);
    assertEquals("argument", result.getArgs()[0]);
  }

  @Test(expected = MissingOptionException.class)
  public void testCheckRequiredOptions() throws MissingOptionException {
    Options options = new Options();
    Option option = new Option("r", "required", true, "required option");
    option.setRequired(true);
    options.addOption(option);

    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };

    // Missing required option
    parser.checkRequiredOptions();
  }
}
