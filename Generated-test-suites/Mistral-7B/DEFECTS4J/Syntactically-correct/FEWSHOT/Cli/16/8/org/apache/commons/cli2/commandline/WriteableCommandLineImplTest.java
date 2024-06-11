package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);

        assertEquals(option, cmd.getOption("opt"));
        assertTrue(cmd.hasOption(option));
        assertEquals(1, cmd.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addValue(option, "val1");
        cmd.addValue(option, "val2");

        assertEquals(Arrays.asList("val1", "val2"), cmd.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addSwitch(option, true);

        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);

        assertEquals(option, cmd.getOption("opt"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addValue(option, "val1");
        cmd.addValue(option, "val2");

        assertEquals(Arrays.asList("val1", "val2"), cmd.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addValue(option, "val1");

        assertEquals(Arrays.asList("val1"), cmd.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addSwitch(option, true);

        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "prop", true, "a property");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addProperty(option, "foo", "bar");

        assertEquals("bar", cmd.getProperty(option, "foo"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "prop", true, "a property");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("foo", "bar");
        cmd.addProperty(option, properties);

        assertEquals("bar", cmd.getProperty(option, "foo"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "prop", true, "a property");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("foo1", "bar1");
        properties.setProperty("foo2", "bar2");
        cmd.addProperty(option, properties);

        Set<String> propertiesSet = cmd.getProperties(option);

        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(Arrays.asList("foo1", "foo2")));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), Collections.emptyList());

        assertTrue(cmd.looksLikeOption("o"));
        assertTrue(cmd.looksLikeOption("--opt"));
        assertFalse(cmd.looksLikeOption("foo"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        assertEquals("arg1 arg2", cmd.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "opt1", false, "option1");
        Option option2 = new Option("o2", "opt2", false, "option2");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, Collections.emptyList());

        cmd.addOption(option2);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(option2, cmd.getOptions().get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "opt1", false, "option1");
        Option option2 = new Option("o2", "opt2", false, "option2");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, Collections.emptyList());

        cmd.addOption(option1);
        cmd.addOption(option2);

        assertEquals(2, cmd.getOptionTriggers().size());
        assertTrue(cmd.getOptionTriggers().containsAll(Arrays.asList("o1", "o2")));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        List defaults = Arrays.asList("val1", "val2");
        cmd.setDefaultValues(option, defaults);

        assertEquals(defaults, cmd.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, true);

        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "opt", false, "an option");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        assertEquals(Arrays.asList("arg1", "arg2"), cmd.getNormalised());
    }
}