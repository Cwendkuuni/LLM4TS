package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testSimpleFlatten() {
        Options options = new Options();
        String[] args = {"--foo", "-bar", "-baz", "arg1", "arg2", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar", "-baz", "arg1", "arg2"}, result);
    }

    @Test
    public void testSimpleFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "-bar", "-baz", "arg1", "arg2", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "-bar", "-baz", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithOption() {
        Options options = new Options();
        options.addOption("b", true, "boolean option");
        String[] args = {"-b", "--foo", "-bar", "arg1", "arg2", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-b", "--foo", "-bar", "arg1", "arg2"}, result);
        assertEquals("boolean option", parser.getCurrentOption().getValue());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("b", true, "boolean option");
        options.addOption("i", Integer.class, "integer option");
        String[] args = {"-bi", "123", "-b", "--foo", "-bar", "arg1", "arg2", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-bi", "123", "--foo", "-b", "--bar", "arg1", "arg2"}, result);
        assertEquals("boolean option", parser.getCurrentOption().getValue());
        assertEquals(123, parser.getCurrentOption().getValue(Integer.class));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("long-option", false, "long option");
        String[] args = {"--long-option", "-b", "--foo", "-bar", "arg1", "arg2", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long-option", "--foo", "-b", "--bar", "arg1", "arg2"}, result);
        assertEquals("long option", parser.getCurrentOption().getLongOpt());
        assertEquals("long option", parser.getCurrentOption().getOpt());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithLongAndShortOption() {
        Options options = new Options();
        options.addOption("l", false, "long option");
        options.addOption("long-option", false, "long option");
        String[] args = {"--long-option", "--l", "-b", "--foo", "-bar", "arg1", "arg2", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long-option", "--l", "--foo", "-b", "--bar", "arg1", "arg2"}, result);
        assertEquals("long option", parser.getCurrentOption().getLongOpt());
        assertEquals("l", parser.getCurrentOption().getOpt());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithArgumentOption() {
        Options options = new Options();
        options.addOption("a", true, "option with an argument");
        String[] args = {"-a", "arg1", "--foo", "-b", "--bar", "arg3", "arg4", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "arg1", "--foo", "-b", "--bar", "arg3", "arg4"}, result);
        assertEquals("arg1", parser.getCurrentOption().getValue());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithMultipleArgumentOptions() {
        Options options = new Options();
        options.addOption("a", true, "option with an argument");
        String[] args = {"-a", "arg1", "-a", "arg2", "--foo", "-b", "--bar", "arg3", "arg4", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "arg1", "-a", "arg2", "--foo", "-b", "--bar", "arg3", "arg4"}, result);
        assertEquals("arg1", parser.getCurrentOption().getValue());
        assertEquals("arg2", parser.getCurrentOption().getValue(String.class, parser.getCurrentOption().getValue()));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "-bar", "-baz", "arg1", "arg2", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "-bar", "-baz", "arg1", "arg2"}, result);
    }

    @Test
    public void testBurstTokenShortOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-b", false);
        assertArrayEquals(new String[]{"-b"}, result);
    }

    @Test
    public void testBurstTokenLongOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--long-option", false);
        assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testBurstTokenOptionWithOptionalArgument() {
        PosixParser parser = new PosixParser();
        Option option = new Option("o", true, "optional option");
        parser.setOptions(new Options().addOption(option));
        String[] result = parser.burstToken("o", false);
        assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testBurstTokenOptionWithRequiredArgument() {
        PosixParser parser = new PosixParser();
        Option option = new Option("r", true, "required option");
        option.setRequired(true);
        parser.setOptions(new Options().addOption(option));
        String[] args = {"--r"};
        parser.parse(args);
        String[] result = parser.burstToken("r", false);
        assertArrayEquals(new String[]{"--r"}, result);
        assertEquals("Required option is missing", parser.getParseErrors()[0].getMessage());
    }

    @Test
    public void testBurstTokenOptionWithValue() {
        PosixParser parser = new PosixParser();
        Option option = new Option("v", Integer.class, "value option");
        parser.setOptions(new Options().addOption(option));
        String[] args = {"--v", "123"};
        parser.parse(args);
        String[] result = parser.burstToken("v", false);
        assertEquals("123", parser.getCurrentOption().getValue());
        assertArrayEquals(new String[]{"--v"}, result);
    }

    @Test
    public void testBurstTokenOptionWithMultipleValues() {
        PosixParser parser = new PosixParser();
        Option option = new Option("v", String[].class, "value option");
        parser.setOptions(new Options().addOption(option));
        String[] args = {"--v", "value1", "--v", "value2"};
        parser.parse(args);
        String[] result = parser.burstToken("v", false);
        String[] values = (String[]) parser.getCurrentOption().getValue();
        assertArrayEquals(new String[]{"value1", "value2"}, values);
        assertArrayEquals(new String[]{"--v"}, result);
    }

    @Test
    public void testBurstTokenUnknownOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("x", false);
        assertArrayEquals(new String[]{"--x"}, result);
        assertEquals("Unrecognized option: x", parser.getParseErrors()[0].getMessage());
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-bar", "-baz", "arg1", "arg2", "--"};
        parser.setOptions(new Options());
        parser.parse(args);
        String[] result = parser.burstToken("x", true);
        assertArrayEquals(new String[]{"--foo", "-bar", "-baz", "arg1", "arg2"}, result);
    }
}