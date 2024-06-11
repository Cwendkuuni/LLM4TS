package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    private Option rootOption;
    private WriteableCommandLine commandLine;

    @org.junit.Before
    public void setUp() {
        final Option opt = new Option("opt", "option", true, "Option value");
        final Argument arg = new Argument("arg", "argument", false, "Argument value");

        rootOption = new OptionBuilder()
            .withLongOpt("root")
            .withDescription("Root option")
            .withValueSeparator('=')
            .withValuePrefix("option:")
            .createOption();

        rootOption.addOption("opt", opt);
        rootOption.addArgument(arg);

        commandLine = new WriteableCommandLineImpl(rootOption, Collections.singletonList("opt=value arg"));
    }

    @Test
    public void testAddOption() {
        final Option opt = new Option("opt2", "option2", false, "Option2");
        commandLine.addOption(opt);

        final List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertSame(opt, options.get(1));
    }

    @Test
    public void testAddValue() {
        final Argument arg = new Argument("arg2", "argument2", false, "Argument2 value");
        commandLine.addArgument(arg);

        final List values = commandLine.getValues(arg, null);
        assertEquals(1, values.size());
        assertEquals("arg", values.get(0));

        commandLine.addValue(arg, "value2");

        final List values2 = commandLine.getValues(arg, null);
        assertEquals(2, values2.size());
        assertSame("arg", values2.get(0));
        assertSame("value2", values2.get(1));
    }

    @Test
    public void testAddSwitch() {
        final Option opt = new Option("opt3", "option3", false, "Option3");
        commandLine.addOption(opt);

        commandLine.addSwitch(opt, true);

        final boolean switchValue = commandLine.getSwitch(opt, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        final Option opt = new Option("opt4", "option4", false, "Option4");
        commandLine.addOption(opt);

        commandLine.addSwitch(opt, true);
        commandLine.addSwitch(opt, false);
    }

    @Test
    public void testHasOption() {
        final Option opt = new Option("opt5", "option5", false, "Option5");
        commandLine.addOption(opt);

        assertTrue(commandLine.hasOption(opt));
    }

    @Test
    public void testGetOption() {
        final Option opt = new Option("opt6", "option6", false, "Option6");
        commandLine.addOption(opt);

        final Option option = commandLine.getOption("opt6");
        assertSame(opt, option);
    }

    @Test
    public void testGetValues() {
        final Argument arg = new Argument("arg3", "argument3", false, "Argument3 value");
        commandLine.addArgument(arg);

        commandLine.addValue(arg, "value3");
        commandLine.addValue(arg, "value4");

        final List values = commandLine.getValues(arg, null);
        assertEquals(2, values.size());
        assertSame("value3", values.get(0));
        assertSame("value4", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        final Argument arg = new Argument("arg4", "argument4", false, "Argument4 value");
        commandLine.addArgument(arg);

        final List values = commandLine.getUndefaultedValues(arg);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        final Option opt = new Option("opt7", "option7", false, "Option7");
        commandLine.addOption(opt);

        final Boolean switchValue = commandLine.getSwitch(opt, false);
        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        final Option propertyOpt = new Option("prop", "property", true, "Property value");
        propertyOpt.setLongOpt("--property");

        commandLine.addOption(propertyOpt);

        commandLine.addProperty(propertyOpt, "propertyName", "propertyValue");

        final String propertyValue = commandLine.getProperty("propertyName");
        assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        final Option propertyOpt = new Option("prop", "property", true, "Property value");
        propertyOpt.setLongOpt("--property");

        commandLine.addOption(propertyOpt);

        commandLine.addProperty(propertyOpt, "propertyName", "propertyValue");

        final Map properties = commandLine.getProperties(propertyOpt);
        assertEquals(1, properties.size());
        assertSame("propertyName", properties.keySet().iterator().next());
        assertEquals("propertyValue", properties.get("propertyName"));
    }

    @Test
    public void testGetProperties() {
        final Option propertyOpt = new Option("prop", "property", true, "Property value");
        propertyOpt.setLongOpt("--property");

        commandLine.addOption(propertyOpt);

        commandLine.addProperty(propertyOpt, "propertyName1", "propertyValue1");
        commandLine.addProperty(propertyOpt, "propertyName2", "propertyValue2");

        final Set properties = commandLine.getProperties(propertyOpt);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("propertyName1"));
        assertTrue(properties.contains("propertyName2"));
    }

    @Test
    public void testToString() {
        assertEquals("opt=value arg arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        final List options = commandLine.getOptions();
        assertEquals(3, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        final Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(5, optionTriggers.size());
    }

    @Test
    public void testSetDefaultValues() {
        final Argument arg = new Argument("arg5", "argument5", false, "Argument5 value");
        commandLine.addArgument(arg);

        final List defaults = new ArrayList();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");

        commandLine.setDefaultValues(arg, defaults);

        final List values = commandLine.getValues(arg, null);
        assertEquals(2, values.size());
        assertSame("defaultValue1", values.get(0));
        assertSame("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        final Option opt = new Option("opt8", "option8", false, "Option8");
        commandLine.addOption(opt);

        commandLine.setDefaultSwitch(opt, true);

        final boolean switchValue = commandLine.getSwitch(opt, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Collections.singletonList("opt=value arg arg2"), commandLine.getNormalised());
    }
}