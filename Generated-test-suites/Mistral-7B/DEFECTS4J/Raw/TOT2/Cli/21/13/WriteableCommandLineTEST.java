import org.junit.Test;
import org.apache.commons.cli2.*;
import org.junit.Assert.*;

public class WriteableCommandLineTest {

  @Test
  public void testAddOption() {
    WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[]{"--test"});
    Option option = Option.builder("t").longOpt("test").build();
    commandLine.addOption(option);
    List<Option> options = commandLine.getOptions();
    Assert.assertEquals(1, options.size());
    Assert.assertEquals(option, options.get(0));
  }

  @Test
  public void testAddValue() {
    WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[]{"--test", "value"});
    Option option = Option.builder("t").longOpt("test").build();
    commandLine.addValue(option, "value");
    List<String> values = commandLine.getOptionValues("test");
    Assert.assertEquals(1, values.size());
    Assert.assertEquals("value", values.get(0));
  }

  @Test(expected = IllegalStateException.class)
  public void testAddSwitchTwice() {
    WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[]{"--test"});
    Option option = Option.builder("t").longOpt("test").build();
    commandLine.addSwitch(option, true);
    commandLine.addSwitch(option, true);
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[]{"--test", "--property=value"});
    Option option = Option.builder("t").longOpt("test").build();
    commandLine.addProperty(option, "property", "value");
    Assert.assertEquals("value", commandLine.getOptionPropertyValue("test", "property"));
  }

  @Test
  public void testLooksLikeOption() {
    Assert.assertTrue(WriteableCommandLine.looksLikeOption("--test"));
    Assert.assertFalse(WriteableCommandLine.looksLikeOption("value"));
  }

  // Test edge cases and exceptions for addOption:
  // - Test adding an option with an existing long opt name.
  // - Test adding an option with an existing short opt name.
  // - Test adding an option with a null Option object.

  // Test edge cases and exceptions for addValue:
  // - Test adding a value to an option with no values.
  // - Test adding a value to an option with an existing value.
  // - Test adding a value to an option with a null Option object.
  // - Test adding a value to an option with a null value.

}