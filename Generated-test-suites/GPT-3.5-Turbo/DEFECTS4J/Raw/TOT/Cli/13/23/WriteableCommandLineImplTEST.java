import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    Option option1 = new OptionBuilder().withPreferredName("option1").create();
    Option option2 = new OptionBuilder().withPreferredName("option2").create();
    Argument argument1 = new ArgumentBuilder().withName("argument1").create();
    Argument argument2 = new ArgumentBuilder().withName("argument2").create();

    commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("value1", "value2"));
    commandLine.addOption(option2);
    commandLine.addOption(argument1);
    commandLine.addOption(argument2);
  }

  @Test
  public void testAddOption() {
    Option option3 = new OptionBuilder().withPreferredName("option3").create();
    
    commandLine.addOption(option3);

    assertEquals(4, commandLine.getOptions().size());
    assertTrue(commandLine.getOptions().contains(option3));
  }

  @Test
  public void testAddValue() {
    Argument argument1 = (Argument) commandLine.getOption("argument1");
    
    commandLine.addValue(argument1, "value3");

    assertTrue(commandLine.getValues(argument1, null).contains("value3"));
  }

  @Test
  public void testAddSwitch() {
    Option option2 = (Option) commandLine.getOption("option2");
    
    commandLine.addSwitch(option2, true);

    assertTrue(commandLine.getSwitch(option2, false));
  }

  @Test
  public void testHasOption() {
    Option option1 = (Option) commandLine.getOption("option1");
    Option option3 = new OptionBuilder().withPreferredName("option3").create();

    assertTrue(commandLine.hasOption(option1));
    assertFalse(commandLine.hasOption(option3));
  }

  @Test
  public void testGetOption() {
    Option option2 = (Option) commandLine.getOption("option2");
    Option option3 = new OptionBuilder().withPreferredName("option3").create();

    assertSame(option2, commandLine.getOption("option2"));
    assertNull(commandLine.getOption("option3"));
  }

  @Test
  public void testGetValues() {
    Argument argument1 = (Argument) commandLine.getOption("argument1");
    Argument argument2 = (Argument) commandLine.getOption("argument2");

    assertTrue(commandLine.getValues(argument1, null).contains("value1"));
    assertTrue(commandLine.getValues(argument1, null).contains("value2"));
    assertTrue(commandLine.getValues(argument2, null).isEmpty());
    assertTrue(commandLine.getValues(argument1, Arrays.asList("default1", "default2")).isEmpty());
  }

  @Test
  public void testGetSwitch() {
    Option option2 = (Option) commandLine.getOption("option2");

    assertFalse(commandLine.getSwitch(option2, true));
    assertTrue(commandLine.getSwitch(option2, false));
    assertTrue(commandLine.getSwitch(option2, null));
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");

    assertTrue(commandLine.getProperties().contains("property1"));
    assertTrue(commandLine.getProperties().contains("property2"));
    assertEquals("value1", commandLine.getProperty("property1", null));
    assertEquals("value2", commandLine.getProperty("property2", "default"));
    assertNull(commandLine.getProperty("property3", null));
  }

  @Test
  public void testLookLikeOption() {
    assertTrue(commandLine.looksLikeOption("-option1"));
    assertTrue(commandLine.looksLikeOption("--option2"));
    assertFalse(commandLine.looksLikeOption("value1"));
    assertFalse(commandLine.looksLikeOption("\"value2\""));
    assertFalse(commandLine.looksLikeOption("$option1"));
  }

  @Test
  public void testToString() {
    assertEquals("value1 value2", commandLine.toString());
  }

  @Test
  public void testGetOptionTriggers() {
    assertTrue(commandLine.getOptionTriggers().contains("-option1"));
    assertTrue(commandLine.getOptionTriggers().contains("--option2"));
    assertFalse(commandLine.getOptionTriggers().contains("--option3"));
  }

  @Test
  public void testSetDefaultValues() {
    Argument argument1 = (Argument) commandLine.getOption("argument1");
    
    commandLine.setDefaultValues(argument1, Arrays.asList("default1", "default2"));

    assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(argument1, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option2 = (Option) commandLine.getOption("option2");
    
    commandLine.setDefaultSwitch(option2, true);

    assertTrue(commandLine.getSwitch(option2, false));
  }

  @Test
  public void testGetNormalised() {
    assertEquals(Arrays.asList("value1", "value2"), commandLine.getNormalised());
  }
}