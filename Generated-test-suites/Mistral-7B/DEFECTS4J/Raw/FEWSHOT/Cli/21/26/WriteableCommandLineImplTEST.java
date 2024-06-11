package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    @Test
    public void testNewInstance() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        assertEquals(rootOption, cmdLine.getRootOption());
        assertEquals(Collections.emptyList(), cmdLine.getOptions());
        assertEquals(Collections.emptyMap(), cmdLine.getNameToOption());
        assertEquals(Collections.emptyMap(), cmdLine.getValues());
        assertEquals(Collections.emptyMap(), cmdLine.getSwitches());
        assertEquals(Collections.emptyMap(), cmdLine.getDefaultValues());
        assertEquals(Collections.emptyMap(), cmdLine.getDefaultSwitches());
        assertEquals(Collections.emptyList(), cmdLine.getNormalised());
        assertEquals(Collections.emptySet(), cmdLine.getOptionTriggers());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("opt1", "opt1", false, "opt1 option");
        cmdLine.addOption(opt1);

        assertEquals(Collections.singletonList(opt1), cmdLine.getOptions());
        assertEquals(opt1, cmdLine.getOption("opt1"));
        assertEquals(Collections.singletonMap(opt1.getPreferredName(), opt1), cmdLine.getNameToOption());
        assertEquals(Collections.emptyMap(), cmdLine.getDefaultValues());
        assertEquals(false, cmdLine.hasOption(new Option("opt2", "opt2", false, "opt2 option")));
    }

    @Test
    public void testAddValue() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        Argument arg = new Argument("arg1", "arg1");
        cmdLine.addOption(arg);

        Option opt = new Option("opt", "opt", false, "opt option");
        cmdLine.addOption(opt);
        cmdLine.addValue(opt, "value1");
        cmdLine.addValue(opt, "value2");

        List values = getValues(cmdLine, opt);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        Option opt = new Option("opt", "opt", false, "opt option");

        cmdLine.addOption(opt);
        cmdLine.addSwitch(opt, true);

        assertEquals(Boolean.TRUE, cmdLine.getSwitch(opt, Boolean.FALSE));
    }

    @Test
    public void testHasOption() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("opt1", "opt1", false, "opt1 option");
        Option opt2 = new Option("opt2", "opt2", false, "opt2 option");

        cmdLine.addOption(opt1);

        assertTrue(cmdLine.hasOption(opt1));
        assertFalse(cmdLine.hasOption(opt2));
    }

    @Test
    public void testGetOption() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("opt1", "opt1", false, "opt1 option");
        Option opt2 = new Option("opt2", "opt2", false, "opt2 option");

        cmdLine.addOption(opt1);
        cmdLine.addOption(opt2);

        Option opt = cmdLine.getOption("opt1");
        assertEquals(opt1, opt);
        assertNull(cmdLine.getOption("opt3"));
    }

    @Test
    public void testGetValues() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        Argument arg = new Argument("arg", "arg");
        cmdLine.addOption(arg);

        Option opt = new Option("opt", "opt", false, "opt option");
        cmdLine.addOption(opt);
        cmdLine.addValue(opt, "value1");
        cmdLine.addValue(opt, "value2");

        List values = getValues(cmdLine, opt);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        Argument arg = new Argument("arg", "arg");
        cmdLine.addOption(arg);

        Option opt = new Option("opt", "opt", false, "opt option");
        cmdLine.addOption(opt);
        cmdLine.addValue(opt, "value1");
        cmdLine.addValue(opt, "value2");

        List values = getUndefaultedValues(cmdLine, opt);
        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        Option opt = new Option("opt", "opt", false, "opt option");

        cmdLine.addOption(opt);
        cmdLine.addSwitch(opt, true);

        assertTrue(cmdLine.getSwitch(opt, Boolean.FALSE).booleanValue());
        assertFalse(cmdLine.getSwitch(opt, Boolean.FALSE).booleanValue());
    }

    @Test
    public void testGetProperty() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        PropertyOption propOption = new PropertyOption("prop", "prop", true);
        cmdLine.addOption(propOption);
        cmdLine.addProperty(propOption, "value");

        assertEquals("value", cmdLine.getProperty("prop"));
        assertNull(cmdLine.getProperty("nonexistentProp"));
    }

    @Test
    public void testAddProperty() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        PropertyOption propOption = new PropertyOption("prop", "prop", true);
        cmdLine.addOption(propOption);

        cmdLine.addProperty(propOption, "value1");
        cmdLine.addProperty(propOption, "value2");

        Properties properties = cmdLine.getProperties(propOption);
        assertEquals(Arrays.asList("value1", "value2"), properties.stringPropertyNames());
        assertEquals("value2", cmdLine.getProperty("prop"));
    }

    @Test
    public void testGetProperties() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        PropertyOption propOption = new PropertyOption("prop", "prop", true);
        cmdLine.addOption(propOption);
        cmdLine.addProperty(propOption, "value1");
        cmdLine.addProperty(propOption, "value2");

        Set properties = cmdLine.getProperties();
        assertEquals(4, properties.size());
        assertTrue(properties.contains("prop"));
        assertTrue(properties.contains("prop.value1"));
        assertTrue(properties.contains("prop.value2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        assertTrue(cmdLine.looksLikeOption("--opt"));
        assertTrue(cmdLine.looksLikeOption("--opt="));
        assertTrue(cmdLine.looksLikeOption("opt"));
        assertFalse(cmdLine.looksLikeOption("val"));
        assertFalse(cmdLine.looksLikeOption(""));
        assertFalse(cmdLine.looksLikeOption("--"));
    }

    @Test
    public void testToString() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Arrays.asList(" arg1", " arg2"));

        assertEquals("arg1 arg2", cmdLine.toString());
    }

    @Test
    public void testGetOptionsList() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("opt1", "opt1", false, "opt1 option");
        Option opt2 = new Option("opt2", "opt2", false, "opt2 option");

        cmdLine.addOption(opt1);
        cmdLine.addOption(opt2);

        assertEquals(Arrays.asList(opt1, opt2), cmdLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("opt1", "opt1", false, "opt1 option");
        Option opt2 = new Option("opt2", "opt2", false, "opt2 option");

        cmdLine.addOption(opt1);
        cmdLine.addOption(opt2);

        assertEquals(Arrays.asList("opt1", "opt2"), cmdLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("opt", "opt", false, "opt option");
        List values = Arrays.asList("value1", "value2");

        cmdLine.addOption(opt);
        cmdLine.setDefaultValues(opt, values);

        assertEquals(values, cmdLine.getValues(opt, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("opt", "opt", false, "opt option");
        Boolean defaultSwitch = Boolean.TRUE;

        cmdLine.addOption(opt);
        cmdLine.setDefaultSwitch(opt, defaultSwitch);

        assertTrue(cmdLine.getSwitch(opt, Boolean.FALSE).booleanValue());
    }

    @Test
    public void testGetNormalised() {
        Option rootOption = new Option("root", "root", false, "root option");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Arrays.asList(" arg1", " arg2"));

        assertEquals(Arrays.asList("arg1", "arg2"), cmdLine.getNormalised());
    }

    private List getValues(WriteableCommandLineImpl cmdLine, Option option) {
        return cmdLine.getValues(option, null);
    }

    private List getUndefaultedValues(WriteableCommandLineImpl cmdLine, Option option) {
        return cmdLine.getUndefaultedValues(option);
    }
}