package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import junit.framework.TestCase;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest extends TestCase {

    /**
     * Test flattening of simple command line arguments.
     */
    public void testSimpleFlattening() {
        String[] args = {"--option1", "--option2=value2", "--option3=value3", "arg1", "arg2"};
        Options options = new Options();
        options.addRequiredOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", true, "o3");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(6, flattenedArgs.length);
        assertEquals("--option1", flattenedArgs[0]);
        assertEquals("--option2", flattenedArgs[1]);
        assertEquals("value2", flattenedArgs[2]);
        assertEquals("--option3", flattenedArgs[3]);
        assertEquals("arg1", flattenedArgs[4]);
        assertEquals("arg2", flattenedArgs[5]);
    }

    /**
     * Test flattening of command line arguments with -- option.
     */
    public void testFlatteningWithDoubleDash() {
        String[] args = {"--", "--option1=value1", "--option2=value2"};
        Options options = new Options();
        options.addRequiredOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(3, flattenedArgs.length);
        assertEquals("--option1", flattenedArgs[0]);
        assertEquals("value1", flattenedArgs[1]);
        assertEquals("--option2", flattenedArgs[2]);
    }

    /**
     * Test handling of multiple short options.
     */
    public void testHandlingOfMultipleShortOptions() {
        String[] args = {"-a", "-b", "-c"};
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        options.addOption("c", true, "c");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(3, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("-b", flattenedArgs[1]);
        assertEquals("-c", flattenedArgs[2]);
    }

    /**
     * Test handling of single short option with argument.
     */
    public void testHandlingOfSingleShortOptionWithArgument() {
        String[] args = {"-a", "arg"};
        Options options = new Options();
        options.addOption("a", true, "a");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(2, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("arg", flattenedArgs[1]);
    }

    /**
     * Test handling of multiple short options with arguments.
     */
    public void testHandlingOfMultipleShortOptionsWithArguments() {
        String[] args = {"-a", "arg1", "-b", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(4, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("arg1", flattenedArgs[1]);
        assertEquals("-b", flattenedArgs[2]);
        assertEquals("arg2", flattenedArgs[3]);
    }

    /**
     * Test handling of long options with arguments.
     */
    public void testHandlingOfLongOptionsWithArguments() {
        String[] args = {"--longOption1", "arg1", "--longOption2=arg2"};
        Options options = new Options();
        options.addOption("longOption1", true, "lo1");
        options.addOption("longOption2", true, "lo2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(4, flattenedArgs.length);
        assertEquals("--longOption1", flattenedArgs[0]);
        assertEquals("arg1", flattenedArgs[1]);
        assertEquals("--longOption2", flattenedArgs[2]);
        assertEquals("arg2", flattenedArgs[3]);
    }

    /**
     * Test handling of multiple long options with arguments.
     */
    public void testHandlingOfMultipleLongOptionsWithArguments() {
        String[] args = {"--longOption1=arg1", "--longOption2=arg2"};
        Options options = new Options();
        options.addOption("longOption1", true, "lo1");
        options.addOption("longOption2", true, "lo2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(4, flattenedArgs.length);
        assertEquals("--longOption1", flattenedArgs[0]);
        assertEquals("arg1", flattenedArgs[1]);
        assertEquals("--longOption2", flattenedArgs[2]);
        assertEquals("arg2", flattenedArgs[3]);
    }

    /**
     * Test handling of options with no arguments.
     */
    public void testHandlingOfOptionsWithNoArguments() {
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", false, "o1");
        options.addOption("option2", false, "o2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(2, flattenedArgs.length);
        assertEquals("--option1", flattenedArgs[0]);
        assertEquals("--option2", flattenedArgs[1]);
    }

    /**
     * Test handling of options that can occur multiple times.
     */
    public void testHandlingOfOptionsThatCanOccurMultipleTimes() {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("--option", flattenedArgs[0]);
    }

    /**
     * Test handling of options that can occur multiple times with arguments.
     */
    public void testHandlingOfOptionsThatCanOccurMultipleTimesWithArguments() {
        String[] args = {"--option=arg1", "--option=arg2"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(3, flattenedArgs.length);
        assertEquals("--option", flattenedArgs[0]);
        assertEquals("arg1", flattenedArgs[1]);
        assertEquals("--option", flattenedArgs[2]);
    }

    /**
     * Test handling of unrecognized arguments.
     */
    public void testHandlingOfUnrecognizedArguments() {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o");

        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    /**
     * Test handling of unrecognized arguments with stopAtNonOption enabled.
     */
    public void testHandlingOfUnrecognizedArgumentsWithStopAtNonOption() {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);

        assertEquals(2, flattenedArgs.length);
        assertEquals("arg1", flattenedArgs[0]);
        assertEquals("arg2", flattenedArgs[1]);
    }
}