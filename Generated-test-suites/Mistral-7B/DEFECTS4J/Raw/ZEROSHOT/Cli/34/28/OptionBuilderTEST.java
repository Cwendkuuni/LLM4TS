package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", optionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArg_hasArgFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testIsRequired_isRequiredFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator_noArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getValueSeparator());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgs_numArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs_numArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(5, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.getType());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option");
        assertEquals("Test Option", optionBuilder.getDescription());
    }

    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .hasArgs()
                .withType(String.class)
                .withDescription("Test Option");

        Option option = optionBuilder.create('T');

        assertEquals("longopt", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(':', option.getValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertSame(String.class, option.getType());
        assertEquals("Test Option", option.getDescription());
    }

    @Test
    public void testCreate_noLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        try
        {
            optionBuilder.create((char) 0);
            fail();
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testReset()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .hasArgs()
                .withType(String.class)
                .withDescription("Test Option");

        Option option = optionBuilder.create('T');

        optionBuilder.reset();

        assertNull(optionBuilder.getLongOpt());
        assertNull(optionBuilder.getArgName());
        assertNull(optionBuilder.getDescription());
        assertNull(optionBuilder.getType());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertFalse(optionBuilder.isRequired());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getValueSeparator());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getArgs());
    }
}