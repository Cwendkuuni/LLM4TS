import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    Option rootOption = new Option("root");
    rootOption.createOption("child1")
      .withDescription("Child 1 option")
      .createOption("grandchild")
        .withDescription("Grandchild option")
        .endOption()
      .endOption()
      .createOption("child2")
        .withDescription("Child 2 option")
        .endOption()
      .endOption();
    commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
  }

  @Test
  public void testAddOption() {
    Option option = new Option("testOption");
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    Option option = new Argument();
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    assertEquals(2, commandLine.getValues(option, null).size());
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("testSwitch");
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("testOption");
    assertFalse(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = commandLine.getOption("root");
    assertNotNull(option);
  }

  @Test
  public void testGetValues() {
    Option option = new Argument();
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    assertEquals(2, commandLine.getValues(option, null).size());
  }

  @Test
  public void testGetUndefaultedValues() {
    Option option = new Argument();
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    assertEquals(2, commandLine.getUndefaultedValues(option).size());
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("testSwitch");
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetProperty() {
    String property = commandLine.getProperty("testProperty");
    assertNull(property);
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty("testProperty", "value");
    String property = commandLine.getProperty("testProperty");
    assertEquals("value", property);
  }

  @Test
  public void testGetPropertyWithDefaultValue() {
    String property = commandLine.getProperty("testProperty", "default");
    assertEquals("default", property);
  }

  @Test
  public void testGetProperties() {
    assertEquals(0, commandLine.getProperties().size());
  }

  @Test
  public void testLooksLikeOption() {
    assertFalse(commandLine.looksLikeOption("test"));
  }

  @Test
  public void testToString() {
    assertEquals("", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    assertEquals(4, commandLine.getOptions().size());
  }

  @Test
  public void testGetOptionTriggers() {
    assertEquals(6, commandLine.getOptionTriggers().size());
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Argument();
    List defaults = new ArrayList();
    defaults.add("default1");
    defaults.add("default2");
    commandLine.setDefaultValues(option, defaults);
    assertEquals(2, commandLine.getValues(option, null).size());
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("testSwitch");
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    assertEquals(0, commandLine.getNormalised().size());
  }
}