import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    commandLine.addOption(new Option());
    assertEquals(1, commandLine.getOptions().size());
  }

  @Test
  public void testAddValue() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    commandLine.addValue(new Option(), "value");
    assertEquals("value", commandLine.getValues(new Option(), new ArrayList<String>()).get(0));
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    commandLine.addSwitch(new Option(), true);
    assertEquals(true, commandLine.getSwitch(new Option(), false));
  }

  @Test
  public void testHasOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    commandLine.addOption(option);
    assertEquals(true, commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    commandLine.addOption(option);
    assertEquals(option, commandLine.getOption(option.getPreferredName()));
  }

  @Test
  public void testGetValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    List<String> values = new ArrayList<>();
    values.add("value1");
    values.add("value2");
    commandLine.addValue(option, "value1");
    assertEquals(values, commandLine.getValues(option, new ArrayList<String>()));

    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("defaultValue");
    assertEquals(defaultValues, commandLine.getValues(option, defaultValues));
  }

  @Test
  public void testGetUndefaultedValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    List<String> values = new ArrayList<>();
    values.add("value1");
    values.add("value2");
    commandLine.addValue(option, "value1");
    assertEquals(values, commandLine.getUndefaultedValues(option));
  }

  @Test
  public void testGetSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    commandLine.addSwitch(option, true);
    assertEquals(true, commandLine.getSwitch(option, false));

    assertEquals(false, commandLine.getSwitch(option, false));
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    assertEquals("", commandLine.getProperty("property"));
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    commandLine.addProperty(new Option(), "property", "value");
    assertEquals("value", commandLine.getProperty(new Option(), "property", ""));
  }

  @Test
  public void testGetPropertyWithDefault() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    assertEquals("defaultValue", commandLine.getProperty(new Option(), "property", "defaultValue"));
  }

  @Test
  public void testGetProperties() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    commandLine.addProperty(option, "property", "value");
    assertEquals(1, commandLine.getProperties(option).size());
  }

  @Test
  public void testGetPropertiesWithoutOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    assertEquals(0, commandLine.getProperties().size());
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    assertEquals(false, commandLine.looksLikeOption("trigger"));
    assertEquals(true, commandLine.looksLikeOption("-trigger"));
  }

  @Test
  public void testToString() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    assertEquals("", commandLine.toString());

    commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    List<String> arguments = new ArrayList<>();
    arguments.add("arg1");
    arguments.add("arg2");
    commandLine = new WriteableCommandLineImpl(new Option(), arguments);
    assertEquals("arg1 arg2", commandLine.toString());

    commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    arguments = new ArrayList<>();
    arguments.add("arg with spaces");
    commandLine = new WriteableCommandLineImpl(new Option(), arguments);
    assertEquals("\"arg with spaces\"", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    commandLine.addOption(new Option());
    assertEquals(1, commandLine.getOptions().size());
  }

  @Test
  public void testGetOptionTriggers() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    commandLine.addOption(option);
    assertEquals(1, commandLine.getOptionTriggers().size());
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    List<String> defaults = new ArrayList<>();
    defaults.add("defaultValue");
    commandLine.setDefaultValues(option, defaults);
    assertEquals(defaults, commandLine.getValues(option, new ArrayList<String>()));
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    Option option = new Option();
    commandLine.setDefaultSwitch(option, true);
    assertEquals(true, commandLine.getSwitch(option, false));
  }

  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<String>());
    commandLine.addOption(new Option());
    assertEquals(1, commandLine.getNormalised().size());
  }
}