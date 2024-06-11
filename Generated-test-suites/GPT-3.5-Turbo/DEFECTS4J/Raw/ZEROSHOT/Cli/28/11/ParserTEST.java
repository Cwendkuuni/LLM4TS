import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
  
  @Test
  public void testSetOptions() {
    Parser parser = new Parser(){};
    Options options = new Options();
    parser.setOptions(options);
    assertEquals(options, parser.getOptions());
    assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
  }
  
  @Test
  public void testFlatten() {
    Parser parser = new Parser(){};
    Options options = new Options();
    String[] arguments = new String[]{"arg1", "arg2", "arg3"};
    boolean stopAtNonOption = false;
    try {
      parser.flatten(options, arguments, stopAtNonOption);
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }
  
  @Test
  public void testParseWithOptionsAndArguments() {
    Parser parser = new Parser(){};
    Options options = new Options();
    String[] arguments = new String[]{"arg1", "arg2", "arg3"};
    try {
      parser.parse(options, arguments);
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }
  
  @Test
  public void testParseWithOptionsArgumentsAndProperties() {
    Parser parser = new Parser(){};
    Options options = new Options();
    String[] arguments = new String[]{"arg1", "arg2", "arg3"};
    Properties properties = new Properties();
    try {
      parser.parse(options, arguments, properties);
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }
  
  @Test
  public void testParseWithOptionsArgumentsAndStopAtNonOption() {
    Parser parser = new Parser(){};
    Options options = new Options();
    String[] arguments = new String[]{"arg1", "arg2", "arg3"};
    boolean stopAtNonOption = false;
    try {
      parser.parse(options, arguments, stopAtNonOption);
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }
  
  @Test
  public void testParseWithOptionsArgumentsPropertiesAndStopAtNonOption() {
    Parser parser = new Parser(){};
    Options options = new Options();
    String[] arguments = new String[]{"arg1", "arg2", "arg3"};
    Properties properties = new Properties();
    boolean stopAtNonOption = false;
    try {
      parser.parse(options, arguments, properties, stopAtNonOption);
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }
  
  @Test
  public void testProcessProperties() {
    Parser parser = new Parser(){};
    Options options = new Options();
    Properties properties = new Properties();
    parser.processProperties(properties);
  }
  
  @Test
  public void testCheckRequiredOptions() {
    Parser parser = new Parser(){};
    Options options = new Options();
    try {
      parser.checkRequiredOptions();
    } catch (MissingOptionException e) {
      fail("MissingOptionException should not be thrown");
    }
  }
  
  @Test
  public void testProcessArgs() {
    Parser parser = new Parser(){};
    Option opt = new Option("opt", true, "option with value");
    ListIterator iter = Arrays.asList("value1", "value2", "value3").listIterator();
    try {
      parser.processArgs(opt, iter);
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
    assertEquals(iter.nextIndex(), 3);
  }
  
  @Test
  public void testProcessOption() {
    Parser parser = new Parser(){};
    String arg = "opt";
    ListIterator iter = Arrays.asList("arg1", "arg2", "arg3").listIterator();
    try {
      parser.processOption(arg, iter);
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
    assertEquals(iter.nextIndex(), 0);
  }
  
}