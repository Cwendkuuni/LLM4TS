package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption");
        Option option = optionBuilder.create();
        assertEquals("myOption", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasRequiredArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator('=');
        Option option = optionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(new Object());
        optionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description for myOption");
        Option option = optionBuilder.create();
        assertEquals("Description for myOption", option.getDescription());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasArgsWithSpecifiedNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create();
        assertEquals(3, option.getArgs().length);
    }
}