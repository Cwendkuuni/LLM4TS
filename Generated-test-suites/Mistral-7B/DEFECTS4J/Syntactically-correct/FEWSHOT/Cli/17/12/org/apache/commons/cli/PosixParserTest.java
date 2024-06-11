package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = {};

        String[] expected = {};
        String[] result = parser.flatten(new Options(), args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        options.addOption("a", "another-option", true, "Another option description");

        String[] args = {"-o", "--option", "-a", "arg1", "--another-option=arg2"};
        String[] expected = {"-o", "--option", "-a", "arg1", "--another-option=arg2"};
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBursting() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("ab", "ab-option", true, "AB option description");
        options.addOption("cd", "cd-option", false, "CD option description");

        String[] args = {"-abcd", "--cd", "-a123", "--ab-option=def"};
        String[] expected = {"-a", "--ab-option=def"};
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        options.addOption("a", "another-option", true, "Another option description");

        String[] args = {"--option", "arg1", "--another-option=arg2", "non-option"};
        String[] expected = {"--option", "arg1", "--another-option=arg2"};
        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenOneCharacterOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");

        String token = "-o";
        List tokens = parser.burstToken(token, false);

        List expected = Arrays.asList("-o");
        assertEquals(expected, tokens);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenTwoCharacterOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("oo", "two-character-option", true, "Two character option description");

        String token = "--oo";
        List tokens = parser.burstToken(token, false);

        List expected = Arrays.asList("-o", "--oo");
        assertEquals(expected, tokens);
        assertSame(options.getOption("oo"), parser.currentOption);
    }

    @Test
    public void testBurstTokenTwoCharacterOptionWithoutArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("oo", "two-character-option", false, "Two character option description");

        String token = "--oo";
        List tokens = parser.burstToken(token, false);

        List expected = Arrays.asList("-oo");
        assertEquals(expected, tokens);
        assertSame(options.getOption("oo"), parser.currentOption);
    }

    @Test
    public void testBurstTokenNonOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String token = "--non-option";
        List tokens = parser.burstToken(token, true);

        assertArrayEquals(new String[]{"--non-option"}, tokens.toArray(new String[0]));
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenNonOptionWithNoStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String token = "--non-option";
        List tokens = parser.burstToken(token, false);

        List expected = Arrays.asList("--non-option");
        assertEquals(expected, tokens);
        assertNull(parser.currentOption);
    }
}