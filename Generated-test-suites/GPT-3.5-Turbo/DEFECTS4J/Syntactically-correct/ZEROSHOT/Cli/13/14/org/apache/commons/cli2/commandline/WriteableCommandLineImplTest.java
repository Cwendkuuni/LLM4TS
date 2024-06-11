import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  private Option option;
  
  @Before
  public void setUp() {
    option = new Option("option");
    commandLine = new WriteableCommandLineImpl(option, null);
  }
  
  @Test
  public void testAddOption() {
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testAddValue() {
    commandLine.addValue(option, "value");
    assertEquals("value", commandLine.getValues(option, null).get(0));
  }
  
  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testHasOption() {
    assertFalse(commandLine.hasOption(option));
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    assertNull(commandLine.getOption("option"));
    commandLine.addOption(option);
    assertEquals(option, commandLine.getOption("option"));
  }
  
  @Test
  public void testGetValues() {
    assertNull(commandLine.getValues(option, null));
    commandLine.addValue(option, "value");
    assertEquals(1, commandLine.getValues(option, null).size());
  }
  
  @Test
  public void testGetSwitch() {
    assertNull(commandLine.getSwitch(option, null));
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testAddProperty() {
    commandLine.addProperty("property", "value");
    assertEquals("value", commandLine.getProperty("property", null));
  }
  
  @Test
  public void testGetProperty() {
    assertNull(commandLine.getProperty("property", null));
    commandLine.addProperty("property", "value");
    assertEquals("value", commandLine.getProperty("property", null));
  }
  
  @Test
  public void testGetProperties() {
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");
    assertEquals(2, commandLine.getProperties().size());
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-option"));
    assertFalse(commandLine.looksLikeOption("value"));
  }
  
  @Test
  public void testToString() {
    assertEquals("", commandLine.toString());
    commandLine.addValue(option, "value1");
    assertEquals("value1", commandLine.toString());
  }
  
  @Test
  public void testGetOptions() {
    assertEquals(0, commandLine.getOptions().size());
    commandLine.addOption(option);
    assertEquals(1, commandLine.getOptions().size());
  }
  
  @Test
  public void testGetOptionTriggers() {
    assertEquals(0, commandLine.getOptionTriggers().size());
    commandLine.addOption(option);
    assertEquals(1, commandLine.getOptionTriggers().size());
  }
  
  @Test
  public void testSetDefaultValues() {
    commandLine.setDefaultValues(option, null);
    assertNull(commandLine.getValues(option, null));
    commandLine.setDefaultValues(option, Collections.singletonList("default"));
    assertEquals(1, commandLine.getValues(option, null).size());
  }
  
  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option, null);
    assertNull(commandLine.getSwitch(option, null));
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testGetNormalised() {
    assertNull(commandLine.getNormalised());
    commandLine.addValue(option, "value");
    assertEquals(1, commandLine.getNormalised().size());
  }
  
}