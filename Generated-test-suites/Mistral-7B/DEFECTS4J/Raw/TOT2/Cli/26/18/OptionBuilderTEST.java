package org.apache.commons.cli.test;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli.Option.*;
import static org.apache.commons.cli.OptionBuilder.*;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1");
        Option option = optionBuilder.create();
        Assert.assertEquals("option1", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(VALUE_REQUIRED, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(VALUE_OPTIONAL, option.getArgs());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create();
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArgsWithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create();
        Assert.assertEquals(3, option.getArgs());
    }

    @Test
    public void testOptionBuilderWithType() {
        String typeName = String.class.getName();
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        Assert.assertEquals(typeName, option.getType().getClass().getName());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description for OptionBuilder");
        Option option = optionBuilder.create();
        Assert.assertEquals("Description for OptionBuilder", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
    }
}