import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    commandLine.addOption(option);
    List<Option> options = commandLine.getOptions();
    Assert.assertTrue(options.contains(option));
  }

  @Test
  public void testAddValue() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Argument argument = new Argument();
    argument.setValue("testValue");
    commandLine.addValue(argument, "testValue");
    List values = commandLine.getValues(argument, null);
    Assert.assertTrue(values.contains("testValue"));
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    commandLine.addSwitch(option, true);
    Boolean switchValue = commandLine.getSwitch(option, false);
    Assert.assertEquals(true, switchValue);
  }

  @Test
  public void testHasOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    commandLine.addOption(option);
    boolean hasOption = commandLine.hasOption(option);
    Assert.assertTrue(hasOption);
  }

  @Test
  public void testGetOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    commandLine.addOption(option);
    Option retrievedOption = commandLine.getOption("testOption");
    Assert.assertEquals(option, retrievedOption);
  }

  @Test
  public void testGetValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    Argument argument = new Argument();
    argument.setValue("testValue");
    commandLine.addValue(argument, "testValue");
    List values = commandLine.getValues(argument, null);
    Assert.assertTrue(values.contains("testValue"));
  }

  @Test
  public void testGetUndefaultedValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    Argument argument = new Argument();
    argument.setValue("testValue");
    commandLine.addValue(argument, "testValue");
    List values = commandLine.getUndefaultedValues(argument);
    Assert.assertTrue(values.contains("testValue"));
  }

  @Test
  public void testGetSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    commandLine.addSwitch(option, true);
    Boolean switchValue = commandLine.getSwitch(option, false);
    Assert.assertEquals(true, switchValue);
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new PropertyOption();
    String property = "testProperty";
    String value = "testValue";
    commandLine.addProperty(option, property, value);
    String retrievedValue = commandLine.getProperty(option, property, null);
    Assert.assertEquals(value, retrievedValue);
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("defaultValue");
    commandLine.setDefaultValues(option, defaultValues);
    List<String> retrievedDefaultValues = commandLine.getValues(option, null);
    Assert.assertEquals(defaultValues, retrievedDefaultValues);
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    Option option = new Option("testOption");
    commandLine.setDefaultSwitch(option, true);
    Boolean defaultSwitchValue = commandLine.getSwitch(option, null);
    Assert.assertEquals(true, defaultSwitchValue);
  }

  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("rootOption"), new ArrayList<>());
    List<String> arguments = new ArrayList<>();
    arguments.add("argument1");
    arguments.add("argument2");
    commandLine = new WriteableCommandLineImpl(new Option("rootOption"), arguments);
    List<String> normalised = commandLine.getNormalised();
    Assert.assertEquals(arguments, normalised);
  }
}