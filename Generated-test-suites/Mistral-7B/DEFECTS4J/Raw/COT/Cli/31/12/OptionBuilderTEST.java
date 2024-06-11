package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.util.Arrays;

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
        Option option = optionBuilder.withLongOpt("option1").create();
        assertEquals("option1", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArg().create();
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArg_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArg(false).create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);

        Option optionWithArg = optionBuilder.hasArg(true).create();
        assertEquals(1, optionWithArg.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.withArgName("argName").create();
        assertEquals("argName", option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator_char()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.withValueSeparator().create();
        assertEquals('=', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequired_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.isRequired(false).create();
        assertFalse(option.isRequired());

        Option optionRequired = optionBuilder.isRequired(true).create();
        assertTrue(optionRequired.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArgs(2).create();
        assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasOptionalArg().create();
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasOptionalArgs().create();
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasOptionalArgs(2).create();
        assertTrue(option.isOptional());
        assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.withType(String.class).create();
        assertEquals(String.class, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.withDescription("Test option").create();
        assertEquals("Test option", option.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreate_char()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.withLongOpt("testOption").create('t');
        assertEquals("testOption", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreate_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.withLongOpt("testOption").create("testOption");
        assertEquals("testOption", option.getLongOpt());
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     */
    @Test
    public void testGetValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArgs(2).create('t');
        option.setValue(new String[]{"test1", "test2"});

        assertEquals("test1", option.getValue(0));
        assertEquals("test2", option.getValue(1));
    }

    /**
     * Test method for {@link Option#getValue()}.
     */
    @Test
    public void testGetValue_noArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.create('t');

        String[] value = option.getValue();
        assertNull(value);
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     *
     * @throws Exception the exception
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_outOfBounds() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArgs(1).create('t');
        option.setValue(new String[]{"test"});

        option.getValue(1);
    }

    /**
     * Test method for {@link Option#getValue()}.
     *
     * @throws Exception the exception
     */
    @Test(expected = IllegalStateException.class)
    public void testGetValue_notSet() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArgs(1).create('t');

        option.getValue();
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     *
     * @throws Exception the exception
     */
    @Test(expected = IllegalStateException.class)
    public void testGetValue_optionalArg_noValue() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasOptionalArg().create('t');

        option.getValue(0);
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     */
    @Test
    public void testGetValue_optionalArg_withValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasOptionalArg().create('t');
        option.setValue("testValue");

        assertEquals("testValue", option.getValue(0));
    }
}