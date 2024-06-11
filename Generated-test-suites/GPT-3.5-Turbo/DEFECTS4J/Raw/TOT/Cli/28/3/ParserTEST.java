import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testFlatten() {
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[] {"-a", "-b", "value"};
      }
    };
    
    String[] arguments = {"-a", "-b", "value"};
    String[] flattenedArguments = parser.flatten(options, arguments, false);
    
    assertEquals("-a", flattenedArguments[0]);
    assertEquals("-b", flattenedArguments[1]);
    assertEquals("value", flattenedArguments[2]);
  }

  @Test
  public void testProcessProperties() {
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    
    final Properties properties = new Properties();
    properties.setProperty("a", "true");
    properties.setProperty("b", "value");
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[] {};
      }
    };
    
    CommandLine commandLine = parser.parse(options, new String[0], properties);
    assertTrue(commandLine.hasOption("a"));
    assertTrue(commandLine.hasOption("b"));
    assertEquals("value", commandLine.getOptionValue("b"));
  }

  @Test
  public void testProcessArgs() {
    Option option = new Option("a", false, "option a");
    ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[] {};
      }
    };
    
    try {
      parser.processArgs(option, iterator);
    } catch (ParseException e) {
      fail("Exception thrown during processArgs: " + e.getMessage());
    }
    
    assertEquals("value1", option.getValue(0));
    assertEquals("value2", option.getValue(1));
  }

  @Test
  public void testProcessOption() {
    Option option = new Option("a", false, "option a");
    ListIterator<String> iterator = Arrays.asList("value1", "b", "value2").listIterator();
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[] {};
      }
    };
    
    try {
      parser.processOption("b", iterator);
    } catch (ParseException e) {
      fail("Exception thrown during processOption: " + e.getMessage());
    }
    
    assertFalse(option.hasValue());
    assertEquals("b", option.getValue());
    assertEquals("value1", option.getArg(0));
    assertEquals("value2", option.getArg(1));
  }

  @Test
  public void testParse() {
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[] {};
      }
    };
    
    try {
      CommandLine commandLine = parser.parse(options, new String[] {"-a", "-b", "value"});
      
      assertTrue(commandLine.hasOption("a"));
      assertTrue(commandLine.hasOption("b"));
      assertEquals("value", commandLine.getOptionValue("b"));
    } catch (ParseException e) {
      fail("Exception thrown during parse: " + e.getMessage());
    }
  }

  @Test(expected = MissingOptionException.class)
  public void testCheckRequiredOptions() throws ParseException {
    Options options = new Options();
    options.addRequiredOption("a", false, "option a");
    options.addRequiredOption("b", true, "option b");
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[] {};
      }
    };
    
    parser.parse(options, new String[0]);
  }

}