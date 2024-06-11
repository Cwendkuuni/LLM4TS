package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Properties;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParseEmpty() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNull(cmd.getArgs());
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseSimple() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] args = {"-a", "--b", "2"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals(Arrays.asList("-a", "--b", "2"), Arrays.asList(cmd.getArgs()));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(2, cmd.getOptionValue("b").intValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] args = {"-a", "--b"};
        parser.parse(options, args);
        fail();
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", null, "Option C");

        Properties props = new Properties();
        props.setProperty("c", "Value C");

        String[] args = {"-a", "--b", "--c=Value C"};
        CommandLine cmd = parser.parse(options, args, props, false);
        assertEquals(Arrays.asList("-a", "--b", "--c=Value C"), Arrays.asList(cmd.getArgs()));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
        assertEquals("Value C", cmd.getOptionValue("c"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-z", "-a"};
        parser.parse(options, args, null, false);
        fail();
    }

    @Test
    public void testParseStopAtNonOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--", "-a", "arg"};
        CommandLine cmd = parser.parse(options, args, null, true);
        assertEquals(Arrays.asList("--", "-a", "arg"), Arrays.asList(cmd.getArgs()));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithoutOptions() throws Exception
    {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(null, new String[0]);
        assertNull(cmd.getArgs());
        assertFalse(cmd.hasOptions());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] args = {"-a", "1"};
        parser.parse(options, args);
        fail();
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseNoSuchOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-x", "--a"};
        parser.parse(options, args);
        fail();
    }
}