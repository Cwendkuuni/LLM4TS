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
    private Option rootOption;
    private List arguments;

    @Before
    public void setUp() {
        rootOption = new Option("root");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        Object value = "testValue";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        commandLine.addSwitch(option, true);
        Boolean bool = commandLine.getSwitch(option, null);
        assertTrue(bool);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        assertNull(commandLine.getOption("testOption"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue");
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(values, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        assertNull(commandLine.getProperty(property));
        Option option = new PropertyOption();
        commandLine.addProperty(option, property, "value");
        assertEquals("value", commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "testProperty";
        String defaultValue = "defaultValue";
        Option option = new PropertyOption();
        assertEquals(defaultValue, commandLine.getProperty(option, property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testGetPropertiesWithoutOption() {
        Set properties = commandLine.getProperties();
        assertEquals(Collections.EMPTY_SET, properties);
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-t";
        assertFalse(commandLine.looksLikeOption(trigger));
        Option option = new Option("testOption");
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        arguments.add(arg1);
        arguments.add(arg2);
        assertEquals(arg1 + " " + arg2, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        String trigger1 = "trigger1";
        Option option1 = new Option(trigger1);
        Option option2 = new Option("trigger2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains(trigger1));
        assertTrue(triggers.contains("trigger2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List normalised = commandLine.getNormalised();
        assertTrue(normalised.contains("arg1"));
        assertTrue(normalised.contains("arg2"));
        assertTrue(normalised.contains(option1));
        assertTrue(normalised.contains(option2));
    }
}