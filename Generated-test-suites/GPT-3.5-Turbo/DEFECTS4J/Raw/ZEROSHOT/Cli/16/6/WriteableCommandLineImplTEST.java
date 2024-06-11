import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.apache.commons.cli2.option.DefaultOptionBuilder;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import java.util.ArrayList;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;

  @Before
  public void setup() {
    Option option1 = new DefaultOptionBuilder().withLongName("option1").withDescription("Option 1").create();
    Option option2 = new DefaultOptionBuilder().withLongName("option2").withDescription("Option 2").withArgument(new ArgumentImpl("arg")).create();
    Option option3 = new DefaultOptionBuilder().withLongName("option3").withDescription("Option 3").withArgument(new ArgumentImpl("arg1")).withArgument(new ArgumentImpl("arg2")).create();
    
    commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>());
    
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    commandLine.addOption(option3);    
  }

  @Test
  public void testAddOption() {
    Option option4 = new DefaultOptionBuilder().withLongName("option4").withDescription("Option 4").create();
    
    commandLine.addOption(option4);
    
    assertTrue(commandLine.hasOption(option4));
    assertFalse(commandLine.hasOption(new DefaultOptionBuilder().withLongName("optionX").withDescription("Option X").create()));  
  }

  @Test
  public void testAddValue() {
    Option option2 = commandLine.getOption("option2");
    
    commandLine.addValue(option2, "value1");
    
    assertEquals("value1", commandLine.getValues(option2, null).get(0));
  }

  @Test
  public void testAddSwitch() {
    Option option1 = commandLine.getOption("option1");
    
    commandLine.addSwitch(option1, true);
    
    assertTrue(commandLine.getSwitch(option1, null));
    assertFalse(commandLine.getSwitch(new DefaultOptionBuilder().withLongName("optionX").withDescription("Option X").create(), null));
  }

  @Test
  public void testHasOption() {
    Option option1 = commandLine.getOption("option1");
    
    assertTrue(commandLine.hasOption(option1));
    assertFalse(commandLine.hasOption(new DefaultOptionBuilder().withLongName("optionX").withDescription("Option X").create()));
  }

  @Test
  public void testGetOption() {
    Option option2 = commandLine.getOption("option2");
    
    assertEquals(option2, commandLine.getOption("option2"));
    assertNull(commandLine.getOption("optionX"));
  }

  @Test
  public void testGetValues() {
    Option option3 = commandLine.getOption("option3");
    
    commandLine.addValue(option3, "value1");
    commandLine.addValue(option3, "value2");
    
    assertEquals("value1", commandLine.getValues(option3, null).get(0));
    assertEquals("value2", commandLine.getValues(option3, null).get(1));
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option3 = commandLine.getOption("option3");
    
    commandLine.addValue(option3, "value1");
    commandLine.addValue(option3, "value2");
    
    assertEquals("value1", commandLine.getUndefaultedValues(option3).get(0));
    assertEquals("value2", commandLine.getUndefaultedValues(option3).get(1));
  }

  @Test
  public void testGetSwitch() {
    Option option1 = commandLine.getOption("option1");
    
    commandLine.addSwitch(option1, false);
    
    assertFalse(commandLine.getSwitch(option1, null));
    assertTrue(commandLine.getSwitch(option1, true));
  }

  @Test
  public void testGetProperty() {
    Option option3 = commandLine.getOption("option3");
    
    commandLine.addProperty(option3, "property1", "value1");
    
    assertEquals("value1", commandLine.getProperty(option3, "property1", null));
  }

  @Test
  public void testAddProperty() {
    Option option3 = commandLine.getOption("option3");
    
    commandLine.addProperty(option3, "property1", "value1");
    
    assertEquals("value1", commandLine.getProperty(option3, "property1", null));
  }

  @Test
  public void testGetPropertyWithDefaultValue() {
    Option option3 = commandLine.getOption("option3");
    
    assertEquals("default", commandLine.getProperty(option3, "property1", "default"));
  }

  @Test
  public void testGetPropertiesForOption() {
    Option option3 = commandLine.getOption("option3");
    
    commandLine.addProperty(option3, "property1", "value1");
    commandLine.addProperty(option3, "property2", "value2");
    
    assertEquals(2, commandLine.getProperties(option3).size());
    assertTrue(commandLine.getProperties(option3).contains("property1"));
    assertTrue(commandLine.getProperties(option3).contains("property2"));
  }

  @Test
  public void testGetProperties() {
    Option option3 = commandLine.getOption("option3");
    
    commandLine.addProperty(option3, "property1", "value1");
    
    assertEquals(1, commandLine.getProperties().size());
    assertTrue(commandLine.getProperties().contains("property1"));
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-option1"));
    assertFalse(commandLine.looksLikeOption("value"));
  }

  @Test
  public void testToString() {
    commandLine.addValue(commandLine.getOption("option2"), "value");
    
    assertEquals("value", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    assertEquals(3, commandLine.getOptions().size());
  }

  @Test
  public void testGetOptionTriggers() {
    assertEquals(3, commandLine.getOptionTriggers().size());
  }

  @Test
  public void testSetDefaultValues() {
    Option option3 = commandLine.getOption("option3");
    
    commandLine.setDefaultValues(option3, new ArrayList<String>());
    
    assertEquals(0, commandLine.getValues(option3, null).size());
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option1 = commandLine.getOption("option1");
    
    commandLine.setDefaultSwitch(option1, false);
    
    assertFalse(commandLine.getSwitch(option1, true));
  }

  @Test
  public void testGetNormalised() {
    assertEquals(0, commandLine.getNormalised().size());
  }
  
}