import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Argument option = new Argument();
        Integer value = 10;
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, Collections.emptyList()).contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option");
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
        Argument option = new Argument();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.addValue(option, "value");
        assertEquals(commandLine.getValues(option, defaultValues), Collections.singletonList("value"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument option = new Argument();
        commandLine.addValue(option, "value");
        assertEquals(commandLine.getUndefaultedValues(option), Collections.singletonList("value"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        assertEquals(commandLine.getSwitch(option, false), false);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(commandLine.getProperty(property), value);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(commandLine.getProperty(property), value);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "property";
        String value = "value";
        assertEquals(commandLine.getProperty(property, value), value);
    }

    @Test
    public void testGetProperties() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertTrue(commandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-option";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String argument = "argument";
        List<String> arguments = new ArrayList<>();
        arguments.add(argument);
        commandLine = new WriteableCommandLineImpl(new Option("root"), arguments);
        assertEquals(commandLine.toString(), argument);
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains("option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(commandLine.getValues(option, Collections.emptyList()), defaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("argument");
        commandLine = new WriteableCommandLineImpl(new Option("root"), arguments);
        assertEquals(commandLine.getNormalised(), arguments);
    }
}