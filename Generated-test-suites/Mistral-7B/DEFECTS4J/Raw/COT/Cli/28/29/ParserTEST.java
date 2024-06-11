package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    @Test
    public void testParse() throws Exception
    {
        // Given
        String[] args = {"--test", "-a", "-b", "c", "--option-with-arg=d"};
        Options options = new Options();
        options.addOption("test", true, "t", "Test flag");
        options.addOption("a", true, "a", "Option A");
        options.addOption("b", false, "b", "Option B");
        options.addOption("option-with-arg", true, "o", "Option with argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, args, null, false);

        // Then
        assertTrue(cmd.hasOption("test"));
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("option-with-arg"));
        assertEquals("d", cmd.getOptionValue("option-with-arg"));
        assertEquals(3, cmd.getArgs().size());
        assertEquals("c", cmd.getArgs().get(0));
        assertEquals("", cmd.getArgs().get(1));
        assertEquals("", cmd.getArgs().get(2));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception
    {
        // Given
        String[] args = {"--test", "-a", "c"};
        Options options = new Options();
        options.addOption("test", true, "t", "Test flag");
        options.addOption("a", true, "a", "Option A");
        options.addOption("option", true, "o", "Option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, args, null, false);

        // Then
        fail("MissingOptionException expected but not thrown");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception
    {
        // Given
        String[] args = {"--test", "-a"};
        Options options = new Options();
        options.addOption("test", true, "t", "Test flag");
        options.addOption("a", true, "a", "Option A", true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, args, null, false);

        // Then
        fail("MissingArgumentException expected but not thrown");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception
    {
        // Given
        String[] args = {"--test", "--unknown-option", "-a"};
        Options options = new Options();
        options.addOption("test", true, "t", "Test flag");
        options.addOption("a", true, "a", "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, args, null, false);

        // Then
        fail("UnrecognizedOptionException expected but not thrown");
    }
}