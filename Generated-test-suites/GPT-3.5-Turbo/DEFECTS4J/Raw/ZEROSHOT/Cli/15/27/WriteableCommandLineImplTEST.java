import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.resource.ResourceConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("testArgument", "Test Argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList());
        commandLine.addValue(argument, "value");
        List values = commandLine.getValues(argument, new ArrayList());
        assertEquals(Arrays.asList("value"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, new ArrayList());
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument("testArgument", "Test Argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList());
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        List values = commandLine.getUndefaultedValues(argument);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option, "property1", ""));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option, "property1", ""));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        assertEquals("default", commandLine.getProperty(option, "property1", "default"));
    }

    @Test
    public void testGetPropertiesOption() {
        Option option = new PropertyOption("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addProperty(option, "property1", "value1");
        Set properties = commandLine.getProperties(option);
        assertEquals(Collections.singleton("property1"), properties);
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addProperty(option, "property1", "value1");
        Set properties = commandLine.getProperties();
        assertEquals(Collections.singleton("property1"), properties);
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        assertTrue(commandLine.looksLikeOption("-testOption"));
    }

    @Test
    public void testToString() {
        Argument argument = new Argument("testArgument", "Test Argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, Arrays.asList("arg1", "arg2"));
        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("testOption1", "Test Option 1");
        Option option2 = new Option("testOption2", "Test Option 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("testOption1", "Test Option 1");
        Option option2 = new Option("testOption2", "Test Option 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set triggers = commandLine.getOptionTriggers();
        assertEquals(new HashSet(Arrays.asList("testOption1", "testOption2")), triggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        List values = commandLine.getValues(option, new ArrayList());
        assertEquals(Arrays.asList("default1", "default2"), values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Argument argument = new Argument("testArgument", "Test Argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, Arrays.asList("arg1", "arg2"));
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }
}