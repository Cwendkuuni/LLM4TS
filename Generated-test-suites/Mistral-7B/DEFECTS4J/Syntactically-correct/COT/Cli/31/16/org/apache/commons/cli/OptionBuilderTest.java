package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", OptionBuilder.longopt);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArg_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired();
        assertEquals(true, OptionBuilder.required);
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withValueSeparator(';');
        assertEquals(';', OptionBuilder.valuesep);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreate_char() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("longOpt");
        optionBuilder.hasArg();

        Option option = OptionBuilder.create('opt');
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(1, option.getArgs().length);
        assertEquals(false, option.isRequired());
        assertNull(option.getValue());
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("longOpt");

        Option option = OptionBuilder.create();
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreate_String() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("longOpt");

        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getLongOpt());
        assertEquals(null, option.getValue());
        assertNull(option.getArgs());
        assertEquals(false, option.isRequired());
    }
}