package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                                      .isRequired()
                                      .hasArg()
                                      .withArgName("arg")
                                      .create();

        // verify the Option properties
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
        assertEquals("myLongOpt", option.getLongOpt());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                                      .hasOptionalArg()
                                      .create();

        // verify the Option properties
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                                      .hasArg()
                                      .withArgName("arg")
                                      .withValueSeparator(',')
                                      .create();

        // verify the Option properties
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
        assertEquals(',', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithMissingLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }
}