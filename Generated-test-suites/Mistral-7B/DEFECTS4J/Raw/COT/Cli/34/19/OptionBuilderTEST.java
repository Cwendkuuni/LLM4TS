package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli.Option.UNINITIALIZED;

/**
 * Test suite for the OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = builder.create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder builder = OptionBuilder.hasArg();
        Option option = builder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isRequired());

        builder.reset();
        builder.hasArg(true);
        option = builder.create();
        Assert.assertEquals(1, option.getArgs());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        Option option = builder.create();
        Assert.assertEquals(1, option.getArgs());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder builder = OptionBuilder.hasArgs();
        Option option = builder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder builder = OptionBuilder.hasArgs(2);
        Option option = builder.create();
        Assert.assertEquals(2, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithUnsetLongOpt()
    {
        OptionBuilder builder = OptionBuilder.instance();
        builder.create();
    }

    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder builder = OptionBuilder.withDescription("testDescription");
        Option option = builder.create();
        Assert.assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testCreateWithValueSeparator()
    {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        Option option = builder.create('T');
        Assert.assertEquals(':', option.getValueSeparator());

        builder.reset();
        builder.withValueSeparator();
        option = builder.create('T');
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testCreateWithArgName()
    {
        OptionBuilder builder = OptionBuilder.withArgName("testArgName");
        Option option = builder.create('T');
        Assert.assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testCreateWithIsRequired()
    {
        OptionBuilder builder = OptionBuilder.isRequired();
        Option option = builder.create('T');
        Assert.assertTrue(option.isRequired());

        builder.reset();
        builder.isRequired(false);
        option = builder.create('T');
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testCreateWithOptionalArgAndHasArg()
    {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        Option option = builder.create('T');
        Assert.assertEquals(1, option.getArgs());
        Assert.assertFalse(option.isRequired());

        builder.reset();
        builder.hasArg();
        option = builder.create('T');
        Assert.assertEquals(1, option.getArgs());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCreateWithType()
    {
        OptionBuilder builder = OptionBuilder.withType(String.class);
        Option option = builder.create('T');
        Assert.assertEquals(String.class, option.getType());
    }
}