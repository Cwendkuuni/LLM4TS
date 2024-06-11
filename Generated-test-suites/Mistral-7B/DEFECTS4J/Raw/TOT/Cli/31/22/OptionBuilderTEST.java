package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOptAndArg()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt and arg", option.getDescription());
        assertEquals(1, option.getArgs().numberOfArgs);
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithLongOptAndNoArg()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt and no arg", option.getDescription());
        assertEquals(Option.UNINITIALIZED, option.getArgs().numberOfArgs);
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithShortOptAndArg()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .create('l')
                .withShortOpt('s');

        assertEquals("Option with long opt and short opt and arg", option.getDescription());
        assertEquals('s', option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(1, option.getArgs().numberOfArgs);
        assertFalse(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.create('l');
    }

    @Test
    public void testOptionBuilderWithLongOptAndOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArg()
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt and optional arg", option.getDescription());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().numberOfArgs);
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithLongOptAndRequiredArg()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt and required arg", option.getDescription());
        assertEquals(1, option.getArgs().numberOfArgs);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withValueSeparator(':')
                .create('l');

        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt and value separator", option.getDescription());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withDescription("Option with long opt and description")
                .create('l');

        assertEquals("Option with long opt and description", option.getDescription());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs().numberOfArgs);
        assertFalse(option.isRequired());
    }
}