import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import java.util.List;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  
  @Before
  public void setUp() {
    Option option1 = new Option("option1", "Option 1");
    Argument argument1 = new Argument("argument1", "Argument 1");
    List arguments = new ArrayList();
    arguments.add("value1");
    commandLine = new WriteableCommandLineImpl(option1, arguments);
  }
  
  @Test
  public void testAddOption() {
    Option option2 = new Option("option2", "Option 2");
    commandLine.addOption(option2);
    assertTrue(commandLine.getOptions().contains(option2));
  }
  
  @Test
  public void testAddValue() {
    Option option1 = new Option("option1", "Option 1");
    commandLine.addValue(option1, "value2");
    assertTrue(commandLine.getValues(option1, null).contains("value2"));
  }
  
  @Test
  public void testAddSwitch() {
    Option option1 = new Option("option1", "Option 1");
    commandLine.addSwitch(option1, true);
    assertTrue(commandLine.getSwitch(option1, false));
  }
  
  @Test
  public void testHasOption() {
    Option option1 = new Option("option1", "Option 1");
    assertTrue(commandLine.hasOption(option1));
  }
  
  @Test
  public void testGetOption() {
    Option option = commandLine.getOption("option1");
    assertNotNull(option);
    assertEquals("option1", option.getPreferredName());
  }
  
  @Test
  public void testGetValues() {
    Option option1 = new Option("option1", "Option 1");
    List values = commandLine.getValues(option1, Collections.emptyList());
    assertEquals(1, values.size());
    assertEquals("value1", values.get(0));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    Option option1 = new Option("option1", "Option 1");
    List values = commandLine.getUndefaultedValues(option1);
    assertEquals(1, values.size());
    assertEquals("value1", values.get(0));
  }
  
  @Test
  public void testGetSwitch() {
    Option option1 = new Option("option1", "Option 1");
    boolean switchValue = commandLine.getSwitch(option1, false);
    assertFalse(switchValue);
  }
  
  @Test
  public void testGetProperty() {
    String propertyValue = commandLine.getProperty("property");
    assertNull(propertyValue);
  }
  
  @Test
  public void testAddProperty() {
    commandLine.addProperty("property", "value");
    String propertyValue = commandLine.getProperty("property");
    assertEquals("value", propertyValue);
  }
  
  @Test
  public void testGetPropertyWithDefault() {
    String propertyValue = commandLine.getProperty(new PropertyOption(), "property", "defaultValue");
    assertEquals("defaultValue", propertyValue);
  }
  
  @Test
  public void testGetProperties() {
    Set properties = commandLine.getProperties(new PropertyOption());
    assertNotNull(properties);
    assertEquals(0, properties.size());
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-option1"));
  }
  
  @Test
  public void testToString() {
    String commandLineString = commandLine.toString();
    assertEquals("value1", commandLineString);
  }
  
  @Test
  public void testGetOptions() {
    List options = commandLine.getOptions();
    assertNotNull(options);
    assertEquals(1, options.size());
  }
  
  @Test
  public void testGetOptionTriggers() {
    Set triggers = commandLine.getOptionTriggers();
    assertNotNull(triggers);
    assertEquals(1, triggers.size());
    assertTrue(triggers.contains("option1"));
  }
  
  @Test
  public void testSetDefaultValues() {
    Option option1 = new Option("option1", "Option 1");
    List defaults = Collections.singletonList("defaultValue");
    commandLine.setDefaultValues(option1, defaults);
    List values = commandLine.getValues(option1, null);
    assertNotNull(values);
    assertEquals(1, values.size());
    assertEquals("defaultValue", values.get(0));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option option1 = new Option("option1", "Option 1");
    commandLine.setDefaultSwitch(option1, true);
    boolean switchValue = commandLine.getSwitch(option1, false);
    assertTrue(switchValue);
  }
  
  @Test
  public void testGetNormalised() {
    List normalised = commandLine.getNormalised();
    assertNotNull(normalised);
    assertEquals(1, normalised.size());
    assertEquals("value1", normalised.get(0));
  }
  
}