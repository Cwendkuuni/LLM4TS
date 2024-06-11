
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

  private Option option;
  private WriteableCommandLine commandLine;

  @Before
  public void setUp() {
    option = new Option("o");
    commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
  }

  @Test
  public void testAddOption() {
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    commandLine.addValue(option, "value");
    assertTrue(commandLine.hasOption(option));
    assertEquals("value", commandLine.getValues(option, null).get(0));
  }

  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.hasOption(option));
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    assertFalse(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    assertNull(commandLine.getOption("o"));
    commandLine.addOption(option);
    assertEquals(option, commandLine.getOption("o"));
  }

  @Test
  public void testGetValues() {
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("default");
    commandLine.setDefaultValues(option, defaultValues);
    assertEquals(defaultValues, commandLine.getValues(option, null));

    commandLine.addValue(option, "value");
    List<String> values = new ArrayList<>();
    values.add("value");
    assertEquals(values, commandLine.getValues(option, null));
  }

  @Test
  public void testGetUndefaultedValues() {
    assertEquals(new ArrayList<>(), commandLine.getUndefaultedValues(option));

    commandLine.addValue(option, "value");
    List<String> values = new ArrayList<>();
    values.add("value");
    assertEquals(values, commandLine.getUndefaultedValues(option));
  }

  @Test
  public void testGetSwitch() {
    assertNull(commandLine.getSwitch(option, null));
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    assertEquals(null, commandLine.getProperty("property"));

    commandLine.addProperty("property", "value");
    assertEquals("value", commandLine.getProperty("property"));
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty(option, "property", "value");
    assertEquals("value", commandLine.getProperty(option, "property", null));
  }

  @Test
  public void testGetProperty_defaultValue() {
    assertEquals("defaultValue", commandLine.getProperty(option, "property", "defaultValue"));

    commandLine.addProperty(option, "property", "value");
    assertEquals("value", commandLine.getProperty(option, "property", "defaultValue"));
  }

  @Test
  public void testGetProperties() {
    assertEquals(0, commandLine.getProperties().size());

    commandLine.addProperty(option, "property", "value");
    assertTrue(commandLine.getProperties().contains("property"));
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-o"));
    assertFalse(commandLine.looksLikeOption("value"));
  }

  @Test
  public void testToString() {
    assertEquals("", commandLine.toString());

    List<String> arguments = new ArrayList<>();
    arguments.add("arg1");
    arguments.add("arg2");
    commandLine = new WriteableCommandLineImpl(option, arguments);
    assertEquals("arg1 arg2", commandLine.toString());

    arguments.clear();
    arguments.add("arg 1");
    arguments.add("arg2");
    assertEquals("\"arg 1\" arg2", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
  }

  @Test
  public void testGetOptionTriggers() {
    assertEquals(0, commandLine.getOptionTriggers().size());
    commandLine.addOption(option);
    assertTrue(commandLine.getOptionTriggers().contains("o"));
  }

  @Test
  public void testSetDefaultValues() {
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("default");
    commandLine.setDefaultValues(option, defaultValues);
    assertEquals(defaultValues, commandLine.getValues(option, null));

    commandLine.setDefaultValues(option, null);
    assertEquals(new ArrayList<>(), commandLine.getValues(option, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));

    commandLine.setDefaultSwitch(option, null);
    assertNull(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    assertEquals(new ArrayList<>(), commandLine.getNormalised());

    List<String> arguments = new ArrayList<>();
    arguments.add("arg1");
    arguments.add("arg2");
    commandLine = new WriteableCommandLineImpl(option, arguments);
    assertEquals(arguments, commandLine.getNormalised());
  }
}
