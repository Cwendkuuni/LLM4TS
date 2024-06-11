package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test creating an option with no arguments.
     */
    @Test
    public void testNoArgs()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("Test option without arguments")
                                     .create('T');

        assertEquals("Test option without arguments", option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with one required argument.
     */
    @Test
    public void testOneArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("Test option with one required argument")
                                     .withArgName("arg")
                                     .create('A');

        assertEquals("Test option with one required argument", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with one optional argument.
     */
    @Test
    public void testOneOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                     .withDescription("Test option with one optional argument")
                                     .withArgName("arg")
                                     .create('O');

        assertEquals("Test option with one optional argument", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with multiple optional arguments.
     */
    @Test
    public void testMultipleOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs(2)
                                     .withDescription("Test option with multiple optional arguments")
                                     .withArgName("arg")
                                     .create('M');

        assertEquals("Test option with multiple optional arguments", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with value separator.
     */
    @Test
    public void testValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(',')
                                     .hasArg()
                                     .withDescription("Test option with value separator")
                                     .create('V');

        assertEquals("Test option with value separator", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a long name.
     */
    @Test
    public void testLongName()
    {
        Option option = OptionBuilder.withLongOpt("mylongoption")
                                     .hasArg()
                                     .withDescription("Test option with long name")
                                     .create('M');

        assertEquals("mylongoption", option.getLongOpt());
        assertEquals("Test option with long name", option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a required argument of boolean type.
     */
    @Test
    public void testBooleanArg()
    {
        Option option = OptionBuilder.hasArg(true)
                                     .withDescription("Test option with boolean argument")
                                     .withType(Boolean.TYPE)
                                     .create('B');

        assertEquals("Test option with boolean argument", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a required argument of int type.
     */
    @Test
    public void testIntArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("Test option with integer argument")
                                     .withType(Integer.TYPE)
                                     .create('I');

        assertEquals("Test option with integer argument", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a required argument of long type.
     */
    @Test
    public void testLongArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("Test option with long argument")
                                     .withType(Long.TYPE)
                                     .create('L');

        assertEquals("Test option with long argument", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a required argument of double type.
     */
    @Test
    public void testDoubleArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("Test option with double argument")
                                     .withType(Double.TYPE)
                                     .create('D');

        assertEquals("Test option with double argument", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a required argument of String type.
     */
    @Test
    public void testStringArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("Test option with string argument")
                                     .withType(String.class)
                                     .create('S');

        assertEquals("Test option with string argument", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }
}