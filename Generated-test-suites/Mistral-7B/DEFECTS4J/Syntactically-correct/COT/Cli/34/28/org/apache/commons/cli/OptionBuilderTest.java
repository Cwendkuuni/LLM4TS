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
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", optionBuilder.longopt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
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
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredWithFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.required);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, optionBuilder.numberOfArgs);
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
    public void testHasOptionalArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.type);
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test description");
        assertEquals("test description", optionBuilder.description);
    }

    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("test description")
                .isRequired();

        Option option = optionBuilder.create('t');
        assertNull(option.getValue());
        assertEquals("test description", option.getLongOpt());
        assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        optionBuilder.create('t');
    }
}