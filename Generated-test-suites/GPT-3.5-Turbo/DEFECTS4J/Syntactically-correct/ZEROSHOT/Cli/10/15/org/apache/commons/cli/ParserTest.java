import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
  
  @Test
  public void testParseWithoutProperties() {
    Options options = new Options();
    Option option1 = new Option("h", "help", false, "display help message");
    Option option2 = new Option("v", "version", false, "display version information");
    options.addOption(option1);
    options.addOption(option2);
    
    String[] arguments = {"-h"};
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = parser.parse(options, arguments);
      
      assertTrue(cmd.hasOption("h"));
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseWithProperties() {
    Options options = new Options();
    Option option1 = new Option("d", "debug", false, "debug mode");
    Option option2 = new Option("c", "config", true, "config file path");
    options.addOption(option1);
    options.addOption(option2);
    
    String[] arguments = {"-d"};
    
    Properties properties = new Properties();
    properties.setProperty("config", "/path/to/config");
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = parser.parse(options, arguments, properties);
      
      assertTrue(cmd.hasOption("d"));
      assertTrue(cmd.hasOption("c"));
      assertEquals("/path/to/config", cmd.getOptionValue("c"));
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseWithStopAtNonOption() {
    Options options = new Options();
    Option option1 = new Option("f", "file", true, "input file");
    Option option2 = new Option("o", "output", true, "output file");
    options.addOption(option1);
    options.addOption(option2);
    
    String[] arguments = {"-f", "input.txt", "--", "-o", "output.txt"};
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = parser.parse(options, arguments, true);
      
      assertTrue(cmd.hasOption("f"));
      assertEquals("input.txt", cmd.getOptionValue("f"));
      assertFalse(cmd.hasOption("o"));
      assertTrue(cmd.getArgList().contains("-o"));
      assertTrue(cmd.getArgList().contains("output.txt"));
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseMissingRequiredOption() {
    Options options = new Options();
    Option option1 = new Option("f", "file", true, "input file");
    Option option2 = new Option("o", "output", true, "output file");
    option1.setRequired(true);
    options.addOption(option1);
    options.addOption(option2);
    
    String[] arguments = {"-o", "output.txt"};
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = parser.parse(options, arguments);
      
      fail("MissingOptionException not thrown");
    } catch (MissingOptionException e) {
      assertEquals("Missing required option: f", e.getMessage());
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseUnrecognizedOption() {
    Options options = new Options();
    Option option1 = new Option("f", "file", true, "input file");
    Option option2 = new Option("o", "output", true, "output file");
    options.addOption(option1);
    options.addOption(option2);
    
    String[] arguments = {"-f", "input.txt", "-x"};
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = parser.parse(options, arguments);
      
      fail("UnrecognizedOptionException not thrown");
    } catch (UnrecognizedOptionException e) {
      assertEquals("Unrecognized option: -x", e.getMessage());
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseInvalidOptionValue() {
    Options options = new Options();
    Option option1 = new Option("n", "number", true, "a number");
    options.addOption(option1);
    
    String[] arguments = {"-n", "one"};
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = parser.parse(options, arguments);
      
      fail("ParseException not thrown");
    } catch (ParseException e) {
      assertEquals("Unable to add value 'one' for option: n", e.getMessage());
    }
  }
}