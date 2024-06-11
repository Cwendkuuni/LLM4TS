import org.apache.commons.cli2.BaseOption;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.util.Tests;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        commandLine = Tests.newWriteableCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNull() {
        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullOption() {
        Option option = null;
        commandLine.addValue(option, "value");
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        Option option = Option.builder("option").build();
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("option").build();
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertEquals("default1", commandLine.getUndefaultedValues(option).get(0));
        assertEquals("default2", commandLine.getUndefaultedValues(option).get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getOption("option").getValue().isPresent());
        assertTrue(commandLine.getOption("option").getValue().get());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getOption("option").getValue().isPresent());
        assertTrue(commandLine.getOption("option").getValue().get());
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getOption("option").getProperties().get("property"));
    }

    @Test
    public void testAddPropertyNullOption() {
        Option option = null;
        commandLine.addProperty(option, "property", "value");
        assertNull(commandLine.getOption("option").getProperties());
    }

    @Test
    public void testAddPropertyNullProperty() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addProperty(option, null, "value");
        assertNull(commandLine.getOption("option").getProperties());
    }

    @Test
    public void testAddPropertyNullValue() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", null);
        assertNull(commandLine.getOption("option").getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("--value"));
        assertFalse(commandLine.looksLikeOption("--"));
        assertFalse(commandLine.looksLikeOption(""));
    }

    @Test
    public void testGetCurrentOption() {
        Option option = Option.builder("option").build();
        commandLine.parse(new ParseContext(options));
        commandLine.processOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();
        commandLine.parse(new ParseContext(options));
        commandLine.processOption(option1);
        commandLine.setCurrentOption(option2);
        assertEquals(option2, commandLine.getCurrentOption());
    }
}