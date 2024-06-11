package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        assertSame(optionBuilder.longopt, "optionLongName");
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.required);
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        assertEquals("argName", optionBuilder.argName);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.required);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.valuesep);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNum()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertSame(String.class, optionBuilder.type);
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertSame("description", optionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName").hasArg();
        Option option = OptionBuilder.create('o');
        assertSame(optionBuilder.longopt, option.getLongOpt());
        assertSame("optionLongName", option.getLongOpt());
        assertSame(Option.UNINITIALIZED, option.getArgs());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreateWithOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create('o');
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertTrue(option.isOptional());
    }

    @Test
    public void testCreateWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',').hasArgs();
        Option option = OptionBuilder.create('o');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testCreateWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = OptionBuilder.create('o');
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testCreateWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class).hasArg();
        Option option = OptionBuilder.create('i');
        assertSame(Integer.class, option.getType());
    }

    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description").hasArg();
        Option option = OptionBuilder.create('d');
        assertEquals("description", option.getDescription());
    }
}