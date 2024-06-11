package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgWithName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertFalse(option.isOptional());
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        assertEquals(2, option.getArgs());
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option option = optionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        assertEquals(String.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
    }
}