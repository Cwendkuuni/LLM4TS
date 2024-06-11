package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

/**
 * Test class for PosixParser
 */
public class PosixParserTest
{
    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * .
     *
     * @throws ParseException
     */
    @Test
    public void testFlatten() throws ParseException
    {
        // Given
        String[] args = new String[] {"--foo", "--bar=baz", "-c", "-d", "-e", "arg1", "-f arg2", "-g=arg3", "--"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("c", true, "c");
        options.addOption("d", false, "d");
        options.addOption("e", false, "e");
        options.addOption("f", true, "f");
        options.addOption("g", true, "g");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // Then
        List<String> expected = Arrays.asList(new String[] {"--foo", "--bar", "baz", "-c", "-d", "-e", "arg1", "-f", "arg2", "--g", "arg3"});
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple required options.
     *
     * @throws ParseException
     */
    @Test
    public void testFlattenMultipleRequiredOptions() throws ParseException
    {
        // Given
        String[] args = new String[] {"--foo=arg1", "--bar=arg2", "--baz=arg3", "arg4"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f");
        options.addRequiredOption("bar", true, "b");
        options.addRequiredOption("baz", true, "z");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // Then
        List<String> expected = Arrays.asList(new String[] {"--foo", "arg1", "--bar", "arg2", "--baz", "arg3"});
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no options.
     *
     * @throws ParseException
     */
    @Test
    public void testFlattenNoOptions() throws ParseException
    {
        // Given
        String[] args = new String[] {"arg1", "arg2", "arg3"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // Then
        List<String> expected = Arrays.asList(new String[] {"arg1", "arg2", "arg3"});
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with stopatnonoption set to true.
     *
     * @throws ParseException
     */
    @Test
    public void testFlattenStopAtNonOption() throws ParseException
    {
        // Given
        String[] args = new String[] {"--foo", "arg1", "-notanoption", "arg3"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        // Then
        List<String> expected = Arrays.asList(new String[] {"--foo", "arg1", "--", "arg3"});
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with long options.
     *
     * @throws ParseException
     */
    @Test
    public void testFlattenLongOptions() throws ParseException
    {
        // Given
        String[] args = new String[] {"--longoption1", "--longoption2=value1", "--longoption3=value2"};
        Options options = new Options();
        options.addOption("longoption1", true, "l1");
        options.addOption("longoption2", true, "l2");
        options.addOption("longoption3", true, "l3");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // Then
        List<String> expected = Arrays.asList(new String[] {"--longoption1", "--longoption2", "value1", "--longoption3", "value2"});
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid option.
     *
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException
    {
        // Given
        String[] args = new String[] {"--invalidoption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // Then
        fail("Expected ParseException");
    }
}