import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testSetOptions() {
    Options options = new Options();
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return null;
      }
    };
    
    parser.setOptions(options);
    assertSame(options, parser.getOptions());
    assertSame(options.getRequiredOptions(), parser.getRequiredOptions());
   
  }

  @Test
  public void testParse() {
    Options options = new Options();
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return null;
      }
    };
    
    try {
      parser.parse(options, null);
    } catch (ParseException e) {
      fail("Unexpected ParseException");
    }
  }

  @Test
  public void testProcessProperties() {
    Options options = new Options();
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return null;
      }
    };
    
    Properties properties = new Properties();
    properties.setProperty("option1", "value1");
    parser.processProperties(properties);
    assertEquals(properties.getProperty("option1"), parser.getCommandLine().getOptionValue("option1"));
  }

  @Test
  public void testCheckRequiredOptions() {
    Options options = new Options();
    Option option1 = new Option("option1", "description");
    Option option2 = new Option("option2", "description");
    options.addOption(option1);
    options.addOption(option2);
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return null;
      }
    };
    
    try {
      parser.parse(options, null);
      fail("MissingOptionException not thrown.");
    } catch (MissingOptionException e) {
      assertEquals("Missing required option: option1, option2", e.getMessage());
    } catch (ParseException e) {
      fail("Unexpected ParseException");
    }
  }

  @Test
  public void testProcessArgs() {
    Options options = new Options();
    Option option = new Option("option1", "description");
    option.setArgs(2);
    options.addOption(option);
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return null;
      }
    };
    
    ListIterator iter = Arrays.asList(new String[] {"arg1", "arg2"}).listIterator();
    try {
      parser.processArgs(option, iter);
      assertEquals("arg1", option.getValue(0));
      assertEquals("arg2", option.getValue(1));
    } catch (ParseException e) {
      fail("Unexpected ParseException");
    }
  }

  @Test
  public void testProcessOption() {
    Options options = new Options();
    Option option = new Option("option1", "description");
    options.addOption(option);
    
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return null;
      }
    };
    
    ListIterator iter = Arrays.asList(new String[] {"arg1"}).listIterator();
    try {
      parser.processOption("option1", iter);
      assertTrue(option.hasArg());
      assertEquals("arg1", option.getValue());
      assertTrue(parser.getCommandLine().hasOption("option1"));
      assertSame(option, parser.getCommandLine().getOption("option1"));
    } catch (ParseException e) {
      fail("Unexpected ParseException");
    }
  }
  
}