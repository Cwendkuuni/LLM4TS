import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    List<String> values = commandLine.getValues(option, null);
    assertEquals(2, values.size());
    assertEquals("value1", values.get(0));
    assertEquals("value2", values.get(1));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
    assertFalse(commandLine.hasOption(new Option("testOption2")));
  }

  @Test
  public void testGetOption() {
    Option option1 = new Option("testOption1");
    Option option2 = new Option("testOption2");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    assertEquals(option1, commandLine.getOption("testOption1"));
    assertEquals(option2, commandLine.getOption("testOption2"));
  }

  @Test
  public void testGetValues() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    List<String> values = commandLine.getValues(option, null);
    assertEquals(2, values.size());
    assertEquals("value1", values.get(0));
    assertEquals("value2", values.get(1));
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    List<String> values = commandLine.getUndefaultedValues(option);
    assertEquals(2, values.size());
    assertEquals("value1", values.get(0));
    assertEquals("value2", values.get(1));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    assertEquals("value1", commandLine.getProperty("property1"));
    assertEquals("value2", commandLine.getProperty("property2"));
    assertNull(commandLine.getProperty("property3"));
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    assertEquals("value1", commandLine.getProperty("property1"));
    assertEquals("value2", commandLine.getProperty("property2"));
  }

  @Test
  public void testGetPropertyWithDefault() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    assertEquals("value1", commandLine.getProperty(new Option("testOption"), "property1", "defaultValue1"));
    assertEquals("defaultValue2", commandLine.getProperty(new Option("testOption"), "property3", "defaultValue2"));
  }

  @Test
  public void testGetProperties() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    assertEquals(2, commandLine.getProperties(new Option("testOption")).size());
    assertEquals(0, commandLine.getProperties(new Option("testOption2")).size());
  }

  @Test
  public void testGetPropertiesAll() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    assertEquals(2, commandLine.getProperties().size());
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
    assertTrue(commandLine.looksLikeOption("-testOption"));
    assertFalse(commandLine.looksLikeOption("testOption"));
  }

  @Test
  public void testToString() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
    commandLine.addValue(new Option("testOption"), "value1");
    commandLine.addValue(new Option("testOption"), "value2");
    assertEquals("-testOption value1 value2", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("testOption1");
    Option option2 = new Option("testOption2");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    List<Option> options = commandLine.getOptions();
    assertEquals(2, options.size());
    assertEquals(option1, options.get(0));
    assertEquals(option2, options.get(1));
  }

  @Test
  public void testGetOptionTriggers() {
    Option option1 = new Option("testOption1");
    Option option2 = new Option("testOption2");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    assertTrue(commandLine.getOptionTriggers().contains("testOption1"));
    assertTrue(commandLine.getOptionTriggers().contains("testOption2"));
    assertFalse(commandLine.getOptionTriggers().contains("testOption3"));
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("defaultValue1");
    defaultValues.add("defaultValue2");
    commandLine.setDefaultValues(option, defaultValues);
    assertEquals(defaultValues, commandLine.getValues(option, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));
  }

  @Test
  public void testGetNormalised() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    List<String> normalised = commandLine.getNormalised();
    assertEquals(2, normalised.size());
    assertEquals("value1", normalised.get(0));
    assertEquals("value2", normalised.get(1));
  }

}