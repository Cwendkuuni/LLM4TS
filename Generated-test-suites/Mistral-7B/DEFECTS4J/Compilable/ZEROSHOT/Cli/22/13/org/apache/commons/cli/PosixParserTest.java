package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest
{
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", "optionA", false, "This is option A.");
        options.addOption("b", "optionB", true, "This is option B.");
    }

    @After
    public void tearDown()
    {
        options = null;
    }

    @Test
    public void testFlattenEmptyArguments() throws ParseException
    {
        String[] arguments = {};
        String[] expected = {};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleArgument() throws ParseException
    {
        String[] arguments = {"arg"};
        String[] expected = {"arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() throws ParseException
    {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionWithArgument() throws ParseException
    {
        String[] arguments = {"-a", "arg"};
        String[] expected = {"-a", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() throws ParseException
    {
        String[] arguments = {"-a", "-b"};
        String[] expected = {"-a", "-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() throws ParseException
    {
        String[] arguments = {"--optionA"};
        String[] expected = {"--optionA"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithArgument() throws ParseException
    {
        String[] arguments = {"--optionA", "arg"};
        String[] expected = {"--optionA", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMixedArgumentsAndOptions() throws ParseException
    {
        String[] arguments = {"arg1", "-a", "arg2", "-b", "arg3"};
        String[] expected = {"arg1", "-a", "arg2", "-b", "arg3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() throws ParseException
    {
        String[] arguments = {"-x"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException
    {
        String[] arguments = {"-x", "arg1", "arg2"};
        String[] expected = {"-x", "arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }
}