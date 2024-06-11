package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private static final String OPTION_A = "a";
    private static final String OPTION_B = "b";
    private static final String OPTION_C = "c";
    private static final String OPTION_D = "d";
    private static final String ARG = "argument";

    @Before
    public void setUp() {
        Options options = new Options();
        options.addOption(OPTION_A, true, "Option A");
        options.addOption(OPTION_B, false, "Option B", ARG);
        options.addOption(OPTION_C, false, "Option C");
        parser = new DefaultParser();
    }

    @Test
    public void testParse() throws ParseException {
        String[] arguments = new String[] { "-a", "1", "-b", ARG };
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getArgs().size());
        assertEquals(ARG, cmd.getArgs().get(0));
        assertTrue(cmd.hasOption(OPTION_A));
        assertTrue(cmd.hasOption(OPTION_B));
        assertFalse(cmd.hasOption(OPTION_C));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("optionA", "true");
        properties.setProperty("optionB", ARG);

        CommandLine cmd = parser.parse(options, new String[0], properties);

        assertEquals(1, cmd.getArgs().size());
        assertEquals(ARG, cmd.getArgs().get(0));
        assertTrue(cmd.hasOption(OPTION_A));
        assertTrue(cmd.hasOption(OPTION_B));
        assertFalse(cmd.hasOption(OPTION_C));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException {
        String[] arguments = new String[] { "-a" };
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        String[] arguments = new String[] { OPTION_Z };
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(OPTION_D, true, "Option D", ARG);
        options.addRequiredOption(OPTION_A, true, "Option A");
        options.addRequiredOption(OPTION_B, true, "Option B");

        String[] arguments = new String[] { ARG, "-a", "-b" };
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption(OPTION_A));
        assertTrue(cmd.hasOption(OPTION_B));
        assertTrue(cmd.hasOption(OPTION_D));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(OPTION_D, true, "Option D", ARG);
        options.addRequiredOption(OPTION_A, true, "Option A");
        options.addRequiredOption(OPTION_B, true, "Option B");

        String[] arguments = new String[] { ARG };
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test
    public void testParseOptionalGroups() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("Group");
        group.addOption(OPTION_C, false, null, "Option C");
        options.addOptionGroup(group);
        options.addOption(OPTION_B, false, "Option B", ARG);

        String[] arguments = new String[] { "-c", "-b", ARG };
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption(OPTION_B));
        assertTrue(cmd.hasOption(OPTION_C));
    }
}