package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        options.addOption("o", false, null, "option");
        options.addOption("a", true, "arg", "an argument");

        String[] arguments = {"-o", "--arg=value", "-a", "arg1", "--", "arg3"};
        String[] expected = {"-o", "--option", "-a", "--an-argument=value", "arg1", "--", "arg3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", false, null, "option");
        options.addOption("a", true, "arg", "an argument");

        String[] arguments = {"-o", "--arg=value", "-a", "arg1", "--non-option", "arg3"};
        String[] expected = {"-o", "--option", "-a", "arg1", "--non-option", "arg3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("long-option", false, null, "long option");

        String[] arguments = {"--long-option"};
        String[] expected = {"--long-option", "--long-option"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionArg() {
        Options options = new Options();
        options.addOption("long-option", true, "arg", "long option argument");

        String[] arguments = {"--long-option=value"};
        String[] expected = {"--long-option", "--an-argument=value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionArgAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("long-option", true, "arg", "long option argument");

        String[] arguments = {"--long-option=value", "--non-option"};
        String[] expected = {"--long-option=value", "--non-option"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true, "arg", "an argument");
        options.addOption("b", true, "arg", "another argument");

        String token = "-ab";
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken(token, false);

        assertEquals(2, tokens.size());
        assertSame(options.getOption("a"), tokens.get(0));
        assertSame(options.getOption("b"), tokens.get(1));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "arg", "an argument");
        options.addOption("b", true, "arg", "another argument");

        String token = "-abc";
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken(token, true);

        assertEquals(2, tokens.size());
        assertSame(options.getOption("a"), tokens.get(0));
        assertSame(options.getOption("c"), tokens.get(1));
    }

    @Test
    public void testBurstTokenWithNoOptions() {
        String token = "-abc";
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken(token, false);

        assertEquals(1, tokens.size());
        assertEquals("-abc", tokens.get(0).toString());
    }

    @Test
    public void testBurstTokenWithEmptyToken() {
        String token = "--";
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken(token, false);

        assertEquals(1, tokens.size());
        assertEquals("--", tokens.get(0).toString());
    }
}