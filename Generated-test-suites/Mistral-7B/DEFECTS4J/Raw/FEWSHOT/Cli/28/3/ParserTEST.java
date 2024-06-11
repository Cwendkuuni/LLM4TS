package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        Options options = new Options();
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            assertEquals(0, cmd.getArgs().size());
            assertNull(cmd.getOption(Constants.LONG_OPT_HELP));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseSingleArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"arg1"});
            assertEquals(1, cmd.getArgs().size());
            assertEquals("arg1", cmd.getArgs().get(0));
            assertNull(cmd.getOption(Constants.LONG_OPT_HELP));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseSingleOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"-a"});
            assertEquals(0, cmd.getArgs().size());
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.getOption("a").isSet());
            assertNull(cmd.getOption(Constants.LONG_OPT_HELP));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseSingleOptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1", "--b=arg2", "--c=arg3", "-d"});
            assertEquals(2, cmd.getArgs().size());
            assertEquals("arg1", cmd.getArgs().get(0));
            assertEquals("arg2", cmd.getOptionValue("b"));
            assertEquals("arg3", cmd.getOptionValue("c"));
            assertNull(cmd.getOptionValue("d"));
            assertNull(cmd.getOption(Constants.LONG_OPT_HELP));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "-c", "-d"});
            assertEquals(0, cmd.getArgs().size());
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertTrue(cmd.hasOption("c"));
            assertTrue(cmd.hasOption("d"));
            assertTrue(cmd.isSetAll());
            assertNull(cmd.getOption(Constants.LONG_OPT_HELP));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseMultipleOptionsWithArgs() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options,
                    new String[]{"-a", "arg1", "--b=arg2", "--c=arg3", "-d=arg4"});
            assertEquals(1, cmd.getArgs().size());
            assertEquals("arg1", cmd.getArgs().get(0));
            assertEquals("arg2", cmd.getOptionValue("b"));
            assertEquals("arg3", cmd.getOptionValue("c"));
            assertEquals("arg4", cmd.getOptionValue("d"));
            assertNull(cmd.getOptionValue("a"));
            assertNull(cmd.getOption(Constants.LONG_OPT_HELP));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseMissingOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"-x"});
            fail();
        } catch (ParseException e) {
            assertEquals("Unrecognized option: -x", e.getMessage());
        }
    }

    @Test
    public void testParseMissingOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--a=x"});
            fail();
        } catch (ParseException e) {
            assertEquals("Unrecognized option: --a=x", e.getMessage());
        }
    }

    @Test
    public void testParseMissingRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", false, "Required Option B");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("[b]", e.getMessage());
        }
    }

    @Test
    public void testParseMissingRequiredOptionWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption(null, "b", false, "Required Option B");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("[b]", e.getMessage());
        }
    }

    @Test
    public void testParseMissingGroupRequiredOption() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addRequiredOption("a", false, "Required Option A", 1);

        options.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("[a]", e.getMessage());
        }
    }

    @Test
    public void testParseMissingGroupRequiredOptionWithArguments() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addRequiredOption("a", false, "Required Option A", 1);

        options.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("[a]", e.getMessage());
        }
    }

    @Test
    public void testParseMissingGroupRequiredOptionWithValue() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addRequiredOption("a", false, "Required Option A", 1);

        options.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--a=value"});
            fail();
        } catch (MissingOptionException e) {
            assertEquals("[a]", e.getMessage());
        }
    }

    @Test
    public void testParseMultipleMissingGroupRequiredOptions() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addRequiredOption("a", false, "Required Option A", 1);
        group.addRequiredOption("b", false, "Required Option B", 1);

        options.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("[a, b]", e.getMessage());
        }
    }

    @Test
    public void testParseMultipleMissingGroupRequiredOptionsWithValue() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addRequiredOption("a", false, "Required Option A", 1);
        group.addRequiredOption("b", false, "Required Option B", 1);

        options.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--a=value", "--b=value"});
            fail();
        } catch (MissingOptionException e) {
            assertEquals("[a, b]", e.getMessage());
        }
    }

    @Test
    public void testParseMissingRequiredOptionWithValue() {
        Options options = new Options();
        options.addRequiredOption("a", false, "Required Option A");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[]{"--a=value"});
            fail();
        } catch (MissingOptionException e) {
            assertEquals("[a]", e.getMessage());
        }
    }
}