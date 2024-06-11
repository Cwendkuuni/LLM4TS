package org.apache.commons.cli2.test;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for WriteableCommandLine interface.
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultParser().parse(new TestWriteableCommandLineBuilder(), "test");
    }

    /**
     * Test method for {@link WriteableCommandLine#addOption(Option)}.
     */
    @Test
    public void testAddOption() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#addValue(Option, Object)}.
     */
    @Test
    public void testAddValue() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getOptionValues("option");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#getValues(Option)}.
     */
    @Test
    public void testGetValues() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultValues(Option, List)}.
     */
    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("option").build();
        List<String> defaults = new ArrayList<>();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)}.
     */
    @Test
    public void testAddSwitch() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("option"));
        Assert.assertTrue(commandLine.isSet("option"));
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultSwitch(Option, Boolean)}.
     */
    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("option").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("option"));
        Assert.assertTrue(commandLine.isSet("option"));
        Assert.assertTrue(commandLine.getOptionValue("option").equals("true"));
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(String, String)}.
     */
    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getPropertyValues("property").get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#looksLikeOption(String)}.
     */
    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }
}