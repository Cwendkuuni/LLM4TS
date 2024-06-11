package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.commandline.WriteableCommandLineImplTest.DummyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Tests for {@link WriteableCommandLineImpl}.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testCreateNewWriteableCommandLineImpl() {
        Option rootOption = new DummyOption("root", false, "rootArg");
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(rootOption, writeableCommandLine.getOption(rootOption.getPreferredName()));
        assertEquals(Collections.unmodifiableList(arguments), writeableCommandLine.getNormalised());
        assertEquals(rootOption.getPrefixes(), writeableCommandLine.getPrefixes());
    }

    @Test
    public void testAddOption() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testHasOption() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
        assertFalse(writeableCommandLine.hasOption(new DummyOption("other", false, null)));
    }

    @Test
    public void testGetOption() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);

        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
        assertNull(writeableCommandLine.getOption("other"));
    }

    @Test
    public void testGetValues() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        values = writeableCommandLine.getValues(option, Collections.singletonList("value3"));
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));

        assertFalse(writeableCommandLine.getSwitch(option, false));
        assertFalse(writeableCommandLine.getSwitch(option, true));
        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option, true));
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");

        assertEquals("value", writeableCommandLine.getProperty(option, "property", "default"));
    }

    @Test
    public void testAddProperty() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");

        writeableCommandLine.addProperty(option, "property", properties);

        Set propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(properties.keySet()));
    }

    @Test
    public void testGetProperties() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");
        writeableCommandLine.addProperty(option, "property", properties);

        Set propertiesSet = writeableCommandLine.getProperties();
        assertEquals(0, propertiesSet.size());

        propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(properties.keySet()));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.looksLikeOption("option"));
        assertTrue(writeableCommandLine.looksLikeOption("option-"));
        assertTrue(writeableCommandLine.looksLikeOption("option--"));
        assertFalse(writeableCommandLine.looksLikeOption("other"));
    }

    @Test
    public void testToString() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        writeableCommandLine.addSwitch(option, true);

        String toString = writeableCommandLine.toString();
        String expected = String.join(" ", new String[]{
            "root", option.getShortOpt(), "option", "value1", option.getShortOpt(), "option", "value2"});
        assertEquals(expected, toString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new DummyOption("option1", false, null);
        Option option2 = new DummyOption("option2", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new DummyOption("option1", false, null);
        Option option2 = new DummyOption("option2", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList(option1.getPreferredName(), option2.getPreferredName())));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        Option option = new DummyOption("option", false, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new DummyOption("root", false, "rootArg"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List normalised = writeableCommandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("option", normalised.get(1));
        assertEquals("value1", normalised.get(2));
        assertEquals("value2", normalised.get(3));
    }

    static class DummyOption extends Option {

        public DummyOption(final String longOpt, final boolean required, final String optArg) {
            super(longOpt, optArg, required, null);
        }

        @Override
        public Option setShortOpt(final char shortOpt) {
            return this;
        }

        @Override
        public Option addValue(final String value) {
            return this;
        }

        @Override
        public Option addValues(final String... values) {
            return this;
        }

        @Override
        public Option addOption(final Option option) {
            return this;
        }

        @Override
        public Option addOptions(final Option... options) {
            return this;
        }
    }
}