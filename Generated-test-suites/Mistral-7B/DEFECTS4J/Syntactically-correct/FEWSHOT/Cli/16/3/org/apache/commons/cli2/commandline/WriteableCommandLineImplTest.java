package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertEquals("testOption", cmd.getOption(0).getName());
        assertEquals(Collections.emptyList(), cmd.getValues(option, null));
        assertFalse(cmd.hasOption(option));
        assertFalse(cmd.getSwitch(option, false));
        assertNull(cmd.getProperty(option, "default"));
        assertNull(cmd.getProperty(option, "default", "defaultValue"));
        assertEquals(Collections.emptySet(), cmd.getProperties(option));
        assertEquals(Collections.emptySet(), cmd.getProperties());
        assertFalse(cmd.looksLikeOption("--testOption"));
        assertEquals("", cmd.toString());
        assertEquals(Collections.singletonList(option), cmd.getOptions());
        assertEquals(Collections.singleton("testOption"), cmd.getOptionTriggers());
        assertNull(cmd.getDefaultValues(option));
        assertNull(cmd.getDefaultSwitch(option));
        assertNull(cmd.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        assertEquals(Collections.singletonList(option), cmd.getOptions());
        Option newOption = OptionBuilder.withName("newOption")
                .create();
        cmd.addOption(newOption);
        assertEquals(Arrays.asList(option, newOption), cmd.getOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = OptionBuilder.withName("testOption").hasArg(true)
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addValue(option, "value1");
        assertEquals(Arrays.asList("value1"), cmd.getValues(option, null));
        cmd.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), cmd.getValues(option, null));
    }

    @Test
    public void testAddSwitches() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
        cmd.addSwitch(option, false);
        assertFalse(cmd.getSwitch(option, false));
    }

    @Test
    public void testGetValues() {
        Option option = OptionBuilder.withName("testOption").hasArg(true)
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        assertEquals(Arrays.asList("arg1", "arg2"), cmd.getValues(option, null));
        assertEquals(Arrays.asList("arg1"), cmd.getValues(option, null, "arg1"));
        assertEquals(Arrays.asList("arg2"), cmd.getValues(option, null, "arg2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = OptionBuilder.withName("testOption").hasArg(true)
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        assertEquals(Collections.emptyList(), cmd.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
        cmd.addSwitch(option, false);
        assertFalse(cmd.getSwitch(option, false));
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addProperty(option, "property", "value");
        assertEquals("value", cmd.getProperty(option, "property"));
        assertEquals("value", cmd.getProperty(option, "property", null));
    }

    @Test
    public void testHasOption() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testToString() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option,Arrays.asList("arg1", "arg2"));
        assertEquals("arg1 arg2", cmd.toString());
    }

    @Test
    public void testGetOptionsAndOptionTriggers() {
        Option option1 = OptionBuilder.withName("test1")
                .create();
        Option option2 = OptionBuilder.withName("test2")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, Arrays.asList("arg1"));
        cmd.addOption(option2);
        assertEquals(Arrays.asList(option1, option2), cmd.getOptions());
        assertEquals(Arrays.asList("test1", "test2"), cmd.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        List values = Arrays.asList("val1", "val2");
        cmd.setDefaultValues(option, values);
        assertEquals(values, cmd.getDefaultValues(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.setDefaultSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = OptionBuilder.withName("testOption")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "--testOption=arg2", "arg3"));
        assertEquals(Arrays.asList("--testOption=arg2", "arg1", "arg3"), cmd.getNormalised());
    }
}