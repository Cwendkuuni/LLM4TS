import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new Cmd();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option")
                .hasArg(false)
                .build();
        commandLine.addOption(option);
        assertNotNull(commandLine.getOptions());
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValueToOption() {
        Option option = Option.builder("o").longOpt("option")
                .hasArg(true)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getOptionValue("option");
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetValuesForOption() {
        Option option = Option.builder("o").longOpt("option")
                .hasArg(true)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValue("option");
        assertNotNull(values);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option")
                .hasArg(false)
                .build();
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValue("option");
        assertNotNull(values);
        assertTrue(values.isEmpty());
        assertEquals(2, defaultValues.size());
        assertEquals("defaultValue1", defaultValues.get(0));
        assertEquals("defaultValue2", defaultValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption("option"));
        assertFalse(commandLine.hasOption("option", true));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("o").longOpt("option")
                .build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasOption("option"));
        assertTrue(commandLine.getOptionValue("option") != null);
        assertTrue(commandLine.getOptionValue("option").booleanValue());
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOptionWithArgument() {
        assertTrue(commandLine.looksLikeOption("--option value"));
    }

    @Test
    public void testLooksLikeOptionWithoutArgument() {
        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOptionWithDash() {
        assertTrue(commandLine.looksLikeOption("-o"));
    }

    @Test
    public void testLooksLikeOptionWithDashAndArgument() {
        assertTrue(commandLine.looksLikeOption("-o value"));
    }

    @Test
    public void testLooksLikeOptionWithDashAndOption() {
        Option option = Option.builder("o").longOpt("option")
                .build();
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("-o value"));
    }

    @Test
    public void testLooksLikeOptionWithArgumentAndOtherArgument() {
        assertTrue(commandLine.looksLikeOption("--option value1 --other value2"));
    }

    @Test
    public void testLooksLikeOptionWithArgumentAndSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .hasArg(false)
                .build();
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption("--option --switch"));
    }

    private static class Cmd implements WriteableCommandLine {
        // An empty implementation for testing purposes.
    }
}