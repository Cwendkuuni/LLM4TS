package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseBasic() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] args = {"--a"};
        CommandLine cmd = parser.parse(options, args, null);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithValue() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("b", true, "Option B");
        String[] args = {"--b", "value"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, null);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = {"--invalid"};
        parser.parse(options, args, null);
        fail("ParseException expected but not thrown");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = {"--unrecognized"};
        parser.parse(options, args, null);
        fail("UnrecognizedOptionException expected but not thrown");
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("r", false, "Required Option");
        String[] args = {};
        try {
            parser.parse(options, args, null);
            fail("MissingOptionException expected but not thrown");
        } catch (MissingOptionException e) {
            List requiredOptions = options.getRequiredOptions();
            assertEquals(1, requiredOptions.size());
            assertEquals("r", requiredOptions.get(0).getKey());
        }
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String[] args = {"--a", "value1", "--b", "value2"};
        CommandLine cmd = parser.parse(options, args, null);
        assertTrue(cmd.hasOption("a"));
        assertEquals("value1", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value2", cmd.getOptionValue("b"));
        assertArrayEquals(new String[]{}, cmd.getArgs());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("p", true, "Option P");
        Properties properties = new Properties();
        properties.setProperty("p", "value");
        CommandLine cmd = parser.parse(options, null, properties, false);
        assertTrue(cmd.hasOption("p"));
        assertEquals("value", cmd.getOptionValue("p"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithTooManyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = {"--a", "value1", "value2"};
        try {
            parser.parse(options, args, null);
            fail("ParseException expected but not thrown");
        } catch (ParseException e) {
            assertEquals("Unexpected option: value2", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnmatchedArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = {"--a", "value1", "--b", "value3"};
        try {
            parser.parse(options, args, null);
            fail("ParseException expected but not thrown");
        } catch (ParseException e) {
            assertEquals("Option '--b' is unrecognized.", e.getMessage());
        }
    }
}