import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

public class ParserTest {

  private Parser parser;

  @Before
  public void setUp() {
    parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return null;
      }

      protected void processArgs(Option opt, ListIterator iter) throws ParseException {

      }

      protected void processOption(String arg, ListIterator iter) throws ParseException {

      }
    };
  }

  @After
  public void tearDown() {
    parser = null;
  }

  @Test
  public void testParse_OptionsStringArray() {
    Options options = new Options();
    String[] arguments = {};
    CommandLine result = null;
    try {
      result = parser.parse(options, arguments);
      fail("Expected ParseException to be thrown");
    } catch (ParseException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testParse_OptionsStringArrayProperties() {
    Options options = new Options();
    String[] arguments = {};
    Properties properties = new Properties();
    CommandLine result = null;
    try {
      result = parser.parse(options, arguments, properties);
      fail("Expected ParseException to be thrown");
    } catch (ParseException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testParse_OptionsStringArrayBoolean() {
    Options options = new Options();
    String[] arguments = {};
    boolean stopAtNonOption = false;
    CommandLine result = null;
    try {
      result = parser.parse(options, arguments, stopAtNonOption);
      fail("Expected ParseException to be thrown");
    } catch (ParseException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testParse_OptionsStringArrayPropertiesBoolean() {
    Options options = new Options();
    String[] arguments = {};
    Properties properties = new Properties();
    boolean stopAtNonOption = false;
    CommandLine result = null;
    try {
      result = parser.parse(options, arguments, properties, stopAtNonOption);
      fail("Expected ParseException to be thrown");
    } catch (ParseException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testProcessArgs_OptionListIterator() {
    Option opt = new Option("test", true, "test option");
    ListIterator iter = null;
    try {
      parser.processArgs(opt, iter);
      fail("Expected MissingArgumentException to be thrown");
    } catch (ParseException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testProcessOption_StringListIterator() {
    String arg = "test";
    ListIterator iter = null;
    try {
      parser.processOption(arg, iter);
      fail("Expected UnrecognizedOptionException to be thrown");
    } catch (ParseException e) {
      assertTrue(true);
    }
  }

}