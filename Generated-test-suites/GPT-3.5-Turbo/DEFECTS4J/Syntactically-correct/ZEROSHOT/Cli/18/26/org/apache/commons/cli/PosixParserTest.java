import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {
  
  private PosixParser parser;
  
  @Before
  public void setUp() {
    parser = new PosixParser();
  }
  
  @After
  public void tearDown() {
    parser = null;
  }
  
  @Test
  public void testFlatten() {
    String[] arguments = {"-a", "-b", "value", "--option", "arg1", "--option=arg2"};
    Options options = new Options();
    String[] result = parser.flatten(options, arguments, true);
    assertEquals(6, result.length);
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("value", result[2]);
    assertEquals("--option", result[3]);
    assertEquals("arg1", result[4]);
    assertEquals("--option=arg2", result[5]);
  }
  
  @Test
  public void testFlatten_stopAtNonOptionFalse() {
      String[] arguments = {"-a", "-b", "value", "--option", "arg1", "--option=arg2"};
      Options options = new Options();
      String[] result = parser.flatten(options, arguments, false);
      assertEquals(6, result.length);
      assertEquals("-a", result[0]);
      assertEquals("-b", result[1]);
      assertEquals("value", result[2]);
      assertEquals("--option", result[3]);
      assertEquals("arg1", result[4]);
      assertEquals("--option=arg2", result[5]);
  }
  
  @Test
  public void testFlatten_emptyArguments() {
      String[] arguments = {};
      Options options = new Options();
      String[] result = parser.flatten(options, arguments, true);
      assertEquals(0, result.length);
  }
  
  @Test
  public void testFlatten_singleHyphen() {
      String[] arguments = {"-"};
      Options options = new Options();
      String[] result = parser.flatten(options, arguments, true);
      assertEquals(1, result.length);
      assertEquals("-", result[0]);
  }
  
  @Test
  public void testFlatten_optionDoesNotExist() {
      String[] arguments = {"-c"};
      Options options = new Options();
      String[] result = parser.flatten(options, arguments, true);
      assertEquals(1, result.length);
      assertEquals("-c", result[0]);
  }
  
  @Test
  public void testFlatten_optionWithArgument() {
      String[] arguments = {"-a", "value"};
      Options options = new Options();
      options.addOption("a", true, "option a");
      String[] result = parser.flatten(options, arguments, true);
      assertEquals(2, result.length);
      assertEquals("-a", result[0]);
      assertEquals("value", result[1]);
  }
  
  @Test
  public void testFlatten_optionWithArguments() {
      String[] arguments = {"-b", "arg1", "arg2"};
      Options options = new Options();
      options.addOption("b", true, "option b");
      String[] result = parser.flatten(options, arguments, true);
      assertEquals(3, result.length);
      assertEquals("-b", result[0]);
      assertEquals("arg1", result[1]);
      assertEquals("arg2", result[2]);
  }
}