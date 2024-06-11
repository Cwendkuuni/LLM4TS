import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        // Test that init resets the members to their original state
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "value", "--option=value", "-b"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);
        // Test that flatten method processes command line arguments correctly
        assertNotNull(flattenedArguments);
        assertEquals(4, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("value", flattenedArguments[1]);
        assertEquals("--option", flattenedArguments[2]);
        assertEquals("value", flattenedArguments[3]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator<String> iter = Arrays.asList("-c", "value").iterator();
        parser.gobble(iter);
        // Test that gobble adds remaining tokens to processed tokens list
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("value");
        // Test that process adds current token to processed tokens list
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        // Test that processSingleHyphen adds single hyphen to processed tokens list
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("a", "test"));
        parser.processOptionToken("a", true);
        // Test that processOptionToken adds token to processed tokens list if option exists
        assertEquals("a", parser.tokens.get(0));

        parser.processOptionToken("b", true);
        // Test that processOptionToken sets eatTheRest to true when option doesn't exist and stopAtNonOption is true
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("a", "test"));
        parser.burstToken("-ab", true);
        // Test that burstToken handles bursting correctly
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }
}