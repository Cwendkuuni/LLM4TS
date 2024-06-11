import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class WriteableCommandLineImplTest {

  // Test addOption method
  @Test
  public void testAddOption() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addOption(option);
    Assert.assertTrue(commandLine.getOptions().contains(option));
  }

  // Test addValue method
  @Test
  public void testAddValue() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addValue(option, "value");
    Assert.assertTrue(commandLine.getValues(option, null).contains("value"));
  }

  // Test addSwitch method
  @Test
  public void testAddSwitch() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addSwitch(option, true);
    Assert.assertEquals(true, commandLine.getSwitch(option, null));
  }

  // Test hasOption method
  @Test
  public void testHasOption() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addOption(option);
    Assert.assertTrue(commandLine.hasOption(option));
  }

  // Test getOption method
  @Test
  public void testGetOption() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addOption(option);
    Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
  }

  // Test getValues method
  @Test
  public void testGetValues() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    Assert.assertEquals(2, commandLine.getValues(option, null).size());
  }

  // Test getUndefaultedValues method
  @Test
  public void testGetUndefaultedValues() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    Assert.assertEquals(2, commandLine.getUndefaultedValues(option).size());
  }

  // Test getSwitch method
  @Test
  public void testGetSwitch() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addSwitch(option, true);
    Assert.assertEquals(true, commandLine.getSwitch(option, null));
  }

  // Test getProperty method
  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addProperty("property", "value");
    Assert.assertEquals("value", commandLine.getProperty("property"));
  }

  // Test addProperty method
  @Test
  public void testAddProperty() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addProperty(option, "property", "value");
    Assert.assertEquals("value", commandLine.getProperty(option, "property", null));
  }

  // Test getProperty method with default value
  @Test
  public void testGetPropertyWithDefault() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    Assert.assertEquals("default", commandLine.getProperty("property", "default"));
  }

  // Test getProperties method
  @Test
  public void testGetProperties() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    Assert.assertEquals(2, commandLine.getProperties().size());
  }

  // Test looksLikeOption method
  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    Assert.assertTrue(commandLine.looksLikeOption("-option"));
  }

  // Test toString method
  @Test
  public void testToString() {
    Option option = mock(Option.class);
    List arguments = new ArrayList<>();
    arguments.add("arg1");
    arguments.add("arg2");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
    Assert.assertEquals("arg1 arg2", commandLine.toString());
  }

  // Test getOptions method
  @Test
  public void testGetOptions() {
    Option option1 = mock(Option.class);
    Option option2 = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    Assert.assertEquals(2, commandLine.getOptions().size());
  }

  // Test getOptionTriggers method
  @Test
  public void testGetOptionTriggers() {
    Option option1 = mock(Option.class);
    Option option2 = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    Assert.assertEquals(2, commandLine.getOptionTriggers().size());
  }

  // Test setDefaultValues method
  @Test
  public void testSetDefaultValues() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    List defaults = new ArrayList<>();
    defaults.add("default1");
    defaults.add("default2");
    commandLine.setDefaultValues(option, defaults);
    Assert.assertEquals(2, commandLine.getValues(option, null).size());
  }

  // Test setDefaultSwitch method
  @Test
  public void testSetDefaultSwitch() {
    Option option = mock(Option.class);
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.setDefaultSwitch(option, true);
    Assert.assertEquals(true, commandLine.getSwitch(option, null));
  }

  // Test getNormalised method
  @Test
  public void testGetNormalised() {
    Option option = mock(Option.class);
    List arguments = new ArrayList<>();
    arguments.add("arg1");
    arguments.add("arg2");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
    Assert.assertEquals(2, commandLine.getNormalised().size());
  }

}