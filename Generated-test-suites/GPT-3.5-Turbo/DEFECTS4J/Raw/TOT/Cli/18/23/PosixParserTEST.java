import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

  private Options options;
  private CommandLineParser parser;
  
  @Before
  public void setUp() {
    options = new Options();
    parser = new PosixParser();
  }

  @Test
  public void testFlatten_StopAtNonOption() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"-a", "value", "-b", "-c", "arg1", "arg2"};
    String[] expected = {"-a", "value", "-b", "-c", "arg1", "arg2"};
    String[] result = parser.flatten(options, args, true);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_ContinuePastNonOptions() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"-a", "value", "-b", "-c", "arg1", "arg2"};
    String[] expected = {"-a", "value", "-b", "-c", "arg1", "arg2"};
    String[] result = parser.flatten(options, args, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_SingleHyphen() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"-"};
    String[] expected = {"-"};
    String[] result = parser.flatten(options, args, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_LongOptionWithValue() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"--option=value"};
    String[] expected = {"--option", "value"};
    String[] result = parser.flatten(options, args, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_LongOptionWithNoValue() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"--option"};
    String[] expected = {"--option"};
    String[] result = parser.flatten(options, args, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_SingleOptionWithValue() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"-a", "value"};
    String[] expected = {"-a", "value"};
    String[] result = parser.flatten(options, args, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_SingleOptionWithNoValue() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"-b"};
    String[] expected = {"-b"};
    String[] result = parser.flatten(options, args, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_InvalidOption_StopAtNonOption() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"-x", "value"};
    String[] expected = {"-x", "value"};
    String[] result = parser.flatten(options, args, true);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_InvalidOption_ContinuePastNonOptions() throws ParseException {
    options.addOption("a", true, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", true, "Option c");
    options.addOption("d", false, "Option d");

    String[] args = {"-x", "value"};
    String[] expected = {"-x", "value"};
    String[] result = parser.flatten(options, args, false);

    assertArrayEquals(expected, result);
  }

}