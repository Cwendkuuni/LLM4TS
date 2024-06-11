import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Properties;
import static org.junit.Assert.*;

public class ParserTest {
  private Parser parser;
  private Options options;
  private String[] arguments;
  private Properties properties;

  @Before
  public void setUp() {
    parser = Mockito.mock(Parser.class);
    options = new Options();
    arguments = null;
    properties = null;
  }

  @Test
  public void testParse() throws ParseException {
    CommandLine expected = new CommandLine();
    Mockito.when(parser.parse(options, arguments)).thenReturn(expected);
    CommandLine actual = parser.parse(options, arguments);
    assertEquals(expected, actual);
  }

  @Test
  public void testParseWithOptionsAndProperties() throws ParseException {
    CommandLine expected = new CommandLine();
    Mockito.when(parser.parse(options, arguments, properties)).thenReturn(expected);
    CommandLine actual = parser.parse(options, arguments, properties);
    assertEquals(expected, actual);
  }

  @Test
  public void testParseWithStopAtNonOption() throws ParseException {
    CommandLine expected = new CommandLine();
    Mockito.when(parser.parse(options, arguments, true)).thenReturn(expected);
    CommandLine actual = parser.parse(options, arguments, true);
    assertEquals(expected, actual);
  }

  @Test
  public void testParseWithOptionsAndPropertiesAndStopAtNonOption() throws ParseException {
    CommandLine expected = new CommandLine();
    Mockito.when(parser.parse(options, arguments, properties, true)).thenReturn(expected);
    CommandLine actual = parser.parse(options, arguments, properties, true);
    assertEquals(expected, actual);
  }

  @Test
  public void testFlatten() {
    Options opts = new Options();
    String[] arguments = new String[] {"-a", "arg1", "-b", "arg2"};
    boolean stopAtNonOption = false;
    String[] expected = new String[] {"-a", "arg1", "-b", "arg2"};
    String[] actual = parser.flatten(opts, arguments, stopAtNonOption);
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testProcessProperties() {
    Properties properties = new Properties();
    Option option = new Option("a", "description");
    options.addOption(option);
    properties.setProperty("a", "value");
    parser.processProperties(properties);
    assertTrue(parser.cmd.hasOption("a"));
  }

  @Test
  public void testCheckRequiredOptions() {
    Option option = new Option("a", "description");
    option.setRequired(true);
    options.addOption(option);
    try {
      parser.checkRequiredOptions();
    } catch (MissingOptionException moe) {
      assertEquals("Missing required option: a", moe.getMessage());
    }
  }

  @Test
  public void testProcessArgs() throws ParseException {
    Option option = new Option("a", "description");
    option.setArgs(2);
    parser.options.addOption(option);
    ListIterator iter = Mockito.mock(ListIterator.class);
    Mockito.when(iter.hasNext()).thenReturn(true, true, false);
    Mockito.when(iter.next()).thenReturn("arg1", "arg2");
    parser.processArgs(option, iter);
    assertEquals(2, option.getValues().length);
  }

  @Test
  public void testProcessOption() throws ParseException {
    Option option = new Option("a", "description");
    option.setArgs(2);
    parser.options.addOption(option);
    ListIterator iter = Mockito.mock(ListIterator.class);
    Mockito.when(iter.hasNext()).thenReturn(true, true, false);
    Mockito.when(iter.next()).thenReturn("arg1", "arg2");
    parser.processOption("a", iter);
    assertTrue(parser.cmd.hasOption("a"));
    assertEquals(2, option.getValues().length);
  }
}