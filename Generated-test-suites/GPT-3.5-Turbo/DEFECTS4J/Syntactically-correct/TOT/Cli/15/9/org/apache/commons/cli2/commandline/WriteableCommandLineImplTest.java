import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  @Test
  public void testAddOption() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.addOption(option);
    assertTrue(cmd.hasOption(option));
  }

  @Test
  public void testAddValue() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.addValue(option, "value1");
    cmd.addValue(option, "value2");
    assertEquals(2, cmd.getValues(option).size());
    assertEquals("value1", cmd.getValues(option).get(0));
    assertEquals("value2", cmd.getValues(option).get(1));
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.addSwitch(option, true);
    assertTrue(cmd.getSwitch(option, false));
  }

  @Test
  public void testHasOption() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option1 = new OptionImpl("option1", "option1", "option1");
    Option option2 = new OptionImpl("option2", "option2", "option2");
    cmd.addOption(option1);
    assertTrue(cmd.hasOption(option1));
    assertFalse(cmd.hasOption(option2));
  }

  @Test
  public void testGetOption() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option1 = new OptionImpl("option1", "option1", "option1");
    Option option2 = new OptionImpl("option2", "option2", "option2");
    cmd.addOption(option1);
    cmd.addOption(option2);
    assertEquals(option1, cmd.getOption("option1"));
  }

  @Test
  public void testGetValues() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.addValue(option, "value1");
    cmd.addValue(option, "value2");
    assertEquals(2, cmd.getValues(option).size());
    assertEquals("value1", cmd.getValues(option).get(0));
    assertEquals("value2", cmd.getValues(option).get(1));
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("default1");
    defaultValues.add("default2");
    assertEquals(defaultValues, cmd.getValues(option, defaultValues));

  }

  @Test
  public void testGetUndefaultedValues() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.addValue(option, "value1");
    cmd.addValue(option, "value2");
    assertEquals(2, cmd.getUndefaultedValues(option).size());
    assertEquals("value1", cmd.getUndefaultedValues(option).get(0));
    assertEquals("value2", cmd.getUndefaultedValues(option).get(1));
  }

  @Test
  public void testGetSwitch() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.addSwitch(option, true);
    assertTrue(cmd.getSwitch(option, false));
    assertNull(cmd.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.addProperty(option, "property1", "value1");
    cmd.addProperty(option, "property2", "value2");
    assertEquals("value1", cmd.getProperty(option, "property1", "default"));
    assertEquals("default", cmd.getProperty(option, "property3", "default"));
  }

  @Test
  public void testGetProperties() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.addProperty(option, "property1", "value1");
    cmd.addProperty(option, "property2", "value2");
    assertTrue(cmd.getProperties(option).contains("property1"));
    assertTrue(cmd.getProperties(option).contains("property2"));
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    cmd.addOption(new OptionImpl("option1", "option1", "option1"));
    cmd.addOption(new OptionImpl("option2", "option2", "option2"));
    cmd.addOption(new OptionImpl("option3", "option3", "option3"));
    assertTrue(cmd.looksLikeOption("option1"));
    assertTrue(cmd.looksLikeOption("option2"));
    assertTrue(cmd.looksLikeOption("option3"));
    assertFalse(cmd.looksLikeOption("nonOption"));
  }

  @Test
  public void testToString() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    cmd.getNormalised().add("arg1");
    cmd.getNormalised().add("arg2");
    cmd.getNormalised().add("arg3");
    assertEquals("arg1 arg2 arg3", cmd.toString());
  }

  @Test
  public void testGetOptions() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option1 = new OptionImpl("option1", "option1", "option1");
    Option option2 = new OptionImpl("option2", "option2", "option2");
    cmd.addOption(option1);
    cmd.addOption(option2);
    assertEquals(2, cmd.getOptions().size());
    assertTrue(cmd.getOptions().contains(option1));
    assertTrue(cmd.getOptions().contains(option2));
  }

  @Test
  public void testGetOptionTriggers() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option1 = new OptionImpl("option1", "option1", "option1");
    Option option2 = new OptionImpl("option2", "option2", "option2");
    cmd.addOption(option1);
    cmd.addOption(option2);
    assertEquals(2, cmd.getOptionTriggers().size());
    assertTrue(cmd.getOptionTriggers().contains("option1"));
    assertTrue(cmd.getOptionTriggers().contains("option2"));
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    List defaultValues = new ArrayList<>();
    defaultValues.add("default1");
    defaultValues.add("default2");
    cmd.setDefaultValues(option, defaultValues);
    assertEquals(defaultValues, cmd.getValues(option));
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    Option option = new OptionImpl("option", "option", "option");
    cmd.setDefaultSwitch(option, true);
    assertTrue(cmd.getSwitch(option, false));
    assertNull(cmd.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
    cmd.getNormalised().add("arg1");
    cmd.getNormalised().add("arg2");
    cmd.getNormalised().add("arg3");
    assertEquals(3, cmd.getNormalised().size());
    assertEquals("arg1", cmd.getNormalised().get(0));
    assertEquals("arg2", cmd.getNormalised().get(1));
    assertEquals("arg3", cmd.getNormalised().get(2));
  }
  
}