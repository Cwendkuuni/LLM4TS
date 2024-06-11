package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        assertEquals("optionLongName", optionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        Option option = OptionBuilder.create('O');
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testHasArgWithName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = OptionBuilder.create('O');
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
        Option option = OptionBuilder.create('O');
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValueSeparator());
        Option option = OptionBuilder.create('O');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        Option option = OptionBuilder.create('O');
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getValue());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
        Option option = OptionBuilder.create('O');
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, optionBuilder.getNumberOfArgs());
        Option option = OptionBuilder.create('O');
        assertEquals(2, option.getNumberOfArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.create('O');
    }

    @Test
    public void testCreateWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertNull(optionBuilder.getType());
        Option option = OptionBuilder.create('O');
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        assertNull(optionBuilder.getDescription());
        Option option = OptionBuilder.create('O');
        assertEquals("Option description", option.getLongOpt());
    }

    private boolean isTrue(boolean condition)
    {
        return condition;
    }
}