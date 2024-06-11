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
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    // Basic test cases for each method

    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();

        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();

        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Option option = optionBuilder.create();

        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();

        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();

        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();

        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Option option = optionBuilder.create();

        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        Option option = optionBuilder.create();

        assertTrue(option.isRequired());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create();

        assertEquals(3, option.getArgs().length);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();

        assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();

        assertEquals(3, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();

        assertSame(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription()
    {
        String description = "An example description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option option = optionBuilder.create();

        assertEquals(description, option.getDescription());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        Option option = optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("InvalidOpt");
        Option option = optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription(null);
        Option option = optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("");
        Option option = optionBuilder.create();
    }

    @Test
    public void testCreateWithEmptyLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        Option option = optionBuilder.create();

        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreateWithEmptyArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("");
        Option option = optionBuilder.create();

        assertSame(Option.DEFAULT_ARG_NAME, option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNonBooleanHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg("notBoolean");
        Option option = optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
        Option option = optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(null);
        Option option = optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType("InvalidType");
        Option option = optionBuilder.create();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(null);
        Option option = optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithValueSeparatorLargerThanOneByte()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator((char) 0x100);
        Option option = optionBuilder.create();
    }
}