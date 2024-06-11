package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("foo");
        assertSame(optionBuilder.longopt, "foo");
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
        assertEquals(1, optionBuilder.numberOfArgsAfterCreation());
    }

    @Test
    public void testHasArgWithSpecifiedName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        assertEquals("argName", optionBuilder.argName);
        assertEquals(1, optionBuilder.numberOfArgsAfterCreation());
    }

    @Test(expected = IllegalStateException.class)
    public void testHasArgTwice()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilder.hasArg();
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.required);
    }

    @Test
    public void testIsRequiredTwice()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        OptionBuilder.isRequired();
    }

    @Test
    public void testWithValueSeparatorChar()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.valuesep);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithSpecifiedNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.numberOfArgs);
    }

    @Test(expected = IllegalStateException.class)
    public void testHasArgsTwice()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        OptionBuilder.hasArgs();
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
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
    public void testHasOptionalArgsWithSpecifiedNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertNotNull(optionBuilder.type);
        assertEquals(String.class, optionBuilder.type);
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertNotNull(optionBuilder.description);
        assertEquals("description", optionBuilder.description);
    }

    @Test
    public void testCreateWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("foo").hasArg().withDescription("description");
        Option option = optionBuilder.create('F');
        assertEquals("description", option.getDescription());
        assertEquals("foo", option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertFalse(option.isRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithLongOptAndArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("foo").hasArg("argName").isRequired();
        Option option = optionBuilder.create('F');
        assertEquals("argName", option.getArgName());
        assertEquals("foo", option.getLongOpt());
        assertSame("argName", option.getName());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithShortOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("foo").hasArg().withDescription("description");
        Option option = optionBuilder.create('f');
        assertEquals("description", option.getDescription());
        assertEquals("foo", option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertFalse(option.isRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithShortOptAndArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("foo").hasArg("argName").isRequired();
        Option option = optionBuilder.create('f');
        assertEquals("argName", option.getArgName());
        assertEquals("foo", option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertSame("argName", option.getName());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        optionBuilder.create((char) 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2).withType(String[].class);
        optionBuilder.create((char) 0);
    }

    @Test
    public void testCreateWithOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg().isRequired();
        Option option = optionBuilder.create('F');
        assertFalse(option.isRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs().isRequired();
        Option option = optionBuilder.create('F');
        assertFalse(option.isRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithOptionalArgAndValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg().withValue("value");
        Option option = optionBuilder.create('F');
        assertFalse(option.isRequired());
        assertEquals("value", option.getValue());
    }

    @Test
    public void testCreateWithOptionalArgsAndValues()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs().withValues("value1", "value2");
        Option option = optionBuilder.create('F');
        assertFalse(option.isRequired());
        assertEquals(new String[]{"value1", "value2"}, option.getArgs());
    }

    @Test
    public void testCreateWithOptionalArgsAndValuesWithSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs().withValueSeparator(':').withValues("value1", "value2");
        Option option = optionBuilder.create('F');
        assertFalse(option.isRequired());
        assertEquals(new String[]{"value1:value2"}, option.getArgs());
    }
}