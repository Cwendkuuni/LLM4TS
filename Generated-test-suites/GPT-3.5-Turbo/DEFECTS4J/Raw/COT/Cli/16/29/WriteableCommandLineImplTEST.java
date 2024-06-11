
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root", "Root Option");
        List arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        Option option = argument;
        Object value = "testValue";

        commandLine.addValue(option, value);

        assertTrue(commandLine.hasOption(option));
        assertEquals(Collections.singletonList(value), commandLine.getValues(option, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("test", "Test Option");

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "Test Option");

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option));
        assertEquals(true, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";

        assertEquals(value, commandLine.getProperty(property));
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        Option option = new PropertyOption();

        commandLine.addProperty(option, property, value);

        assertEquals(Collections.singleton(property), commandLine.getProperties(option));
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testLooksLikeOption() {
        String option = "-test";
        assertTrue(commandLine.looksLikeOption(option));
    }
}
