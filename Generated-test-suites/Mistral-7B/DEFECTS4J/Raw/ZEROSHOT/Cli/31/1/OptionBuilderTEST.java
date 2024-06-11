package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilder() {
        Option option = OptionBuilderTest.buildOption();
        Assert.assertNotNull(option);
        Assert.assertEquals("testOption", option.getLongOpt());
        Assert.assertEquals("Test Option", option.getDescription());
        Assert.assertEquals(true, option.isRequired());
        Assert.assertEquals(false, option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals(false, option.hasOptionalArg());
        Assert.assertEquals('', option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());

        Option optionWithArg = OptionBuilderTest.buildOptionWithArg();
        Assert.assertEquals(1, optionWithArg.getArgs());
        Assert.assertEquals(true, optionWithArg.hasArg());
        Assert.assertEquals(false, optionWithArg.isRequired());
        Assert.assertEquals('', optionWithArg.getArgName());

        Option optionWithValue = OptionBuilderTest.buildOptionWithValue();
        Assert.assertEquals("testOptionValue", optionWithValue.getLongOpt());
        Assert.assertEquals("Test Option with value", optionWithValue.getDescription());
        Assert.assertEquals(true, optionWithValue.isRequired());
        Assert.assertEquals(1, optionWithValue.getArgs());
        Assert.assertEquals("value", optionWithValue.getArgName());
        Assert.assertEquals('=', optionWithValue.getValueSeparator());
        Assert.assertNull(optionWithValue.getType());
        Assert.assertEquals(false, optionWithValue.hasOptionalArg());

        Option optionWithType = OptionBuilderTest.buildOptionWithType();
        Assert.assertEquals("testOptionType", optionWithType.getLongOpt());
        Assert.assertEquals("Test Option with type", optionWithType.getDescription());
        Assert.assertEquals(true, optionWithType.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, optionWithType.getArgs());
        Assert.assertEquals("type", optionWithType.getArgName());
        Assert.assertNull(optionWithType.getValueSeparator());
        Assert.assertEquals(String.class, optionWithType.getType());
        Assert.assertEquals(false, optionWithType.hasOptionalArg());

        Option optionWithOptionalArg = OptionBuilderTest.buildOptionWithOptionalArg();
        Assert.assertEquals("testOptionOptionalArg", optionWithOptionalArg.getLongOpt());
        Assert.assertEquals("Test Option with optional arg", optionWithOptionalArg.getDescription());
        Assert.assertEquals(true, optionWithOptionalArg.isRequired());
        Assert.assertEquals(1, optionWithOptionalArg.getArgs());
        Assert.assertEquals("arg", optionWithOptionalArg.getArgName());
        Assert.assertEquals('', optionWithOptionalArg.getValueSeparator());
        Assert.assertNull(optionWithOptionalArg.getType());
        Assert.assertEquals(true, optionWithOptionalArg.hasOptionalArg());

        Option optionWithUnlimitedArgs = OptionBuilderTest.buildOptionWithUnlimitedArgs();
        Assert.assertEquals("testOptionUnlimitedArgs", optionWithUnlimitedArgs.getLongOpt());
        Assert.assertEquals("Test Option with unlimited args", optionWithUnlimitedArgs.getDescription());
        Assert.assertEquals(true, optionWithUnlimitedArgs.isRequired());
        Assert.assertEquals(Option.UNLIMITED_VALUES, optionWithUnlimitedArgs.getArgs());
        Assert.assertEquals("arg", optionWithUnlimitedArgs.getArgName());
        Assert.assertEquals('', optionWithUnlimitedArgs.getValueSeparator());
        Assert.assertNull(optionWithUnlimitedArgs.getType());
        Assert.assertEquals(false, optionWithUnlimitedArgs.hasOptionalArg());
    }

    private static Option buildOption() {
        return OptionBuilder.withLongOpt("testOption")
                .withDescription("Test Option")
                .isRequired()
                .create();
    }

    private static Option buildOptionWithArg() {
        return OptionBuilder.withLongOpt("testOptionArg")
                .hasArg()
                .create();
    }

    private static Option buildOptionWithValue() {
        return OptionBuilder.withLongOpt("testOptionValue")
                .withDescription("Test Option with value")
                .isRequired()
                .withArgName("value")
                .setValueSeparator('=')
                .create();
    }

    private static Option buildOptionWithType() {
        return OptionBuilder.withLongOpt("testOptionType")
                .withDescription("Test Option with type")
                .isRequired()
                .withArgName("type")
                .withType(String.class)
                .create();
    }

    private static Option buildOptionWithOptionalArg() {
        return OptionBuilder.withLongOpt("testOptionOptionalArg")
                .withDescription("Test Option with optional arg")
                .isRequired()
                .hasOptionalArg()
                .create();
    }

    private static Option buildOptionWithUnlimitedArgs() {
        return OptionBuilder.withLongOpt("testOptionUnlimitedArgs")
                .withDescription("Test Option with unlimited args")
                .isRequired()
                .hasArgs(Option.UNLIMITED_VALUES)
                .create();
    }
}