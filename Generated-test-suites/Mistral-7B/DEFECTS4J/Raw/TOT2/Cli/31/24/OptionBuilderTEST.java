package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        assertEquals("longOpt", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertEquals(true, option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertEquals(1, option.getArgs().length);
        assertEquals(true, option.optionalArg);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Object)}
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#create()}
     */
    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired()
                .withLongOpt("longOpt")
                .hasArg();
        Option option = optionBuilder.create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}
     */
    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired()
                .withLongOpt("longOpt")
                .hasArg()
                .withDescription("description");
        Option option = optionBuilder.create('l');
        assertEquals("description", option.getDescription());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#create(char)} with invalid option char.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidOptionChar()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('%');
    }

    /**
     * Test method for {@link OptionBuilder#create(char)} with missing long opt.
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateMissingLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
    }
}