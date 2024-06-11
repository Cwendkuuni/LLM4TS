package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test creating an option with no arguments.
     */
    @Test
    public void testCreateNoArgs()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("opt1")
                                        .isRequired()
                                        .create('o');
        Option option = ob.getOption();
        assertEquals("opt1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertTrue(option.isRequired());
        assertNull(option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with one required argument.
     */
    @Test
    public void testCreateOneArg()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("opt1")
                                        .isRequired()
                                        .hasArg()
                                        .withArgName("arg1")
                                        .create('o');
        Option option = ob.getOption();
        assertEquals("opt1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals("arg1", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with one optional argument.
     */
    @Test
    public void testCreateOneOptionalArg()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("opt1")
                                        .isRequired(false)
                                        .hasArg()
                                        .withArgName("arg1")
                                        .create('o');
        Option option = ob.getOption();
        assertEquals("opt1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertFalse(option.isRequired());
        assertEquals("arg1", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with multiple arguments.
     */
    @Test
    public void testCreateMultipleArgs()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("opt1")
                                        .isRequired()
                                        .hasArgs()
                                        .withArgName("arg1")
                                        .withArgName("arg2")
                                        .create('o');
        Option option = ob.getOption();
        assertEquals("opt1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals("arg1,arg2", option.getArgName());
        assertTrue(option.getArgs().length > 0);
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a value separator.
     */
    @Test
    public void testCreateValueSeparator()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("opt1")
                                        .isRequired()
                                        .hasArg()
                                        .withArgName("arg1")
                                        .withValueSeparator(':')
                                        .create('o');
        Option option = ob.getOption();
        assertEquals("opt1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals("arg1", option.getArgName());
        assertEquals(":", option.getValueSeparator());
        // test with value
        Option opt = ob.create('o', new String[]{"arg1:value1", "arg1:value2"});
        assertEquals("[value1, value2]", opt.getValue());
    }

    /**
     * Test creating an option with a value.
     */
    @Test
    public void testCreateValue()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("opt1")
                                        .isRequired()
                                        .hasArg()
                                        .withArgName("arg1")
                                        .withValue("value1")
                                        .create('o');
        Option option = ob.getOption();
        assertEquals("opt1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals("arg1", option.getArgName());
        assertEquals("value1", option.getValue());
    }

    /**
     * Test creating an option with a type.
     */
    @Test
    public void testCreateType()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("opt1")
                                        .isRequired()
                                        .hasArg()
                                        .withArgName("arg1")
                                        .withType(Integer.class)
                                        .create('o');
        Option option = ob.getOption();
        assertEquals("opt1", option.getLongOpt());
        assertEquals('o', option.getOpt());
        assertTrue(option.isRequired());
        assertEquals("arg1", option.getArgName());
        assertEquals(Integer.class, option.getType());
    }
}