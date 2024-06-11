package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();

        String[] args = {"--foo", "--bar=baz", "-o", "-h", "-p", "arg1", "-a", "--", "arg2", "arg3"};

        List<String> expected = Arrays.asList(
                "--foo", "--bar", "baz", "-o", "-h", "-p", "arg1", "--", "arg2", "arg3"
        );

        String[] tokens = parser.flatten(new Options(), args, false);
        assertEquals(expected.size(), tokens.length);
        assertArrayEquals(expected.toArray(), tokens);

        // Test with stopAtNonOption = true
        String[] tokens2 = parser.flatten(new Options(), args, true);
        expected = Arrays.asList(
                "--foo", "--bar", "baz", "-o", "--", "arg1", "arg2", "arg3"
        );
        assertArrayEquals(expected.toArray(), tokens2);
    }

    @Test
    public void testFlattenEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        List<String> expected = Arrays.asList();

        String[] tokens = parser.flatten(new Options(), args, false);
        assertEquals(expected.size(), tokens.length);
        assertArrayEquals(expected.toArray(), tokens);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};

        parser.flatten(new Options(), args, false);
        fail("Expected ParseException");
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=bar", "arg1", "--bar=baz", "arg2", "--"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(5, parser.tokens.size());

        String currentOption = parser.currentOption;
        String[] expected = {"--foo", "--bar"};

        List<String> processed = new ArrayList<>();
        parser.parse(new String[]{"--", "foo", "bar"}, null);
        while (!parser.tokens.isEmpty()) {
            processed.add((String) parser.tokens.remove(0));
        }

        assertEquals(expected.length, processed.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], processed.get(i));
        }

        parser.init();
        parser.flatten(new Options(), args, false);
        assertEquals(5, parser.tokens.size());

        currentOption = parser.currentOption;
        parsed(parser, "--foo=bar arg1 --bar=baz arg2 --", "--foo", "--bar");
        assertEquals(expected.length, parser.tokens.size());
    }

    @Test
    public void testProcessSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));

        parser.process("--");
        assertEquals(2, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
    }

    @Test
    public void testProcessLongOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=bar"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));

        parser.process("--foo=baz");
        assertEquals(2, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("baz", parser.tokens.get(1));
    }

    @Test
    public void testProcessLongOptionWithoutArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));

        parser.process("--foo");
        assertEquals(1, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
    }

    @Test
    public void testProcessShortOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-f"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));

        parser.process("-h");
        assertEquals(1, parser.tokens.size());
        assertEquals("-h", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "output.txt"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));

        parser.process("--");
        parser.process("input.txt");
        assertEquals(4, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
        assertEquals("input.txt", parser.tokens.get(2));
        assertEquals("output.txt", parser.tokens.get(3));
    }

    @Test
    public void testProcessOptionWithMultipleArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "arg2"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.process("--");
        parser.process("arg3");
        parser.process("arg4");
        assertEquals(6, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
        assertEquals("arg1", parser.tokens.get(2));
        assertEquals("arg2", parser.tokens.get(3));
        assertEquals("arg3", parser.tokens.get(4));
        assertEquals("arg4", parser.tokens.get(5));
    }

    @Test
    public void testProcessInvalidShortOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-x"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-x", parser.tokens.get(0));

        parser.process("--");
        parser.process("input.txt");
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-x", parser.tokens.get(1));
    }

    @Test
    public void testProcessInvalidLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};
        parser.init();

        parser.flatten(new Options(), args, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("--invalidOption", parser.tokens.get(0));

        parser.process("--");
        parser.process("input.txt");
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("--invalidOption", parser.tokens.get(1));
    }

    @Test
    public void testProcessStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "arg1", "-o", "output.txt", "arg2", "--"};
        parser.init();

        parser.flatten(new Options(), args, true);
        assertEquals(6, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("arg1", parser.tokens.get(1));
        assertEquals("-o", parser.tokens.get(2));
        assertEquals("output.txt", parser.tokens.get(3));
        assertEquals("--", parser.tokens.get(4));
        assertEquals("arg2", parser.tokens.get(5));

        parser.process("input.txt");
        assertEquals(7, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("arg1", parser.tokens.get(1));
        assertEquals("-o", parser.tokens.get(2));
        assertEquals("output.txt", parser.tokens.get(3));
        assertEquals("--", parser.tokens.get(4));
        assertEquals("input.txt", parser.tokens.get(5));
        assertEquals("arg2", parser.tokens.get(6));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo-bar-baz"};
        parser.init();

        parser.burstToken("--foo-bar-baz", false);
        List<String> expected = Arrays.asList("-foo", "-bar", "-baz");
        List<String> actual = (List<String>) parser.tokens;

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testBurstTokenSingleCharacterOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-f"};
        parser.init();

        parser.burstToken("-f", false);
        List<String> expected = Arrays.asList("-f");
        List<String> actual = (List<String>) parser.tokens;

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-o", "--bar"};
        parser.init();

        parser.burstToken("--foo-o--bar", true);
        List<String> expected = Arrays.asList("-foo", "--", "-o", "--bar");
        List<String> actual = (List<String>) parser.tokens;

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    private void parsed(PosixParser parser, String input, String... expected) {
        Options options = new Options();
        parser.parse(new String[]{input}, options);

        List<String> actual = new ArrayList<>();
        Iterator<String> it = parser.tokens.iterator();
        while (it.hasNext()) {
            actual.add(it.next());
        }

        assertEquals(expected.length, actual.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual.get(i));
        }
    }
}