import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("test");
        List<String> arguments = new ArrayList<String>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        assertTrue(commandLine.getValues(option, null).contains("value1"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testSwitch");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("test");
        assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("testProperty", "testValue");
        String property = commandLine.getProperty("testProperty");
        assertEquals("testValue", property);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
    }

    @Test
    public void testToString() {
        String output = commandLine.toString();
        assertEquals("", output);
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertEquals(0, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();
        assertEquals(0, triggers.size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testSwitch");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertEquals(0, normalised.size());
    }

}