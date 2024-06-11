import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

  private WriteableCommandLine commandLine;
  private Option option;

  @Before
  public void setup() {
    commandLine = new WriteableCommandLineImpl();
    option = new Option("myOption", "This is a test option");
  }

  @Test
  public void addOption_validOption_optionAdded() {
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption("myOption"));
  }

  @Test
  public void addValue_validOption_validValue_valueAdded() {
    commandLine.addOption(option);
    commandLine.addValue(option, "value1");
    List<String> values = commandLine.getUndefaultedValues(option);
    assertEquals("value1", values.get(0));
  }

  @Test
  public void getUndefaultedValues_optionWithValues_valuesReturned() {
    commandLine.addOption(option);
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    List<String> values = commandLine.getUndefaultedValues(option);
    assertEquals(Arrays.asList("value1", "value2"), values);
  }

  @Test
  public void setDefaultValues_optionWithDefaultValues_defaultValuesSet() {
    commandLine.addOption(option);
    List<String> defaultValues = Arrays.asList("default1", "default2");
    commandLine.setDefaultValues(option, defaultValues);
    assertEquals(defaultValues, option.getDefaultValues());
  }

  @Test
  public void addSwitch_validOption_switchAdded() {
    commandLine.addOption(option);
    commandLine.addSwitch(option, true);
    assertTrue(option.getSwitchValue());
  }

  @Test(expected = IllegalStateException.class)
  public void addSwitch_duplicateSwitchAdded_exceptionThrown() {
    commandLine.addOption(option);
    commandLine.addSwitch(option, true);
    commandLine.addSwitch(option, false);
  }

  @Test
  public void setDefaultSwitch_optionWithDefaultSwitch_defaultSwitchSet() {
    commandLine.addOption(option);
    commandLine.setDefaultSwitch(option, true);
    assertTrue(option.getDefaultSwitch());
  }

  @Test
  public void addProperty_validOption_validProperty_propertyAdded() {
    commandLine.addOption(option);
    commandLine.addProperty(option, "property1", "value1");
    assertEquals("value1", option.getProperty("property1"));
  }

  @Test
  public void addProperty_defaultProperty_validProperty_defaultPropertyAdded() {
    commandLine.addProperty("property1", "value1");
    assertEquals("value1", commandLine.getDefaultProperty("property1"));
  }

  @Test
  public void looksLikeOption_validOption_trueReturned() {
    assertTrue(commandLine.looksLikeOption("-myOption"));
  }

  @Test
  public void setCurrentOption_validOption_currentOptionSet() {
    commandLine.setCurrentOption(option);
    assertEquals(option, commandLine.getCurrentOption());
  }

  @Test
  public void setCurrentOption_nullOption_noCurrentOptionSet() {
    commandLine.setCurrentOption(option);
    commandLine.setCurrentOption(null);
    assertEquals(null, commandLine.getCurrentOption());
  }

}