package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.PosixParser;

/**
 * Test cases for {@link PosixParser} class.
 */
public class PosixParserTest {

    /**
     * Test flattening simple command line arguments.
     */
    @Test
    public void testFlatteningSimpleArgs() {
        String[] args = {"--foo", "--bar=value", "-a", "-b"};
        Options options = new Options();
        options.addOption("foo", true, "help");
        options.addOption("bar", true, "barValue");
        options.addOption("a", true, "aValue");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo", "--bar=value", "-a", "-b"}, parsedArgs);
    }

    /**
     * Test handling long options with no arguments.
     */
    @Test
    public void testHandlingLongOptions() {
        String[] args = {"--foo", "--bar"};
        Options options = new Options();
        options.addOption("foo", false, "helpFoo");
        options.addOption("bar", false, "helpBar");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo", "--bar"}, parsedArgs);
        assertTrue(parser.currentOption == null);
    }

    /**
     * Test handling long options with arguments.
     */
    @Test
    public void testHandlingLongOptionsWithArguments() {
        String[] args = {"--foo=value1", "--bar=value2"};
        Options options = new Options();
        options.addOption("foo", true, "helpFoo");
        options.addOption("bar", true, "helpBar");
        options.addOption("foo", "value", "fooValue");
        options.addOption("bar", "value", "barValue");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo=value1", "--bar=value2"}, parsedArgs);
        assertEquals("value1", parser.currentOption.getValue());
        assertEquals("value2", options.getOptionValue("bar"));
        assertTrue(parser.currentOption == null);
    }

    /**
     * Test handling short options.
     */
    @Test
    public void testHandlingShortOptions() {
        String[] args = {"-f", "-a"};
        Options options = new Options();
        options.addOption("foo", true, "helpFoo");
        options.addOption("a", true, "helpA");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo", "-a"}, parsedArgs);
        assertTrue(parser.currentOption == null);
    }

    /**
     * Test handling short options with arguments.
     */
    @Test
    public void testHandlingShortOptionsWithArguments() {
        String[] args = {"-f", "value1", "-a", "value2"};
        Options options = new Options();
        options.addOption("foo", true, "helpFoo");
        options.addOption("a", true, "helpA");
        options.addOption("foo", "value", "fooValue");
        options.addOption("a", "value", "aValue");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo=value1", "--a=value2"}, parsedArgs);
        assertEquals("value1", parser.currentOption.getValue());
        assertEquals("value2", options.getOptionValue("a"));
        assertTrue(parser.currentOption == null);
    }

    /**
     * Test handling multiple short options.
     */
    @Test
    public void testHandlingMultipleShortOptions() {
        String[] args = {"-fa"};
        Options options = new Options();
        options.addOption("foo", true, "helpFoo");
        options.addOption("a", true, "helpA");
        options.addOption("foo", true, "helpFooAgain");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo", "--foo"}, parsedArgs);
        assertTrue(parser.currentOption == null);
    }

    /**
     * Test handling short and long options together.
     */
    @Test
    public void testHandlingShortAndLongOptionsTogether() {
        String[] args = {"--foo", "-a", "--bar"};
        Options options = new Options();
        options.addOption("foo", true, "helpFoo");
        options.addOption("a", true, "helpA");
        options.addOption("bar", true, "helpBar");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo", "--a", "--bar"}, parsedArgs);
        assertTrue(parser.currentOption == null);
    }

    /**
     * Test handling unknown options.
     */
    @Test(expected = ParseException.class)
    public void testHandlingUnknownOptions() {
        String[] args = {"--unknown"};
        Options options = new Options();
        options.addOption("foo", true, "helpFoo");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test handling unknown options with stopAtNonOption flag.
     */
    @Test
    public void testHandlingUnknownOptionsWithStopAtNonOption() {
        String[] args = {"--unknown"};
        Options options = new Options();
        options.addOption("foo", true, "helpFoo");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--unknown"}, parsedArgs);
        assertTrue(parser.eatTheRest);
    }
}