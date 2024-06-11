import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

  private Option option;
  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    option = new Option("option", "Option");

    commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
  }

  @Test
  public void testAddOption() {
    commandLine.addOption(option);

    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    commandLine.addValue(option, "value");

    assertEquals(Arrays.asList("value"), commandLine.getValues(option, Collections.emptyList()));
  }

  @Test(expected = IllegalStateException.class)
  public void testAddSwitch_ThrowsIllegalStateException() {
    commandLine.addSwitch(option, true);
    commandLine.addSwitch(option, false);
  }

  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option, true);

    assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
  }

  @Test
  public void testHasOption() {
    assertFalse(commandLine.hasOption(option));

    commandLine.addOption(option);

    assertTrue(commandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    assertNull(commandLine.getOption("option"));

    commandLine.addOption(option);

    assertEquals(option, commandLine.getOption("option"));
  }

  @Test
  public void testGetValues() {
    assertEquals(Collections.emptyList(), commandLine.getValues(option, Collections.emptyList()));

    commandLine.addValue(option, "value");

    assertEquals(Arrays.asList("value"), commandLine.getValues(option, Collections.emptyList()));
  }

  @Test
  public void testGetSwitch() {
    assertNull(commandLine.getSwitch(option, null));

    commandLine.addSwitch(option, true);

    assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty("property", "value");

    assertEquals("value", commandLine.getProperty("property", null));
  }

  @Test
  public void testGetProperty() {
    assertNull(commandLine.getProperty("property", null));

    commandLine.addProperty("property", "value");

    assertEquals("value", commandLine.getProperty("property", null));
  }

  @Test
  public void testGetProperties() {
    commandLine.addProperty("property1", "value1");
    commandLine.addProperty("property2", "value2");

    assertEquals(2, commandLine.getProperties().size());
    assertTrue(commandLine.getProperties().contains("property1"));
    assertTrue(commandLine.getProperties().contains("property2"));
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-option"));

    assertFalse(commandLine.looksLikeOption("value"));
  }

  @Test
  public void testToString() {
    commandLine.addValue(option, "value");

    assertEquals("value", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    commandLine.addOption(option);

    assertEquals(Collections.singletonList(option), commandLine.getOptions());
  }

  @Test
  public void testGetOptionTriggers() {
    commandLine.addOption(option);

    assertEquals(Collections.singleton("option"), commandLine.getOptionTriggers());
  }

  @Test
  public void testSetDefaultValues() {
    commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

    assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option, Collections.emptyList()));
  }

  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option, true);

    assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
  }

  @Test
  public void testGetNormalised() {
    assertEquals(Collections.emptyList(), commandLine.getNormalised());
  }
}