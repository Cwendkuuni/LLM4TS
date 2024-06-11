package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optLong");
        Option option = optionBuilder.create();
        Assert.assertEquals("optLong", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertTrue(option.isRequired());

        OptionBuilder optionBuilderHasArg = OptionBuilder.hasArg(true);
        Option optionHasArg = optionBuilderHasArg.create();
        Assert.assertEquals(1, optionHasArg.getArgs().length);
        Assert.assertFalse(optionHasArg.isRequired());
    }

    @Test
    public void testHasArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());

        OptionBuilder optionBuilderNotRequired = OptionBuilder.isRequired(false);
        Option optionNotRequired = optionBuilderNotRequired.create();
        Assert.assertFalse(optionNotRequired.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilderWithValueSeparator = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilderWithValueSeparator.create();
        Assert.assertEquals(':', option.getValueSeparator());

        OptionBuilder optionBuilderWithValueSeparatorEmpty = OptionBuilder.withValueSeparator();
        Option optionWithValueSeparatorEmpty = optionBuilderWithValueSeparatorEmpty.create();
        Assert.assertEquals('=', optionWithValueSeparatorEmpty.getValueSeparator());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilderHasArgs = OptionBuilder.hasArgs();
        Option option = optionBuilderHasArgs.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);

        OptionBuilder optionBuilderHasArgsTwo = OptionBuilder.hasArgs(2);
        Option optionHasArgsTwo = optionBuilderHasArgsTwo.create();
        Assert.assertEquals(2, optionHasArgsTwo.getArgs().length);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilderHasOptionalArg = OptionBuilder.hasOptionalArg();
        Option option = optionBuilderHasOptionalArg.create();
        Assert.assertFalse(option.isOptional());

        OptionBuilder optionBuilderHasOptionalArgs = OptionBuilder.hasOptionalArgs();
        Option optionHasOptionalArgs = optionBuilderHasOptionalArgs.create();
        Assert.assertTrue(optionHasOptionalArgs.isOptional());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilderWithType = OptionBuilder.withType(String.class);
        Option option = optionBuilderWithType.create();
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilderWithDescription = OptionBuilder.withDescription("Sample option description");
        Option option = optionBuilderWithDescription.create();
        Assert.assertEquals("Sample option description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.create();
        Option option = optionBuilder.create();
    }

    @Test
    public void testCreateWithValidOptions()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optLong")
                .hasArgName("argName")
                .isRequired()
                .hasArgs()
                .hasOptionalArg()
                .withType(String.class)
                .withDescription("Sample option description");
        Option option = optionBuilder.create();
        Assert.assertEquals("optLong", option.getLongOpt());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertTrue(option.isRequired());
        Assert.assertTrue(option.isOptional());
        Assert.assertEquals(String.class, option.getType());
        Assert.assertEquals("Sample option description", option.getDescription());
    }
}