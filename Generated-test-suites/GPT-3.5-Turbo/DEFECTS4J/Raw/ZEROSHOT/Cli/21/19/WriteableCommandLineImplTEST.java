import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
  }

  @Test
  public void testAddValue() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    Object value = new Object();
    commandLine.addValue(option, value);
    assertTrue(commandLine.getValues(option, null).contains(value));
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    boolean value = true;
    commandLine.addSwitch(option, value);
    assertTrue(commandLine.getSwitch(option, null).equals(value));
  }

  @Test
  public void testHasOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    commandLine.addOption(option);
    assertTrue(commandLine.getOption(option.getPreferredName()).equals(option));
  }

  @Test
  public void testGetValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    Object value = new Object();
    commandLine.addValue(option, value);
    assertTrue(commandLine.getValues(option, null).contains(value));
  }

  @Test
  public void testGetUndefaultedValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    Object value = new Object();
    commandLine.addValue(option, value);
    assertTrue(commandLine.getUndefaultedValues(option).contains(value));
  }

  @Test
  public void testGetSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    boolean value = false;
    commandLine.addSwitch(option, value);
    assertTrue(commandLine.getSwitch(option, null).equals(value));
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    String property = "prop";
    String value = "val";
    commandLine.addProperty(property, value);
    assertTrue(commandLine.getProperty(property).equals(value));
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    String property = "prop";
    String value = "val";
    commandLine.addProperty(option, property, value);
    assertTrue(commandLine.getProperty(option, property, null).equals(value));
  }

  @Test
  public void testGetPropertyWithDefault() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    String property = "prop";
    String defaultValue = "default";
    assertTrue(commandLine.getProperty(property, defaultValue).equals(defaultValue));
  }

  @Test
  public void testGetProperties() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    String property = "prop";
    String value = "val";
    commandLine.addProperty(option, property, value);
    assertTrue(commandLine.getProperties(option).contains(property));
  }

  @Test
  public void testGetPropertiesDefault() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    assertTrue(commandLine.getProperties().isEmpty());
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    String trigger = "-option";
    assertTrue(commandLine.looksLikeOption(trigger));
  }

  @Test
  public void testToString() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    String arg1 = "arg1";
    String arg2 = "arg2";
    commandLine.getNormalised().add(arg1);
    commandLine.getNormalised().add(arg2);
    assertTrue(commandLine.toString().equals(arg1 + " " + arg2));
  }

  @Test
  public void testGetOptions() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
  }

  @Test
  public void testGetOptionTriggers() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    commandLine.addOption(option);
    assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    commandLine.setDefaultValues(option, null);
    assertTrue(commandLine.getValues(option, null).isEmpty());
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    Option option = new Option();
    commandLine.setDefaultSwitch(option, null);
    assertTrue(commandLine.getSwitch(option, null) == null);
  }

  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
    String arg1 = "arg1";
    String arg2 = "arg2";
    commandLine.getNormalised().add(arg1);
    commandLine.getNormalised().add(arg2);
    assertTrue(commandLine.getNormalised().size() == 2);
  }
}