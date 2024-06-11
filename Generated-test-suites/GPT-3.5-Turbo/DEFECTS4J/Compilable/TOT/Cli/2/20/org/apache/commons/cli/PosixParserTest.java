package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption_StopAtNonOptionTrue() {
        String[] arguments = {"-a", "arg1", "--", "-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "arg1", "--", "-b"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_StopAtNonOptionFalse() {
        String[] arguments = {"-a", "arg1", "--", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "arg1", "--", "-b"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_SingleCharacterOption_NoArgument() {
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_SingleCharacterOption_WithArgument() {
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "arg1"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "arg1"}, result);
    }

    @Test
    public void testFlatten_LongOption_NoArgument() {
        options.addOption("option", false, "Long Option");

        String[] arguments = {"--option"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--option"}, result);
    }

    @Test
    public void testFlatten_LongOption_WithArgument() {
        options.addOption("option", true, "Long Option");

        String[] arguments = {"--option", "arg1"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--option", "arg1"}, result);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOptionTrue() {
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-ab"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOptionFalse() {
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-ab"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlatten_BurstToken_NoOptionExists_StopAtNonOptionTrue() {
        options.addOption("a", false, "Option A");

        String[] arguments = {"-abc"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "-b", "-c"}, result);
    }

    @Test
    public void testFlatten_BurstToken_NoOptionExists_StopAtNonOptionFalse() {
        options.addOption("a", false, "Option A");

        String[] arguments = {"-abc"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-b", "-c"}, result);
    }

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_NullArguments() {
        String[] arguments = null;

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{}, result);
    }

}