import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new CommandLineParser().parse("-t, --test");
        Option option = Option.builder("t").longOpt("test").build();
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new CommandLineParser().parse("-t, --test value");
        Option option = Option.builder("t").longOpt("test").build();
        cmd.addValue(option, "testValue");
        List<String> values = cmd.getOptionValues("test");
        assertEquals(1, values.size());
        assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine cmd = new CommandLineParser().parse("--test value1 value2");
        Option option = Option.builder("test").build();
        List<String> values = cmd.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new CommandLineParser().parse("-t, --test");
        Option option = Option.builder("t").longOpt("test").build();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        cmd.setDefaultValues(option, defaultValues);
        List<String> values = cmd.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
        List<String> defaults = cmd.getOptionValues("test");
        assertEquals(2, defaults.size());
        assertEquals("default1", defaults.get(0));
        assertEquals("default2", defaults.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine cmd = new CommandLineParser().parse("");
        Option option = Option.builder("t").build();
        cmd.addSwitch(option, true);
        assertThrows(IllegalStateException.class, () -> cmd.addSwitch(option, true));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new CommandLineParser().parse("");
        Option option = Option.builder("t").longOpt("test").build();
        cmd.addSwitch(option, true);
        assertTrue(cmd.isSet("test"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new CommandLineParser().parse("");
        Option option = Option.builder("t").longOpt("test").build();
        cmd.setDefaultSwitch(option, true);
        assertFalse(cmd.isSet("test"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new CommandLineParser().parse("");
        Option option = Option.builder("p").longOpt("property").build();
        cmd.addProperty(option, "name", "value");
        assertEquals("value", cmd.getOptionValue("property", "name"));
    }

    @Test
    public void testAddPropertyToDefault() {
        WriteableCommandLine cmd = new CommandlineParser().parse("");
        cmd.addProperty("name", "defaultValue");
        assertEquals("defaultValue", cmd.getOptionValue("name"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(cmd.looksLikeOption("--test"));
        assertFalse(cmd.looksLikeOption("--testValue"));
        assertFalse(cmd.looksLikeOption("testValue"));
    }

    @Test
    public void testCurrentOption() {
        Option option = Option.builder("t").longOpt("test").build();
        WriteableCommandLine cmd = new CommandLineParser().parse("--test");
        cmd.setCurrentOption(option);
        assertEquals(option, cmd.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option option = Option.builder("t").longOpt("test").build();
        WriteableCommandLine cmd = new CommandLineParser().parse("--test");
        cmd.setCurrentOption(option);
        Option currentOption = cmd.getCurrentOption();
        cmd.setCurrentOption(null);
        assertNull(cmd.getCurrentOption());
    }
}