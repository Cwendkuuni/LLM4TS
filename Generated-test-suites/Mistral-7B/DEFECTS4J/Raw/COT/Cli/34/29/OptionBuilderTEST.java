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
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        String actualLongOpt = optionBuilder.getLongOpt();
        assertEquals("optionLong", actualLongOpt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        int actualNumberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(1, actualNumberOfArgs);
    }

    @Test
    public void testHasArgWithFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        int actualNumberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(Option.UNINITIALIZED, actualNumberOfArgs);
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        String actualArgName = optionBuilder.getArgName();
        assertEquals("argName", actualArgName);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        boolean actualRequired = optionBuilder.isRequired();
        assertTrue(actualRequired);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        char actualValuesep = optionBuilder.getValueSeparator();
        assertEquals(':', actualValuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        char actualValuesep = optionBuilder.getValueSeparator();
        assertEquals('=', actualValuesep);
    }

    @Test
    public void testIsRequiredFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        boolean actualRequired = optionBuilder.isRequired();
        assertFalse(actualRequired);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        int actualNumberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(Option.UNLIMITED_VALUES, actualNumberOfArgs);
    }

    @Test
    public void testHasArgsWithNum()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        int actualNumberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(2, actualNumberOfArgs);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        boolean actualOptionalArg = optionBuilder.isOptionalArg();
        assertTrue(actualOptionalArg);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        int actualNumberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(Option.UNLIMITED_VALUES, actualNumberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithNum()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        int actualNumberOfArgs = optionBuilder.getNumberOfArgs();
        assertEquals(3, actualNumberOfArgs);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Class<Integer> actualType = optionBuilder.getType();
        assertEquals(Integer.class, actualType);
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        String actualDescription = optionBuilder.getDescription();
        assertEquals("description", actualDescription);
    }

    @Test
    public void testCreate() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option")
                .withDescription("description")
                .hasArg()
                .withValueSeparator(':')
                .isRequired()
                .withType(Integer.class);
        Option option = optionBuilder.create('o');

        assertEquals("option", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(Option.VALUE_REQUIRED, option.getOpt());
        assertNull(option.getValue());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg()
                .withValueSeparator(':')
                .isRequired()
                .withType(Integer.class);
        Option option = optionBuilder.create('o');
    }
}