import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new org.apache.commons.cli2.basic.WriteableCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertEquals("option", commandLine.getOption("o").getLongOpt());
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").build();
        List defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getUndefaultedValues(option);
        assertNull(values);
        assertEquals(2, commandLine.getOption(option).getValues().size());
        assertEquals("default1", commandLine.getOption(option).getValues().get(0));
        assertEquals("default2", commandLine.getOption(option).getValues().get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getOption(option).isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getOption(option).isSet());
        assertFalse(commandLine.getOption(option).getValue().isEmpty());
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        commandLine.addProperty(option, "name", "value");
        assertEquals("value", commandLine.getOption(option).getProperties().get("name"));
    }

    @Test
    public void testAddPropertyDefault() {
        commandLine.addProperty("name", "default");
        assertEquals("default", commandLine.getProperty("name"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testParseCommandLine() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        CommandLineParser parser = new DefaultParser();
        commandLine.parse(parser, new String[]{"--option", "value1", "--option=value2"});
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testParseCommandLineNoOption() {
        CommandLineParser parser = new DefaultParser();
        commandLine.parse(parser, new String[]{"value"});
        List values = commandLine.getUndefaultedValues(Option.builder("o").build());
        assertNull(values);
    }
}