import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
  private WriteableCommandLineImpl commandLine;
  private Option option;
  private Argument argument;
  
  @Before
  public void setup() {
    option = new Option("option", "Option");
    argument = new Argument("argument", "Argument");
    List arguments = new ArrayList<>();
    arguments.add("argument1");
    arguments.add("argument2");
    commandLine = new WriteableCommandLineImpl(option, arguments);
  }
  
  @Test
  public void testAddOption() {
    Option newOption = new Option("newOption", "New Option");
    commandLine.addOption(newOption);
    assertTrue(commandLine.getOptions().contains(newOption));
  }
  
  @Test
  public void testAddValue() {
    commandLine.addValue(argument, "value");
    assertTrue(commandLine.getValues(argument, new ArrayList<>()).contains("value"));
  }
  
  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));
  }
  
  @Test
  public void testHasOption() {
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    Option returnedOption = commandLine.getOption("option");
    assertEquals(option, returnedOption);
  }
  
  @Test
  public void testGetValues() {
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("defaultValue1");
    defaultValues.add("defaultValue2");
    commandLine.setDefaultValues(argument, defaultValues);
    List<String> values = commandLine.getValues(argument, null);
    assertEquals(values, defaultValues);
  }
  
  @Test
  public void testGetSwitch() {
    commandLine.setDefaultSwitch(option, true);
    Boolean switchValue = commandLine.getSwitch(option, null);
    assertTrue(switchValue == true);
  }
  
  @Test
  public void testAddProperty() {
    commandLine.addProperty("property1", "value1");
    assertEquals("value1", commandLine.getProperty("property1", null));
  }
  
  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-option"));
  }
  
  @Test
  public void testToString() {
    assertEquals("argument1 argument2", commandLine.toString());
  }
  
  @Test
  public void testGetOptions() {
    List<Option> options = commandLine.getOptions();
    assertTrue(options.contains(option));
  }
  
  @Test
  public void testGetOptionTriggers() {
    assertTrue(commandLine.getOptionTriggers().contains("option"));
  }
  
  @Test
  public void testSetDefaultValues() {
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("defaultValue1");
    defaultValues.add("defaultValue2");
    commandLine.setDefaultValues(argument, defaultValues);
    assertTrue(commandLine.getDefaultValues(argument).containsAll(defaultValues));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getDefaultSwitch(option));
  }
  
  @Test
  public void testGetNormalised() {
    List<String> normalisedArguments = commandLine.getNormalised();
    assertEquals(normalisedArguments.get(0), "argument1");
    assertEquals(normalisedArguments.get(1), "argument2");
  }
}