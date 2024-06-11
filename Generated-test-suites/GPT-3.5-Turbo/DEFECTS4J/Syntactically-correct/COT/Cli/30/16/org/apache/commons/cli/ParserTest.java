import org.junit.Before;
import org.junit.Test;

public class ParserTest {
  private Parser parser;
  
  @Before
  public void setUp() {
    parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return new String[0];
      }
    };
  }
  
  @Test
  public void testParse_OptionsArguments() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "toggle option");
    
    String[] args = {"-a"};
    CommandLine cmd = parser.parse(options, args);
    assertTrue(cmd.hasOption("a"));
  }
  
  @Test
  public void testParse_OptionsArgumentsProperties() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "toggle option");
    Properties properties = new Properties();
    properties.setProperty("a", "true");
    
    String[] args = {"-a"};
    CommandLine cmd = parser.parse(options, args, properties);
    assertTrue(cmd.hasOption("a"));
  }
  
  @Test
  public void testParse_OptionsArgumentsStopAtNonOption() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "toggle option");
    
    String[] args = {"-a", "nonOption"};
    CommandLine cmd = parser.parse(options, args, true);
    assertTrue(cmd.hasOption("a"));
    assertEquals(1, cmd.getArgList().size());
    assertEquals("nonOption", cmd.getArgList().get(0));
  }
  
  @Test
  public void testParse_OptionsArgumentsStopAtNonOptionWithProperties() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "toggle option");
    Properties properties = new Properties();
    properties.setProperty("a", "true");
    
    String[] args = {"-a", "nonOption"};
    CommandLine cmd = parser.parse(options, args, properties, true);
    assertTrue(cmd.hasOption("a"));
    assertEquals(1, cmd.getArgList().size());
    assertEquals("nonOption", cmd.getArgList().get(0));
  }
  
  @Test
  public void testProcessArgs() throws ParseException {
    Option option = new Option("a", true, "option with value");
    ListIterator iter = Arrays.asList(new String[]{"value"}).listIterator();
    parser.processArgs(option, iter);
    assertEquals("value", option.getValue());
  }
  
  @Test
  public void testProcessOption_ValidOption() throws ParseException {
    Option option = new Option("a", false, "toggle option");
    ListIterator iter = Arrays.asList(new String[0]).listIterator();
    parser.processOption("a", iter);
    assertTrue(cmd.hasOption("a"));
  }
  
  @Test(expected = UnrecognizedOptionException.class)
  public void testProcessOption_InvalidOption() throws ParseException {
    Option option = new Option("a", false, "toggle option");
    ListIterator iter = Arrays.asList(new String[0]).listIterator();
    parser.processOption("b", iter);
  }
  
  @Test
  public void testProcessProperties_PropertyWithValue() throws ParseException {
    Option option = new Option("a", true, "option with value");
    options.addOption(option);
    
    Properties properties = new Properties();
    properties.setProperty("a", "value");
    
    parser.processProperties(properties);
    assertTrue(cmd.hasOption("a"));
    assertEquals("value", option.getValue());
  }
  
  @Test
  public void testProcessProperties_PropertyWithoutValue() throws ParseException {
    Option option = new Option("a", true, "option with value");
    options.addOption(option);
    
    Properties properties = new Properties();
    properties.setProperty("a", "");
    
    parser.processProperties(properties);
    assertTrue(cmd.hasOption("a"));
    assertNull(option.getValue());
  }
  
  @Test
  public void testCheckRequiredOptions_AllRequiredOptionsPresent() throws MissingOptionException {
    Option option1 = new Option("a", false, "toggle option");
    Option option2 = new Option("b", false, "toggle option");
    options.addOption(option1);
    options.addOption(option2);
    List requiredOptions = new ArrayList();
    requiredOptions.add("a");
    requiredOptions.add("b");
    parser.setOptions(options);
    parser.setRequiredOptions(requiredOptions);
    
    parser.checkRequiredOptions();
  }
  
  @Test(expected = MissingOptionException.class)
  public void testCheckRequiredOptions_MissingRequiredOptions() throws MissingOptionException {
    Option option1 = new Option("a", false, "toggle option");
    Option option2 = new Option("b", false, "toggle option");
    options.addOption(option1);
    options.addOption(option2);
    List requiredOptions = new ArrayList();
    requiredOptions.add("a");
    requiredOptions.add("b");
    parser.setOptions(options);
    parser.setRequiredOptions(requiredOptions);
    cmd = new CommandLine();
    
    parser.checkRequiredOptions();
  }
}