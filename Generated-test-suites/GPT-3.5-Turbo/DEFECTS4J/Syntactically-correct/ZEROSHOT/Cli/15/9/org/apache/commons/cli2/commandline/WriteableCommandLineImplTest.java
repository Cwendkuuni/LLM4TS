import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
  private Option option;
  private Argument arg;
  private WriteableCommandLineImpl cmd;

  @Before
  public void setUp() {
    option = new OptionImpl("o", "option", false, "Option");
    arg = new ArgumentImpl("arg", false, "Argument");
    cmd = new WriteableCommandLineImpl(option, Collections.singletonList("value"));
  }

  @Test
  public void testAddOption() {
    cmd.addOption(option);
    assertTrue(cmd.hasOption(option));
    assertEquals(option, cmd.getOption("o"));
  }

  @Test
  public void testAddValue() {
    cmd.addValue(arg, "value");
    assertEquals("value", cmd.getValues(arg, null).get(0));
  }

  @Test
  public void testAddSwitch() {
    cmd.addSwitch(option, true);
    assertTrue(cmd.getSwitch(option, false));
  }

  @Test
  public void testHasOption() {
    assertTrue(cmd.hasOption(option));
  }

  @Test
  public void testGetOption() {
    assertEquals(option, cmd.getOption("o"));
  }

  @Test
  public void testGetValues() {
    cmd.addValue(arg, "value");
    assertEquals("value", cmd.getValues(arg, null).get(0));
  }

  @Test
  public void testGetUndefaultedValues() {
    cmd.addValue(arg, "value");
    assertEquals("value", cmd.getUndefaultedValues(arg).get(0));
  }

  @Test
  public void testGetSwitch() {
    cmd.addSwitch(option, true);
    assertTrue(cmd.getSwitch(option, false));
  }

  @Test
  public void testGetProperty() {
    cmd.addProperty(option, "property", "value");
    assertEquals("value", cmd.getProperty(option, "property", null));
  }

  @Test
  public void testAddProperty() {
    cmd.addProperty(option, "property", "value");
    assertEquals("value", cmd.getProperty(option, "property", null));
  }

  @Test
  public void testLooksLikeOption() {
    assertFalse(cmd.looksLikeOption("value"));
    assertTrue(cmd.looksLikeOption("-o"));
  }

  @Test
  public void testToString() {
    assertEquals("value", cmd.toString());
  }

  @Test
  public void testGetOptions() {
    cmd.addOption(option);
    assertEquals(option, cmd.getOptions().get(0));
  }

  @Test
  public void testGetOptionTriggers() {
    cmd.addOption(option);
    assertTrue(cmd.getOptionTriggers().contains("o"));
  }

  @Test
  public void testSetDefaultValues() {
    cmd.setDefaultValues(option, Collections.singletonList("default"));
    assertEquals("default", cmd.getValues(option, null).get(0));
  }

  @Test
  public void testSetDefaultSwitch() {
    cmd.setDefaultSwitch(option, true);
    assertTrue(cmd.getSwitch(option, false));
  }

  @Test
  public void testGetNormalised() {
    assertEquals(Collections.singletonList("value"), cmd.getNormalised());
  }
}