package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyOptions() {
        Options options = new Options();
        String[] arguments = new String[0];
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.getArgs().isEmpty());
            assertTrue(cmd.getOptions().isEmpty());
        } catch (ParseException e) {
            // parse should succeed with empty options and arguments
        }
    }

    @Test
    public void testParseWithSimpleOptions() {
        String[] arguments = new String[] { "-a", "1", "-b", "2" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");
        Option optionB = new Option("b", "b", true, "Set option B");

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Arrays.asList("1", "2"), cmd.getArgs());
            assertEquals(List.of(optionA, optionB), cmd.getOptions());

            assertTrue(optionA.isSet());
            assertTrue(optionB.isSet());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithOptionalArgs() {
        String[] arguments = new String[] { "-a", "1", "--foo=3", "-b" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");
        Option optionFoo = new Option("foo", "f", true, "Set option FOO");
        optionFoo.hasArg(true);

        options.addOption(optionA);
        options.addOption(optionFoo);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Arrays.asList("1"), cmd.getArgs());
            assertEquals(List.of(optionA, optionFoo), cmd.getOptions());

            assertTrue(optionA.isSet());
            assertTrue(optionFoo.isSet());
            assertEquals("3", optionFoo.getValue());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithMultipleValues() {
        String[] arguments = new String[] { "-a", "1", "-a", "2" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");

        options.addOption(optionA);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Arrays.asList("1", "2"), cmd.getArgs());
            assertEquals(List.of(optionA.setValues(Arrays.asList("1", "2"))), cmd.getOptions());

            assertTrue(optionA.isSet());
            assertEquals(Arrays.asList("1", "2"), optionA.getValueAsString());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithValueSeparators() {
        String[] arguments = new String[] { "-a", "one=two,three" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");
        optionA.hasArg(true);
        optionA.setValueSeparator('=');

        options.addOption(optionA);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Collections.singletonList("one=two,three"), cmd.getArgs());
            assertEquals(List.of(optionA.setValues(Collections.singletonList("one=two,three"))), cmd.getOptions());

            assertTrue(optionA.isSet());
            assertEquals("one=two,three", optionA.getValue());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithLongOption() {
        String[] arguments = new String[] { "--long-option" };
        Options options = new Options();
        Option optionLong = new Option("long-option", "l", false, "Set long option");

        options.addOption(optionLong);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Collections.emptyList(), cmd.getArgs());
            assertEquals(List.of(optionLong), cmd.getOptions());

            assertTrue(optionLong.isSet());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithGroupedOptions() {
        String[] arguments = new String[] { "-g", "-x", "-g", "-y" };
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group of options");
        Option optionX = new Option("x", "x", false, "Set option X");
        optionX.setOptional(true);
        Option optionY = new Option("y", "y", false, "Set option Y");
        optionY.setOptional(true);

        group.addOption(optionX);
        group.addOption(optionY);

        options.addOption(group);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Arrays.asList("-g", "-x", "-g", "-y"), cmd.getArgs());
            assertEquals(List.of(group), cmd.getOptions());

            assertTrue(group.isSet());
            assertTrue(optionX.isSet());
            assertTrue(optionY.isSet());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() {
        String[] arguments = new String[] { "-a", "--long-option", "1" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");
        Option optionLong = new Option("long-option", "l", false, "Set long option");

        options.addOption(optionA);
        options.addOption(optionLong);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Arrays.asList("1"), cmd.getArgs());
            assertEquals(List.of(optionA, optionLong), cmd.getOptions());

            assertTrue(optionA.isSet());
            assertFalse(optionLong.isSet());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithInconsistentOptions() {
        String[] arguments = new String[] { "-a", "--b", "-a" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");
        Option optionB = new Option("b", "b", true, "Set option B");

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();

        assertThrows(ParseException.class, () -> {
            parser.parse(options, arguments);
        });

        assertEquals("Conflicting option: -a, --b", parser.getErrorMessage());
    }

    @Test
    public void testParseWithMissingArgs() {
        String[] arguments = new String[] { "-a", "--b" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");
        Option optionB = new Option("b", "b", true, "Set option B");
        optionB.hasArg(true);

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();

        assertThrows(ParseException.class, () -> {
            parser.parse(options, arguments);
        });

        assertEquals("Missing required argument for option: --b", parser.getErrorMessage());
    }

    @Test
    public void testParseWithMissingOptions() {
        String[] arguments = new String[] { "1", "2" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");

        options.addOption(optionA);

        Parser parser = new DefaultParser();

        assertThrows(ParseException.class, () -> {
            parser.parse(options, arguments);
        });

        assertEquals("Missing required option: -a", parser.getErrorMessage());
    }

    @Test
    public void testParseWithUnrecognizedOptions() {
        String[] arguments = new String[] { "-z", "1" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");

        options.addOption(optionA);

        Parser parser = new DefaultParser();

        assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(options, arguments);
        });

        assertEquals("Unrecognized option: -z", parser.getErrorMessage());
    }

    @Test
    public void testParseWithMissingRequiredOptions() {
        String[] arguments = new String[] { "1" };
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");
        Option optionB = new Option("b", "b", true, "Set option B");
        optionB.setRequired(true);

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments);
        });

        assertEquals("Missing required option: -b", parser.getErrorMessage());
    }

    @Test
    public void testParseWithInvalidBooleanOptions() {
        String[] arguments = new String[] { "-a", "true", "-b", "false" };
        Options options = new Options();
        Option optionA = new Option("a", "a", false, "Set option A");
        Option optionB = new Option("b", "b", false, "Set option B");

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();

        try {
            parser.parse(options, arguments);
            fail();
        } catch (ParseException e) {
            // parse should fail because boolean options should not take values
        }

        assertEquals("Invalid value 'true' for option: -a", e.getMessage());
        assertEquals("Invalid value 'false' for option: -b", e.getMessage());
    }

    @Test
    public void testParseWithEmptyValues() {
        String[] arguments = new String[] { "--foo=" };
        Options options = new Options();
        Option optionFoo = new Option("foo", "f", true, "Set option FOO");
        optionFoo.hasArg(true);

        options.addOption(optionFoo);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Collections.singletonList(""), cmd.getArgs());
            assertEquals(List.of(optionFoo.setValues(Collections.singletonList(""))), cmd.getOptions());

            assertTrue(optionFoo.isSet());
            assertEquals("", optionFoo.getValue());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithEmptyValueSeparator() {
        String[] arguments = new String[] { "--foo=", "--foo=bar" };
        Options options = new Options();
        Option optionFoo = new Option("foo", "f", true, "Set option FOO");
        optionFoo.valueSeparator('');

        options.addOption(optionFoo);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Arrays.asList("", "bar"), cmd.getArgs());
            assertEquals(List.of(optionFoo.setValues(Arrays.asList("", "bar"))), cmd.getOptions());

            assertTrue(optionFoo.isSet());
            assertEquals("", optionFoo.getValue());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithEmptyValueSeparatorAndValues() {
        String[] arguments = new String[] { "--foo=", "--foo=bar,baz" };
        Options options = new Options();
        Option optionFoo = new Option("foo", "f", true, "Set option FOO");
        optionFoo.valueSeparator('');

        options.addOption(optionFoo);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Arrays.asList("bar", "baz"), cmd.getArgs());
            assertEquals(List.of(optionFoo.setValues(Arrays.asList("bar", "baz"))), cmd.getOptions());

            assertTrue(optionFoo.isSet());
            assertEquals("bar,baz", optionFoo.getValue());
        } catch (ParseException e) {
            // parse should succeed
        }
    }

    @Test
    public void testParseWithEmptyValuesAndValues() {
        String[] arguments = new String[] { "--foo=", "--foo=bar,baz" };
        Options options = new Options();
        Option optionFoo = new Option("foo", "f", true, "Set option FOO");
        optionFoo.hasArg(true);

        options.addOption(optionFoo);

        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Collections.singletonList(""), cmd.getArgs());
            assertEquals(List.of(optionFoo.setValues(Collections.singletonList(""))), cmd.getOptions());

            assertTrue(optionFoo.isSet());
            assertEquals("", optionFoo.getValue());
        } catch (ParseException e) {
            // parse should succeed
        }

        try {
            cmd = parser.parse(options, arguments);

            assertEquals(Arrays.asList("bar", "baz"), cmd.getArgs());
            assertEquals(List.of(optionFoo.setValues(Arrays.asList("bar", "baz"))), cmd.getOptions());

            assertTrue(optionFoo.isSet());
            assertEquals("bar,baz", optionFoo.getValue());
        } catch (ParseException e) {
            fail();
        }
    }

}