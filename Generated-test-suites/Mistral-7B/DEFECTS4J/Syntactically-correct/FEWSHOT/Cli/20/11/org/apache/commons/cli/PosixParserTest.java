package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-bar"};
        String[] expected = {"--foo", "-bar"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-value", "--"};
        String[] expected = {"--long-option-value"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option", "--long-option-value"};
        String[] expected = {"--long-option", "--long-option-value"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithValue() {
        Options options = new Options();
        options.addOption("f", "foo", true, "Foo value");
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "bar"};
        String[] expected = {"-f", "bar"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "arg1", "--", "arg2"};
        String[] expected = {"-f", "arg1", "--", "arg2"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithArgs() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        options.addOption("i", "integer", true, "Integer value");
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "arg1", "-i", "123"};
        String[] expected = {"-f", "arg1", "-i", "123"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithArgAndValue() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        options.addOption("i", "integer", true, "Integer value");
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "arg1", "-i", "123", "--integer-value", "value"};
        String[] expected = {"-f", "arg1", "-i", "123", "--integer-value", "value"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenComplex() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        options.addOption("i", "integer", true, "Integer value");
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "arg1", "--long-option", "--long-option-value", "arg2", "-i", "123", "-", "arg3"};
        String[] expected = {"-f", "arg1", "--long-option", "--long-option-value", "arg2", "-i", "123"};

        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "arg1", "-bar", "arg2"};
        String[] expected = {"-f", "arg1", "-bar"};

        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "--long-option-arg";

        List<String> tokens = parser.burstToken(token, false);
        String[] expected = {"--long-option", "arg"};

        assertArrayEquals(expected.clone(), tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "-abcd";

        List<String> tokens = parser.burstToken(token, true);
        String[] expected = {"--", "-abcd"};

        assertArrayEquals(expected.clone(), tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        PosixParser parser = new PosixParser();
        String token = "-f";

        parser.processOptionToken(token, false);
        Option expected = options.getOption("f");

        assertSame(expected, parser.currentOption);
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "-bar";

        parser.processOptionToken(token, true);

        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "arg1";

        parser.process(token);
        String[] expected = {"--", "arg1"};

        assertArrayEquals(expected.clone(), parser.tokens.toArray(new String[parser.tokens.size()]));
    }
}