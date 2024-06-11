package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optA");
        assertEquals("optA", optionBuilder.longopt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName")
                                                  .hasArg();
        assertEquals("argName", optionBuilder.argName);
        assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertEquals(true, optionBuilder.required);
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
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, optionBuilder.numberOfArgs);
        assertEquals(true, optionBuilder.optionalArg);
    }

    @Test
    public void testCreateWithRequiredArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg()
                                                  .isRequired();
        Option option = optionBuilder.create('A');
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('A');
        assertNull(option.getValue());
    }

    @Test
    public void testCreateWithArgValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg()
                                                  .isRequired()
                                                  .withValueSeparator(':');
        Option option = optionBuilder.create('A');
        option.parseArgument("valueA:valueB");
        assertEquals("valueA", option.getValue()[0]);
        assertEquals("valueB", option.getValue()[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertThrows(IllegalArgumentException.class, ()->optionBuilder.create('A'));
    }
}