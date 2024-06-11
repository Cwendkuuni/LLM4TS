package org.apache.commons.cli.parser;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSetOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        assertSame(parser.getOptions(), options);
    }

    @Test
    public void testGetOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        assertSame(parser.getOptions(), options);
    }

    @Test
    public void testGetRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = parser.getRequiredOptions();
        options.addOption("a", true, "Option A");
        parser.setOptions(options);
        assertEquals(0, requiredOptions.size());

        options.addOption("b", true, "Option B", "B");
        requiredOptions = parser.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals("b", requiredOptions.get(0).getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithNullOptions() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                throw new UnsupportedOperationException();
            }
        };
        parser.parse(null, new String[0]);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithNullArguments() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A");
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, (String[]) null);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A");
        parser.setOptions(options);

        String[] arguments = new String[]{"--a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.hasOption("a"));

        arguments = new String[]{"--a", "value"};
        cmd = parser.parse(options, arguments);
        assertFalse(cmd.getArgs().isEmpty());
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A");
        parser.setOptions(options);

        String[] arguments = new String[]{"--b"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }

        arguments = new String[]{"--a", "--b"};
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A");
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                if (stopAtNonOption) {
                    return new String[0];
                }
                return arguments;
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A");
        parser.setOptions(options);

        String[] arguments = new String[]{"--a"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.hasOption("a"));

        arguments = new String[]{"--a", "value"};
        cmd = parser.parse(options, arguments, false);
        assertFalse(cmd.getArgs().isEmpty());
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));

        arguments = new String[]{"--b", "value"};
        cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.getArgs().size() == 1);
        assertEquals("value", cmd.getArgs().get(0));
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new DefaultParser() {
            @Override
            protected void processProperties(Properties properties) {
                // empty implementation
            }
        };

        Properties properties = new Properties();
        parser.parse(new Options(), new String[0], properties);
        assertTrue(properties.isEmpty());

        properties.setProperty("a", "value");
        parser.parse(new Options().addOption("a", true, "Option A"), new String[0], properties);
        assertFalse(properties.isEmpty());
        assertTrue(parser.cmd.hasOption("a"));
        assertEquals("value", parser.cmd.getOptionValue("a"));
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected void checkRequiredOptions() throws MissingOptionException {
                throw new UnsupportedOperationException();
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A", "A");
        options.addOption("b", true, "Option B");
        parser.setOptions(options);

        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }

        options.removeOption("a");
        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }

        options.addOption("c", true, "Option C");
        parser.parse(options, new String[0]);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected void processArgs(Option opt, ListIterator iter)
                    throws ParseException {
                throw new UnsupportedOperationException();
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A", "A");
        parser.setOptions(options);

        String[] arguments = new String[]{"--a", "arg1", "--a", "arg2"};
        try {
            parser.parse(options, arguments, false);
            fail();
        } catch (MissingArgumentException e) {
            // expected
        }

        parser = new DefaultParser() {
            @Override
            protected void processArgs(Option opt, ListIterator iter)
                    throws ParseException {
                processArgs(opt, iter);
            }
        };

        options = new Options();
        options.addOption("a", true, "Option A", "A");
        parser.setOptions(options);

        arguments = new String[]{"--a", "arg1", "--a", "arg2"};
        parser.parse(options, arguments, false);
        assertEquals(2, parser.cmd.getOptions().size());
        assertEquals("arg1", parser.cmd.getOptionValue("a"));
        assertEquals("arg2", parser.cmd.getOptionValue("a"));
    }

    @Test
    public void testProcessOption() throws ParseException {
        Parser parser = new DefaultParser() {
            @Override
            protected void processOption(String arg, ListIterator iter)
                    throws ParseException {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void processArgs(Option opt, ListIterator iter)
                    throws ParseException {
                // empty implementation
            }
        };

        Options options = new Options();
        options.addOption("a", true, "Option A", "A");
        parser.setOptions(options);

        String[] arguments = new String[]{"--a", "arg1"};
        try {
            parser.parse(options, arguments, false);
            fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }

        parser = new DefaultParser() {
            @Override
            protected void processOption(String arg, ListIterator iter)
                    throws ParseException {
                processOption(arg, iter);
            }

            @Override
            protected void processArgs(Option opt, ListIterator iter)
                    throws ParseException {
                // empty implementation
            }
        };

        options = new Options();
        options.addOption("a", true, "Option A", "A");
        parser.setOptions(options);

        arguments = new String[]{"--a", "arg1"};
        parser.parse(options, arguments, false);
        assertTrue(parser.cmd.hasOption("a"));
        assertEquals("arg1", parser.cmd.getOptionValue("a"));
    }
}