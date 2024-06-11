package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlattenOptionWithNoArg() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        String[] arguments = {"-a", "-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlattenOptionWithArgValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());

        String[] arguments = {"-a", "value", "-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlattenOptionWithoutArgButWithArgValues() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());

        String[] arguments = {"-a", "value1", "value2", "-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("-b", result[3]);
    }

    @Test
    public void testFlattenOptionWithInvalidOptionIdAndStopAtNonOptionFlagSet() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-x", "-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("--", result[0]);
        assertEquals("-x", result[1]);
        assertEquals("-a", result[2]);
    }

    @Test
    public void testFlattenOptionWithInvalidOptionIdAndStopAtNonOptionFlagNotSet() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-x", "-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-x", result[0]);
        assertEquals("-a", result[1]);
    }

    @Test
    public void testFlattenOptionWithLongOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").longOpt("long").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"--long"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("--long", result[0]);
    }

    @Test
    public void testFlattenOptionWithLongOptionAndArgument() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").longOpt("long").hasArg().build());

        String[] arguments = {"--long", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("--long", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenOptionWithLongOptionAndArgumentWithEqualsSign() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").longOpt("long").hasArg().build());

        String[] arguments = {"--long=value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("--long", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenOptionWithLongOptionAndArgumentWithEqualsSignAndExtraValues() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").longOpt("long").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"--long=value", "extra", "--flag"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("--long", result[0]);
        assertEquals("value", result[1]);
        assertEquals("extra", result[2]);
        assertEquals("--flag", result[3]);
    }

    @Test
    public void testFlattenOptionWithHyphen() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-", result[0]);
    }

    @Test
    public void testFlattenOptionIgnoringStopAtNonOptionFlag() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-x", "value", "-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-x", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-a", result[2]);
    }
}