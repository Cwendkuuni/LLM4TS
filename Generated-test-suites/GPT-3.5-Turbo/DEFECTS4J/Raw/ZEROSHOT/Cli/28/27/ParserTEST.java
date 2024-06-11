import org.junit.Test;
import org.junit.Assert;
import java.util.Properties;

public class ParserTest {
  
  @Test
  public void testParse() throws ParseException {
    Options options = new Options();
    Option option = new Option("h", "help", false, "Print help message");
    options.addOption(option);
    
    String[] arguments = {"-h"};

    Parser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, arguments);

    Assert.assertTrue(cmd.hasOption("h"));
  }
  
  @Test
  public void testParseWithProperties() throws ParseException {
    Options options = new Options();
    Option option = new Option("n", "name", true, "Name");
    options.addOption(option);
    
    Properties properties = new Properties();
    properties.setProperty("name", "John");

    String[] arguments = {};

    Parser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, arguments, properties);

    Assert.assertEquals("John", cmd.getOptionValue("name"));
  }
  
  @Test(expected=MissingOptionException.class)
  public void testParseWithMissingOption() throws ParseException {
    Options options = new Options();
    Option option = new Option("n", "name", true, "Name");
    option.setRequired(true);
    options.addOption(option);
    
    String[] arguments = {};

    Parser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, arguments);
  }
  
  @Test
  public void testParseStopAtNonOption() throws ParseException {
    Options options = new Options();
    Option option = new Option("n", "name", true, "Name");
    options.addOption(option);
    
    String[] arguments = {"-n", "John", "non-option"};

    Parser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, arguments, true);

    Assert.assertEquals("John", cmd.getOptionValue("name"));
    Assert.assertEquals("non-option", cmd.getArgs()[0]);
  }
  
  @Test(expected=UnrecognizedOptionException.class)
  public void testParseWithUnrecognizedOption() throws ParseException {
    Options options = new Options();
    Option option = new Option("h", "help", false, "Print help message");
    options.addOption(option);
    
    String[] arguments = {"-n"};

    Parser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, arguments);
  }
  
}