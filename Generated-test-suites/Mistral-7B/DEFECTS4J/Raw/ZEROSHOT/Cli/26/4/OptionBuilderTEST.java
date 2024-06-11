package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilder() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .hasType(String.class)
                .create();

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(true, option.isRequired());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertEquals(String.class, option.getType());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertNull(option.getOpt());
        Assert.assertNull(option.getShortOpt());
    }

    @Test
    public void testOptionBuilderWithShortOpt() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .hasType(String.class)
                .create('s');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(true, option.isRequired());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertEquals(String.class, option.getType());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertEquals('s', option.getOpt());
        Assert.assertNull(option.getShortOpt());
    }

    @Test
    public void testOptionBuilderWithNoArgs() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .create();

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertNull(option.getArgName());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getArgs());
        Assert.assertNull(option.getValueSeparator());
        Assert.assertNull(option.getType());
        Assert.assertNull(option.getArgName());
        Assert.assertNull(option.getOpt());
        Assert.assertNull(option.getShortOpt());
    }

    @Test
    public void testOptionBuilderWithShortOptAndArgs() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArgs(2)
                .create('s');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals(Option.TWO_ARGUMENTS, option.getArgs());
        Assert.assertNull(option.getArgName());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertNull(option.getValueSeparator());
        Assert.assertNull(option.getType());
        Assert.assertEquals('s', option.getOpt());
        Assert.assertNull(option.getShortOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create();
        OptionBuilder.reset();
    }
}