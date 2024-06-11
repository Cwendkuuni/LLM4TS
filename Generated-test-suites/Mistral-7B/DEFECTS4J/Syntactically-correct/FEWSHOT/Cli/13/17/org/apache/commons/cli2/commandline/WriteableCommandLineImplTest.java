package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "output file path");
        Option option2 = new Option("i", "input", true, "input file path");
        Argument arg = new Argument("0", "zero");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList(arg, "input.txt", "output.txt"));

        cmd.addOption(option2);
        cmd.addValue(option2, "input2.txt");
        cmd.addSwitch(option, true);
        cmd.addProperty("test", "property value");

        assertEquals("output.txt zero input2.txt", cmd.toString());
        assertEquals(2, cmd.getOptions().size());
        assertEquals(3, cmd.getNormalised().size());
        assertEquals("output", cmd.getOption(0).getLongOpt());
        assertEquals("input", cmd.getOption(1).getLongOpt());
        assertEquals("output file path", cmd.getOption(0).getDescription());
        assertEquals("input file path", cmd.getOption(1).getDescription());
        assertEquals(true, cmd.hasOption(option));
        assertEquals(true, cmd.getSwitch(option, false));
        assertEquals("property value", cmd.getProperty("test"));
        assertEquals(Arrays.asList("input.txt"), cmd.getValues(option, null));
        assertEquals(Arrays.asList("input2.txt"), cmd.getValues(option2, null));
        assertEquals(true, cmd.looksLikeOption("o"));
    }

    @Test
    public void testWriteableCommandLineImplAddingSameOptionTwice() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);
        cmd.addOption(option);

        assertEquals(1, cmd.getOptions().size());
        assertFalse(cmd.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImplAddingSameSwitchTwice() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addSwitch(option, true);
        cmd.addSwitch(option, true);

        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplAddingSameArgumentTwice() {
        Option option = new Option("o", "output");
        Argument arg = new Argument("0", "zero");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addValue(option, arg);
        cmd.addValue(option, arg);

        assertEquals(1, cmd.getValues(option, null).size());
    }

    @Test
    public void testWriteableCommandLineImplAddingSamePropertyTwice() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Collections.emptyList());

        cmd.addProperty("test", "value1");
        cmd.addProperty("test", "value2");

        assertEquals("value2", cmd.getProperty("test"));
    }

    @Test
    public void testWriteableCommandLineImplGettingNullValues() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addSwitch(option, true);

        assertNull(cmd.getSwitch(option, null));
    }

    @Test
    public void testWriteableCommandLineImplGettingNullDefaultSwitch() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertNull(cmd.getDefaultSwitch(option));
    }

    @Test
    public void testWriteableCommandLineImplGettingNullDefaultValues() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertNull(cmd.getDefaultValues(option));
    }

    @Test
    public void testWriteableCommandLineImplGettingEmptyDefaultValues() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultValues(option, new ArrayList());

        assertEquals(Collections.emptyList(), cmd.getDefaultValues(option));
    }

    @Test
    public void testWriteableCommandLineImplGettingEmptyDefaultSwitch() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, null);

        assertNull(cmd.getDefaultSwitch(option));
    }

    @Test
    public void testWriteableCommandLineImplSettingEmptyDefaultValues() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultValues(option, new ArrayList());

        assertEquals(new ArrayList(), cmd.getDefaultValues(option));
    }

    @Test
    public void testWriteableCommandLineImplSettingEmptyDefaultSwitchTrue() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, Boolean.TRUE);

        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplSettingEmptyDefaultSwitchFalse() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, Boolean.FALSE);

        assertFalse(cmd.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplSettingNullDefaultSwitch() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, null);

        assertNull(cmd.getDefaultSwitch(option));
    }

    @Test
    public void testWriteableCommandLineImplSettingNullDefaultValues() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultValues(option, null);

        assertNull(cmd.getDefaultValues(option));
    }

    @Test
    public void testWriteableCommandLineImplSettingDefaultValues() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        List defaultValues = new ArrayList(Arrays.asList("value1", "value2"));
        cmd.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, cmd.getDefaultValues(option));
    }

    @Test
    public void testWriteableCommandLineImplSettingDefaultSwitchTrue() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, Boolean.TRUE);

        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplSettingDefaultSwitchFalse() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, Boolean.FALSE);

        assertFalse(cmd.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplSettingNullDefaultSwitchTrue() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, null);
        cmd.setDefaultSwitch(option, Boolean.TRUE);

        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplSettingNullDefaultSwitchFalse() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.setDefaultSwitch(option, null);
        cmd.setDefaultSwitch(option, Boolean.FALSE);

        assertFalse(cmd.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplSettingDefaultValuesWithSameValue() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        List defaultValues = new ArrayList(Arrays.asList("value1", "value1"));
        cmd.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, cmd.getDefaultValues(option));
    }

    @Test
    public void testWriteableCommandLineImplSettingDefaultValuesWithSameValueAndNull() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        List defaultValues = new ArrayList(Arrays.asList("value1", null));
        cmd.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, cmd.getDefaultValues(option));
    }

    @Test
    public void testWriteableCommandLineImplSettingDefaultValuesWithSameValueAndEmptyList() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        List defaultValues = new ArrayList(Arrays.asList("value1", Collections.emptyList()));
        cmd.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, cmd.getDefaultValues(option));
    }
}