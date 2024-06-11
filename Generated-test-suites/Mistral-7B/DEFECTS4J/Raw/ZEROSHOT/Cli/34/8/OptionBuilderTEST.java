package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilder()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .hasValueSeparator('=')
                .withType(String.class)
                .create();

        assertEquals("longOption", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals(true, option.isRequired());
        assertEquals('=', option.getValueSeparator());
        assertEquals(String.class, option.getType());

        Option optionNoArg = OptionBuilder.withLongOpt("longOptionNoArg")
                .create();

        assertEquals("longOptionNoArg", optionNoArg.getLongOpt());
        assertEquals(null, optionNoArg.getArgName());
        assertEquals(false, optionNoArg.isRequired());
        assertEquals(0, optionNoArg.getArgs().getValue());
        assertEquals(null, optionNoArg.getType());

        Option optionHasOptionalArg = OptionBuilder.withLongOpt("longOptionHasOptionalArg")
                .hasOptionalArg()
                .create();

        assertEquals("longOptionHasOptionalArg", optionHasOptionalArg.getLongOpt());
        assertEquals(null, optionHasOptionalArg.getArgName());
        assertEquals(false, optionHasOptionalArg.isRequired());
        assertEquals(1, optionHasOptionalArg.getArgs().getValue());
        assertEquals(null, optionHasOptionalArg.getType());

        Option optionHasArgs = OptionBuilder.withLongOpt("longOptionHasArgs")
                .hasArgs(3)
                .create();

        assertEquals("longOptionHasArgs", optionHasArgs.getLongOpt());
        assertEquals(null, optionHasArgs.getArgName());
        assertEquals(false, optionHasArgs.isRequired());
        assertEquals(3, optionHasArgs.getArgs().getValue());
        assertEquals(null, optionHasArgs.getType());
    }
}