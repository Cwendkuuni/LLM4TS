import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        cmdLine.addValue(option, "value");

        List<String> values = cmdLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        cmdLine.addSwitch(option, true);

        Boolean bool = cmdLine.getSwitch(option, null);
        assertTrue(bool);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        cmdLine.addOption(option);

        Option retrievedOption = cmdLine.getOption(option.getPreferredName());
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        List<String> values = cmdLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        List<String> values = cmdLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        cmdLine.addSwitch(option, true);

        Boolean bool = cmdLine.getSwitch(option, null);
        assertTrue(bool);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        String value = cmdLine.getProperty("property");
        assertNull(value);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        String property = "property";
        String value = "value";
        cmdLine.addProperty(new PropertyOption(), property, value);

        String retrievedValue = cmdLine.getProperty(property);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        String defaultValue = "defaultValue";
        String value = cmdLine.getProperty("property", defaultValue);
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Set<String> properties = cmdLine.getProperties();
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        boolean looksLikeOption = cmdLine.looksLikeOption("-option");
        assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        String str = cmdLine.toString();
        assertNotNull(str);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        List<Option> options = cmdLine.getOptions();
        assertNotNull(options);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Set<String> triggers = cmdLine.getOptionTriggers();
        assertNotNull(triggers);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        cmdLine.setDefaultValues(option, defaults);

        List<String> retrievedDefaults = cmdLine.getValues(option, defaults);
        assertEquals(defaults, retrievedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Argument();
        Boolean defaultSwitch = true;
        cmdLine.setDefaultSwitch(option, defaultSwitch);

        Boolean retrievedDefaultSwitch = cmdLine.getSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, retrievedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        List<String> normalised = cmdLine.getNormalised();
        assertNotNull(normalised);
    }

}