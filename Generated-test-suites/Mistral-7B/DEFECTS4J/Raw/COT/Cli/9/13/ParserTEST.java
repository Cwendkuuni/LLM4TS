package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Parser.class)
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        parser = PowerMockito.spy(new DefaultParser());
        parser.setOptions(options);
    }

    @Test
    public void testSetOptions() {
        assertNull(parser.getOptions());
        parser.setOptions(options);
        assertNotNull(parser.getOptions());
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        assertNull(parser.getOptions());
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        options.addOption("a", true, "Option A");
        String[] arguments = {"--a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] arguments = {"--a", "argA", "--b", "argB"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("argA", cmd.getOptionValue("a"));
        assertEquals("argB", cmd.getOptionValue("b"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("argA", cmd.getArgs().get(0));
        assertEquals("argB", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException, MissingOptionException {
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"--a"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException, UnrecognizedOptionException {
        options.addOption("a", true, "Option A");
        String[] arguments = {"--c"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption("a", true, "Option A");
        String[] arguments = {"--a", "argA", "--", "nonOption", "argB"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("argA", cmd.getArgs().get(0));
        assertEquals("nonOption", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption("a", true, "Option A");
        Properties properties = new Properties();
        properties.setProperty("a", "argA");
        CommandLine cmd = parser.parse(options, null, properties, false);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertEquals("argA", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithMultipleProperties() throws ParseException {
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        Properties properties = new Properties();
        properties.setProperty("a", "argA");
        properties.setProperty("b", "argB");
        CommandLine cmd = parser.parse(options, null, properties, false);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("argA", cmd.getOptionValue("a"));
        assertEquals("argB", cmd.getOptionValue("b"));
    }
}