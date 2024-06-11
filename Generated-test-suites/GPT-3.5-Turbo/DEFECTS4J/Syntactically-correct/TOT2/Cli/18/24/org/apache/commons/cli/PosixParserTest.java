import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option optionA = new Option("a", "descA");
        options.addOption(optionA);
        Option optionB = new Option("b", "descB");
        options.addOption(optionB);

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "argA", "--", "-b", "argB"};
        String[] flattenedArgs = parser.flatten(options, arguments, false);

        assertEquals(5, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("argA", flattenedArgs[1]);
        assertEquals("--", flattenedArgs[2]);
        assertEquals("-b", flattenedArgs[3]);
        assertEquals("argB", flattenedArgs[4]);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("argument");

        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("argument", parser.tokens.get(1));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");

        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option optionC = new Option("c", "descC");
        options.addOption(optionC);

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-c", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-c", parser.tokens.get(0));
        assertEquals(optionC, parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option optionD = new Option("d", "descD");
        options.addOption(optionD);

        PosixParser parser = new PosixParser();
        parser.burstToken("-dargument", false);

        assertEquals(2, parser.tokens.size());
        assertEquals("-d", parser.tokens.get(0));
        assertEquals("argument", parser.tokens.get(1));
    }
}