import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testConstructor() {
        Option rootOption = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList());

        assertEquals(rootOption, commandLine.getOption("test"));
        assertEquals(Arrays.asList(), commandLine.getOptions());
        assertEquals(Collections.EMPTY_SET, commandLine.getOptionTriggers());
        assertEquals(Collections.EMPTY_LIST, commandLine.getNormalised());
        assertFalse(commandLine.hasOption(new Option("non-existent", false, false, "Non-existent option")));
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList());

        Option option1 = new Option("option1", "option1", false, "Option 1");
        commandLine.addOption(option1);

        assertEquals(Arrays.asList(rootOption, option1), commandLine.getOptions());
        assertEquals(Arrays.asList("test", "option1"), commandLine.getOptionTriggers());

        Option option2 = new Option("option2", "option2", false, "Option 2");
        commandLine.addOption(option2);

        assertEquals(Arrays.asList(rootOption, option1, option2), commandLine.getOptions());
        assertEquals(Arrays.asList("test", "option1", "option2"), commandLine.getOptionTriggers());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option, Collections.emptyList()));

        commandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, Collections.emptyList()));

        commandLine.addValue(option, "value3");
        assertEquals(Arrays.asList("value1", "value2", "value3"), commandLine.getValues(option, Collections.emptyList()));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, Boolean.FALSE));
        assertTrue(commandLine.getSwitch(option, Boolean.TRUE));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("non-existent", false, false, "Non-existent option")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option");
        assertSame(option, retrievedOption);
        assertNull(commandLine.getOption("non-existent"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(Arrays.asList("value1", "value2"), values);

        List emptyValues = commandLine.getValues(option, Collections.singletonList(new Option("empty", false, false, "Empty option")));
        assertEquals(Collections.EMPTY_LIST, emptyValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(Arrays.asList("value1", "value2"), values);

        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        List defaultValues = commandLine.getValues(option, Collections.emptyList());
        List unfaultedValues = commandLine.getUndefaultedValues(option);

        assertEquals(Arrays.asList("default1", "default2"), defaultValues);
        assertEquals(Arrays.asList(), unfaultedValues);

        commandLine.setDefaultValues(option, null);
        values = commandLine.getUndefaultedValues(option);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        assertFalse(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
        assertFalse(commandLine.getSwitch(option, Boolean.FALSE));
        assertTrue(commandLine.getSwitch(option, Boolean.TRUE));

        commandLine.setDefaultSwitches(option, Arrays.asList(Boolean.TRUE, Boolean.FALSE));
        Boolean defaultSwitch = commandLine.getSwitch(option, null);
        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);

        assertTrue(defaultSwitch);
        assertFalse(switchValue);

        commandLine.setDefaultSwitches(option, null);
        switchValue = commandLine.getSwitch(option, Boolean.FALSE);
        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option, "property1", "defaultValue"));

        commandLine.addProperty(option, "property2", "value2");
        commandLine.addProperty(option, "property1", "value3");
        assertEquals("value3", commandLine.getProperty(option, "property1", "defaultValue"));

        commandLine.addProperty(option, "property2", null);
        assertEquals("value2", commandLine.getProperty(option, "property2", "defaultValue"));

        commandLine.addProperty(option, null, "defaultValue");
        assertEquals("defaultValue", commandLine.getProperty(option, null, "defaultValue"));

        commandLine.setDefaultValues(option, Arrays.asList());
        assertEquals(null, commandLine.getProperty(option, "property1", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option", "option", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Map properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));

        commandLine.addProperty(option, "property1", null);
        properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertNull(properties.get("property1"));
        assertEquals("value2", properties.get("property2"));

        commandLine.setDefaultValues(option, Arrays.asList());
        properties = commandLine.getProperties(option);
        assertEquals(0, properties.size());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("option1", "option1", false, "Option 1");
        Option option2 = new Option("option2", "option2", false, "Option 2");
        Option rootOption = new Option("root", "root", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList(option1, option2));

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addSwitch(option2, true);

        String toString = commandLine.toString();
        String expected = String.format("%s %s %s %s %s %s %s %s",
                rootOption.getLongOpt(), rootOption.getOpt(),
                option1.getLongOpt(), option1.getOpt(),
                option1.getValueStrings()[0], option1.getValueStrings()[1],
                option2.getLongOpt(), option2.getOpt());

        assertEquals(expected, toString);
    }

    @Test
    public void testGetOptionsEmptyList() {
        Option rootOption = new Option("root", "root", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList());

        List options = commandLine.getOptions();
        assertEquals(Collections.emptyList(), options);
    }

    @Test
    public void testGetOptionTriggersEmptySet() {
        Option rootOption = new Option("root", "root", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList());

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(Collections.emptySet(), optionTriggers);
    }

    @Test
    public void testGetNormalisedEmptyList() {
        Option rootOption = new Option("root", "root", false, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList());

        List normalised = commandLine.getNormalised();
        assertEquals(Collections.emptyList(), normalised);
    }
}