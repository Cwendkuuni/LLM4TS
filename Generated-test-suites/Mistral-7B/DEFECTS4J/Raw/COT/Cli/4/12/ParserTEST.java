package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseOptions;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Tests the Parser class.
 */
public class ParserTest {

    @Test
    public void testParser() throws Exception {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option.");
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-o"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("-o", cmd.getArgs().get(0));

        args = new String[]{"--option"};
        cmd = parser.parse(options, args, null);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("--option", cmd.getArgs().get(0));

        args = new String[]{"-o", "value"};
        cmd = parser.parse(options, args, null);
        assertNotNull(cmd.getOptionValue("option"));
        assertEquals("value", cmd.getOptionValue("option"));
        assertNull(cmd.getOptionValue("unknown"));

        args = new String[]{"-o", "--value"};
        try {
            parser.parse(options, args, null);
            fail();
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: --value", e.getMessage());
        }

        args = new String[]{"-o", "--", "value"};
        cmd = parser.parse(options, args, false);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("-o--", cmd.getArgs().get(0));
        assertEquals("value", cmd.getArgs().get(1));

        args = new String[]{"-o", "--", "--value"};
        try {
            parser.parse(options, args, false);
            fail();
        } catch (ParseException e) {
            assertEquals("Unrecognized option: --", e.getMessage());
        }

        options.addOption("n", "number", true, "A number.");
        args = new String[]{"-o", "value", "-n", "123"};
        cmd = parser.parse(options, args, null);
        assertNotNull(cmd.getOptionValue("option"));
        assertEquals("value", cmd.getOptionValue("option"));
        assertNotNull(cmd.getOptionValue("number"));
        assertEquals("123", cmd.getOptionValue("number"));

        args = new String[]{"-o", "value", "-n"};
        try {
            parser.parse(options, args, null);
            fail();
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: number", e.getMessage());
        }

        args = new String[]{"-o", "--number"};
        try {
            parser.parse(options, args, null);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing option: number", e.getMessage());
        }

        options.addOptionGroup(new OptionGroup("group", "Group options.", true));
        options.getOptionGroup("group").addOption("g", "group", false, "A group option.");
        args = new String[]{"-o", "-g"};
        cmd = parser.parse(options, args, null);
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.getOptionValue("group") instanceof OptionGroup);
        assertEquals("group", cmd.getOptionValue("group").getKey());

        args = new String[]{"-o", "--group"};
        cmd = parser.parse(options, args, null);
        assertNotNull(cmd.getOptionValue("group"));
        assertEquals("group", cmd.getOptionValue("group").getKey());

        args = new String[]{"-o", "--group=subgroup"};
        OptionGroup subgroup = new OptionGroup("subgroup", "Subgroup options.", false);
        options.addOptionGroup(subgroup);
        options.getOptionGroup("group").addOption("g", "subgroup", false, "A subgroup option.", subgroup);
        cmd = parser.parse(options, args, null);
        assertNotNull(cmd.getOptionValue("group"));
        assertEquals("subgroup", cmd.getOptionValue("group").getKey());
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option.");
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption) throws ParseException {
                return new DefaultParser().parse(options, arguments, properties);
            }
        };

        Properties properties = new Properties();
        properties.setProperty("o", "value");
        String[] args = {"--option"};
        CommandLine cmd = parser.parse(options, args, properties, false);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("--option", cmd.getArgs().get(0));

        args = new String[]{"--option", "--value"};
        cmd = parser.parse(options, args, properties, false);
        assertNotNull(cmd.getOptionValue("option"));
        assertEquals("value", cmd.getOptionValue("option"));

        args = new String[]{"--option=value"};
        cmd = parser.parse(options, args, properties, false);
        assertEquals(1, cmd.getOptions().size());
        assertEquals("value", cmd.getOptionValue("option"));

        args = new String[]{"--option=value2"};
        properties.setProperty("o", "value2");
        cmd = parser.parse(options, args, properties, false);
        assertEquals(1, cmd.getOptions().size());
        assertEquals("value2", cmd.getOptionValue("option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullOptions() {
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                throw new UnsupportedOperationException();
            }
        };
        parser.parse(null, new String[0], null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullArguments() {
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                throw new UnsupportedOperationException();
            }
        };
        parser.parse(new Options(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullProperties() {
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption) throws ParseException {
                return new DefaultParser().parse(options, arguments, null);
            }
        };
        parser.parse(new Options(), new String[0], null);
    }
}