package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.hasOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsWithLimit()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArgs(3);
        Option option = optionBuilder.create();
        Assert.assertEquals(3, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option option = optionBuilder.create();
        Assert.assertNull(option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreateWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create("test");
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#create(String)} with an invalid long opt.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.create("test");
    }
}