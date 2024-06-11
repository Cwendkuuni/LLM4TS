import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "output directory");
        List arguments = new ArrayList();
        arguments.add("input.txt");
        arguments.add("output");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Test hasOption
        assertTrue(commandLine.hasOption(option));

        // Test getOption
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);

        // Test getValues
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("input.txt", values.get(0));
        assertEquals("output", values.get(1));

        // Test getUndefaultedValues
        List undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(1, undefaultedValues.size());
        assertEquals("output", undefaultedValues.get(0));

        // Test getSwitch
        assertFalse(commandLine.hasSwitch(option, false));
        assertTrue(commandLine.hasSwitch(option, true));

        // Test getProperty
        commandLine.addProperty(option, "myProperty", "value");
        assertEquals("value", commandLine.getProperty(option, "defaultValue"));

        // Test addProperty
        commandLine.addProperty("myProperty2", "value2");
        assertEquals("value", commandLine.getProperty(option, "defaultValue"));
        assertEquals("value2", commandLine.getProperty("myProperty2", "defaultValue2"));

        // Test getProperties
        Set properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("myProperty"));

        // Test looksLikeOption
        assertTrue(commandLine.looksLikeOption("o"));
        assertFalse(commandLine.looksLikeOption("x"));

        // Test toString
        assertEquals("output input.txt", commandLine.toString());

        // Test getOptions
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));

        // Test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));

        // Test setDefaultValues
        commandLine.setDefaultValues(option, new ArrayList());
        assertEquals(0, commandLine.getValues(option, null).size());

        // Test setDefaultSwitch
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, true));
        assertTrue(commandLine.getSwitch(option, false));

        // Test getNormalised
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("output", normalised.get(1));
    }
}