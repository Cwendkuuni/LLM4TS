package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private static final String[] FLATTEN_TEST_ARGS = {
            "--arg1", "value1",
            "-ab",
            "arg2",
            "--",
            "arg3"
    };

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption(OptionBuilder.withArgName("arg").hasArg().create("arg1"));
        options.addOption(OptionBuilder.withArgName("arg").hasArg().create("arg2"));

        String[] flattenedArgs = parser.flatten(options, FLATTEN_TEST_ARGS, false);

        assertEquals(7, flattenedArgs.length);
        assertEquals("--arg1", flattenedArgs[0]);
        assertEquals("value1", flattenedArgs[1]);
        assertEquals("-a", flattenedArgs[2]);
        assertEquals("-b", flattenedArgs[3]);
        assertEquals("arg2", flattenedArgs[4]);
        assertEquals("--", flattenedArgs[5]);
        assertEquals("arg3", flattenedArgs[6]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption(OptionBuilder.withArgName("arg").hasArg().create("arg1"));
        options.addOption(OptionBuilder.withArgName("arg").hasArg().create("arg2"));

        String[] flattenedArgs = parser.flatten(options, FLATTEN_TEST_ARGS, true);

        assertEquals(5, flattenedArgs.length);
        assertEquals("--arg1", flattenedArgs[0]);
        assertEquals("value1", flattenedArgs[1]);
        assertEquals("-a", flattenedArgs[2]);
        assertEquals("-b", flattenedArgs[3]);
        assertEquals("arg2", flattenedArgs[4]);
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-", flattenedArgs[0]);
    }

    @Test
    public void testFlattenUnknownOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] args = {"-b"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-b", flattenedArgs[0]);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption(OptionBuilder.withArgName("arg").hasArg().create("arg1"));

        String[] args = {"--arg1", "value"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(2, flattenedArgs.length);
        assertEquals("--arg1", flattenedArgs[0]);
        assertEquals("value", flattenedArgs[1]);
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption(OptionBuilder.withArgName("arg").hasArgs().create("arg1"));

        String[] args = {"--arg1", "value1", "value2"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(3, flattenedArgs.length);
        assertEquals("--arg1", flattenedArgs[0]);
        assertEquals("value1", flattenedArgs[1]);
        assertEquals("value2", flattenedArgs[2]);
    }

    @Test
    public void testFlattenOptionWithMultipleCharacters() {
        Options options = new Options();
        options.addOption("ab", false, "Option ab");

        String[] args = {"-ab"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-ab", flattenedArgs[0]);
    }
}