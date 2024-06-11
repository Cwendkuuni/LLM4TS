import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);
    Option option3 = new Option("option3", "Option 3", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    cli.addOption(option2);
    cli.addOption(option3);

    assertTrue(cli.getOptions().contains(option2));
    assertTrue(cli.getOptions().contains(option3));
  }

  @Test
  public void testAddValue() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    cli.addValue(option1, "value1");
    cli.addValue(option1, "value2");

    List expectedValues = Arrays.asList("value1", "value2");
    assertEquals(expectedValues, cli.getValues(option1, null));

    // Test adding values for non-argument option
    try {
      cli.addValue(option2, "value1");
    } catch (IllegalStateException e) {
      // Exception expected
    }
  }

  @Test
  public void testAddSwitch() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    cli.addSwitch(option1, true);
    cli.addSwitch(option2, false);

    assertTrue(cli.getSwitch(option1, null));
    assertFalse(cli.getSwitch(option2, null));

    // Test adding switch for already existing option
    try {
      cli.addSwitch(option1, false);
    } catch (IllegalStateException e) {
      // Exception expected
    }
  }

  @Test
  public void testHasOption() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    assertTrue(cli.hasOption(option1));
    assertFalse(cli.hasOption(option2));
  }

  @Test
  public void testGetOption() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    assertEquals(option1, cli.getOption("option1"));
    assertNull(cli.getOption("option3"));
  }

  @Test
  public void testGetValues() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    cli.addValue(option1, "value1");
    cli.addValue(option1, "value2");

    List expectedValues = Arrays.asList("value1", "value2");
    assertEquals(expectedValues, cli.getValues(option1, null));

    List expectedDefaultValues = Arrays.asList("default1", "default2");
    cli.setDefaultValues(option1, expectedDefaultValues);

    assertEquals(expectedValues, cli.getValues(option1, null));
    assertEquals(expectedDefaultValues, cli.getValues(option1, expectedDefaultValues));

    List undefaultedValues = cli.getUndefaultedValues(option1);
    assertEquals(expectedValues, undefaultedValues);
  }

  @Test
  public void testGetSwitch() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    cli.addSwitch(option1, true);
    cli.addSwitch(option2, false);

    assertTrue(cli.getSwitch(option1, false));
    assertFalse(cli.getSwitch(option2, true));

    Boolean defaultSwitch = true;
    cli.setDefaultSwitch(option1, defaultSwitch);

    assertTrue(cli.getSwitch(option1, false));
    assertTrue(cli.getSwitch(option1, defaultSwitch));
  }

  @Test
  public void testGetProperty() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    cli.addProperty(option1, "prop1", "value1");
    cli.addProperty(option1, "prop2", "value2");

    assertEquals("value1", cli.getProperty(option1, "prop1", null));
    assertEquals("default", cli.getProperty(option1, "prop3", "default"));

    assertEquals(Collections.EMPTY_SET, cli.getProperties(option2));
  }

  @Test
  public void testLooksLikeOption() {
    Option option1 = new Option("option1", "Option 1", true);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    assertTrue(cli.looksLikeOption("-option1"));
    assertTrue(cli.looksLikeOption("/option1"));
    assertTrue(cli.looksLikeOption("--option1"));
    assertFalse(cli.looksLikeOption("value"));
  }

  @Test
  public void testToString() {
    Option option1 = new Option("option1", "Option 1", true);
    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());
    cli.normalize("arg1 arg2");

    assertEquals("arg1 arg2", cli.toString());
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);
    Option option3 = new Option("option3", "Option 3", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    cli.addOption(option2);
    cli.addOption(option3);

    List expectedOptions = Arrays.asList(option2, option3);
    assertEquals(expectedOptions, cli.getOptions());
  }

  @Test
  public void testGetOptionTriggers() {
    Option option1 = new Option("option1", "Option 1", true);
    Option option2 = new Option("option2", "Option 2", false);
    Option option3 = new Option("option3", "Option 3", false);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    cli.addOption(option2);
    cli.addOption(option3);

    List expectedTriggers = Arrays.asList("option1", "option2", "option3");
    assertEquals(expectedTriggers, new ArrayList(cli.getOptionTriggers()));
  }

  @Test
  public void testSetDefaultValues() {
    Option option1 = new Option("option1", "Option 1", true);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    List defaultValues = Arrays.asList("default1", "default2");
    cli.setDefaultValues(option1, defaultValues);

    assertEquals(defaultValues, cli.getValues(option1, null));

    cli.setDefaultValues(option1, null);
    assertEquals(Collections.EMPTY_LIST, cli.getValues(option1, null));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option1 = new Option("option1", "Option 1", true);

    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());

    Boolean defaultSwitch = true;
    cli.setDefaultSwitch(option1, defaultSwitch);

    assertTrue(cli.getSwitch(option1, false));

    cli.setDefaultSwitch(option1, null);
    assertFalse(cli.getSwitch(option1, true));
  }

  @Test
  public void testGetNormalised() {
    Option option1 = new Option("option1", "Option 1", true);
    WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, new ArrayList());
    cli.normalize("arg1 arg2");

    List expectedArgs = Arrays.asList("arg1", "arg2");
    assertEquals(expectedArgs, cli.getNormalised());
  }
}