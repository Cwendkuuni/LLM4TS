import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b with argument");

        PosixParser parser = new PosixParser();

        String[] args = {"-a", "-b", "argValue"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(3, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("-b", flattenedArgs[1]);
        assertEquals("argValue", flattenedArgs[2]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2", "arg3"};

        PosixParser parser = new PosixParser();
        Iterator<String> iter = Arrays.asList(args).iterator();

        parser.gobble(iter);

        assertEquals(3, parser.tokens.size());
        assertEquals("arg1", parser.tokens.get(0));
        assertEquals("arg2", parser.tokens.get(1));
        assertEquals("arg3", parser.tokens.get(2));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        Option option = new Option("t", "test", false, "Test option");

        parser.currentOption = option;
        parser.process("value");

        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("t", "test", false, "Test option");

        PosixParser parser = new PosixParser();
        boolean stopAtNonOption = false;

        parser.processOptionToken("t", stopAtNonOption);

        assertEquals(1, parser.tokens.size());
        assertEquals("t", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");

        PosixParser parser = new PosixParser();
        boolean stopAtNonOption = false;

        parser.burstToken("-option", stopAtNonOption);

        assertEquals(2, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("-option", parser.tokens.get(1));
    }
}