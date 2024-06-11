package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        assertEquals("optionLong", optionBuilder.getLongOpt());
    }

    @Test(expected = AssertionError.class)
    public void testWithLongOptNull()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testIsRequiredFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getValueSeparator());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsOne()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(1);
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgsTwo()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(2, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.getType());
    }

    @Test(expected = AssertionError.class)
    public void testWithTypeNull()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(null);
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        assertEquals("Option description", optionBuilder.getDescription());
    }

    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                                                  .withDescription("Option description")
                                                  .hasArg()
                                                  .withValueSeparator(':')
                                                  .isRequired()
                                                  .withType(Integer.class);

        Option option = optionBuilder.create('o');

        assertEquals("optionLong", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(':', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());

        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        optionBuilder.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMissingLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.create('o');
    }
}