import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

  private Parser parser;
  private Options options;

  @Before
  public void setup() {
    parser = new ParserImpl();
    options = new Options();

    options.addOption(Option.builder("a").argName("arg1").desc("Option A").build());
    options.addOption(Option.builder("b").argName("arg2").desc("Option B").hasArg().build());
  }

  @Test
  public void testParse_NoArguments() throws ParseException {
    String[] args = {};
    CommandLine cmd = parser.parse(options, args);

    assertFalse(cmd.hasOption("a"));
    assertFalse(cmd.hasOption("b"));
  }

  @Test
  public void testParse_SingleOption() throws ParseException {
    String[] args = { "-a" };
    CommandLine cmd = parser.parse(options, args);

    assertTrue(cmd.hasOption("a"));
    assertFalse(cmd.hasOption("b"));
  }

  @Test
  public void testParse_OptionWithValue() throws ParseException {
    String[] args = { "-b", "value" };
    CommandLine cmd = parser.parse(options, args);

    assertFalse(cmd.hasOption("a"));
    assertTrue(cmd.hasOption("b"));
    assertEquals("value", cmd.getOptionValue("b"));
  }

  @Test(expected = MissingOptionException.class)
  public void testParse_MissingRequiredOption() throws ParseException {
    options.addOption(Option.builder("c").required().build());
    String[] args = {};
    parser.parse(options, args);
  }

  // Add more test cases for other methods, including edge cases and error scenarios

}