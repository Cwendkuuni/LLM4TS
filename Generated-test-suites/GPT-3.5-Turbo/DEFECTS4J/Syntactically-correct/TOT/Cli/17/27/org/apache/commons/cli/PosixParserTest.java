import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

  private PosixParser posixParser;

  @Before
  public void setUp() {
    posixParser = new PosixParser();
  }

  @Test
  public void testFlatten_NoOptions_ReturnsEmptyArray() {
    Options options = new Options();
    String[] arguments = new String[]{};
    String[] result = posixParser.flatten(options, arguments, false);
    assertEquals(0, result.length);
  }

  @Test
  public void testFlatten_SingleOption_ReturnsSingleOption() {
    Options options = new Options();
    Option option = new Option("a", null);
    options.addOption(option);
    String[] arguments = new String[]{"-a"};
    String[] result = posixParser.flatten(options, arguments, false);
    assertEquals(1, result.length);
    assertEquals("-a", result[0]);
  }

  @Test
  public void testFlatten_SingleOptionWithArg_ReturnsOptionAndArgument() {
    Options options = new Options();
    Option option = Option.builder("a").hasArg().build();
    options.addOption(option);
    String[] arguments = new String[]{"-a", "argument"};
    String[] result = posixParser.flatten(options, arguments, false);
    assertEquals(2, result.length);
    assertEquals("-a", result[0]);
    assertEquals("argument", result[1]);
  }

  @Test
  public void testFlatten_StopAtNonOption_ReturnsNonOptionArguments() {
    Options options = new Options();
    String[] arguments = new String[]{"-a", "argument", "nonOption"};
    String[] result = posixParser.flatten(options, arguments, true);
    assertEquals(2, result.length);
    assertEquals("-a", result[0]);
    assertEquals("argument", result[1]);
  }

  @Test
  public void testFlatten_LongOptionWithArg_ReturnsOptionAndArgument() {
    Options options = new Options();
    Option option = Option.builder().longOpt("option").hasArg().build();
    options.addOption(option);
    String[] arguments = new String[]{"--option", "value"};
    String[] result = posixParser.flatten(options, arguments, false);
    assertEquals(2, result.length);
    assertEquals("--option", result[0]);
    assertEquals("value", result[1]);
  }

  @Test
  public void testFlatten_LongOptionWithValueContainingEqual_ReturnsOptionAndValue() {
    Options options = new Options();
    Option option = Option.builder().longOpt("option").hasArg().build();
    options.addOption(option);
    String[] arguments = new String[]{"--option=value"};
    String[] result = posixParser.flatten(options, arguments, false);
    assertEquals(2, result.length);
    assertEquals("--option", result[0]);
    assertEquals("value", result[1]);
  }

  @Test
  public void testFlatten_MultipleOptions_ReturnsAllOptions() {
    Options options = new Options();
    Option option1 = new Option("a", null);
    Option option2 = Option.builder().longOpt("option").hasArg().build();
    options.addOption(option1);
    options.addOption(option2);
    String[] arguments = new String[]{"-a", "--option", "value"};
    String[] result = posixParser.flatten(options, arguments, false);
    assertEquals(3, result.length);
    assertEquals("-a", result[0]);
    assertEquals("--option", result[1]);
    assertEquals("value", result[2]);
  }

  @Test
  public void testFlatten_InvalidOption_StopAtNonOption_ReturnsNonOptionArguments() {
    Options options = new Options();
    String[] arguments = new String[]{"-a", "-b", "nonOption"};
    String[] result = posixParser.flatten(options, arguments, true);
    assertEquals(2, result.length);
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
  }

  @Test
  public void testFlatten_LongOptionWithInvalidArg_StopAtNonOption_ReturnsNonOptionArguments() {
    Options options = new Options();
    String[] arguments = new String[]{"--option", "nonOption"};
    String[] result = posixParser.flatten(options, arguments, true);
    assertEquals(2, result.length);
    assertEquals("--option", result[0]);
    assertEquals("nonOption", result[1]);
  }

}