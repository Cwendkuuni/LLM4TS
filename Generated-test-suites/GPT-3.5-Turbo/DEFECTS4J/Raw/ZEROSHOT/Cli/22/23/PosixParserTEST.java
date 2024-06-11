import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;

public class PosixParserTest {
  
  private PosixParser parser;
  private Options options;
  
  @Before
  public void setUp() {
    parser = new PosixParser();
    options = new Options();
  }
  
  @Test
  public void testFlatten_noOptions() {
    String[] arguments = new String[] {};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(arguments, result);
  }
  
  @Test
  public void testFlatten_singleOption_noArg() {
    options.addOption("a", false, "");
    String[] arguments = new String[] {"-a"};
    String[] expected = new String[] {"-a"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_singleOption_withArg() {
    options.addOption("b", true, "");
    String[] arguments = new String[] {"-b", "arg"};
    String[] expected = new String[] {"-b", "arg"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_multipleOptions_withArgs() {
    options.addOption("c", true, "");
    options.addOption("d", true, "");
    String[] arguments = new String[] {"-c", "arg1", "-d", "arg2"};
    String[] expected = new String[] {"-c", "arg1", "-d", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_stopAtNonOption() {
    options.addOption("e", true, "");
    options.addOption("f", true, "");
    String[] arguments = new String[] {"-e", "arg1", "nonOption", "-f", "arg2"};
    String[] expected = new String[] {"-e", "arg1", "--", "nonOption", "-f", "arg2"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_burstToken() {
    options.addOption("g", true, "");
    options.addOption("h", false, "");
    String[] arguments = new String[] {"-gharg"};
    String[] expected = new String[] {"-g", "harg"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_singleHyphen() {
    options.addOption("i", true, "");
    String[] arguments = new String[] {"-"};
    String[] expected = new String[] {"-"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_longOption_noArg() {
    options.addOption("j", true, "");
    options.addOption("k", true, "");
    String[] arguments = new String[] {"--j"};
    String[] expected = new String[] {"--j"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_longOption_withArg() {
    options.addOption("l", true, "");
    options.addOption("m", true, "");
    String[] arguments = new String[] {"--l=arg"};
    String[] expected = new String[] {"--l", "arg"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_invalidOption_stopAtNonOption() {
    options.addOption("n", false, "");
    options.addOption("o", false, "");
    String[] arguments = new String[] {"-n", "arg1", "invalid", "-o", "arg2"};
    String[] expected = new String[] {"-n", "arg1", "--", "invalid", "-o", "arg2"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_invalidOption_noStopAtNonOption() {
    options.addOption("p", false, "");
    options.addOption("q", false, "");
    String[] arguments = new String[] {"-p", "arg1", "invalid", "-q", "arg2"};
    String[] expected = new String[] {"-p", "arg1", "-invalid", "-q", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }

}