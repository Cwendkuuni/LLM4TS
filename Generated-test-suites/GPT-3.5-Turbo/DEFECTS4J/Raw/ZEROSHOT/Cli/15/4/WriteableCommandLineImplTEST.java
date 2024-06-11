import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class WriteableCommandLineImplTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAddOption() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addOption(option);
        List<Option> options = commandLineImpl.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addValue(argument, "value");
        List<String> values = commandLineImpl.getValues(argument, null);
        assertTrue(values.contains("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addSwitch(option, true);
        Boolean value = commandLineImpl.getSwitch(option, false);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addOption(option);
        assertTrue(commandLineImpl.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addOption(option);
        Option result = commandLineImpl.getOption("name");
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addValue(argument, "value");
        List<String> values = commandLineImpl.getValues(argument, null);
        assertTrue(values.contains("value"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument();
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addValue(argument, "value");
        List<String> values = commandLineImpl.getUndefaultedValues(argument);
        assertTrue(values.contains("value"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addSwitch(option, true);
        Boolean value = commandLineImpl.getSwitch(option, false);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addProperty(option, "property", "value");
        String result = commandLineImpl.getProperty(option, "property", null);
        assertEquals("value", result);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(null, null);
        commandLineImpl.addProperty("property", "value");
        String result = commandLineImpl.getProperty("property");
        assertEquals("value", result);
    }

    @Test
    public void testGetPropertyWithDefault() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addProperty(option, "property", "value");
        String result = commandLineImpl.getProperty(option, "property", "default");
        assertEquals("value", result);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.addProperty(option, "property", "value");
        Set<String> properties = commandLineImpl.getProperties(option);
        assertTrue(properties.contains("property"));
    }

    @Test
    public void testGetAllProperties() {
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(null, null);
        commandLineImpl.addProperty("property1", "value1");
        commandLineImpl.addProperty("property2", "value2");
        Set<String> properties = commandLineImpl.getProperties();
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("name", "desc", 0);
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");
        prefixes.add("/");
        option.setPrefixes(prefixes);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        boolean result = commandLineImpl.looksLikeOption("-name");
        assertTrue(result);
    }

    @Test
    public void testToString() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, arguments);
        String result = commandLineImpl.toString();
        assertEquals("arg1 arg2", result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("name1", "desc1", 0);
        Option option2 = new Option("name2", "desc2", 1);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(null, null);
        commandLineImpl.addOption(option1);
        commandLineImpl.addOption(option2);

        List<Option> options = commandLineImpl.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("name1", "desc1", 0);
        Option option2 = new Option("name2", "desc2", 1);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(null, null);
        commandLineImpl.addOption(option1);
        commandLineImpl.addOption(option2);

        Set<String> triggers = commandLineImpl.getOptionTriggers();
        assertTrue(triggers.contains("name1"));
        assertTrue(triggers.contains("name2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        List<String> defaults = new ArrayList<>();
        defaults.add("default");
        commandLineImpl.setDefaultValues(option, defaults);
        List<String> result = commandLineImpl.getValues(option, null);
        assertEquals(defaults, result);
    }

    @Test
    public void testSetDefaultValuesWithNull() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        List<String> defaults = new ArrayList<>();
        defaults.add("default");
        commandLineImpl.setDefaultValues(option, defaults);
        commandLineImpl.setDefaultValues(option, null);
        List<String> result = commandLineImpl.getValues(option, null);
        assertNotEquals(defaults, result);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.setDefaultSwitch(option, true);
        Boolean result = commandLineImpl.getSwitch(option, false);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testSetDefaultSwitchWithNull() {
        Option option = new Option("name", "desc", 0);
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(option, null);
        commandLineImpl.setDefaultSwitch(option, true);
        commandLineImpl.setDefaultSwitch(option, null);
        Boolean result = commandLineImpl.getSwitch(option, false);
        assertNotEquals(Boolean.TRUE, result);
    }

    @Test
    public void tetGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(null, arguments);
        List<String> result = commandLineImpl.getNormalised();
        assertEquals(arguments, result);
    }
}