import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        // Test addOption
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        Option optionFromCommandLine = commandLine.getOption("o");
        assertEquals(option, optionFromCommandLine);

        // Test addValue
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        assertEquals("test.txt", values.get(0));
        assertEquals(1, values.size());

        // Test addSwitch
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));

        // Test hasOption
        assertTrue(commandLine.hasOption(option));

        // Test getOption
        Option optionFromCommandLine = commandLine.getOption("o");
        assertEquals(option, optionFromCommandLine);

        // Test getValues
        List defaultValues = new ArrayList();
        defaultValues.add("default.txt");
        commandLine.setDefaultValues(option, defaultValues);
        List valuesFromCommandLine = commandLine.getValues(option, defaultValues);
        assertEquals(1, valuesFromCommandLine.size());
        assertEquals("test.txt", valuesFromCommandLine.get(0));

        // Test getUndefaultedValues
        List undefaultedValues = commandLine.getUndefaultedValues(option);
        assertNull(undefaultedValues);

        // Test getSwitch
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        // Test getProperty
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals("value", propertyValue);

        // Test addProperty
        commandLine.addProperty("property2", "defaultValue2");
        Set properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("property", properties.iterator().next());

        // Test getProperties
        Set propertiesAll = commandLine.getProperties();
        assertEquals(2, propertiesAll.size());
        assertTrue(propertiesAll.contains("property"));
        assertTrue(propertiesAll.contains("property2"));

        // Test looksLikeOption
        assertTrue(commandLine.looksLikeOption("o"));

        // Test toString
        assertEquals("o output test.txt", commandLine.toString());

        // Test getListOptions
        List optionsList = commandLine.getOptions();
        assertEquals(1, optionsList.size());
        Option optionFromList = (Option) optionsList.get(0);
        assertEquals(option, optionFromList);

        // Test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));

        // Test setDefaultValues
        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.getDefaultValues(option));

        // Test setDefaultSwitch
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getDefaultSwitch(option, false));

        // Test getNormalised
        List normalised = commandLine.getNormalised();
        assertNotNull(normalised);
    }

    @Test
    public void testWriteableCommandLineImplEmpty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(0, commandLine.getOptions().size());
        assertEquals(0, commandLine.getValues(null, null).size());
        assertFalse(commandLine.hasOption(null));
        assertNull(commandLine.getOption(""));
        assertNull(commandLine.getProperty(""));
    }
}