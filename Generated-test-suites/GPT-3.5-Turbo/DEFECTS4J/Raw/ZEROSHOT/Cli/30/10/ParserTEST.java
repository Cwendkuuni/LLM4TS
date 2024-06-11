import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import java.util.Properties;
import static org.junit.Assert.*;

public class ParserTest {
  
  private Parser parser;
  private Options options;
  
  @Before
  public void setUp() {
    parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    options = new Options();
  }
  
  @Test
  public void testParse_NoOptions_ReturnsEmptyCommandLine() throws ParseException {
    String[] args = {};
    CommandLine cmd = parser.parse(options, args);
    assertTrue(cmd.getOptions().isEmpty());
    assertTrue(cmd.getArgs().isEmpty());
  }
  
  @Test
  public void testParse_SingleOption_ReturnsCommandLineWithOption() throws ParseException {
    options.addOption("a", false, "A test option");
    String[] args = {"-a"};
    CommandLine cmd = parser.parse(options, args);
    assertTrue(cmd.hasOption("a"));
    assertFalse(cmd.hasOptions());
    assertTrue(cmd.getArgs().isEmpty());
  }
  
  @Test
  public void testParse_MultipleOptions_ReturnsCommandLineWithAllOptions() throws ParseException {
    options.addOption("a", false, "A test option");
    options.addOption("b", true, "Another test option");
    String[] args = {"-a", "-b", "value"};
    CommandLine cmd = parser.parse(options, args);
    assertTrue(cmd.hasOption("a"));
    assertTrue(cmd.hasOption("b"));
    assertEquals("value", cmd.getOptionValue("b"));
    assertFalse(cmd.hasOptions());
    assertTrue(cmd.getArgs().isEmpty());
  }
  
  @Test
  public void testParse_Argument_ReturnsCommandLineWithArgument() throws ParseException {
    String[] args = {"argument"};
    CommandLine cmd = parser.parse(options, args);
    assertTrue(cmd.getOptions().isEmpty());
    assertEquals(1, cmd.getArgs().size());
    assertEquals("argument", cmd.getArgs().get(0));
  }
  
  @Test
  public void testParse_StopAtNonOption_ReturnsCommandLineWithStopOptionAndRemainingArguments() throws ParseException {
    options.addOption("a", false, "A test option");
    String[] args = {"-a", "--", "argument1", "argument2"};
    CommandLine cmd = parser.parse(options, args, true);
    assertTrue(cmd.hasOption("a"));
    assertTrue(cmd.hasOption("--"));
    assertFalse(cmd.hasOptions());
    assertEquals(2, cmd.getArgs().size());
    assertEquals("argument1", cmd.getArgs().get(0));
    assertEquals("argument2", cmd.getArgs().get(1));
  }
  
  @Test(expected = MissingOptionException.class)
  public void testCheckRequiredOptions_MissingRequiredOption_ThrowsMissingOptionException() throws ParseException {
    options.addOption(Option.builder("a").required().build());
    parser.checkRequiredOptions();
  }
  
  @Test
  public void testCheckRequiredOptions_RequiredOptionPresent_NoException() throws ParseException {
    options.addOption(Option.builder("a").required().build());
    CommandLine cmd = new CommandLine();
    cmd.addOption(Option.builder("a").build());
    parser.cmd = cmd;
    parser.checkRequiredOptions();
  }
  
  @Test
  public void testParse_Properties_PropertiesProcessedSuccessfully() throws ParseException {
    options.addOption("a", true, "A test option");
    options.addOption("b", false, "Another test option");
    Properties properties = new Properties();
    properties.setProperty("a", "value");
    properties.setProperty("b", "true");
    CommandLine cmd = parser.parse(options, new String[0], properties);
    assertTrue(cmd.hasOption("a"));
    assertEquals("value", cmd.getOptionValue("a"));
    assertTrue(cmd.hasOption("b"));
    assertFalse(cmd.hasOptions());
    assertTrue(cmd.getArgs().isEmpty());
  }
  
  @Test
  public void testProcessOption_ValidOption_OptionProcessedSuccessfully() throws ParseException {
    options.addOption(Option.builder("a").hasArg().build());
    CommandLine cmd = new CommandLine();
    ListIterator iter = Arrays.asList("-a", "value").listIterator();
    parser.processOption("-a", iter);
    assertTrue(cmd.hasOption("a"));
    assertEquals("value", cmd.getOptionValue("a"));
  }
  
  @Test(expected = UnrecognizedOptionException.class)
  public void testProcessOption_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
    options.addOption("a", false, "A test option");
    ListIterator iter = Arrays.asList("-b").listIterator();
    parser.processOption("-b", iter);
  }
  
  @Test
  public void testProcessArgs_ArgumentWithOption_OptionProcessedSuccessfully() throws ParseException {
    Option option = Option.builder("a").hasArgs().build();
    ListIterator iter = Arrays.asList("value1", "value2", "-b").listIterator();
    parser.processArgs(option, iter);
    assertTrue(option.getValues().length == 2);
    assertEquals("value1", option.getValue(0));
    assertEquals("value2", option.getValue(1));
  }
  
  @Test(expected = MissingArgumentException.class)
  public void testProcessArgs_RequiredArgumentMissing_ThrowsMissingArgumentException() throws ParseException {
    Option option = Option.builder("a").hasArg().required().build();
    ListIterator iter = Arrays.asList("-a").listIterator();
    parser.processArgs(option, iter);
  }
  
  @Test
  public void testUpdateRequiredOptions_OptionIsRequired_OptionRemovedFromRequiredOptions() throws ParseException {
    Option option = Option.builder("a").required().build();
    parser.updateRequiredOptions(option);
    assertTrue(parser.getRequiredOptions().isEmpty());
  }
  
  @Test
  public void testUpdateRequiredOptions_OptionBelongsToGroup_GroupSelectedSetToOption() throws ParseException {
    Option option = Option.builder("a").longOpt("optionA").build();
    Option optionB = Option.builder("b").longOpt("optionB").build();
    OptionGroup group = new OptionGroup();
    group.addOption(option);
    group.addOption(optionB);
    options.addOptionGroup(group);
    parser.updateRequiredOptions(option);
    assertTrue(group.getSelected() == option);
  }
}