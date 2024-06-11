import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;

public class WriteableCommandLineImplTest {
  private Option option;
  private Argument argument;
  private WriteableCommandLineImpl commandLine;

  @Before
  public void setUp() {
    option = new Option("o", "output", true, "output file");
    argument = new Argument("input", true, "input file");

    commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());
    commandLine.addOption(option);
    commandLine.addOption(argument);
  }

  @Test
  public void testAddOption() {
    assertTrue(commandLine.hasOption(option));
    assertTrue(commandLine.hasOption(argument));
  }

  @Test
  public void testAddValue() {
    commandLine.addValue(option, "output.txt");
    assertEquals("output.txt", commandLine.getValues(option, Collections.emptyList()).get(0));

    commandLine.addValue(argument, "input.txt");
    assertEquals("input.txt", commandLine.getValues(argument, Collections.emptyList()).get(0));
  }

  @Test
  public void testAddSwitch() {
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));

    commandLine.addSwitch(option, false);
    assertFalse(commandLine.getSwitch(option, true));
  }

  @Test
  public void testHasOption() {
    assertTrue(commandLine.hasOption(option));
    assertTrue(commandLine.hasOption(argument));
    assertFalse(commandLine.hasOption(new Option("nonexistent")));
  }

  @Test
  public void testGetOption() {
    assertEquals(option, commandLine.getOption("o"));
    assertEquals(option, commandLine.getOption("output"));
    assertEquals(argument, commandLine.getOption("input"));
    assertNull(commandLine.getOption("nonexistent"));
  }

  @Test
  public void testGetValues() {
    commandLine.addValue(option, "output1.txt");
    commandLine.addValue(option, "output2.txt");

    assertEquals("output1.txt", commandLine.getValues(option, Collections.emptyList()).get(0));
    assertEquals("output2.txt", commandLine.getValues(option, Collections.emptyList()).get(1));

    assertEquals(Collections.emptyList(), commandLine.getValues(argument, Collections.emptyList()));
    assertEquals(Collections.emptyList(), commandLine.getValues(new Option("nonexistent"), Collections.emptyList()));
  }

  @Test
  public void testGetUndefaultedValues() {
    commandLine.addValue(option, "output1.txt");
    commandLine.addValue(option, "output2.txt");

    assertEquals("output1.txt", commandLine.getUndefaultedValues(option).get(0));
    assertEquals("output2.txt", commandLine.getUndefaultedValues(option).get(1));

    assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(argument));
    assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(new Option("nonexistent")));
  }

  @Test
  public void testGetSwitch() {
    commandLine.addSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));

    commandLine.addSwitch(option, false);
    assertFalse(commandLine.getSwitch(option, true));

    assertNull(commandLine.getSwitch(new Option("nonexistent"), true));
  }

  @Test
  public void testGetProperty() {
    commandLine.addProperty(option, "output", "output.txt");
    assertEquals("output.txt", commandLine.getProperty(option, "output", "default"));

    assertEquals("default", commandLine.getProperty(new PropertyOption(), "property", "default"));
  }

  @Test
  public void testAddProperty() {
    commandLine.addProperty(option, "output", "output.txt");
    assertEquals("output.txt", commandLine.getProperty(option, "output", "default"));

    commandLine.addProperty("property", "value");
    assertEquals("value", commandLine.getProperty(new PropertyOption(), "property", "default"));
  }

  @Test
  public void testGetPropertyWithDefault() {
    assertEquals("default", commandLine.getProperty(option, "output", "default"));

    assertEquals("default", commandLine.getProperty(new PropertyOption(), "property", "default"));
  }

  @Test
  public void testGetProperties() {
    commandLine.addProperty(option, "output", "output.txt");
    commandLine.addProperty(option, "format", "text");

    assertEquals(2, commandLine.getProperties(option).size());
    assertTrue(commandLine.getProperties().isEmpty());
    assertTrue(commandLine.getProperties(new PropertyOption()).isEmpty());
  }

  @Test
  public void testLooksLikeOption() {
    assertTrue(commandLine.looksLikeOption("-o"));
    assertTrue(commandLine.looksLikeOption("--output"));
    assertFalse(commandLine.looksLikeOption("input"));
    assertFalse(commandLine.looksLikeOption("nonexistent"));
  }

  @Test
  public void testToString() {
    assertEquals("input", commandLine.toString());

    commandLine.addSwitch(option, true);
    assertEquals("input -o", commandLine.toString());

    commandLine.addValue(option, "output.txt");
    assertEquals("input -o output.txt", commandLine.toString());

    commandLine.addValue(option, "output with spaces.txt");
    assertEquals("input -o output.txt \"output with spaces.txt\"", commandLine.toString());
  }

  @Test
  public void testGetOptions() {
    assertEquals(2, commandLine.getOptions().size());
  }

  @Test
  public void testGetOptionTriggers() {
    assertEquals(3, commandLine.getOptionTriggers().size());
    assertEquals(2, commandLine.getOptionTriggers().size());
    assertTrue(commandLine.getOptionTriggers().contains("o"));
    assertTrue(commandLine.getOptionTriggers().contains("output"));
    assertTrue(commandLine.getOptionTriggers().contains("input"));
  }

  @Test
  public void testSetDefaultValues() {
    commandLine.setDefaultValues(option, Collections.singletonList("output.txt"));
    assertEquals("output.txt", commandLine.getValues(option, Collections.emptyList()).get(0));

    commandLine.setDefaultValues(option, null);
    assertFalse(commandLine.hasOption(option));
  }

  @Test
  public void testSetDefaultSwitch() {
    commandLine.setDefaultSwitch(option, true);
    assertTrue(commandLine.getSwitch(option, false));

    commandLine.setDefaultSwitch(option, null);
    assertFalse(commandLine.hasOption(option));
  }

  @Test
  public void testGetNormalised() {
    assertEquals(Collections.emptyList(), commandLine.getNormalised());
  }
}