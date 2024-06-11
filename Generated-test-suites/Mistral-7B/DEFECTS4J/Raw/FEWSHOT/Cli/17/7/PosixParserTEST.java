package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"foo", "-a", "-b", "--optionC", "-d", "--optionD=value", "--", "e", "f"};
        Options options = new Options();
        options.addOption("a", true, "a help message for option a");
        options.addOption("b", false, "a help message for option b");
        options.addOption("c", false, "a help message for option c");
        options.addOption("d", true, "a help message for option d");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(11, result.length);
        assertArrayEquals(new String[]{"foo", "-a", "--", "-b", "--optionC", "-d", "--optionD=value", "--", "e", "f"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"foo", "-a", "-b", "--optionC", "-d", "--optionD=value", "--", "e", "f"};
        Options options = new Options();
        options.addOption("a", true, "a help message for option a");
        options.addOption("b", false, "a help message for option b");
        options.addOption("c", false, "a help message for option c");
        options.addOption("d", true, "a help message for option d");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(13, result.length);
        assertArrayEquals(new String[]{"foo", "-a", "--optionC", "-d=value", "--", "e", "f"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = {"foo", "--longOption=value1", "--longOption2", "bar"};
        Options options = new Options();
        options.addOption("longOption", true, "a help message for longOption");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertArrayEquals(new String[]{"foo", "--longOption=value1", "--longOption2", "bar"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndValue() {
        String[] args = {"foo", "--longOption=value1", "--longOption2", "bar"};
        Options options = new Options();
        options.addOption("longOption", true, "a help message for longOption");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(6, result.length);
        assertArrayEquals(new String[]{"foo", "--longOption=value1"}, result);
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] args = {"foo", "bar"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"foo", "--", "bar"}, result);
    }

    @Test
    public void testFlattenWithNoOptionsAndStopAtNonOption() {
        String[] args = {"foo", "bar"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testBurstToken() {
        String token = "--longOptionWithValue";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "a help message for longOption");

        parser.burstToken(token, false);

        assertEquals(2, parser.tokens.size());
        assertEquals("-longOption", parser.tokens.get(0));
        assertEquals("--longOptionWithValue", parser.tokens.get(1));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "--longOptionWithValue";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "a help message for longOption");

        parser.burstToken(token, true);

        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("longOptionWithValue", parser.tokens.get(1));
    }

    @Test
    public void testBurstTokenEmpty() {
        String token = "";
        PosixParser parser = new PosixParser();

        parser.burstToken(token, false);

        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testBurstTokenWithSingleCharacter() {
        String token = "-a";
        PosixParser parser = new PosixParser();

        parser.burstToken(token, false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }
}