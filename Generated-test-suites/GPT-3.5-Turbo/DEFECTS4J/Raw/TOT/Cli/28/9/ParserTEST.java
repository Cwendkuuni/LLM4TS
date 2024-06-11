import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ParserTest {

  private Parser parser;
  private Options options;

  @Before
  public void setUp() {
    parser = new ParserImpl();
    options = new Options();
  }

  @Test
  public void testParse_ValidOptions_Success() throws ParseException {
    options.addOption(Option.builder("a").hasArg().build());
    options.addOption(Option.builder("b").build());
    String[] args = { "-a", "valueA", "-b" };
    CommandLine result = parser.parse(options, args);
    assert result.hasOption("a");
    assert result.hasOption("b");
    assert result.getOptionValue("a").equals("valueA");
  }

  @Test(expected = UnrecognizedOptionException.class)
  public void testParse_InvalidOption_ThrowsException() throws ParseException {
    options.addOption(Option.builder("a").build());
    String[] args = { "-b" };
    parser.parse(options, args);
  }

  @Test(expected = MissingArgumentException.class)
  public void testParse_MissingArgumentForOption_ThrowsException() throws ParseException {
    options.addOption(Option.builder("a").hasArg().build());
    String[] args = { "-a" };
    parser.parse(options, args);
  }

  @Test
  public void testParse_StopAtNonOption_AddsRemainingArgumentsToList() throws ParseException {
    options.addOption(Option.builder("a").build());
    String[] args = { "-a", "valueA", "nonOption1", "nonOption2" };
    CommandLine result = parser.parse(options, args, true);
    assert result.hasOption("a");
    assert result.getOptionValue("a") == null;
    assert result.getArgs().length == 2;
    assert result.getArgs()[0].equals("nonOption1");
    assert result.getArgs()[1].equals("nonOption2");
  }

  // Add more test cases for other methods as required.

}