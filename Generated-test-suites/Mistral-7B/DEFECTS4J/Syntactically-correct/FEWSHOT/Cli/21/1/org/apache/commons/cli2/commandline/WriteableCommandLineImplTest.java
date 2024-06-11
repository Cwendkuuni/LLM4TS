package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.argument.ArgType;
import org.apache.commons.cli2.argument.MultiValuedArgument;
import org.apache.commons.cli2.option.OptionBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicFunctionality() {
        List args = Arrays.asList("app", "--option1", "arg1", "--option2", "arg2");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();
        Option option2 = OptionBuilder.withName("option2")
                .withDescription("description2")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        assertEquals(Arrays.asList("app"), cmd.getCommandLineArgs());
        assertEquals(Arrays.asList(option1), cmd.getOptions());
        assertEquals("description1", option1.getDescription());
        assertEquals("arg1", cmd.getValue(option1).get(0).toString());
        assertEquals(Arrays.asList("arg2"), cmd.getValue(option2));
    }

    @Test
    public void testAddOption() {
        List args = Arrays.asList("app");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();
        Option option2 = OptionBuilder.withName("option2")
                .withDescription("description2")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        cmd.addOption(option2);

        assertEquals(Arrays.asList("app"), cmd.getCommandLineArgs());
        assertEquals(Arrays.asList(option1, option2), cmd.getOptions());
        assertEquals("description1", option1.getDescription());
        assertTrue(cmd.hasOption(option2));
        assertNull(cmd.getValue(option2));
    }

    @Test
    public void testAddValue() {
        List args = Arrays.asList("app", "--option1", "arg1", "--option1", "arg2");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        cmd.addValue(option1, "arg3");

        assertEquals(Arrays.asList("app"), cmd.getCommandLineArgs());
        assertEquals(Arrays.asList(option1), cmd.getOptions());
        assertEquals("description1", option1.getDescription());
        assertEquals(Arrays.asList("arg1", "arg2", "arg3"), cmd.getValue(option1));
    }

    @Test
    public void testAddSwitch() {
        List args = Arrays.asList("app", "--option1", "--option2");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        cmd.addSwitch(option1, true);

        assertEquals(Arrays.asList("app"), cmd.getCommandLineArgs());
        assertEquals(Arrays.asList(option1), cmd.getOptions());
        assertEquals("description1", option1.getDescription());
        assertTrue(cmd.hasOption(option1));
        assertTrue(cmd.getSwitch(option1, false));
    }

    @Test
    public void testGetOption() {
        List args = Arrays.asList("app", "--option1", "arg1");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        Option retrievedOption = cmd.getOption("option1");

        assertEquals(option1, retrievedOption);
    }

    @Test
    public void testGetValues() {
        List args = Arrays.asList("app", "--option1", "arg1", "--option1", "arg2", "arg3");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        List values = cmd.getValues(option1, null);

        assertEquals(Arrays.asList("arg1", "arg2", "arg3"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        List args = Arrays.asList("app", "--option1", "arg1");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        List values = cmd.getUndefaultedValues(option1);

        assertEquals(Arrays.asList("arg1"), values);
    }

    @Test
    public void testGetSwitch() {
        List args = Arrays.asList("app", "--option1");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        cmd.addSwitch(option1, true);

        assertTrue(cmd.getSwitch(option1, false));
        assertFalse(cmd.getSwitch(option1, true));
    }

    @Test
    public void testgetProperty() {
        Option option = new OptionBuilder()
                .withName("option")
                .withDescription("description")
                .withLongOpt("long-option")
                .withArgName("value")
                .withArgType(ArgType.STRING)
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.addProperty(option, "property", "value");

        assertEquals("value", cmd.getProperty(option, "default"));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder()
                .withName("option")
                .withDescription("description")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        Map<String, String> properties = new LinkedHashMap<>();
        properties.put("property1", "value1");
        cmd.addProperty(option, "property", properties);

        Map<String, String> result = cmd.getProperties(option);

        assertEquals(properties, result);
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder()
                .withName("option")
                .withDescription("description")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.addProperty(option, "property1", "value1");
        cmd.addProperty(option, "property2", "value2");

        Map<String, String> result = cmd.getProperties();

        assertEquals(2, result.size());
        assertEquals("value1", result.get("property1"));
        assertEquals("value2", result.get("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);

        assertTrue(cmd.looksLikeOption("--option"));
        assertFalse(cmd.looksLikeOption("--"));
        assertFalse(cmd.looksLikeOption("foo"));
    }

    @Test
    public void testToString() {
        List args = Arrays.asList("app", "--option1", "arg1");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        assertEquals("app --option1 arg1", cmd.toString());
    }

    @Test
    public void testGetOptionsList() {
        List args = Arrays.asList("app");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        cmd.addOption(OptionBuilder.withName("option2")
                .withDescription("description2")
                .create());

        assertEquals(Arrays.asList(option1, option2), cmd.getOptions());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder()
                .withName("option")
                .withDescription("description")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        List defaults = Arrays.asList("value1", "value2");
        cmd.setDefaultValues(option, defaults);

        List values = cmd.getValues(option, null);

        assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder()
                .withName("option")
                .withDescription("description")
                .create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        cmd.setDefaultSwitch(option, true);

        assertTrue(cmd.getSwitch(option, false));
        assertTrue(cmd.getSwitch(option, true));
    }

    @Test
    public void testGetNormalisedList() {
        List args = Arrays.asList("app", "--option1", "arg1", "--option2", "--option1", "arg2");
        Option option1 = OptionBuilder.withName("option1")
                .withDescription("description1")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, args);

        assertEquals(Arrays.asList("app", "--option1", "arg1", "--option1", "arg2"), cmd.getNormalised());
    }
}