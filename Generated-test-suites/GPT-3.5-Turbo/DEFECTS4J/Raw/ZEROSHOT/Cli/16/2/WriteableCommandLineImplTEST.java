import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;

public class WriteableCommandLineImplTest {
  private Option rootOption;
  private List arguments;
  private WriteableCommandLineImpl writeableCommandLine;

  @Before
  public void setUp() {
    rootOption = new Option("rootOption", "This is the root option");
    arguments = new ArrayList();
    writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
  }

  @Test
  public void testAddOption() {
    Option option = new Option("testOption", "This is a test option");
    writeableCommandLine.addOption(option);
    assertTrue(writeableCommandLine.getOptions().contains(option));
  }

  @Test
  public void testAddValue() {
    Option option = new Argument("testArgument", "This is a test argument");
    Object value = "testValue";
    writeableCommandLine.addValue(option, value);
    assertTrue(writeableCommandLine.getValues(option, null).contains(value));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("testSwitch", "This is a test switch");
    boolean value = true;
    writeableCommandLine.addSwitch(option, value);
    assertTrue(writeableCommandLine.getSwitch(option, null).equals(value));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("testOption", "This is a test option");
    writeableCommandLine.addOption(option);
    assertTrue(writeableCommandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("testOption", "This is a test option");
    writeableCommandLine.addOption(option);
    assertEquals(writeableCommandLine.getOption("testOption"), option);
  }

  @Test
  public void testGetValues() {
    Option option = new Argument("testArgument", "This is a test argument");
    List defaultValues = new ArrayList();
    defaultValues.add("defaultValue");
    Object value = "testValue";
    writeableCommandLine.addValue(option, value);
    assertEquals(writeableCommandLine.getValues(option, defaultValues), writeableCommandLine.getUndefaultedValues(option));
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option = new Argument("testArgument", "This is a test argument");
    Object value = "testValue";
    writeableCommandLine.addValue(option, value);
    assertTrue(writeableCommandLine.getUndefaultedValues(option).contains(value));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("testSwitch", "This is a test switch");
    boolean defaultValue = false;
    writeableCommandLine.addSwitch(option, defaultValue);
    assertEquals(writeableCommandLine.getSwitch(option, defaultValue), writeableCommandLine.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    String property = "testProperty";
    assertEquals(writeableCommandLine.getProperty(property), writeableCommandLine.getProperty(new PropertyOption(), property, null));
  }

  @Test
  public void testAddProperty() {
    Option option = new PropertyOption("testProperty", "This is a test property");
    String property = "testProperty";
    String value = "testValue";
    writeableCommandLine.addProperty(option, property, value);
    assertEquals(writeableCommandLine.getProperty(property), value);
  }

  @Test
  public void testGetProperty() {
    Option option = new PropertyOption("testProperty", "This is a test property");
    String property = "testProperty";
    String defaultValue = "defaultValue";
    writeableCommandLine.addProperty(option, property, defaultValue);
    assertEquals(writeableCommandLine.getProperty(option, property, null), writeableCommandLine.getProperty(property));
  }

  @Test
  public void testGetProperties() {
    Option option = new PropertyOption("testProperty", "This is a test property");
    writeableCommandLine.addProperty(option, "testProperty1", "testValue1");
    writeableCommandLine.addProperty(option, "testProperty2", "testValue2");
    assertTrue(writeableCommandLine.getProperties(option).contains("testProperty1"));
    assertTrue(writeableCommandLine.getProperties(option).contains("testProperty2"));
  }

  @Test
  public void testGetProperties() {
    String property = "testProperty";
    writeableCommandLine.addProperty(new PropertyOption(), property, "testValue");
    assertTrue(writeableCommandLine.getProperties().contains(property));
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(writeableCommandLine.looksLikeOption("-testOption"));
  }

  @Test
  public void testToString() {
    String arg = "testArgument";
    writeableCommandLine.getNormalised().add(arg);
    assertEquals(writeableCommandLine.toString(), arg);
  }

  @Test
  public void testGetOptions() {
    Option option = new Option("testOption", "This is a test option");
    writeableCommandLine.addOption(option);
    assertTrue(writeableCommandLine.getOptions().contains(option));
  }

  @Test
  public void testGetOptionTriggers() {
    Option option = new Option("testOption", "This is a test option");
    writeableCommandLine.addOption(option);
    assertTrue(writeableCommandLine.getOptionTriggers().contains("testOption"));
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Argument("testArgument", "This is a test argument");
    List defaultValues = new ArrayList();
    defaultValues.add("defaultValue");
    writeableCommandLine.setDefaultValues(option, defaultValues);
    assertTrue(writeableCommandLine.getValues(option, null).containsAll(defaultValues));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("testSwitch", "This is a test switch");
    boolean defaultSwitch = true;
    writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
    assertEquals(writeableCommandLine.getSwitch(option, null), defaultSwitch);
  }

  @Test
  public void testGetNormalised() {
    String arg = "testArgument";
    writeableCommandLine.getNormalised().add(arg);
    assertTrue(writeableCommandLine.getNormalised().contains(arg));
  }
}