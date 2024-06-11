import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parse.ParseResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the WriteableCommandLine interface.
 */
public class WriteableCommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultWriteableCommandLine();
    }

    @After
    public void tearDown() {
        commandLine = null;
    }

    /**
     * Test adding an Option.
     */
    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);
        Option addedOption = commandLine.getOption("o");
        assertEquals(option, addedOption);
    }

    /**
     * Test adding a value to an Option.
     */
    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getValuesForOption("o");
        assertEquals("value", values.get(0));
    }

    /**
     * Test retrieving values for an Option.
     */
    @Test
    public void testGetValuesForOption() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValuesForOption("o");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    /**
     * Test setting default values for an Option.
     */
    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValuesForOption("o");
        assertNull(values);
        assertEquals(2, defaultValues.size());
        assertEquals("defaultValue1", defaultValues.get(0));
        assertEquals("defaultValue2", defaultValues.get(1));
    }

    /**
     * Test adding a switch value to an Option.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    /**
     * Test setting the default state for a Switch.
     */
    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, true);
        boolean switchValue = commandLine.getSwitch("o");
        assertTrue(switchValue);
    }

    /**
     * Test adding a property value.
     */
    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }

    /**
     * Test detecting whether an argument looks like an Option trigger.
     */
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    /**
     * Test parsing the command line.
     */
    @Test
    public void testParse() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);
        ParseResults parseResults = commandLine.parse("-o value");
        List values = parseResults.getValuesForOption("o");
        assertEquals("value", values.get(0));
    }
}