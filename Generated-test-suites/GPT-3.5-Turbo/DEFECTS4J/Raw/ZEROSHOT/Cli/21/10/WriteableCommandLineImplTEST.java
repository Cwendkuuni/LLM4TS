import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  private Option option1;
  private Option option2;
  
  @Before
  public void setUp() {
    option1 = new Option("o1", "Option 1", true);
    option2 = new Option("o2", "Option 2", false);
    commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
  }
  
  @Test
  public void testAddOption() {
    commandLine.addOption(option2);
    assertTrue(commandLine.hasOption(option2));
  }
  
  @Test
  public void testAddValue() {
    commandLine.addValue(option1, "value1");
    assertEquals("value1", commandLine.getValues(option1, null).get(0));
  }
  
  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option2, true);
    assertTrue(commandLine.getSwitch(option2, false));
  }
  
  @Test
  public void testHasOption() {
    assertTrue(commandLine.hasOption(option1));
    assertFalse(commandLine.hasOption(option2));
  }
  
  @Test
  public void testGetOption() {
    assertEquals(option1, commandLine.getOption("o1"));
    assertEquals(option2, commandLine.getOption("o2"));
  }
  
  @Test
  public void testGetValues() {
    commandLine.addValue(option1, "value1");
    commandLine.addValue(option1, "value2");
    assertEquals("value1", commandLine.getValues(option1, null).get(0));
    assertEquals("value2", commandLine.getValues(option1, null).get(1));
  }
  
  @Test
  public void testGetUndefaultedValues() {
    commandLine.addValue(option1, "value1");
    commandLine.addValue(option1, "value2");
    assertEquals("value1", commandLine.getUndefaultedValues(option1).get(0));
    assertEquals("value2", commandLine.getUndefaultedValues(option1).get(1));
  }
  
  @Test
  public void testGetSwitch() {
    commandLine.addSwitch(option2, true);
    assertTrue(commandLine.getSwitch(option2, false));
  }
  
  @Test
  public void testGetProperty() {
    commandLine.addProperty(new PropertyOption(), "property1", "value1");
    assertEquals("value1", commandLine.getProperty(new PropertyOption(), "property1", null));
  }
  
  @Test
  public void testAddProperty() {
    commandLine.addProperty(new PropertyOption(), "property1", "value1");
    assertEquals("value1", commandLine.getProperty(new PropertyOption(), "property1", null));
  }
  
  @Test
  public void testGetPropertyWithDefault() {
    assertEquals("default", commandLine.getProperty(new PropertyOption(), "property1", "default"));
  }
  
  @Test
  public void testGetProperties() {
    commandLine.addProperty(new PropertyOption(), "property1", "value1");
    assertEquals(1, commandLine.getProperties().size());
  }
  
  @Test
  public void testGetOptions() {
    assertEquals(1, commandLine.getOptions().size());
  }
  
  @Test
  public void testGetOptionTriggers() {
    assertEquals(2, commandLine.getOptionTriggers().size());
  }
  
  @Test
  public void testSetDefaultValues() {
    List defaults = new ArrayList();
    defaults.add("default1");
    defaults.add("default2");
    commandLine.setDefaultValues(option1, defaults);
    assertEquals(defaults, commandLine.getValues(option1, null));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option2, true);
    assertTrue(commandLine.getSwitch(option2, false));
  }
  
  @Test
  public void testGetNormalised() {
    assertEquals(0, commandLine.getNormalised().size());
  }
}