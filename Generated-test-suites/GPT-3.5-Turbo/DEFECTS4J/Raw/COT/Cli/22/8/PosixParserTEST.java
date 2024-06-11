
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
  
  @Test
  public void testFlatten_StopAtNonOption() {
    Options options = new Options();
    options.addOption("a", false, "Option a");
    options.addOption("b", true, "Option b with argument");
    options.addOption("c", false, "Option c");
    
    String[] arguments = new String[]{"-a", "-b", "value", "--", "-c"};
    
    PosixParser posixParser = new PosixParser();
    String[] result = posixParser.flatten(options, arguments, true);
    
    assertEquals(3, result.length);
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("value", result[2]);
  }
  
  @Test
  public void testFlatten_NotStopAtNonOption() {
    Options options = new Options();
    options.addOption("a", false, "Option a");
    options.addOption("b", true, "Option b with argument");
    options.addOption("c", false, "Option c");
    
    String[] arguments = new String[]{"-a", "-b", "value", "--", "-c"};
    
    PosixParser posixParser = new PosixParser();
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(5, result.length);
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("value", result[2]);
    assertEquals("--", result[3]);
    assertEquals("-c", result[4]);
  }
  
  @Test
  public void testFlatten_NoOptions() {
    Options options = new Options();
    
    String[] arguments = new String[]{"-a", "-b", "value", "--", "-c"};
    
    PosixParser posixParser = new PosixParser();
    String[] result = posixParser.flatten(options, arguments, true);
    
    assertEquals(5, result.length);
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("value", result[2]);
    assertEquals("--", result[3]);
    assertEquals("-c", result[4]);
  }
}
