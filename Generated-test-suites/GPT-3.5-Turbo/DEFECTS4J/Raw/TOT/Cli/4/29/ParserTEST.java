import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  private Parser parser;
  private Options options;

  @Before
  public void setUp() {
    parser = new ParserImpl();
    options = new Options();
  }

  @Test
  public void testParse_NoArguments() {
    String[] arguments = {};
    CommandLine commandLine = parser.parse(options, arguments);
    assertEquals(0, commandLine.getOptions().length);
    assertEquals(0, commandLine.getArgs().length);
  }

  @Test
  public void testParse_SingleOption() {
    Option option = new Option("a", "optionA", false, "Option A");
    options.addOption(option);

    String[] arguments = {"-a"};
    CommandLine commandLine = parser.parse(options, arguments);
    assertTrue(commandLine.hasOption("a"));
    assertFalse(commandLine.hasOption("optionA"));
    assertEquals(1, commandLine.getOptions().length);
    assertEquals(0, commandLine.getArgs().length);
  }

  @Test
  public void testParse_MultipleOptions() {
    Option optionA = new Option("a", "optionA", false, "Option A");
    Option optionB = new Option("b", "optionB", false, "Option B");
    options.addOption(optionA);
    options.addOption(optionB);

    String[] arguments = {"-a", "-b"};
    CommandLine commandLine = parser.parse(options, arguments);
    assertTrue(commandLine.hasOption("a"));
    assertFalse(commandLine.hasOption("optionA"));
    assertTrue(commandLine.hasOption("b"));
    assertFalse(commandLine.hasOption("optionB"));
    assertEquals(2, commandLine.getOptions().length);
    assertEquals(0, commandLine.getArgs().length);
  }

  @Test
  public void testParse_OptionWithValue() {
    Option option = new Option("a", "optionA", true, "Option A");
    options.addOption(option);

    String[] arguments = {"-a", "value"};
    CommandLine commandLine = parser.parse(options, arguments);
    assertTrue(commandLine.hasOption("a"));
    assertFalse(commandLine.hasOption("optionA"));
    assertEquals("value", commandLine.getOptionValue("a"));
    assertEquals(1, commandLine.getOptions().length);
    assertEquals(0, commandLine.getArgs().length);
  }

  @Test
  public void testParse_OptionWithMultipleValues() {
    Option option = new Option("a", "optionA", true, "Option A");
    option.setArgs(2);
    options.addOption(option);

    String[] arguments = {"-a", "value1", "value2"};
    CommandLine commandLine = parser.parse(options, arguments);
    assertTrue(commandLine.hasOption("a"));
    assertFalse(commandLine.hasOption("optionA"));
    assertEquals(2, commandLine.getOptionValues("a").length);
    assertEquals("value1", commandLine.getOptionValues("a")[0]);
    assertEquals("value2", commandLine.getOptionValues("a")[1]);
    assertEquals(1, commandLine.getOptions().length);
    assertEquals(0, commandLine.getArgs().length);
  }

  @Test
  public void testParse_OptionWithOptionalValue() {
    Option option = new Option("a", "optionA", true, "Option A");
    option.setOptionalArg(true);
    options.addOption(option);

    String[] arguments = {"-a"};
    CommandLine commandLine = parser.parse(options, arguments);
    assertTrue(commandLine.hasOption("a"));
    assertFalse(commandLine.hasOption("optionA"));
    assertNull(commandLine.getOptionValue("a"));
    assertEquals(1, commandLine.getOptions().length);
    assertEquals(0, commandLine.getArgs().length);
  }

  @Test(expected = MissingArgumentException.class)
  public void testParse_OptionMissingValue() {
    Option option = new Option("a", "optionA", true, "Option A");
    options.addOption(option);

    String[] arguments = {"-a"};
    parser.parse(options, arguments);
  }

  @Test
  public void testParse_StopAtNonOption() {
    Option optionA = new Option("a", "optionA", false, "Option A");
    Option optionB = new Option("b", "optionB", false, "Option B");
    options.addOption(optionA);
    options.addOption(optionB);

    String[] arguments = {"-a", "value", "-b", "nonOption"};
    CommandLine commandLine = parser.parse(options, arguments, true);
    assertTrue(commandLine.hasOption("a"));
    assertFalse(commandLine.hasOption("optionA"));
    assertEquals("value", commandLine.getOptionValue("a"));
    assertEquals(1, commandLine.getOptions().length);
    assertEquals(1, commandLine.getArgs().length);
    assertEquals("nonOption", commandLine.getArgs()[0]);
  }

  @Test(expected = UnrecognizedOptionException.class)
  public void testParse_UnrecognizedOption() {
    String[] arguments = {"-c"};
    parser.parse(options, arguments);
  }

  @Test
  public void testParse_Properties() {
    Option option = new Option("a", "optionA", false, "Option A");
    options.addOption(option);

    Properties properties = new Properties();
    properties.setProperty("a", "");
    properties.setProperty("b", "");

    String[] arguments = {};
    CommandLine commandLine = parser.parse(options, arguments, properties);
    assertTrue(commandLine.hasOption("a"));
    assertFalse(commandLine.hasOption("optionA"));
    assertEquals(1, commandLine.getOptions().length);
    assertEquals(0, commandLine.getArgs().length);
  }

  @Test(expected = MissingOptionException.class)
  public void testParse_MissingRequiredOption() {
    Option option = new Option("a", "optionA", true, "Option A");
    option.setRequired(true);
    options.addOption(option);

    String[] arguments = {};
    parser.parse(options, arguments);
  }

}