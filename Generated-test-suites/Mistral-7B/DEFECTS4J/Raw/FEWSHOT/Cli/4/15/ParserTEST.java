package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "-bar", "-baz", "--baz", "qux"};

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(6, cmd.getArgs().size());
            assertEquals("--foo", cmd.getOptionValues("foo")[0]);
            assertNull(cmd.getOptionValues("quux")[0]);
            assertEquals("qux", cmd.getArgs().get(5));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertSame(options, cmd.getOptions());
    }

    @Test
    public void testParse_requiredOptions() {
        Options options = new Options();
        List<Option> requiredOptions = Arrays.asList(
                new Option("r", "required", true, "a required option"),
                new Option("o", "optional", false, "an optional option")
        );
        options.setRequiredOptions(requiredOptions);
        String[] arguments = new String[]{"--o", "-r"};

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("required"));
            assertFalse(cmd.hasOption("quux"));
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("--required"));
        }

        assertEquals(1, cmd.getArgs().size());
        assertEquals("-r", cmd.getArgs().get(0));
    }

    @Test
    public void testParse_properties() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=bar", "--qux=quux", "baz"};

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            assertEquals(3, cmd.getArgs().size());
            assertEquals("bar", cmd.getOptionValues("foo")[0]);
            assertEquals("quux", cmd.getOptionValues("qux")[0]);
            assertEquals("baz", cmd.getArgs().get(2));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertSame(options, cmd.getOptions());
    }

    @Test
    public void testParse_booleanOptions() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "--bar", "--qux"};

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            Option foo = options.getOption("foo");
            Option bar = options.getOption("bar");
            Option qux = options.getOption("qux");

            assertFalse(cmd.hasOption("quux"));
            assertTrue(cmd.hasOption("foo"));
            assertFalse(foo.getValue().isEmpty());
            assertTrue(cmd.hasOption("bar"));
            assertFalse(bar.getValue().isEmpty());
            assertFalse(cmd.hasOption("qux"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParse_multiValueOptions() {
        Options options = new Options();
        List<Option> multiValueOptions = Arrays.asList(
                new Option("m", "multiValue", true, "multi value option")
                        .setValueSeparator(' ').isMultiValued(true)
        );
        options.addOptions(multiValueOptions);
        String[] arguments = new String[]{"--m=v1", "--m=v2", "--m=v3"};

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            Option multiValueOption = options.getOption("multiValue");
            assertEquals(3, multiValueOption.getValue().size());
            assertEquals("v1", multiValueOption.getValue().get(0));
            assertEquals("v2", multiValueOption.getValue().get(1));
            assertEquals("v3", multiValueOption.getValue().get(2));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParse_help() {
        Options options = new Options();
        String[] arguments = new String[]{"--help"};

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            assertNotNull(cmd.getHelpMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParse_invalidOptions() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "--qux=quux", "bar"};

        Parser parser = new DefaultParser();

        assertThrows(ParseException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    @Test
    public void testParse_longOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--long-option"};

        Parser parser = new DefaultParser();

        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("longOption"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParse_shortOption() {
        Options options = new Options();
        List<Option> shortOptions = Arrays.asList(
                new Option('f', "foo", true, "a foo option")
        );
        options.addOptionGroup(new OptionGroup("foo", "Foo options", shortOptions.toArray(new Option[0])));
        String[] arguments = new String[]{"-f"};

        Parser parser = new DefaultParser();

        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("foo"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParse_shortOptionWithValue() {
        Options options = new Options();
        List<Option> shortOptions = Arrays.asList(
                new Option('f', "foo", true, "a foo option")
        );
        options.addOptionGroup(new OptionGroup("foo", "Foo options", shortOptions.toArray(new Option[0])));
        String[] arguments = new String[]{"-f", "value"};

        Parser parser = new DefaultParser();

        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("foo"));
            assertEquals("value", cmd.getOptionValues("foo")[0]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParse_shortOptionWithValueAndLongOption() {
        Options options = new Options();
        List<Option> shortOptions = Arrays.asList(
                new Option('f', "foo", true, "a foo option"),
                new Option("long-foo", "long foo option", true, "a long foo option")
        );
        options.addOptionGroup(new OptionGroup("foo", "Foo options", shortOptions.toArray(new Option[0])));
        String[] arguments = new String[]{"-f", "value", "--long-foo"};

        Parser parser = new DefaultParser();

        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("foo"));
            assertEquals("value", cmd.getOptionValues("foo")[0]);
            assertTrue(cmd.hasOption("longFoo"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParse_positionalArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"foo", "1", "2", "3"};

        Parser parser = new DefaultParser();

        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);
            assertEquals(3, cmd.getArgs().size());
            assertEquals("2", cmd.getArgs().get(1));
            assertEquals("3", cmd.getArgs().get(2));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}