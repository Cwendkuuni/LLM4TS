package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParsingException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.test.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    private static final String[] ARGS = new String[]{"-a", "--b", "--c", "--d=E", "--e=F", "--f", "--g", "1", "2", "3"};
    private static final String[] WRONG_ARGS = new String[]{"--a", "--b", "--c", "--d=E", "--e=F", "--x", "--g", "1", "2", "3"};
    private static final String[] ARGS_WITH_PROPERTIES = new String[]{"--a", "--b", "--c", "--d=E", "--e=F", "--g", "--properties=properties.txt"};
    private static final String[] ARGS_WITH_PROPERTIES_INVALID = new String[]{"--a", "--b", "--c", "--d=E", "--e=F", "--g", "--properties=properties_invalid.txt"};
    private static final String[] ARGS_WITH_MISSING_REQUIRED = new String[]{"--a", "--b", "--c"};

    private Options options;

    @Before
    public void setup()
    {
        options = new Options();
        options.addRequiredOption("a", "a", false, "Option A");
        options.addOption("b", "b", false, "Option B");
        options.addOption("c", "c", false, "Option C");
        options.addOption("d", "d", true, "Option D");
        options.addOption("e", "e", true, "Option E");
        options.addOption("f", "f", false, "Option F");
        options.addOptionGroup(new OptionGroup("g", "Group", "Options in the group", Arrays.asList(new Option("g1", "g1"), new Option("g2", "g2"))));
    }

    @Test
    public void testParseArgs() throws Exception
    {
        CommandLine cmd = new Parser().parse(options, ARGS, null);

        assertEquals(12, cmd.getArgs().size());
        assertEquals("1", cmd.getArgString(0));
        assertEquals("2", cmd.getArgString(11));
        assertEquals(null, cmd.getArgString(7));

        assertEquals("E", cmd.getOptionValue("d"));
        assertEquals("F", cmd.getOptionValue("e"));
        assertNull(cmd.getOptionValue("x"));

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("x"));

        assertEquals(null, cmd.getOptionValue("g", 0));
        assertEquals("g1", cmd.getOptionValue("g", 1));
    }

    @Test
    public void testParseArgsWithStopAtNonOption() throws Exception
    {
        CommandLine cmd = new Parser().parse(options, ARGS, true);

        assertEquals(13, cmd.getArgs().size());
        assertEquals("--", cmd.getArgString(12));

        assertEquals("E", cmd.getOptionValue("d"));
        assertEquals("F", cmd.getOptionValue("e"));
        assertNull(cmd.getOptionValue("x"));

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("x"));

        assertEquals(null, cmd.getOptionValue("g", 0));
        assertEquals("g1", cmd.getOptionValue("g", 1));
    }

    @Test
    public void testParseArgsWithProperties() throws IOException, ParsingException
    {
        CommandLine cmd = new Parser().parse(options, ARGS_WITH_PROPERTIES, null, true);

        Properties properties = cmd.getParsedProperties();
        assertEquals(2, properties.size());
        assertEquals("E", properties.getProperty("d"));
        assertEquals("F", properties.getProperty("e"));

        assertEquals(11, cmd.getArgs().size());
        assertEquals("1", cmd.getArgString(0));
        assertEquals("2", cmd.getArgString(10));
        assertEquals(null, cmd.getArgString(7));

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("x"));

        assertEquals(null, cmd.getOptionValue("g", 0));
        assertEquals("g1", cmd.getOptionValue("g", 1));
    }

    @Test
    public void testParseArgsWithPropertiesInvalid() throws IOException, ParsingException
    {
        assertThrows(ParsingException.class, () -> new Parser().parse(options, ARGS_WITH_PROPERTIES_INVALID, null, true));
    }

    @Test
    public void testMissingRequiredOption() throws Exception
    {
        assertThrows(MissingOptionException.class, () -> new Parser().parse(options, ARGS_WITH_MISSING_REQUIRED, null));
    }

    @Test
    public void testUnrecognizedOption() throws Exception
    {
        assertThrows(UnrecognizedOptionException.class, () -> new Parser().parse(options, WRONG_ARGS, null));
    }
}