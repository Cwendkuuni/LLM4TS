import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import java.util.List;

public class WriteableCommandLineImplTest {
  @Test
  public void testAddOption() {
    Option option = new Option("test");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
    assertNotNull(commandLine.getOption("test"));
  }

  @Test
  public void testAddValue() {
    Option option = new Argument();
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addValue(option, "value");
    List<String> values = commandLine.getValues(option, null);
    assertTrue(values.contains("value"));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("switch");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("test");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("test");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addOption(option);
    Option result = commandLine.getOption("test");
    assertEquals(option, result);
  }

  @Test
  public void testGetValues() {
    Option option = new Argument();
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addValue(option, "value");
    List<String> values = commandLine.getValues(option, null);
    assertTrue(values.contains("value"));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("switch");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    commandLine.addProperty("property", "value");
    String result = commandLine.getProperty("property", null);
    assertEquals("value", result);
  }

  @Test
  public void testLooksLikeOption() {
    Option option = new Option("test");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
    boolean result = commandLine.looksLikeOption("test");
    assertTrue(result);
  }
}