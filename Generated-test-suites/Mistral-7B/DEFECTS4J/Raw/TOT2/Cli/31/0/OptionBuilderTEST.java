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
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test OptionBuilder withLongOpt method.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        Option option = optionBuilder.create();
        assertEquals("optionLongName", option.getLongOpt());
    }

    /**
     * Test OptionBuilder hasArg method.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(1, option.getArgPosition());
    }

    /**
     * Test OptionBuilder hasArg method with false argument.
     */
    @Test
    public void testHasArgFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgPosition());
    }

    /**
     * Test OptionBuilder withArgName method.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    /**
     * Test OptionBuilder isRequired method.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertTrue(option.isRequired());
    }

    /**
     * Test OptionBuilder withValueSeparator method.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test OptionBuilder hasArgs method.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    /**
     * Test OptionBuilder hasArgs method with specific number of args.
     */
    @Test
    public void testHasArgsWithSpecificNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create();
        assertEquals(3, option.getArgs());
    }

    /**
     * Test OptionBuilder hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertTrue(option.isOptional());
    }

    /**
     * Test OptionBuilder hasOptionalArgs method.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.isOptional());
    }

    /**
     * Test OptionBuilder withType method.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        assertEquals(String.class, option.getType());
    }

    /**
     * Test OptionBuilder withDescription method.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        Option option = optionBuilder.create();
        assertSame("Option description", option.getDescription());
    }

    /**
     * Test OptionBuilder create method with no arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    /**
     * Test OptionBuilder create method with required and optional args.
     */
    @Test
    public void testCreateWithRequiredAndOptionalArguments()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired().hasArg().hasOptionalArg();
        Option option = optionBuilder.create();
        assertFalse(option.isOptional());
        assertTrue(option.hasArg());
        assertTrue(option.isOptionalArg());
    }
}