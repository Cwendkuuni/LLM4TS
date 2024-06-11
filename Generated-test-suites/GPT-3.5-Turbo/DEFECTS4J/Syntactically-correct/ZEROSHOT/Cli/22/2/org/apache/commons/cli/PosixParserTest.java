import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "-b", "value", "--option", "--", "input.txt"};
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption(Option.builder().longOpt("option").desc("Option with long name").build());
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(6, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("value", flattenedArguments[2]);
        assertEquals("--option", flattenedArguments[3]);
        assertEquals("--", flattenedArguments[4]);
        assertEquals("input.txt", flattenedArguments[5]);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.options);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();

        String value = "input.txt";
        parser.processNonOptionToken(value);

        assertTrue(parser.eatTheRest);
        assertEquals("--", parser.tokens.get(0));
        assertEquals(value, parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();

        String token = "-a";
        boolean stopAtNonOption = false;
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.options = options;
        parser.processOptionToken(token, stopAtNonOption);

        assertFalse(parser.eatTheRest);
        assertEquals(token, parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.init();

        String token = "-abc";
        boolean stopAtNonOption = false;
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        parser.options = options;
        parser.burstToken(token, stopAtNonOption);

        assertFalse(parser.eatTheRest);
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
    }

}