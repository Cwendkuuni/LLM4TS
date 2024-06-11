package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
     * Test create method with no arguments.
     */
    @Test
    public void testCreateNoArgs()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }

    /**
     * Test create method with optional single argument.
     */
    @Test
    public void testCreateOptionalArg()
    {
        Option option = OptionBuilder.hasArg().create('A');
        assertNotNull(option);
        assertEquals('A', option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue(0));
    }

    /**
     * Test create method with required single argument.
     */
    @Test
    public void testCreateRequiredArg()
    {
        Option option = OptionBuilder.hasArg().isRequired().create('B');
        assertNotNull(option);
        assertEquals('B', option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        try
        {
            option.getValue(0);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    /**
     * Test create method with optional single argument and value separator.
     */
    @Test
    public void testCreateOptionalArgWithValueSeparator()
    {
        Option option = OptionBuilder.hasArg().withValueSeparator(':').create('C');
        assertNotNull(option);
        assertEquals('C', option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue(0));
    }

    /**
     * Test create method with optional multiple arguments.
     */
    @Test
    public void testCreateOptionalArgs()
    {
        Option option = OptionBuilder.hasArgs().create('D');
        assertNotNull(option);
        assertEquals('D', option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("args", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals(new String[] {"arg1", "arg2"}, option.getValues());
    }

    /**
     * Test create method with required multiple arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequiredArgs()
    {
        Option option = OptionBuilder.hasArgs().isRequired().create('E');
        assertNotNull(option);
        assertEquals('E', option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("args", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        option.getValue(0);
        option.getValue(1);
    }

    /**
     * Test create method with optional multiple arguments and value separator.
     */
    @Test
    public void testCreateOptionalArgsWithValueSeparator()
    {
        Option option = OptionBuilder.hasArgs().withValueSeparator(':').create('F');
        assertNotNull(option);
        assertEquals('F', option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("args", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertEquals(new String[] {"arg1:val1", "arg2:val2"}, option.getValues());
    }

    /**
     * Test create method with an optional argument and type.
     */
    @Test
    public void testCreateOptionalArgWithType()
    {
        Option option = OptionBuilder.hasOptionalArg().withType(Integer.class).create('G');
        assertNotNull(option);
        assertEquals('G', option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(Integer.class, option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue(0));
    }

    /**
     * Test create method with an optional argument and description.
     */
    @Test
    public void testCreateOptionalArgWithDescription()
    {
        Option option = OptionBuilder.hasOptionalArg().withDescription("An option with a description").create('H');
        assertNotNull(option);
        assertEquals('H', option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue(0));
        assertEquals("An option with a description", option.getDescription());
    }

    /**
     * Test create method with an optional argument, type and description.
     */
    @Test
    public void testCreateOptionalArgWithTypeAndDescription()
    {
        Option option = OptionBuilder.hasOptionalArg().withType(Integer.class).withDescription("An option with a description").create('I');
        assertNotNull(option);
        assertEquals('I', option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(Integer.class, option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue(0));
        assertEquals("An option with a description", option.getDescription());
    }
}