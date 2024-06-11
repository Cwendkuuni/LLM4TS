package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilder()
    {
        Option option = OptionBuilder.withLongOpt("longOption") // long option
                                       .hasArg() // requires an argument
                                       .withArgName("argName") // argument name
                                       .isRequired() // required
                                       .withValueSeparator(':') // value separator
                                       .hasArgs(2) // two arguments
                                       .withType(Integer.class) // integer type
                                       .withDescription("Test Option") // description
                                       .create('t'); // option char

        // test option properties
        assertEquals("longOption", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(true, option.isRequired());
        assertEquals(2, option.getArgs().numberOfArgs());
        assertNull(option.getValue());
        assertEquals(':', option.getValueSeparator());
        assertEquals(Integer.class, option.getType());

        // test long option with argument
        option = OptionBuilder.withLongOpt("longOption") // long option
                               .hasArg() // requires an argument
                               .withArgName("argName") // argument name
                               .isRequired() // required
                               .withValueSeparator(':') // value separator
                               .hasArgs(2) // two arguments
                               .withType(Integer.class) // integer type
                               .withDescription("Test Option") // description
                               .create('t'); // option char
        String[] args = new String[]{"-t", "arg1", "arg2"};
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(args);
            assertEquals("arg1", option.getValue(0));
            assertEquals("arg2", option.getValue(1));
        }
        catch (ParseException pe)
        {
            // should not reach here
            assert false;
        }

        // test optional argument
        option = OptionBuilder.withLongOpt("longOption") // long option
                               .hasOptionalArg() // optional argument
                               .withArgName("argName") // argument name
                               .isRequired() // required
                               .withValueSeparator(':') // value separator
                               .hasArgs(1) // one argument
                               .withType(Integer.class) // integer type
                               .withDescription("Test Option") // description
                               .create('t'); // option char
        String[] argsOptional = new String[]{"-t", "arg1"};
        String[] argsOptionalValue = new String[]{"-t", "arg1", "arg2"};
        CommandLine lineOptional = null;
        try
        {
            lineOptional = parser.parse(argsOptional);
            assertEquals(null, option.getValue(0));

            lineOptional = parser.parse(argsOptionalValue);
            assertEquals("arg2", option.getValue(0));
        }
        catch (ParseException pe)
        {
            // should not reach here
            assert false;
        }
    }
}