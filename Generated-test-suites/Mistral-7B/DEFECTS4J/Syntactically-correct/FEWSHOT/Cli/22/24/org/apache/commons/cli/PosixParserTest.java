package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", false, "This is option f");
        options.addOption("o", true, "This is option o");
        options.addOption("p", "P", "This is option p with argument P");
        options.addOption("q", "Q", "This is option q with argument Q");

        String[] args = {"", "--foo", "--bar=baz", "-f", "-o", "-pP", "-qQ", "arg1", "arg2"};
        String[] expected = {"--foo", "--bar", "=", "baz", "-f", "-o", "--p", "P", "--q", "Q", "arg1", "arg2"};

        String[] result = new PosixParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", false, "This is option f");
        options.addOption("o", true, "This is option o");
        options.addOption("p", "P", "This is option p with argument P");
        options.addOption("q", "Q", "This is option q with argument Q");

        String[] args = {"", "--foo", "--bar=baz", "-f", "-o", "-pP", "-qQ", "arg1", "arg2", "nonOption"};
        String[] expected = {"--foo", "--bar", "=", "baz", "-f", "-o", "--p", "P", "--q", "Q", "arg1", "arg2", "nonOption"};
        String[] result = new PosixParser().flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        String[] input = {"--", "nonOption"};

        parser.processNonOptionToken("nonOption");

        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("nonOption");
        List<String> result = (List<String>) parser.tokens;

        assertEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        Options options = new Options();
        options.addOption("f", false, "This is option f");

        String[] input = {"-f"};

        parser.processOptionToken("-f", false);

        List<String> expected = new ArrayList<>();
        expected.add("-f");
        List<String> result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();
        options.addOption("o", true, "This is option o");

        String[] input2 = {"--foo", "-o"};

        parser.processOptionToken("-o", false);

        expected.clear();
        expected.add("--foo");
        expected.add("--o");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();
        options.addOption("p", "P", "This is option p with argument P");

        String[] input3 = {"--foo", "-pP"};

        parser.processOptionToken("-pP", false);

        expected.clear();
        expected.add("--foo");
        expected.add("--p");
        expected.add("P");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();
        options.addOption("q", "Q", "This is option q with argument Q");

        String[] input4 = {"--foo", "-pP", "--q", "Q"};

        parser.processOptionToken("-q", false);

        expected.clear();
        expected.add("--foo");
        expected.add("--p");
        expected.add("P");
        expected.add("--q");
        expected.add("Q");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();
        options.addOption("r", null, "This is option r without argument");

        String[] input5 = {"--foo", "-pP", "--q", "Q", "-r"};

        parser.processOptionToken("-r", false);

        expected.clear();
        expected.add("--foo");
        expected.add("--p");
        expected.add("P");
        expected.add("--q");
        expected.add("Q");
        expected.add("--r");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();
        options.clear();

        String[] input6 = {"--foo", "-pP", "--q", "Q", "-r"};

        parser.processOptionToken("-r", true);

        expected.clear();
        expected.add("--foo");
        expected.add("--p");
        expected.add("P");
        expected.add("--q");
        expected.add("Q");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        Options options = new Options();
        options.addOption("a", true, "This is option a with argument A");
        options.addOption("b", "B", "This is option b with argument B");

        String[] input = {"--", "-aA", "--bb", "B", "C"};

        parser.burstToken("-aA", false);

        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("-a");
        expected.add("A");
        List<String> result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();

        String[] input2 = {"--", "-aA", "--bb", "B", "C"};

        parser.burstToken("-aA", true);

        expected.clear();
        expected.add("--");
        expected.add("--a");
        expected.add("A");
        expected.add("B");
        expected.add("C");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();
        options.clear();

        String[] input3 = {"--", "-ab", "B", "C"};

        parser.burstToken("-ab", false);

        expected.clear();
        expected.add("--");
        expected.add("-a");
        expected.add("b");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();

        String[] input4 = {"--", "-ab", "B", "C"};

        parser.burstToken("-ab", true);

        expected.clear();
        expected.add("--
");
        expected.add("--a");
        expected.add("b");
        expected.add("B");
        expected.add("C");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();
        options.clear();

        String[] input5 = {"--", "-a--", "B"};

        parser.burstToken("-a--", false);

        expected.clear();
        expected.add("--");
        expected.add("--a");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();

        String[] input6 = {"--", "-a--", "B"};

        parser.burstToken("-a--", true);

        expected.clear();
        expected.add("--");
        expected.add("--a");
        expected.add("--");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();
        options.clear();

        String[] input7 = {"--", "-a--"};

        parser.burstToken("-a--", false);

        expected.clear();
        expected.add("--");
        expected.add("--a");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);

        parser.init();

        String[] input8 = {"--", "-a--"};

        parser.burstToken("-a--", true);

        expected.clear();
        expected.add("--");
        expected.add("--a");
        expected.add("--");
        result = (List<String>) parser.tokens;

        assertEquals(expected, result);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();

        parser.init();

        List<String> expected = new ArrayList<>();
        List<String> result = (List<String>) parser.tokens;

        assertSame(expected, result);
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("f", false, "This is option f");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;

        parser.processOptionToken("-f", false);

        Option result = parser.getOption("f");

        assertEquals(option, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionNull() {
        PosixParser parser = new PosixParser();
        parser.init();

        Option result = parser.getOption("f");

        assertNull(result);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("f", false, "This is option f");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = options;

        boolean result = parser.hasOption("f");

        assertTrue(result);
    }

    @Test
    public void testHasOptionFalse() {
        PosixParser parser = new PosixParser();
        parser.init();

        boolean result = parser.hasOption("f");

        assertFalse(result);
    }
}