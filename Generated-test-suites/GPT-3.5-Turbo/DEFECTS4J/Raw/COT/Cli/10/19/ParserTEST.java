import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

  private Parser parser;

  @Before
  public void setUp() {
    parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
          return arguments;
      }
    };
  }

  @Test
  public void testParse() throws ParseException {
      // Set up test data
      Options options = new Options();
      options.addOption(Option.builder("a").argName("arg1").hasArg().build());
      options.addOption(Option.builder("b").build());
      options.addOption(Option.builder("c").build());

      String[] args = new String[] { "-a", "value1", "-b", "-c" };

      // Execute the method to be tested
      CommandLine cmd = parser.parse(options, args);

      // Verify the result
      assertTrue(cmd.hasOption("a"));
      assertEquals("value1", cmd.getOptionValue("a"));
      assertTrue(cmd.hasOption("b"));
      assertFalse(cmd.hasOption("c"));
  }

  @Test(expected = UnrecognizedOptionException.class)
  public void testParse_UnrecognizedOption() throws ParseException {
      // Set up test data
      Options options = new Options();
      options.addOption(Option.builder("a").build());
      String[] args = new String[] { "-b" };

      // Execute the method to be tested
      parser.parse(options, args);
  }

  @Test(expected = MissingArgumentException.class)
  public void testParse_MissingArgument() throws ParseException {
      // Set up test data
      Options options = new Options();
      options.addOption(Option.builder("a").hasArg().build());
      String[] args = new String[] { "-a" };

      // Execute the method to be tested
      parser.parse(options, args);
  }

  @Test(expected = MissingOptionException.class)
  public void testParse_MissingRequiredOption() throws ParseException {
      // Set up test data
      Options options = new Options();
      options.addOption(Option.builder("a").required().build());
      options.addOption(Option.builder("b").build());
      String[] args = new String[] { "-b" };

      // Execute the method to be tested
      parser.parse(options, args);
  }
}