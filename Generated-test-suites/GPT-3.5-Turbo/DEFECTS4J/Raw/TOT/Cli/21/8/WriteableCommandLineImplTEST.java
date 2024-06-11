import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        Option option1 = new Option();
        Option option2 = new Option();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertFalse(commandLine.hasOption(new Option()));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        Option option = new Argument();
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        Option option = new Option();
        Option option2 = new Option();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option2, false);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
        assertEquals(Boolean.FALSE, commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        Option option = new Option();
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);

        List<String> values = commandLine.getValues(option, null);
        assertEquals(defaults.size(), values.size());
        assertTrue(values.containsAll(defaults));
    }

    @Test
    public void testDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        Option option = new Option();
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));

        assertTrue(commandLine.looksLikeOption("-trigger"));
        assertTrue(commandLine.looksLikeOption("--trigger"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));

        String expected = "arg1 arg2";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        Option option1 = new Option();
        Option option2 = new Option();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));
        Option option1 = new Option("--trigger1");
        Option option2 = new Option("-trigger2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptionTriggers().contains("--trigger1"));
        assertTrue(commandLine.getOptionTriggers().contains("-trigger2"));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList("arg1", "arg2"));

        List<String> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains("arg1"));
        assertTrue(normalised.contains("arg2"));
    }

}