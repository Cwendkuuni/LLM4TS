import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

  @Test
  public void testHasOption_String() {
    CommandLine commandLine = new CommandLine();

    boolean result = commandLine.hasOption("opt");

    assertFalse(result);
  }

  @Test
  public void testHasOption_char() {
    CommandLine commandLine = new CommandLine();

    boolean result = commandLine.hasOption('o');

    assertFalse(result);
  }

  @Test
  public void testGetOptionObject_String() {
    CommandLine commandLine = new CommandLine();

    Object result = commandLine.getOptionObject("opt");

    assertNull(result);
  }

  @Test
  public void testGetOptionObject_char() {
    CommandLine commandLine = new CommandLine();

    Object result = commandLine.getOptionObject('o');

    assertNull(result);
  }

  @Test
  public void testGetOptionValue_String() {
    CommandLine commandLine = new CommandLine();

    String result = commandLine.getOptionValue("opt");

    assertNull(result);
  }

  @Test
  public void testGetOptionValue_char() {
    CommandLine commandLine = new CommandLine();

    String result = commandLine.getOptionValue('o');

    assertNull(result);
  }

  @Test
  public void testGetOptionValues_String() {
    CommandLine commandLine = new CommandLine();

    String[] result = commandLine.getOptionValues("opt");

    assertNull(result);
  }

  @Test
  public void testGetOptionValues_char() {
    CommandLine commandLine = new CommandLine();

    String[] result = commandLine.getOptionValues('o');

    assertNull(result);
  }

  @Test
  public void testGetOptionValue_String_String() {
    CommandLine commandLine = new CommandLine();

    String result = commandLine.getOptionValue("opt", "default");

    assertEquals("default", result);
  }

  @Test
  public void testGetOptionValue_char_String() {
    CommandLine commandLine = new CommandLine();

    String result = commandLine.getOptionValue('o', "default");

    assertEquals("default", result);
  }

  @Test
  public void testGetArgs() {
    CommandLine commandLine = new CommandLine();

    String[] result = commandLine.getArgs();

    assertEquals(0, result.length);
  }

  @Test
  public void testGetArgList() {
    CommandLine commandLine = new CommandLine();

    List<String> result = commandLine.getArgList();

    assertEquals(0, result.size());
  }
}