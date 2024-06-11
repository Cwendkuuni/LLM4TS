import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument("argument");

        Object value1 = new Object();
        Object value2 = new Object();

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List values = commandLine.getValues(option, null);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option");

        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option");

        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument("argument");
        List defaultValues = new ArrayList();

        Object value1 = new Object();
        Object value2 = new Object();
        Object value3 = new Object();

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List values = commandLine.getValues(option, defaultValues);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));

        defaultValues.add(value3);

        values = commandLine.getValues(option, defaultValues);
        assertEquals(3, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
        assertTrue(values.contains(value3));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument("argument");

        Object value1 = new Object();
        Object value2 = new Object();

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("switch");

        assertTrue(commandLine.getSwitch(option, true));

        commandLine.addSwitch(option, false);

        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        String property1 = "property1";
        String property2 = "property2";
        String value1 = "value1";
        String value2 = "value2";

        commandLine.addProperty(property1, value1);
        commandLine.addProperty(property2, value2);

        assertEquals(value1, commandLine.getProperty(property1));
        assertEquals(value2, commandLine.getProperty(property2));
    }

    @Test
    public void testAddProperty() {
        Option option = new PropertyOption();
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "property";
        String defaultValue = "default";

        assertNull(commandLine.getProperty(property, defaultValue));

        commandLine.addProperty(property, defaultValue);

        assertEquals(defaultValue, commandLine.getProperty(property, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        String property1 = "property1";
        String property2 = "property2";
        String defaultValue = "default";

        assertNull(commandLine.getProperty(option, property1, defaultValue));
        assertNull(commandLine.getProperty(option, property2, defaultValue));

        commandLine.addProperty(option, property1, defaultValue);
        commandLine.addProperty(option, property2, defaultValue);

        assertTrue(commandLine.getProperties().contains(property1));
        assertTrue(commandLine.getProperties().contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("option");

        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("argument"));

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testToString() {
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");

        Option option = new Option("option");

        commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals("argument1 argument2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        List defaultValues = new ArrayList();
        defaultValues.add("value");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, null);

        assertEquals(0, commandLine.getValues(option, null).size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));

        commandLine.setDefaultSwitch(option, null);

        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");

        Option option = new Option("option");

        commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(arguments, commandLine.getNormalised());
    }
}