package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link OptionBuilder} class.
 */
public class OptionBuilderTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        Options options = new Options();

        OptionBuilder.withLongOpt("optionA")
                     .isRequired()
                     .hasArg()
                     .withArgName("argA")
                     .create(options);

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(new String[]{"-optionA", "argB"}, options);

        assertEquals("argA", line.getOptionValue("optionA"));
        assertNull(line.getOptionValue("optionB"));

        try
        {
            parser.parse(new String[]{"-optionA"}, options);
            fail("Missing argument exception expected.");
        }
        catch (ParseException e)
        {
            assertEquals("Required option 'optionA' is missing.", e.getMessage());
        }
    }

    @Test
    public void testOptionalArgumentOption() throws ParseException
    {
        Options options = new Options();

        OptionBuilder.withLongOpt("optionB")
                     .isRequired()
                     .hasOptionalArg()
                     .withArgName("argB")
                     .create(options);

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(new String[]{"-optionB", "argC"}, options);

        assertEquals("argC", line.getOptionValue("optionB"));
        assertNull(line.getOptionValue("optionB", "defaultValue"));

        CommandLine lineWithoutArg = parser.parse(new String[]{"-optionB"}, options);
        assertNull(lineWithoutArg.getOptionValue("optionB"));
    }

    @Test
    public void testMultipleOptionalArgumentsOption() throws ParseException
    {
        Options options = new Options();

        OptionBuilder.withLongOpt("optionC")
                     .isRequired()
                     .hasOptionalArgs(2)
                     .withArgName("argC1 argC2")
                     .create(options);

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(new String[]{"-optionC", "argD", "argE"}, options);

        assertEquals("argD, argE", line.getOptionValue("optionC"));

        try
        {
            parser.parse(new String[]{"-optionC", "argD"}, options);
            fail("Missing argument exception expected.");
        }
        catch (ParseException e)
        {
            assertEquals("The option 'optionC' requires exactly 2 arguments, but only 1 value provided.", e.getMessage());
        }

        try
        {
            parser.parse(new String[]{"-optionC"}, options);
            fail("Missing argument exception expected.");
        }
        catch (ParseException e)
        {
            assertEquals("The option 'optionC' requires exactly 2 arguments, but 0 values provided.", e.getMessage());
        }
    }

    @Test
    public void testOptionWithType() throws ParseException
    {
        Options options = new Options();

        OptionBuilder.withLongOpt("optionD")
                     .isRequired()
                     .hasArgs()
                     .withArgName("argD")
                     .withType(Integer.class)
                     .create(options);

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(new String[]{"-optionD", "123"}, options);

        assertEquals(123, line.getOptionValue("optionD"), 0);

        try
        {
            parser.parse(new String[]{"-optionD"}, options);
            fail("Missing argument exception expected.");
        }
        catch (ParseException e)
        {
            assertEquals("Required option 'optionD' is missing.", e.getMessage());
        }
    }
}