package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Parser Test Cases
 */
public class ParserTest {

    @Test
    public void testSetOptions() throws ParseException {
        Parser parser = new DefaultParser();
        assertNull(parser.getOptions());

        Options options = new Options();
        parser.setOptions(options);

        assertNotNull(parser.getOptions());
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new DefaultParser();
        assertNull(parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new DefaultParser();
        assertNotNull(parser.getRequiredOptions());
        assertEquals(0, parser.getRequiredOptions().size());

        Options options = new Options();
        List requiredOptions = parser.getRequiredOptions();
        options.addRequiredOption("option1", true, "Description for option1");
        parser.setOptions(options);
        assertEquals(1, parser.getRequiredOptions().size());
        assertEquals("option1", parser.getRequiredOptions().get(0).getKey());
    }

    @Test
    public void testFlattenEmpty() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        String[] args = new String[0];
        CommandLine cmd = parser.parse(new Options(), args);
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testFlattenOneArgument() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[]{"arg1"};
            }
        };

        String[] args = new String[]{"arg1"};
        CommandLine cmd = parser.parse(new Options(), args);
        assertNotNull(cmd);
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testFlattenTwoArguments() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[]{"arg1", "arg2"};
            }
        };

        String[] args = new String[]{"arg1", "arg2"};
        CommandLine cmd = parser.parse(new Options(), args);
        assertNotNull(cmd);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test
    public void testFlattenTwoArgumentsWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[]{"arg1", "arg2"};
            }
        };

        String[] args = new String[]{"arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(new Options(), args, true);
        assertNotNull(cmd);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        assertEquals("arg3", cmd.getArgs().get(2));
        assertFalse(cmd.hasOption("arg3"));
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNonOption() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[]{"arg1", "nonOption"};
            }
        };

        String[] args = new String[]{"arg1", "nonOption"};
        parser.parse(new Options(), args, false);
    }

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = new String[0];
        CommandLine cmd = parser.parse(new Options(), args);
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseOneArgument() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = new String[]{"arg1"};
        CommandLine cmd = parser.parse(new Options(), args);
        assertNotNull(cmd);
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testParseTwoArguments() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = new String[]{"arg1", "arg2"};
        CommandLine cmd = parser.parse(new Options(), args);
        assertNotNull(cmd);
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = new String[]{"--unrecognizedOption"};
        parser.parse(new Options(), args);
    }

    @Test(expected = ParseException.class)
    public void testParseRequiredOptionMissing() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("option1", true, "Description for option1");
        Parser parser = new DefaultParser();
        String[] args = new String[0];
        parser.setOptions(options);
        parser.parse(options, args);
    }

    @Test
    public void testParseRequiredOptionPresent() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("option1", true, "Description for option1");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"--option1"};
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("option1"));
        assertEquals(true, cmd.getOptionValue("option1"));
    }

    @Test
    public void testParseOptionalOptionWithValue() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "Description for option1");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"--option1", "value1"};
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("option1"));
        assertEquals("value1", cmd.getOptionValue("option1"));
    }

    @Test
    public void testParseOptionalOptionWithoutValue() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "Description for option1");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"--option1"};
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("option1"));
        assertNull(cmd.getOptionValue("option1"));
    }

    @Test
    public void testParseOptionalGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group1");
        group.setRequired(true);
        group.addOption("option1", true, "Description for option1");
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        String[] args = new String[0];
        parser.setOptions(options);

        try {
            parser.parse(options, args);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals(Arrays.asList(group), e.getMissingOptions());
        }

        args = new String[]{"--group1"};
        parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(options, args);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals(Arrays.asList(group.getOption("option1")), e.getMissingOptions());
        }

        args = new String[]{"--group1", "value1"};
        parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("option1"));
        assertEquals("value1", cmd.getOptionValue("option1"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.put("option1", "value1");
        Options options = new Options();
        options.addOption("option1", true, "Description for option1");
        Parser parser = new DefaultParser();
        String[] args = new String[0];
        parser.setOptions(options);
        parser.parse(options, args, properties);
        assertTrue(cmd.hasOption("option1"));
        assertEquals("value1", cmd.getOptionValue("option1"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "Description for option1");
        options.addOption("option2", true, "Description for option2")
                .requiredArg().hasArg();
        Parser parser = new DefaultParser();
        String[] args = new String[]{"--option1"};
        parser.setOptions(options);
        parser.parse(options, args);
    }

    @Test
    public void testParseWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "Description for option1");
        options.addOption("option2", true, "Description for option2")
                .requiredArg().hasArg();
        Parser parser = new DefaultParser();
        String[] args = new String[]{"--option1", "arg1", "--option2", "arg2"};
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("option1"));
        assertEquals("arg1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("arg2", cmd.getOptionValue("option2"));
    }
}