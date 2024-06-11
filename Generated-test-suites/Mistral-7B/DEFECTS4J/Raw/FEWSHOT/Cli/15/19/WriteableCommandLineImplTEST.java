package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
        assertFalse(writeableCommandLine.hasOption(rootOption));
        assertNull(writeableCommandLine.getValues(rootOption, null));
        assertNull(writeableCommandLine.getSwitch(rootOption, null));
        assertNull(writeableCommandLine.getProperty("test", null));
        assertFalse(writeableCommandLine.looksLikeOption("test"));
        assertEquals("", writeableCommandLine.toString());
    }

    @Test
    public void testAddOption() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        writeableCommandLine.addOption(option);

        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());
        assertTrue(writeableCommandLine.hasOption(option));
        assertTrue(writeableCommandLine.getValues(option, null).isEmpty());
        assertNull(writeableCommandLine.getSwitch(option, null));
        assertNull(writeableCommandLine.getProperty("option", null));
        assertFalse(writeableCommandLine.looksLikeOption("option"));
        assertEquals("", writeableCommandLine.toString());
    }

    @Test
    public void testAddValue() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");

        List valueList = writeableCommandLine.getValues(option, null);

        assertEquals(Collections.singletonList("value1"), valueList);
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("option1")
                .create();

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("option2")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        assertTrue(writeableCommandLine.hasOption(option1));
        assertTrue(writeableCommandLine.hasOption(option2));
        assertFalse(writeableCommandLine.hasOption(new Option("invalid")));
    }

    @Test
    public void testGetOption() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("option1")
                .create();

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("option2")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Option resultOption1 = writeableCommandLine.getOption("option1");
        Option resultOption2 = writeableCommandLine.getOption("option2");

        assertSame(option1, resultOption1);
        assertSame(option2, resultOption2);
    }

    @Test
    public void testGetValues() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List resultValues = writeableCommandLine.getValues(option, null);

        assertEquals(Collections.singletonList(Arrays.asList("value1", "value2")), resultValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .withValueSeparator(' ')
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.singletonList("arg1 arg2"));
        writeableCommandLine.addOption(option);

        List resultValues = writeableCommandLine.getUndefaultedValues(option);

        assertEquals(Collections.singletonList(Arrays.asList("arg1", "arg2")), resultValues);
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        Boolean resultSwitch = writeableCommandLine.getSwitch(option, false);

        assertTrue(resultSwitch);
    }

    @Test
    public void testGetProperty() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "property", "value");

        String resultProperty = writeableCommandLine.getProperty("property", null);

        assertEquals("value", resultProperty);
    }

    @Test
    public void testGetProperties() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Set resultProperties = writeableCommandLine.getProperties(option);

        assertEquals(Sets.newHashSet(Arrays.asList("property1", "property2")), resultProperties);
    }

    @Test
    public void testLooksLikeOption() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        assertTrue(writeableCommandLine.looksLikeOption("test"));
        assertFalse(writeableCommandLine.looksLikeOption("invalid"));
    }

    @Test
    public void testToString() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1 arg2"));

        assertEquals("arg1 arg2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptionsList() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("option1")
                .create();

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("option2")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        assertEquals(Arrays.asList(option1, option2), writeableCommandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("option1")
                .create();

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("option2")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set resultOptionTriggers = writeableCommandLine.getOptionTriggers();

        assertEquals(Sets.newHashSet(Arrays.asList("option1", "option2")), resultOptionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        writeableCommandLine.setDefaultValues(option, defaultValues);

        List resultDefaultValues = writeableCommandLine.getValues(option, null);

        assertEquals(defaultValues, resultDefaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        Boolean defaultSwitch = Boolean.TRUE;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean resultDefaultSwitch = writeableCommandLine.getSwitch(option, Boolean.FALSE);

        assertEquals(defaultSwitch, resultDefaultSwitch);
    }

    @Test
    public void testSetDefaultValuesAndDefaultSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .withDescription("test option")
                .withRequiredArg()
                .create();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("option")
                .create();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        writeableCommandLine.addOption(option);

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        Boolean defaultSwitch = Boolean.TRUE;

        writeableCommandLine.setDefaultValues(option, defaultValues);
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        List resultDefaultValues = writeableCommandLine.getValues(option, null);
        Boolean resultDefaultSwitch = writeableCommandLine.getSwitch(option, Boolean.FALSE);

        assertEquals(defaultValues, resultDefaultValues);
        assertEquals(defaultSwitch, resultDefaultSwitch);
    }
}