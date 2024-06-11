import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  
  @Before
  public void setUp() {
    Option rootOption = new OptionBuilder("rootOption").create();
    List arguments = new ArrayList<>();
    commandLine = new WriteableCommandLineImpl(rootOption, arguments);
  }
  
  @Test
  public void testAddOption() {
    Option option = new OptionBuilder("option").create();
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testAddValue() {
    Option option = new OptionBuilder("option").create();
    String value = "value";
    commandLine.addValue(option, value);
    
    List values = commandLine.getValues(option, null);
    assertTrue(values.contains(value));
  }
  
  @Test
  public void testAddSwitch() {
    Option option = new OptionBuilder("option").create();
    boolean switchValue = true;
    commandLine.addSwitch(option, switchValue);
    
    Boolean switchResult = commandLine.getSwitch(option, null);
    assertEquals(Boolean.TRUE, switchResult);
  }
  
  @Test
  public void testHasOption() {
    Option option = new OptionBuilder("option").create();
    commandLine.addOption(option);
    
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option option = new OptionBuilder("option").create();
    commandLine.addOption(option);
    
    Option result = commandLine.getOption("option");
    assertEquals(option, result);
  }
  
  @Test
  public void testGetValues() {
    Option option = new OptionBuilder("option").create();
    List values = new ArrayList<>();
    values.add("value1");
    values.add("value2");
    commandLine.addValue(option, "value1");
    
    List result = commandLine.getValues(option, values);
    assertEquals(values, result);
  }
  
  @Test
  public void testGetUndefaultedValues() {
    Option option = new OptionBuilder("option").create();
    commandLine.addValue(option, "value");
    
    List result = commandLine.getUndefaultedValues(option);
    assertTrue(result.contains("value"));
  }
  
  @Test
  public void testGetSwitch() {
    Option option = new OptionBuilder("option").create();
    commandLine.addSwitch(option, true);
    
    Boolean switchResult = commandLine.getSwitch(option, null);
    assertEquals(Boolean.TRUE, switchResult);
  }
  
  @Test
  public void testGetProperty() {
    Option option = new OptionBuilder("option").create();
    String property = "property";
    String value = "value";
    commandLine.addProperty(option, property, value);
    
    String result = commandLine.getProperty(option, property, null);
    assertEquals(value, result);
  }
  
  @Test
  public void testGetProperties() {
    Option option = new OptionBuilder("option").create();
    String property = "property";
    String value = "value";
    commandLine.addProperty(option, property, value);
    
    Set result = commandLine.getProperties(option);
    assertTrue(result.contains(property));
  }
  
  @Test
  public void testLooksLikeOption() {
    assertFalse(commandLine.looksLikeOption("arg"));
    assertTrue(commandLine.looksLikeOption("-option"));
  }
  
  @Test
  public void testToString() {
    Option option = new OptionBuilder("option").create();
    commandLine.addValue(option, "value");
    
    String result = commandLine.toString();
    assertEquals("option value", result);
  }
  
  @Test
  public void testGetOptions() {
    Option option1 = new OptionBuilder("option1").create();
    Option option2 = new OptionBuilder("option2").create();
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    
    List options = commandLine.getOptions();
    assertTrue(options.contains(option1));
    assertTrue(options.contains(option2));
  }
  
  @Test
  public void testGetOptionTriggers() {
    Option option1 = new OptionBuilder("option1").create();
    Option option2 = new OptionBuilder("option2").create();
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    
    Set triggers = commandLine.getOptionTriggers();
    assertTrue(triggers.contains("option1"));
    assertTrue(triggers.contains("option2"));
  }
  
  @Test
  public void testSetDefaultValues() {
    Option option = new OptionBuilder("option").create();
    List defaults = new ArrayList<>();
    defaults.add("default1");
    defaults.add("default2");
    commandLine.setDefaultValues(option, defaults);
    
    List values = commandLine.getValues(option, null);
    assertEquals(defaults, values);
  }
  
  @Test
  public void testSetDefaultSwitch() {
    Option option = new OptionBuilder("option").create();
    boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    
    Boolean switchResult = commandLine.getSwitch(option, null);
    assertEquals(Boolean.TRUE, switchResult);
  }
  
  @Test
  public void testGetNormalised() {
    List arguments = new ArrayList<>();
    arguments.add("arg1");
    arguments.add("arg2");
    commandLine = new WriteableCommandLineImpl(null, arguments);
    
    List normalised = commandLine.getNormalised();
    assertTrue(normalised.contains("arg1"));
    assertTrue(normalised.contains("arg2"));
  }

}