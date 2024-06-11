import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
        options.addOption("d", 3, "Option D with three arguments");
        options.addOption("e", 1, "Option E with a short arg name");
        options.addOption("f", "foo", "Option F with a long arg name");

        String[] args = {"--a", "--b", "-c", "arg1", "arg2", "arg3", "-d", "arg4", "arg5", "arg6", "-e", "arg7", "--f", "bar", "--"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        List<String> expected = Arrays.asList(
                "--a",
                "--b",
                "-c",
                "arg1",
                "arg2",
                "arg3",
                "-d",
                "arg4",
                "arg5",
                "arg6",
                "-e",
                "arg7",
                "--f",
                "bar",
                "--"
        );
        assertEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");

        String[] args = {"--a", "--b", "-c", "arg1", "arg2", "-", "-d", "arg4", "arg5", "arg6"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        List<String> expected = Arrays.asList(
                "--a",
                "--b",
                "-c",
                "arg1",
                "arg2",
                "--d",
                "arg4",
                "arg5",
                "arg6"
        );
        assertEquals(expected.toArray(new String[expected.size()]), parsedArgs);

        // Eat the remaining arguments
        Object[] parsedOptions = parser.getParsedOptions();
        assertEquals(Options.class, parsedOptions.getClass());
        assertEquals(options, (Options) parsedOptions);
    }

    @Test
    public void testFlattenWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] args = {"--a", "--x", "-c", "arg1", "arg2", "-"};
        PosixParser parser = new PosixParser();
        try {
            parser.parse(options, args, true);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Check the parsed options
        Object[] parsedOptions = parser.getParsedOptions();
        assertNull(parsedOptions);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.process("--a");
        assertEquals(1, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));

        parser.init();
        parser.process("--a=value");
        assertEquals(2, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));

        parser.init();
        parser.process("-a");
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.init();
        parser.process("--");
        assertEquals(1, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));

        parser.init();
        parser.process("a");
        assertEquals(1, parser.tokens.size());
        assertEquals("a", parser.tokens.get(0));

        parser.init();
        parser.process("--a --b");
        assertEquals(2, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--b", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.burstToken("--ab", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("--ab", parser.tokens.get(0));

        parser.init();
        parser.burstToken("-ab", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-ab", parser.tokens.get(0));

        parser.init();
        parser.burstToken("-a", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.init();
        parser.burstToken("-a--b", false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("--b", parser.tokens.get(1));

        parser.init();
        parser.burstToken("-a--b=value", false);
        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("--b", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));

        parser.init();
        parser.burstToken("-a--b=value", true);
        assertTrue(parser.eatTheRest);
        assertEquals(2, parser.tokens.size());
        assertEquals("--b", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.burstToken("--ab", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("--ab", parser.tokens.get(0));

        parser.init();
        parser.burstToken("-ab", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("-ab", parser.tokens.get(0));

        parser.init();
        parser.burstToken("-a", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.init();
        parser.burstToken("-a--b", true);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("--b", parser.tokens.get(1));

        parser.init();
        parser.burstToken("-a--b=value", true);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("--b=value", parser.tokens.get(1));
    }
}