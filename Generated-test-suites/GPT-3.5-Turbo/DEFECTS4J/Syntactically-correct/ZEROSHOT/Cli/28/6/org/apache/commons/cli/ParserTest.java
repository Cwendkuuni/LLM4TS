import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testParse() {
    Options options = new Options();
    options.addOption("a", false, "Option A");
    String[] arguments = new String[]{"-a"};
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = parser.parse(options, arguments);
      assertTrue(cmd.hasOption("a"));
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testProcessProperties() {
    Options options = new Options();
    options.addOption("a", true, "Option A");
    Properties properties = new Properties();
    properties.setProperty("a", "valueA");
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = parser.parse(options, null, properties);
      assertTrue(cmd.hasOption("a"));
      assertEquals("valueA", cmd.getOptionValue("a"));
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCheckRequiredOptions() {
    Options options = new Options();
    options.addOption(Option.builder("a").required().build());
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };
    
    try {
      parser.parse(options, new String[0]);
      fail("MissingOptionException not thrown");
    } catch (MissingOptionException e) {
      assertEquals(1, e.getMissingOptions().size());
      assertEquals("a", e.getMissingOptions().get(0));
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testProcessArgs() {
    Option option = Option.builder("a").argName("arg").hasArg().build();
    ListIterator iterator = Arrays.asList("-a", "valueA").listIterator();
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };
    
    try {
      parser.processArgs(option, iterator);
      assertEquals(1, option.getValuesList().size());
      assertEquals("valueA", option.getValue());
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testProcessOption() {
    Options options = new Options();
    options.addOption("a", true, "Option A");
    ListIterator iterator = Arrays.asList("-a", "valueA").listIterator();
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
      }
    };
    
    try {
      CommandLine cmd = new CommandLine();
      parser.processOption("-a", iterator);
      assertEquals("valueA", cmd.getOptionValue("a"));
    } catch (ParseException e) {
      fail("ParseException thrown: " + e.getMessage());
    }
  }
}