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
    public void testParserBasicFunctionality() {
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        parser.setOptions(options);
        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals(1, cmd.getOptionValue("a"));
            assertEquals(2, cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParserMissingRequiredOption() {
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("c", true, "Option C");
        options.addRequiredOption("b", true, "Option B");

        parser.setOptions(options);
        parser.parse(options, args);
        fail("Should have thrown ParseException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParserUnrecognizedOption() {
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-x", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        parser.setOptions(options);
        parser.parse(options, args);
        fail("Should have thrown UnrecognizedOptionException");
    }

    @Test
    public void testParserOptionalArguments() {
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a", "1", "--optionB", "2", "--optionB", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        parser.setOptions(options);
        try {
            CommandLine cmd = parser.parse(options, args);
            List<Object> bValues = cmd.getOptionValues("b");
            assertEquals(2, bValues.size());
            assertEquals("2", bValues.get(0));
            assertEquals("3", bValues.get(1));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testParserMissingArgument() {
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B").requiredArg();

        parser.setOptions(options);
        parser.parse(options, args);
        fail("Should have thrown MissingArgumentException");
    }
}