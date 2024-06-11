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
    /**
     * Test hasArg method.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('T');

        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test hasArg method with hasArg false.
     */
    @Test
    public void testHasArgFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        Option option = optionBuilder.create('T');

        assertNull(option.getArgs());
    }

    /**
     * Test hasArgName method.
     */
    @Test
    public void testHasArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("file");
        Option option = optionBuilder.create('F');

        assertEquals("file", option.getArgName());
    }

    /**
     * Test isRequired method.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('R');

        assertTrue(option.isRequired());
    }

    /**
     * Test isRequired method with isRequired false.
     */
    @Test
    public void testIsRequiredFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        Option option = optionBuilder.create('R');

        assertFalse(option.isRequired());
    }

    /**
     * Test hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('O');

        assertTrue(option.isOptional());
    }

    /**
     * Test hasOptionalArgs method.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create('A');

        assertEquals(3, option.getArgs().length);
    }

    /**
     * Test hasOptionalArgs method with no args.
     */
    @Test
    public void testHasOptionalArgsNoArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create('A');

        assertNull(option.getArgs());
    }

    /**
     * Test withLongOpt method.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("foo");
        Option option = optionBuilder.create('f');

        assertEquals("foo", option.getLongOpt());
    }

    /**
     * Test withDescription method.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("foo");
        Option option = optionBuilder.create('f');

        assertEquals("foo", option.getDescription());
    }

    /**
     * Test withType method.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.TYPE);
        Option option = optionBuilder.create('i');

        assertEquals(Integer.TYPE, option.getType());
    }

    /**
     * Test withValueSeparator method.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create('C');

        assertEquals(',', option.getValueSeparator());
    }

    /**
     * Test withValueSeparator method with no separator.
     */
    @Test
    public void testWithValueSeparatorNoSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Option option = optionBuilder.create('C');

        assertSame(Option.VALUE_SEPARATOR_NONE, option.getValueSeparator());
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("file")
                .isRequired().hasOptionalArg().withLongOpt("foo");
        Option option = optionBuilder.create('F');

        assertEquals("file", option.getArgName());
        assertTrue(option.isRequired());
        assertTrue(option.isOptional());
        assertEquals("foo", option.getLongOpt());

        optionBuilder.reset();

        Option option2 = optionBuilder.create('F');

        assertNull(option2.getArgName());
        assertFalse(option2.isRequired());
        assertFalse(option2.isOptional());
        assertNull(option2.getLongOpt());
    }
}