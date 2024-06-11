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
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.withLongOpt("longOpt");
        option = optionBuilder.create();

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("must specify longopt", optionBuilder.create().getClass());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.hasArg();
        option = optionBuilder.create();

        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.hasArg(true);
        option = optionBuilder.create();

        assertEquals(1, option.getArgs().length);

        optionBuilder.reset();
        optionBuilder.hasArg(false);
        option = optionBuilder.create();

        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.withArgName("argName");
        option = optionBuilder.create();

        assertEquals("argName", option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.isRequired();
        option = optionBuilder.create();

        assertTrue(option.isRequired());

        optionBuilder.reset();
        option = optionBuilder.create();

        assertFalse(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.withValueSeparator(':');
        option = optionBuilder.create();

        assertEquals(':', option.getValueSeparator());

        optionBuilder.reset();
        option = optionBuilder.create();

        assertEquals(Option.DEFAULT_VALUE_SEPARATOR, option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.isRequired(true);
        option = optionBuilder.create();

        assertTrue(option.isRequired());

        optionBuilder.reset();
        option = optionBuilder.create();

        assertFalse(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.hasArgs();
        option = optionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);

        optionBuilder.reset();
        option = optionBuilder.create();

        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.hasArgs(2);
        option = optionBuilder.create();

        assertEquals(2, option.getArgs().length);

        optionBuilder.reset();
        option = optionBuilder.create();

        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.hasOptionalArg();
        option = optionBuilder.create();

        assertTrue(option.isOptional());

        optionBuilder.reset();
        option = optionBuilder.create();

        assertFalse(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.hasOptionalArgs();
        option = optionBuilder.create();

        assertTrue(option.isOptional());

        optionBuilder.reset();
        option = optionBuilder.create();

        assertFalse(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.hasOptionalArgs(2);
        option = optionBuilder.create();

        assertTrue(option.isOptional());

        optionBuilder.reset();
        option = optionBuilder.create();

        assertFalse(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.withType(Integer.TYPE);
        option = optionBuilder.create();

        assertEquals(Integer.TYPE, option.getType());

        optionBuilder.reset();
        option = optionBuilder.create();

        assertEquals(Object.class, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        optionBuilder.withDescription("description");
        option = optionBuilder.create();

        assertEquals("description", option.getLongOpt());
        assertEquals("description", option.getDescription());

        optionBuilder.reset();
        option = optionBuilder.create();

        assertEquals(null, option.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharIllegalArgumentException()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create('A');
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateStringIllegalArgumentException()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create("A");
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }
}