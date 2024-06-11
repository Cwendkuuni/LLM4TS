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
 * Test cases for OptionBuilder class
 */
public class OptionBuilderTest
{

    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        OptionBuilder instance = optionBuilder.instance;
        assertEquals("longOpt", instance.longopt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilder instance = optionBuilder.instance;
        assertEquals(1, instance.numberOfArgs);
        assertFalse(instance.optionalArg);
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        OptionBuilder instance = optionBuilder.instance;
        assertEquals("argName", instance.argName);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        OptionBuilder instance = optionBuilder.instance;
        assertTrue(instance.required);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        OptionBuilder instance = optionBuilder.instance;
        assertEquals(':', instance.valuesep);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Boolean.TYPE);
        OptionBuilder instance = optionBuilder.instance;
        assertSame(Boolean.TYPE, instance.type);
    }

    @Test
    public void testCreate() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Test Option")
                .hasArg()
                .withValueSeparator(':')
                .isRequired()
                .withType(String.class);

        Option option = optionBuilder.create('T');
        assertSame("Test Option", option.getDescription());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(1, option.getArgPosition());
        assertSame(String.class, option.getType());
        assertFalse(option.isOptional());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMissingLongOpt() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance;
        optionBuilder.create();
    }

    @Test
    public void testCreateWithOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create('O');
        assertEquals(2, option.getNumberOfValues());
        assertNull(option.getValue(0));
        assertNull(option.getValue(1));
    }
}