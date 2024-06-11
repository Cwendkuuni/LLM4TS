import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    WriteableCommandLineImpl commandLine;
    Option option1;
    Option option2;
    Argument argument1;
    Argument argument2;

    @Before
    public void setup() {
        option1 = new OptionImpl("o1");
        option2 = new OptionImpl("o2");
        argument1 = new ArgumentImpl("a1");
        argument2 = new ArgumentImpl("a2");

        commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option2);
        commandLine.addOption(argument1);
        commandLine.addOption(argument2);
    }

    @Test
    public void testAddOption() {
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertTrue(commandLine.hasOption(argument1));
        assertTrue(commandLine.hasOption(argument2));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(argument1, "value1");
        List<String> values = commandLine.getValues(argument1, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(argument2, "value2");
        values = commandLine.getValues(argument2, null);
        assertEquals(1, values.size());
        assertEquals("value2", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        assertEquals(true, commandLine.getSwitch(option1, null));
        assertEquals(false, commandLine.getSwitch(option2, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_exception() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, false);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertTrue(commandLine.hasOption(argument1));
        assertTrue(commandLine.hasOption(argument2));

        Option option3 = new OptionImpl("o3");
        assertFalse(commandLine.hasOption(option3));
    }

    @Test
    public void testGetOption() {
        assertEquals(option1, commandLine.getOption("o1"));
        assertEquals(option2, commandLine.getOption("o2"));
        assertEquals(argument1, commandLine.getOption("a1"));
        assertEquals(argument2, commandLine.getOption("a2"));

        Option option3 = new OptionImpl("o3");
        assertNull(commandLine.getOption("o3"));
    }

    @Test
    public void testGetValues() {
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("default1");

        List<String> values = commandLine.getValues(argument1, defaultValues);
        assertEquals(defaultValues, values);

        List<String> values2 = new ArrayList<String>();
        values2.add("value2");
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument1, "value2");

        List<String> result = commandLine.getValues(argument1, defaultValues);
        assertEquals(2, result.size());
        assertEquals(values2, result);
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = new ArrayList<String>();
        values.add("value");

        commandLine.addValue(argument1, "value");

        List<String> result = commandLine.getUndefaultedValues(argument1);
        assertEquals(values, result);
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));
        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, false));
        assertNull(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals("value2", commandLine.getProperty("property2"));

        assertEquals("default", commandLine.getProperty("property3", "default"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property1", "value1");

        assertEquals("value1", commandLine.getProperty(option1, "property1", "default"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertTrue(commandLine.getProperties().contains("property1"));
        assertTrue(commandLine.getProperties().contains("property2"));
        assertFalse(commandLine.getProperties().contains("property3"));

        assertTrue(commandLine.getProperties(option1).contains("property1"));
        assertFalse(commandLine.getProperties(option1).contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertTrue(commandLine.looksLikeOption("--o1"));
        assertTrue(commandLine.looksLikeOption("-a1"));
        assertTrue(commandLine.looksLikeOption("--a1"));

        assertFalse(commandLine.looksLikeOption("o1"));
        assertFalse(commandLine.looksLikeOption("a1"));
    }

    @Test
    public void testToString() {
      option1.addValue("o1_value");
      argument1.addValue("a1_value");

      String expected = "-o1 o1_value a1_value";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
      List<Option> options = commandLine.getOptions();

      assertEquals(4, options.size());
      assertTrue(options.contains(option1));
      assertTrue(options.contains(option2));
      assertTrue(options.contains(argument1));
      assertTrue(options.contains(argument2));
    }

    @Test
    public void testGetOptionTriggers() {
      List<String> triggers = new ArrayList<String>();
      triggers.add("o1");
      triggers.add("o2");
      triggers.add("a1");
      triggers.add("a2");

      assertEquals(triggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
      List<String> defaults = new ArrayList<String>();
      defaults.add("default1");

      commandLine.setDefaultValues(argument1, defaults);

      assertEquals(defaults, commandLine.getValues(argument1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
      commandLine.setDefaultSwitch(option1, false);

      assertFalse(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
      List<String> normalised = new ArrayList<String>();
      normalised.add("arg1");
      normalised.add("arg2");

      WriteableCommandLineImpl commandLine2 = new WriteableCommandLineImpl(option1, normalised);

      assertEquals(normalised, commandLine2.getNormalised());
    }
}