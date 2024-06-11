package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli.Option.UNINITIALIZED;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option option = optionBuilder.create();
        Assert.assertEquals("optionLong", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertFalse(option.isUnboundedValued());
    }

    @Test
    public void testHasArg_Unlimited()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        Assert.assertTrue(option.isUnboundedValued());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_NoLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.reset(); // just to be sure
        Option option = OptionBuilder.create('');
    }

    @Test
    public void testCreate_OptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertTrue(option.isOptional());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidChar()
    {
        OptionBuilder.reset();
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("\\u0000");
        try {
            Option option = optionBuilder.create();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreate_WithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .withDescription("Test option description");
        Option option = optionBuilder.create();
        Assert.assertEquals("Test option description", option.getDescription());
    }

    @Test
    public void testCreate_RequiredArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().isRequired();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCreate_ValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('option');
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreate_ArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("myArgName");
        Option option = optionBuilder.create('option');
        Assert.assertEquals("myArgName", option.getArgName());
    }

    @Test
    public void testCreate_Type()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Boolean.TYPE);
        Option option = optionBuilder.create('option');
        Assert.assertEquals(Boolean.TYPE, option.getType());
    }
}