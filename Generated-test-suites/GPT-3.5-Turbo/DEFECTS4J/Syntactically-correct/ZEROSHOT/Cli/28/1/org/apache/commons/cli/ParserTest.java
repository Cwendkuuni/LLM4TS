import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class ParserTest {
  
  private Parser parser;
  private Options options;
  
  @Before
  public void setUp() {
    parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return null;
      }
    };
    
    options = new Options();
    options.addOption("a", "optionA", false, "Option A");
    options.addOption("b", "optionB", true, "Option B");
    parser.setOptions(options);
  }
  
  @After
  public void tearDown() {
    parser = null;
    options = null;
  }
  
  @Test
  public void testParseNoArguments() throws ParseException {
    CommandLine cmd = parser.parse(options, new String[0]);
    assertNotNull(cmd);
    assertFalse(cmd.hasOptions());
  }
  
  @Test
  public void testParseSingleOption() throws ParseException {
    CommandLine cmd = parser.parse(options, new String[] {"-a"});
    assertNotNull(cmd);
    assertTrue(cmd.hasOption("a"));
  }
  
  @Test
  public void testParseOptionWithValue() throws ParseException {
    CommandLine cmd = parser.parse(options, new String[] {"-b", "value"});
    assertNotNull(cmd);
    assertTrue(cmd.hasOption("b"));
    assertEquals("value", cmd.getOptionValue("b"));
  }
  
  @Test(expected = UnrecognizedOptionException.class)
  public void testParseInvalidOption() throws ParseException {
    parser.parse(options, new String[] {"-c"});
  }
  
  @Test(expected = MissingArgumentException.class)
  public void testParseOptionMissingValue() throws ParseException {
    parser.parse(options, new String[] {"-b"});
  }
  
  @Test(expected = MissingOptionException.class)
  public void testParseMissingRequiredOption() throws ParseException {
    Option requiredOption = new Option("r", "required", true, "Required Option");
    requiredOption.setRequired(true);
    options.addOption(requiredOption);
    
    parser.parse(options, new String[0]);
  }
  
}