import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten_emptyOptions_validArguments() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--flag", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--flag", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_emptyOptions_invalidArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlatten_withOptions_validArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "arg1", "--c", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("--c", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlatten_withOptions_invalidArguments_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "arg1", "arg2", "--c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("--c", result[3]);
    }

    @Test
    public void testFlatten_withOptions_invalidArguments_continueParsing() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "arg1", "arg2", "--c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("--c", result[3]);
    }

    @Test
    public void testBurstToken_validOption_oneCharacter() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();

        String token = "-a";
        parser.burstToken(token, false);

        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken_validOption_multipleCharacters() {
        Options options = new Options();
        options.addOption("abc", false, "Option abc");

        PosixParser parser = new PosixParser();

        String token = "-abc";
        parser.burstToken(token, false);

        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
    }

    @Test
    public void testBurstToken_invalidOption_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();

        String token = "-b";
        parser.burstToken(token, true);

        assertEquals("-b", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken_invalidOption_continueParsing() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();

        String token = "-b";
        parser.burstToken(token, false);

        assertEquals("-b", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken_invalidOption_multipleCharacters_stopAtNonOption() {
        Options options = new Options();
        options.addOption("abc", false, "Option abc");

        PosixParser parser = new PosixParser();

        String token = "-def";
        parser.burstToken(token, true);

        assertEquals("-def", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken_invalidOption_multipleCharacters_continueParsing() {
        Options options = new Options();
        options.addOption("abc", false, "Option abc");

        PosixParser parser = new PosixParser();

        String token = "-def";
        parser.burstToken(token, false);

        assertEquals("-def", parser.tokens.get(0));
    }
}