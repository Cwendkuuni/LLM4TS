
import org.junit.Test;
import org.junit.Assert;

public class ParserTest {
  
  @Test
  public void testParse() {
    // Test Case 1: Null options and arguments
    Options options = null;
    String[] arguments = null;
    String[] expected = new String[0];
    CommandLineParser parser = new Parser();
    try {
      CommandLine result = parser.parse(options, arguments);
      Assert.assertEquals(expected, result.getArgs());
    } catch (ParseException e) {
      Assert.fail("Exception thrown: " + e.getMessage());
    }
    
    // Test Case 2: Null properties
    options = new Options();
    arguments = new String[0];
    expected = new String[0];
    properties = null;
    try {
      CommandLine result = parser.parse(options, arguments, properties);
      Assert.assertEquals(expected, result.getArgs());
    } catch (ParseException e) {
      Assert.fail("Exception thrown: " + e.getMessage());
    }
    
    // Test Case 3: Stop at non-option
    options = new Options();
    Option option = new Option("a");
    option.setArgName("arg");
    option.setArgs(1);
    options.addOption(option);
    arguments = new String[]{"-a", "value1", "non-option", "-a", "value2"};
    expected = new String[]{"non-option"};
    try {
      CommandLine result = parser.parse(options, arguments, true);
      Assert.assertEquals(expected, result.getArgs());
      Assert.assertEquals("value1", result.getOptionValue("a"));
    } catch (ParseException e) {
      Assert.fail("Exception thrown: " + e.getMessage());
    }
    
    // Test Case 4: Parse with properties
    options = new Options();
    option = new Option("a");
    option.setArgName("arg");
    option.setArgs(1);
    options.addOption(option);
    arguments = new String[]{"-a", "value1"};
    properties = new Properties();
    properties.put("a", "value2");
    try {
      CommandLine result = parser.parse(options, arguments, properties);
      Assert.assertEquals("value1", result.getOptionValue("a"));
      Assert.assertEquals("value2", result.getOptionValue("a"));
    } catch (ParseException e) {
      Assert.fail("Exception thrown: " + e.getMessage());
    }
    
    
    // Test Case 5: Missing required options
    options = new Options();
    option = new Option("a");
    options.addOption(option);
    arguments = new String[]{"-b"};
    boolean exceptionThrown = false;
    try {
      CommandLine result = parser.parse(options, arguments);
    } catch (MissingOptionException e) {
      exceptionThrown = true;
    } catch (ParseException e) {
      Assert.fail("Wrong exception thrown: " + e.getMessage());
    }
    Assert.assertTrue("MissingOptionException not thrown", exceptionThrown);
    
  }
}
