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
        Option option = optionBuilder.withLongOpt("longOpt").create();
        assertEquals("longOpt", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArg().create();
        assertEquals(1, option.getArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArg(true).create();
        assertEquals(1, option.getArgs());
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
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.withValueSeparator(';').create();
        assertEquals(';', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHasArgsIllegalArgumentException()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs();
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArgs(2).create();
        assertEquals(2, option.getArgs());
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
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasOptionalArgs(2).create();
        assertTrue(option.isOptional());
        assertEquals(2, option.getArgs());
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
        Option option = optionBuilder.withDescription("description").create();
        assertEquals("description", option.getDescription());
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
     * Test method for {@link Option#getValue(int)}.
     */
    @Test
    public void testGetValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasArg().create();
        option.setValue("value");
        assertEquals("value", option.getValue(0));
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     * When option has multiple values.
     */
    @Test
    public void testGetValueMultipleValues()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.hasOptionalArgs().create();
        option.setValue("value1");
        option.setValue("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
}