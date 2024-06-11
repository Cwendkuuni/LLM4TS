import org.apache.commons.cli.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
  
  private Options options;
  private CommandLineParser parser;
  
  @Before
  public void setUp() {
    options = new Options();
    parser = new DefaultParser();
  }
  
  @Test
  public void testParse_NoOptionsAndArguments() throws ParseException {
    String[] args = {};

    CommandLine cmd = parser.parse(options, args);

    assertTrue(cmd.getOptions().length == 0);
    assertTrue(cmd.getArgs().length == 0);
  }
  
  @Test
  public void testParse_SingleOptionNoArgument() throws ParseException {
    options.addOption("a", "optionA", false, "Option A");

    String[] args = {"-a"};

    CommandLine cmd = parser.parse(options, args);

    assertTrue(cmd.hasOption("a"));
  }
  
  @Test
  public void testParse_SingleOptionWithArgument() throws ParseException {
    options.addOption(Option.builder("o")
                            .longOpt("option")
                            .required()
                            .hasArg()
                            .argName("value")
                            .desc("Option with argument")
                            .build());

    String[] args = {"-o", "value"};

    CommandLine cmd = parser.parse(options, args);

    assertTrue(cmd.hasOption("o"));
    assertTrue(cmd.getOptionValue("o").equals("value"));
  }
  
  @Test(expected = ParseException.class)
  public void testParse_InvalidOption() throws ParseException {
    String[] args = {"-invalid"};

    CommandLine cmd = parser.parse(options, args);
  }

  @Test(expected = MissingOptionException.class)
  public void testParse_MissingRequiredOption() throws ParseException {
    options.addOption(Option.builder("o")
                            .longOpt("option")
                            .required()
                            .hasArg()
                            .argName("value")
                            .desc("Required option with argument")
                            .build());

    String[] args = {};

    CommandLine cmd = parser.parse(options, args);
  }
  
}