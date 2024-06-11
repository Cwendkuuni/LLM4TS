import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;



public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Option("test");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Argument();
        commandLine.addValue(option, "value");

        assertTrue(commandLine.getValues(option, null).contains("value"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Option("test");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Option("test");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Option("test");
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Argument();
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Argument();
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Option("test");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new PropertyOption();
        commandLine.addProperty(option, "property", "value");

        Properties properties = (Properties) commandLine.optionToProperties.get(option);
        assertNotNull(properties);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new PropertyOption();
        commandLine.addProperty(option, "property", "value");

        assertEquals(1, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        assertTrue(commandLine.looksLikeOption("--test"));
        assertTrue(commandLine.looksLikeOption("-t"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--root"), new ArrayList());

        commandLine.addValue(new Argument(), "value");
        commandLine.addValue(new Option("test"), "value");

        assertEquals("value value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Option("test");
        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Option("test");
        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Argument();

        List<String> defaults = new ArrayList<>();
        defaults.add("default");

        commandLine.setDefaultValues(option, defaults);

        List<String> values = commandLine.getValues(option, null);
        assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), arguments);

        List<String> normalised = commandLine.getNormalised();
      
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains("arg1"));
        assertTrue(normalised.contains("arg2"));
    }
}

