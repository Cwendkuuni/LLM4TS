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
    public void testEmptyArgs() {
        Options options = new Options();
        String[] arguments = {};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSimpleOptions() {
        Options options = new Options();
        String[] arguments = {"-a", "--optionB", "-c=valueC", "-d=valueD"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "--optionB", "-c", "valueC", "-d", "valueD"}, result);
    }

    @Test
    public void testLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "l");
        options.addOption("longOptionWithValue", true, "lv");

        String[] arguments = {"--longOption", "value", "--longOptionWithValue", "value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--longOption", "value", "--longOptionWithValue", "value2"}, result);
    }

    @Test
    public void testLongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("longOption", false, "l");

        String[] arguments = {"--longOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testMultipleLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", false, "l1");
        options.addOption("longOption2", true, "l2");

        String[] arguments = {"--longOption1", "--longOption2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--longOption1", "--longOption2"}, result);
    }

    @Test
    public void testShortAndLongOption() {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option with short and long form");

        String[] arguments = {"-o", "--opt"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-o", "--opt"}, result);
    }

    @Test
    public void testShortOptionWithValue() {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option with short form and value");

        String[] arguments = {"-o=value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-o", "value"}, result);
    }

    @Test
    public void testShortAndLongOptionWithValue() {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option with short and long form");
        options.addOption("lo", "longOpt", true, "Option with long form and value");

        String[] arguments = {"-o=value", "--longOpt=value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-o", "value", "--longOpt", "value2"}, result);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option with short and long form");

        String[] arguments = {"arg1", "-o", "arg3", "--arg4"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"arg1", "-o", "arg3", "--arg4"}, result);
    }

    @Test
    public void testSpecialTokens() {
        Options options = new Options();

        String[] arguments = {"--foo=bar", "--bar=baz", "--", "-abc"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--foo", "bar", "--bar", "baz", "--", "-abc"}, result);
    }

    @Test
    public void testBursting() {
        Options options = new Options();
        options.addOption("opt1", true, "o1");
        options.addOption("opt2", true, "o2");

        String[] arguments = {"-o1a--o2b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-o1", "--", "-o2"}, result);
    }
}