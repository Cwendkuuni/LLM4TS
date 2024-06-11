import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.CommandLineImpl;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.option.CommandLineArgument;
import org.apache.commons.cli2.option.DefaultOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLine commandLine;
  
  private Option option1;
  private Option option2;
  
  @Before
  public void setup() {
    option1 = new DefaultOption.Builder("o1").withDescription("Option 1").create();
    option2 = new DefaultOption.Builder("o2").withDescription("Option 2").create();
    CommandLineArgument arg1 = new CommandLineArgument.Builder("arg1").create();
    List arguments = Arrays.asList(arg1);
    commandLine = new WriteableCommandLineImpl(option1, arguments);
  }
  
  @Test
  public void addOption_shouldAddOptionToList() {
    commandLine.addOption(option2);
    
    assertTrue(commandLine.getOptions().contains(option2));
  }
  
  @Test
  public void addOption_shouldAddOptionToNameToOptionMap() {
    commandLine.addOption(option2);
    
    assertEquals(option2, commandLine.getOption("o2"));
  }
  
  @Test
  public void addOption_shouldAddOptionToTriggersMap() {
    commandLine.addOption(option2);
    
    assertTrue(commandLine.getOptionTriggers().contains("o2"));
  }
  
  @Test
  public void addValue_shouldAddValueToList() {
    String value = "value";
    commandLine.addValue(option1, value);
    
    assertTrue(commandLine.getUndefaultedValues(option1).contains(value));
  }
  
  @Test
  public void addValue_shouldAddOptionIfOptionIsArgument() {
    String value = "value";
    commandLine.addValue(option1, value);
    
    assertTrue(commandLine.getOptions().contains(option1));
  }
  
  @Test
  public void addSwitch_shouldAddOptionToList() {
    commandLine.addSwitch(option1, true);
    
    assertTrue(commandLine.getOptions().contains(option1));
  }
  
  @Test
  public void addSwitch_shouldAddSwitchToSwitchesMap() {
    commandLine.addSwitch(option1, true);
    
    assertTrue(commandLine.getSwitch(option1, false));
  }
  
  @Test
  public void addSwitch_shouldThrowIllegalStateExceptionIfSwitchAlreadySet() {
    commandLine.addSwitch(option1, true);
    
    try {
      commandLine.addSwitch(option1, false);
    } catch (IllegalStateException e) {
      assertEquals(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.SWITCH_ALREADY_SET),
                   e.getMessage());
    }
  }
  
  @Test
  public void hasOption_shouldReturnTrueIfOptionIsPresent() {
    commandLine.addOption(option1);
    
    assertTrue(commandLine.hasOption(option1));
  }
  
  @Test
  public void hasOption_shouldReturnFalseIfOptionIsNotPresent() {
    assertFalse(commandLine.hasOption(option1));
  }
  
  @Test
  public void getOption_shouldReturnOptionIfTriggerExists() {
    commandLine.addOption(option1);
    
    assertEquals(option1, commandLine.getOption("o1"));
  }
  
  @Test
  public void getOption_shouldReturnNullIfTriggerDoesNotExist() {
    assertNull(commandLine.getOption("o1"));
  }
  
  @Test
  public void getValues_shouldReturnValuesIfPresent() {
    String value1 = "value1";
    String value2 = "value2";
    List<String> defaultValues = Arrays.asList("default1", "default2");
    
    commandLine.addValue(option1, value1);
    commandLine.addValue(option1, value2);
    commandLine.setDefaultValues(option1, defaultValues);
    
    assertEquals(Arrays.asList(value1, value2), commandLine.getValues(option1, null));
  }
  
  @Test
  public void getValues_shouldReturnDefaultValuesIfNoValuesPresent() {
    List<String> defaultValues = Arrays.asList("default1", "default2");
    
    commandLine.setDefaultValues(option1, defaultValues);
    
    assertEquals(defaultValues, commandLine.getValues(option1, null));
  }
  
  @Test
  public void getUndefaultedValues_shouldReturnValuesIfPresent() {
    String value1 = "value1";
    String value2 = "value2";
    
    commandLine.addValue(option1, value1);
    commandLine.addValue(option1, value2);
    
    assertEquals(Arrays.asList(value1, value2), commandLine.getUndefaultedValues(option1));
  }
  
  @Test
  public void getUndefaultedValues_shouldReturnEmptyListIfNoValuesPresent() {
    assertEquals(0, commandLine.getUndefaultedValues(option1).size());
  }
  
  @Test
  public void getSwitch_shouldReturnValueIfPresent() {
    commandLine.addSwitch(option1, true);
    
    assertTrue(commandLine.getSwitch(option1, false));
  }
  
  @Test
  public void getSwitch_shouldReturnDefaultValueIfNoValuePresent() {
    assertTrue(commandLine.getSwitch(option1, true));
  }
  
  @Test
  public void getProperty_shouldReturnPropertyValueIfPresent() {
    String property = "property";
    String value = "value";
    commandLine.addProperty(option1, property, value);
    
    assertEquals(value, commandLine.getProperty(option1, property, null));
  }
  
  @Test
  public void getProperty_shouldReturnDefaultValueIfNoValuePresent() {
    String defaultValue = "defaultValue";
    
    assertEquals(defaultValue, commandLine.getProperty(option1, "property", defaultValue));
  }
  
  @Test
  public void getProperties_shouldReturnPropertiesIfPresent() {
    String property = "property";
    String value = "value";
    commandLine.addProperty(option1, property, value);
    
    assertEquals(Arrays.asList(property), commandLine.getProperties(option1));
  }
  
  @Test
  public void getProperties_shouldReturnEmptyListIfNoPropertiesPresent() {
    assertEquals(0, commandLine.getProperties(option1).size());
  }
  
  @Test
  public void looksLikeOption_shouldReturnTrueIfTriggerStartsWithPrefix() {
    String trigger = "-t";
    option1 = new DefaultOption.Builder(trigger).withPrefixes(Arrays.asList("-")).create();
    
    assertTrue(commandLine.looksLikeOption(trigger));
  }
  
  @Test
  public void looksLikeOption_shouldReturnFalseIfTriggerDoesNotStartWithPrefix() {
    String trigger = "t";
    option1 = new DefaultOption.Builder(trigger).withPrefixes(Arrays.asList("-")).create();
    
    assertFalse(commandLine.looksLikeOption(trigger));
  }
  
  @Test
  public void toString_shouldReturnStringRepresentationOfCommandLine() {
    commandLine.addValue(option1, "value1");
    commandLine.addValue(option1, "value2");
    commandLine.addValue(option2, true);
    commandLine.addValue(option2, false);
    commandLine.addValue(option2, "value3");
    
    assertEquals("value1 value2 -o2 -o2=false value3", commandLine.toString());
  }
  
  @Test
  public void getOptions_shouldReturnListOfOptions() {
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    
    assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
  }
  
  @Test
  public void getOptionTriggers_shouldReturnSetOfTriggers() {
    commandLine.addOption(option1);
    commandLine.addOption(option2);
    
    assertEquals(Arrays.asList("o1", "o2"), commandLine.getOptionTriggers());
  }
  
  @Test
  public void setDefaultValues_shouldSetDefaultValuesForOption() {
    List<String> defaults = Arrays.asList("default1", "default2");
    
    commandLine.setDefaultValues(option1, defaults);
    
    assertEquals(defaults, commandLine.getValues(option1, null));
  }
  
  @Test
  public void setDefaultValues_shouldRemoveDefaultValuesIfPassedNull() {
    List<String> defaults = Arrays.asList("default1", "default2");
    
    commandLine.setDefaultValues(option1, defaults);
    commandLine.setDefaultValues(option1, null);
    
    assertEquals(0, commandLine.getValues(option1, null).size());
  }
  
  @Test
  public void setDefaultSwitch_shouldSetDefaultSwitchForOption() {
    commandLine.setDefaultSwitch(option1, true);
    
    assertTrue(commandLine.getSwitch(option1, false));
  }
  
  @Test
  public void setDefaultSwitch_shouldRemoveDefaultSwitchIfPassedNull() {
    commandLine.setDefaultSwitch(option1, true);
    commandLine.setDefaultSwitch(option1, null);
    
    assertFalse(commandLine.getSwitch(option1, false));
  }
  
  @Test
  public void getNormalised_shouldReturnNormalisedList() {
    List<String> normalised = Arrays.asList("value1", "value2");
    
    assertEquals(normalised, commandLine.getNormalised());
  }
  
}