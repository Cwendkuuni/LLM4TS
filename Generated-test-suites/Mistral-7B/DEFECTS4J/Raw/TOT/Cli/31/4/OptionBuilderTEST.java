package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testSimpleOption()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option simpleOption = optionBuilder.withLongOpt("simpleOption")
                .isRequired()
                .create('s');

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-s", "value"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(args, new DefaultOptions());
            Assert.assertEquals("value", cmd.getOptionValue("simpleOption"));
            Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testOptionalArgOption()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option optionalArgOption = optionBuilder.withLongOpt("optionalArgOption")
                .hasArg()
                .isRequired()
                .create('o');

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-o", "value1", "value2"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(args, new DefaultOptions());
            Assert.assertEquals("value1", cmd.getOptionValue("optionalArgOption"));
            Assert.assertEquals("value2", cmd.getOptionValues("optionalArgOption")[1]);
            Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        String[] args2 = {"-o", "value1"};
        try {
            cmd = parser.parse(args2, new DefaultOptions());
            fail("Expected ParseException due to missing optional argument.");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.hasArgs(-1);
        optionBuilder.create();
    }

    @Test
    public void testUnlimitedArgsOption()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option unlimitedArgsOption = optionBuilder.withLongOpt("unlimitedArgsOption")
                .hasArgs()
                .create('u');

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-u", "arg1", "arg2", "arg3"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(args, new DefaultOptions());
            String[] values = cmd.getOptionValues("unlimitedArgsOption");
            assertEquals(3, values.length);
            assertEquals("arg1", values[0]);
            assertEquals("arg2", values[1]);
            assertEquals("arg3", values[2]);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testValueSeparatorOption()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option valueSeparatorOption = optionBuilder.withLongOpt("valueSeparatorOption")
                .withValueSeparator(':')
                .hasArg()
                .isRequired()
                .create('v');

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-v", "key:value"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(args, new DefaultOptions());
            Assert.assertEquals("key", cmd.getOptionValues("valueSeparatorOption")[0]);
            Assert.assertEquals("value", cmd.getOptionValues("valueSeparatorOption")[1]);
            Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionalValueSeparatorOption()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option optionalValueSeparatorOption = optionBuilder.withLongOpt("optionalValueSeparatorOption")
                .withValueSeparator(':')
                .hasOptionalArg()
                .create('w');

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-w", "arg"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(args, new DefaultOptions());
            Assert.assertNull(cmd.getOptionValues("optionalValueSeparatorOption"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        String[] args2 = {"-w", "arg1:arg2"};
        CommandLine cmd2 = null;
        try {
            cmd2 = parser.parse(args2, new DefaultOptions());
            String[] values = cmd2.getOptionValues("optionalValueSeparatorOption");
            assertEquals(2, values.length);
            assertEquals("arg1", values[0]);
            assertEquals("arg2", values[1]);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        String[] args3 = {"-w"};
        try {
            cmd = parser.parse(args3, new DefaultOptions());
            fail("Expected ParseException due to missing optional value.");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testTypeOption()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option typeOption = optionBuilder.withLongOpt("typeOption")
                .withType(Double.TYPE)
                .hasArg()
                .isRequired()
                .create('t');

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-t", "3.14"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(args, new DefaultOptions());
            Double value = (Double) cmd.getOptionValue("typeOption");
            assertEquals(3.14, value.doubleValue(), 0.0);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        String[] args2 = {"-t", "nonNumericValue"};
        try {
            cmd = parser.parse(args2, new DefaultOptions());
            fail("Expected ParseException due to invalid type.");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testDescriptionOption()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option descriptionOption = optionBuilder.withLongOpt("descriptionOption")
                .withDescription("This is a description for the option")
                .hasArg()
                .isRequired()
                .create('d');

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-d", "This is a description for the option value."};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(args, new DefaultOptions());
            String description = cmd.getOptionValue("descriptionOption");
            assertEquals("This is a description for the option value.", description);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}