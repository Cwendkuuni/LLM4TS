package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest {

    @Test
    public void testOptionBuilder() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withDescription("description")
                .create('l');

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertFalse(option.isOptional());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertNull(option.getValue());

        option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withArgName("argName")
                .isRequired(false)
                .withDescription("description")
                .create('l');

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.isOptional());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertNull(option.getValue());

        option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs(2)
                .withDescription("description")
                .create('l');

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.isOptional());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(2, option.getArgs().length);
        Assert.assertNull(option.getValue());

        option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArg()
                .withDescription("description")
                .create('l');

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertNull(option.getArgName());
        Assert.assertTrue(option.isOptional());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertNull(option.getValue());

        option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArgs(2)
                .withDescription("description")
                .create('l');

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertNull(option.getArgName());
        Assert.assertTrue(option.isOptional());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithType() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withType(Integer.TYPE)
                .withDescription("description")
                .create('l');

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertFalse(option.isOptional());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertNotNull(option.getValue());
        Assert.assertTrue(option.getValue() instanceof Number);
        Assert.assertEquals(0, ((Number) option.getValue()).intValue());

        option.setValue("123");
        Assert.assertEquals(123, ((Number) option.getValue()).intValue());

        option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withArgName("argName")
                .isRequired(false)
                .withType(Integer.TYPE)
                .withDescription("description")
                .create('l');

        option.setValue("123");
        Assert.assertEquals(123, ((Number) option.getValue()).intValue());

        option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs(2)
                .withType(String[].class)
                .withDescription("description")
                .create('l');

        option.setValue(new String[]{"arg1", "arg2"});
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, option.getValue());

        option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArgs(2)
                .withType(String[].class)
                .withDescription("description")
                .create('l');

        option.setValue(new String[]{"arg1", "arg2"});
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, option.getValue());

        option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArgs(2)
                .withValueSeparator(':')
                .withType(String[].class)
                .withDescription("description")
                .create('l');

        option.setValue(Arrays.asList("arg1", "arg2").toArray(new String[0]));
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, option.getValue());
    }
}