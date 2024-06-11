package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Arguments;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Test Option 2")
                .withArgName("VALUE")
                .create();

        commandLine = new WriteableCommandLineImpl(option1, new Arguments(new String[]{"--option1", "value1", "--option2", "value2"}));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option opt = commandLine.getOption("option1");
        assertEquals(option1, opt);
    }

    @Test
    public void testGetValues() {
        List values = commandLine.getValues(option2, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = commandLine.getUndefaultedValues(option2);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        assertFalse(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property", "value");
        Map optionToProperties = commandLine.getOptionToProperties();
        Properties properties = (Properties) optionToProperties.get(option1);
        assertEquals("value", properties.getProperty("property"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyNullOption() {
        commandLine.addProperty(null, "property", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyNullProperty() {
        commandLine.addProperty(option1, null, "value");
    }

    @Test
    public void testAddPropertyDuplicateKey() {
        commandLine.addProperty(option1, "property", "value1");
        commandLine.addProperty(option1, "property", "value2");
        Map optionToProperties = commandLine.getOptionToProperties();
        Properties properties = (Properties) optionToProperties.get(option1);
        assertEquals("value2", properties.getProperty("property"));
        assertEquals(1, properties.size());
    }

    @Test
    public void testToString() {
        assertEquals("--option1 value1 --option2 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option2, Arrays.asList("defaultValue1", "defaultValue2"));
        List values = commandLine.getValues(option2, null);
        assertEquals(2, values.size());
        assertTrue(values.contains("defaultValue1"));
        assertTrue(values.contains("defaultValue2"));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        boolean switchValue = commandLine.getSwitch(option1, null);
        assertTrue(switchValue);
    }

    @Test
    public void testParseException() {
        try {
            WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
            cmdLine.parse();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseExceptionWithMessage() {
        Option option = OptionBuilder.withLongOpt("option")
                .isRequired()
                .create();
        try {
            WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, null);
            cmdLine.parse();
        } catch (ParseException e) {
            assertEquals("Required Option 'option' is missing", e.getMessage());
        }
    }
}