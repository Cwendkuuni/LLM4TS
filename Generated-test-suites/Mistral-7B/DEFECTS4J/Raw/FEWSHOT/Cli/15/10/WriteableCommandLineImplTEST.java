package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImplNewInstance() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, null);
        assertNull(cmd.getOption(null));
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.getOptionTriggers().isEmpty());
        assertNull(cmd.toString());
        assertFalse(cmd.hasOption(null));
    }

    @Test
    public void testWriteableCommandLineImplAddOption() {
        Option opt = new Option("o", "opt", false, "Option testing");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        cmd.addOption(opt);
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getOptionTriggers().size());
        assertEquals(opt, cmd.getOption(opt.getPreferredName()));
        assertEquals(opt, cmd.getOption(opt.getShortName()));
        assertEquals(opt.getLongOpt(), cmd.getOption(opt.getLongOpt()).getLongOpt());
        assertEquals(opt.getShortName(), cmd.getOption(opt.getShortName()).getShortName());
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
        assertEquals(Collections.singleton(opt.getPreferredName()), cmd.getOptionTriggers());
    }

    @Test
    public void testWriteableCommandLineImplAddOptionWithValue() {
        Option opt = new Option("o", "opt", true, "Option testing with value");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        cmd.addValue(opt, "value");
        cmd.addOption(opt);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getOptionTriggers().size());
        assertEquals(opt, cmd.getOption(opt.getPreferredName()));
        assertEquals(opt, cmd.getOption(opt.getShortName()));
        assertEquals(opt.getLongOpt(), cmd.getOption(opt.getLongOpt()).getLongOpt());
        assertEquals(opt.getShortName(), cmd.getOption(opt.getShortName()).getShortName());
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
        assertEquals(Collections.singleton(opt.getPreferredName()), cmd.getOptionTriggers());
        assertEquals(Collections.singleton("value"), cmd.getValues(opt, null));
    }

    @Test
    public void testWriteableCommandLineImplAddOptionWithSwitch() {
        Option opt = new Option("o", "opt", true, "Option testing with switch");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        cmd.addSwitch(opt, true);
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getOptionTriggers().size());
        assertEquals(opt, cmd.getOption(opt.getPreferredName()));
        assertEquals(opt, cmd.getOption(opt.getShortName()));
        assertEquals(opt.getLongOpt(), cmd.getOption(opt.getLongOpt()).getLongOpt());
        assertEquals(opt.getShortName(), cmd.getOption(opt.getShortName()).getShortName());
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
        assertEquals(Collections.singleton(opt.getPreferredName()), cmd.getOptionTriggers());
        assertTrue(cmd.getSwitch(opt, null));
    }

    @Test
    public void testWriteableCommandLineImplAddOptionWithValueAndSwitch() {
        Option opt = new Option("o", "opt", true, "Option testing with value and switch");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        cmd.addValue(opt, "value");
        cmd.addSwitch(opt, true);
        cmd.addOption(opt);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getOptionTriggers().size());
        assertEquals(opt, cmd.getOption(opt.getPreferredName()));
        assertEquals(opt, cmd.getOption(opt.getShortName()));
        assertEquals(opt.getLongOpt(), cmd.getOption(opt.getLongOpt()).getLongOpt());
        assertEquals(opt.getShortName(), cmd.getOption(opt.getShortName()).getShortName());
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
        assertEquals(Collections.singleton(opt.getPreferredName()), cmd.getOptionTriggers());
        assertEquals(Collections.singleton("value"), cmd.getValues(opt, null));
        assertTrue(cmd.getSwitch(opt, null));
    }

    @Test
    public void testWriteableCommandLineImplAddProperty() {
        Option opt = new Option("o", "opt", false, "Option testing with property");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        cmd.addProperty(opt, "property", "value");

        Properties properties = (Properties) cmd.optionToProperties.get(opt);
        assertNotNull(properties);
        assertEquals("value", properties.getProperty("property"));
    }

    @Test
    public void testWriteableCommandLineImplAddPropertyWithEmptyValue() {
        Option opt = new Option("o", "opt", false, "Option testing with property");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        cmd.addProperty(opt, "property", "");

        Properties properties = (Properties) cmd.optionToProperties.get(opt);
        assertNotNull(properties);
        assertNull(properties.getProperty("property"));
    }

    @Test
    public void testWriteableCommandLineImplGetProperty() {
        Option opt = new Option("o", "opt", false, "Option testing with property");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        cmd.addProperty(opt, "property", "value");

        assertEquals("value", cmd.getProperty(opt, "property"));
    }

    @Test
    public void testWriteableCommandLineImplGetPropertyWithDefaultValue() {
        Option opt = new Option("o", "opt", false, "Option testing with property");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        assertEquals("", cmd.getProperty(opt, "property"));
    }

    @Test
    public void testWriteableCommandLineImplGetProperties() {
        Option opt = new Option("o", "opt", false, "Option testing with property");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        Properties properties = new Properties();
        properties.put("property1", "value1");
        cmd.addProperty(opt, "property1", "value1");
        cmd.addProperty(opt, "property2", "value2");

        Set propertiesSet = cmd.getProperties(opt);

        assertIterableEquals(Arrays.asList("property1", "property2"), propertiesSet);
    }

    @Test
    public void testWriteableCommandLineImplGetPropertiesForAllOptions() {
        Option opt1 = new Option("o1", "opt1", false, "Option testing with property for option1");
        Option opt2 = new Option("o2", "opt2", false, "Option testing with property for option2");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, null);

        Properties properties = new Properties();
        properties.put("property1_1", "value1_1");
        properties.put("property1_2", "value1_2");
        cmd.addProperty(opt1, "property1_1", "value1_1");
        cmd.addProperty(opt1, "property1_2", "value1_2");
        cmd.addProperty(opt2, "property2_1", "value2_1");
        cmd.addProperty(opt2, "property2_2", "value2_2");

        Set propertiesSet = cmd.getProperties();

        assertIterableEquals(Collections.singleton("property1_1"), cmd.getProperties(opt1).keySet());
        assertIterableEquals(Collections.singleton("property1_2"), cmd.getProperties(opt1).keySet());
        assertIterableEquals(Collections.singleton("property2_1"), cmd.getProperties(opt2).keySet());
        assertIterableEquals(Collections.singleton("property2_2"), cmd.getProperties(opt2).keySet());
        assertIterableEquals(Arrays.asList("property1_1", "property1_2", "property2_1", "property2_2"), propertiesSet);
    }

    @Test
    public void testWriteableCommandLineImplSetDefaultValues() {
        Option opt = new Option("o", "opt", true, "Option testing with default values");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        cmd.setDefaultValues(opt, defaultValues);

        List values = cmd.getValues(opt, null);
        assertEquals(defaultValues, values);

        List values2 = cmd.getUndefaultedValues(opt);
        assertEquals(Collections.emptyList(), values2);
    }

    @Test
    public void testWriteableCommandLineImplSetDefaultSwitch() {
        Option opt = new Option("o", "opt", true, "Option testing with default switch");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(opt, null);

        cmd.setDefaultSwitch(opt, true);

        assertTrue(cmd.getSwitch(opt, null));

        cmd.setDefaultSwitch(opt, false);

        assertFalse(cmd.getSwitch(opt, null));
    }

    @Test
    public void testWriteableCommandLineImplToString() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, new ArrayList());

        cmd.addOption(new Option("o1", "opt1", false, "Option testing with toString"));
        cmd.addOption(new Option("o2", "opt2", false, "Option testing with toString"));

        cmd.addValue(new Option("a", "arg", true, "Option testing with toString"), "value");

        cmd.setDefaultValues(new Option("d", "defaultValue", true, "Default value"), new ArrayList());

        assertEquals("value opt1 opt2", cmd.toString());
    }
}