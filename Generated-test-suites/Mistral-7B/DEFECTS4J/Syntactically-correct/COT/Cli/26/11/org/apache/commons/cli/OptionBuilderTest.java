package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        String longopt = optionBuilder.getLongopt();
        assertSame("optionLongName", longopt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(1, numberOfArgs);
    }

    @Test
    public void testHasArg_false()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(Option.UNINITIALIZED, numberOfArgs);
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        String argName = optionBuilder.getArgName();
        assertSame("argName", argName);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        boolean required = optionBuilder.isRequired();
        assertTrue(required);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        char valueSeparator = optionBuilder.getValueSeparator();
        assertEquals(':', valueSeparator);
    }

    @Test
    public void testWithValueSeparator_empty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        char valueSeparator = optionBuilder.getValueSeparator();
        assertEquals('=', valueSeparator);
    }

    @Test
    public void testIsRequired_false()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        boolean required = optionBuilder.isRequired();
        assertFalse(required);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasArgs_number()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(2, numberOfArgs);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        boolean optionalArg = optionBuilder.isOptionalArg();
        assertTrue(optionalArg);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs_number()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(3, numberOfArgs);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Class<?> type = optionBuilder.getType();
        assertSame(String.class, type);
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        String description = optionBuilder.getDescription();
        assertSame("Option description", description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create((char) 0);
    }

    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription("Option description")
                .hasArg()
                .isRequired();

        Option option = optionBuilder.create('optionShortName');
        assertSame("optionShortName", option.getOpt());
        assertSame("optionLongName", option.getLongOpt());
        assertSame("Option description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testCreate_withArgValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription("Option description")
                .hasArg()
                .isRequired()
                .withValueSeparator(':');

        Option option = optionBuilder.create('optionShortName', "argValue");
        assertSame("optionShortName", option.getOpt());
        assertSame("optionLongName", option.getLongOpt());
        assertSame("Option description", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertSame("argValue", option.getValue()[0]);
    }
}