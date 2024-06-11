import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.Properties;
import org.apache.commons.cli.UnrecognizedOptionException;

public class ParserTest {

  private Parser parser;
  private Options options;
  private CommandLine cmd;

  @Before
  public void setUp() {
    parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return null;
      }
    };
    options = new Options();
    cmd = new CommandLine();
  }
  
  @Test
  public void testSetOptions() {
    Options testOptions = new Options();
    parser.setOptions(testOptions);
    
    assertEquals(testOptions, parser.getOptions());
    assertEquals(testOptions.getRequiredOptions(), parser.getRequiredOptions());
  }
  
  @Test
  public void testParse() throws ParseException {
    Option option = new Option("t", "test", false, "test option");
    options.addOption(option);
    
    String[] arguments = new String[]{"-t"};
    cmd = parser.parse(options, arguments);
    
    assertTrue(cmd.hasOption("t"));
    assertFalse(cmd.hasOption("k"));
  }
  
  @Test(expected = MissingOptionException.class)
  public void testCheckRequiredOptions() throws MissingOptionException {
    Option option = new Option("t", "test", true, "required option");
    options.addOption(option);
    
    parser.checkRequiredOptions();
  }
  
  @Test
  public void testProcessArgs() throws ParseException {
    Option option = Option.builder("t").hasArg(true).build();
    options.addOption(option);
    ListIterator iterator = Arrays.asList(new String[] {"value1", "value2"}).listIterator();
    
    parser.processArgs(option, iterator);
    
    assertEquals(2, option.getValues().length);
    assertEquals("value1", option.getValue(0));
    assertEquals("value2", option.getValue(1));
  }
  
  @Test(expected = MissingArgumentException.class)
  public void testProcessArgsMissingArg() throws ParseException {
    Option option = Option.builder("t").hasArg(true).build();
    options.addOption(option);
    ListIterator iterator = Arrays.asList(new String[] {}).listIterator();
    
    parser.processArgs(option, iterator);
  }
  
  @Test
  public void testProcessOption() throws ParseException {
    Option option = new Option("t", "test", false, "test option");
    options.addOption(option);
    ListIterator iterator = Arrays.asList(new String[] {}).listIterator();
    
    parser.processOption("-t", iterator);
    
    assertTrue(cmd.hasOption("t"));
  }
  
  @Test(expected = UnrecognizedOptionException.class)
  public void testProcessOptionUnrecognizedOption() throws ParseException {
    Option option = new Option("t", "test", false, "test option");
    options.addOption(option);
    ListIterator iterator = Arrays.asList(new String[] {}).listIterator();
    
    parser.processOption("-k", iterator);
  }
}