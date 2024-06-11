package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test case for Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addRequiredOption("a", "alpha", true, "alpha option");
        options.addOption("b", "beta", false, "beta option");
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @After
    public void tearDown() {
        options = null;
        parser = null;
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = {"-a"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("-a", cmd.getArgs().get(0));

        args = new String[]{"--alpha"};
        cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("alpha"));
        assertNull(cmd.getOptionValue("alpha"));

        args = new String[]{"-a", "value"};
        cmd = parser.parse(options, args, null, false);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("-a", cmd.getArgs().get(0));
        assertEquals("value", cmd.getOptionValue("alpha"));

        args = new String[]{"-a", "--beta"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, null, false));

        args = new String[]{"--beta", "value"};
        cmd = parser.parse(options, args, null, false);
        assertFalse(cmd.hasOption("alpha"));
        assertEquals("value", cmd.getOptionValue("beta"));

        args = new String[]{"-a", "--", "value"};
        cmd = parser.parse(options, args, null, true);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("value", cmd.getArgs().get(0));

        args = new String[]{"--", "value"};
        cmd = parser.parse(options, args, null, true);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("value", cmd.getArgs().get(0));

        args = new String[]{"-a", "-b"};
        cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("alpha"));
        assertTrue(cmd.hasOption("beta"));

        args = new String[]{"--alpha", "--beta"};
        cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("alpha"));
        assertTrue(cmd.hasOption("beta"));

        args = new String[]{"--alpha", "--beta=value"};
        cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("alpha"));
        assertTrue(cmd.hasOption("beta"));
        assertEquals("value", cmd.getOptionValue("beta"));

        args = new String[]{"--alpha=value", "--beta=value"};
        cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("alpha"));
        assertTrue(cmd.hasOption("beta"));
        assertEquals("value", cmd.getOptionValue("alpha"));
        assertEquals("value", cmd.getOptionValue("beta"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = {};
        parser.parse(options, args, null, false);
        Assert.fail();
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptionException() throws Exception {
        parser = new HelpFormatter();
        options = new Options();
        options.addRequiredOption("a", "alpha", true, "alpha option");
        parser.setOptions(options);
        String[] args = {};
        parser.parse(options, args, null, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        options.getOption("a").setArgName("value");
        parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a"};
        parser.parse(options, args, null, false);
        Assert.fail();
    }

    @Test(expected = RuntimeException.class)
    public void testParseInvalidOptionValueType() throws ParseException {
        options.getOption("a").setValueSeparator('=');
        parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a=value,value"};
        parser.parse(options, args, null, false);
        Assert.fail();
    }

    @Test
    public void testParseProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("alpha", "value1");
        properties.setProperty("beta", "value2");

        options.addPropertyAliases("alpha", "a");
        options.addPropertyAliases("beta", "b");

        parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals("value2", cmd.getOptionValue("b"));
    }
}