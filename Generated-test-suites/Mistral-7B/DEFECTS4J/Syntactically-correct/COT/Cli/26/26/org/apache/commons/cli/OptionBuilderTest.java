package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        String longOpt = "longOpt";
        OptionBuilder result = optionBuilder.withLongOpt(longOpt);
        assertEquals(longOpt, result.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.hasArg();
        assertEquals(Option.UNINITIALIZED, result.getNumberOfArgs());
        assertEquals(1, result.getNumberOfArgs(false));
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, result.getNumberOfArgs());
        assertEquals(0, result.getNumberOfArgs(false));

        OptionBuilder optionBuilder2 = OptionBuilder.instance();
        OptionBuilder result2 = optionBuilder2.hasArg(true);
        assertEquals(1, result2.getNumberOfArgs(false));
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String argName = "argName";
        OptionBuilder result = optionBuilder.withArgName(argName);
        assertEquals(argName, result.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.isRequired();
        assertTrue(result.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        char valueSeparator = ':';
        OptionBuilder result = optionBuilder.withValueSeparator(valueSeparator);
        assertEquals(valueSeparator, result.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.withValueSeparator();
        assertEquals('=', result.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.isRequired(false);
        assertFalse(result.isRequired());

        OptionBuilder optionBuilder2 = OptionBuilder.instance();
        OptionBuilder result2 = optionBuilder2.isRequired(true);
        assertTrue(result2.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, result.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.hasArgs(3);
        assertEquals(3, result.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.hasOptionalArg();
        assertEquals(1, result.getNumberOfArgs(false));
        assertTrue(result.isOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, result.getNumberOfArgs());
        assertTrue(result.isOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder result = optionBuilder.hasOptionalArgs(3);
        assertEquals(3, result.getNumberOfArgs());
        assertTrue(result.isOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Object)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Object type = new Object();
        OptionBuilder result = optionBuilder.withType(type);
        assertSame(type, result.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "description";
        OptionBuilder result = optionBuilder.withDescription(description);
        assertEquals(description, result.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreateChar()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String longOpt = "longOpt";
        OptionBuilder result = optionBuilder.withLongOpt(longOpt).hasArg().isRequired();
        Option option = result.create('c');
        assertEquals(longOpt, option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(true, option.hasArg());
        assertNull(option.getValue());
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreateString()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String longOpt = "longOpt";
        OptionBuilder result = optionBuilder.withLongOpt(longOpt).hasArg().isRequired();
        Option option = result.create("opt");
        assertEquals(longOpt, option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    /**
     * Test method for {@link OptionBuilder#create(char, String)}.
     */
    @Test
    public void testCreateCharString()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String longOpt = "longOpt";
        OptionBuilder result = optionBuilder.withLongOpt(longOpt).hasArg().isRequired().withDescription("description");
        Option option = result.create('c', "opt");
        assertEquals(longOpt, option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }
}