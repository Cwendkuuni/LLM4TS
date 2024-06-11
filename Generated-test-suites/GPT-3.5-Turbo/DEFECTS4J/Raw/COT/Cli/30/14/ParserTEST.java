import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
  
  @Test
  public void testFlatten() throws ParseException {
    Options options = new Options();
    options.addOption("a", "alpha", false, "alpha option");
    options.addOption("b", "beta", true, "beta option");
    
    Parser parser = new MyParser();
    String[] arguments = {"-a", "-b", "value"};
    
    String[] flattenedArguments = parser.flatten(options, arguments, false);
    
    Assert.assertArrayEquals(new String[]{"-a", "-b", "value"}, flattenedArguments);
  }
  
  @Test
  public void testParse() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "alpha option");
    options.addOption("b", true, "beta option");
    
    Parser parser = new MyParser();
    String[] arguments = {"-a", "-b", "value"};
    
    CommandLine cmd = parser.parse(options, arguments);
    
    Assert.assertTrue(cmd.hasOption("a"));
    Assert.assertTrue(cmd.hasOption("b"));
    Assert.assertEquals("value", cmd.getOptionValue("b"));
  }
  
  @Test
  public void testParseWithProperties() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "alpha option");
    options.addOption("b", true, "beta option");
    
    Properties properties = new Properties();
    properties.setProperty("a", "true");
    properties.setProperty("b", "value");
    
    Parser parser = new MyParser();
    String[] arguments = {};
    
    CommandLine cmd = parser.parse(options, arguments, properties);
    
    Assert.assertTrue(cmd.hasOption("a"));
    Assert.assertTrue(cmd.hasOption("b"));
    Assert.assertEquals("value", cmd.getOptionValue("b"));
  }
  
  @Test(expected = MissingOptionException.class)
  public void testCheckRequiredOptions() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "alpha option");
    options.addOption("b", true, "beta option");
    options.addRequiredOption("c", true, "required option");
    
    Parser parser = new MyParser();
    String[] arguments = {"-a", "-b", "value"};
    
    parser.parse(options, arguments);
  }
  
  @Test
  public void testProcessOption() throws ParseException {
    Options options = new Options();
    options.addOption("a", false, "alpha option");
    options.addOption("b", true, "beta option");
    
    Parser parser = new MyParser();
    ListIterator iterator = Arrays.asList("-a", "-b", "value").listIterator();
    
    parser.processOption("-a", iterator);
    
    CommandLine cmd = parser.getCommandLine();
    
    Assert.assertTrue(cmd.hasOption("a"));
  }
  
  @Test
  public void testProcessArgs() throws ParseException {
    Option opt = new Option("a", false, "alpha option");
    ListIterator iterator = Arrays.asList("value1", "value2").listIterator();
    
    Parser parser = new MyParser();
    parser.processArgs(opt, iterator);
    
    Assert.assertTrue(opt.hasValue());
    Assert.assertArrayEquals(new String[]{"value1", "value2"}, opt.getValues());
  }
  
  private class MyParser extends Parser {
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
      return arguments;
    }
    
    @Override
    public CommandLine getCommandLine() {
      return cmd;
    }
  }
}