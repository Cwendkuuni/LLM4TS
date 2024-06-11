package org.apache.commons.cli2.test;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for WriteableCommandLine interface
 */
public class WriteableCommandLineTest {

    /**
     * Test method for {@link WriteableCommandLine#addOption(Option)}
     */
    @Test
    public void testAddOption() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();
        Option option = Option.builder("option").build();

        cmdLine.addOption(option);

        Assert.assertNotNull(cmdLine.getOptions().get(0));
        Assert.assertEquals(option, cmdLine.getOptions().get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#addValue(Option, Object)}
     */
    @Test
    public void testAddValue() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();
        Option option = Option.builder("option").build();
        String value = "value";

        cmdLine.addValue(option, value);

        Assert.assertEquals(value, cmdLine.getOptionValues(option).get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#getOptionValues(Option)}
     */
    @Test
    public void testGetOptionValues() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();
        Option option = Option.builder("option").build();
        String value1 = "value1";
        String value2 = "value2";

        cmdLine.addOption(option);
        cmdLine.addValue(option, value1);
        cmdLine.addValue(option, value2);

        List<String> values = cmdLine.getOptionValues(option);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultValues(Option, List)}
     */
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();
        Option option = Option.builder("option").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        cmdLine.setDefaultValues(option, defaultValues);

        List<String> values = cmdLine.getOptionValues(option);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)}
     */
    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();
        Option option = Option.builder("option").build();

        cmdLine.addSwitch(option, true);

        Assert.assertTrue(option.isSet());
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultSwitch(Option, Boolean)}
     */
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();
        Option option = Option.builder("option").build();

        cmdLine.setDefaultSwitch(option, true);

        Assert.assertTrue(option.isSet());
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(String, String)}
     */
    @Test
    public void testAddProperty() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();

        cmdLine.addProperty("property", "value");

        Assert.assertEquals("value", cmdLine.getProperty("property"));
    }

    /**
     * Test method for {@link WriteableCommandLine#looksLikeOption(String)} with valid argument
     */
    @Test
    public void testLooksLikeOptionWithValidArgument() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();

        Assert.assertTrue(cmdLine.looksLikeOption("--option"));
    }

    /**
     * Test method for {@link WriteableCommandLine#looksLikeOption(String)} with invalid argument
     */
    @Test(expected = IllegalStateException.class)
    public void testLooksLikeOptionWithInvalidArgument() {
        WriteableCommandLine cmdLine = MockWriteableCommandLine.createMockCommandLine();

        cmdLine.looksLikeOption("invalidArgument");
    }
}