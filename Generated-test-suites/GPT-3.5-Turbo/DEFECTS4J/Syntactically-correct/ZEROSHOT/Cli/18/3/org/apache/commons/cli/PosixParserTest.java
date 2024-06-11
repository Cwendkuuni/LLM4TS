import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();

        assertFalse(posixParser.eatTheRest);
        assertTrue(posixParser.tokens.isEmpty());
        assertNull(posixParser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "--arg1", "-b", "value", "--arg2=value", "arg3"};

        String[] flattened = posixParser.flatten(options, arguments, false);

        assertNotNull(flattened);
        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--arg1", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value", flattened[3]);
        assertEquals("--arg2=value", flattened[4]);
    }

    @Test
    public void testProcess() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();

        posixParser.currentOption = new Option("arg1", true, "description");
        posixParser.process("value");

        assertFalse(posixParser.eatTheRest);
        assertEquals(2, posixParser.tokens.size());
        assertEquals("arg1", posixParser.tokens.get(0));
        assertEquals("value", posixParser.tokens.get(1));
        assertNull(posixParser.currentOption);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();

        posixParser.processSingleHyphen("-");

        assertEquals(1, posixParser.tokens.size());
        assertEquals("-", posixParser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "description"));

        boolean stopAtNonOption = false;

        posixParser.processOptionToken("-a", stopAtNonOption);

        assertEquals(1, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
        assertNotNull(posixParser.currentOption);
        assertEquals("a", posixParser.currentOption.getOpt());
        assertEquals("optionA", posixParser.currentOption.getLongOpt());

        stopAtNonOption = true;

        posixParser.init();
        posixParser.processOptionToken("-b", stopAtNonOption);
        assertTrue(posixParser.eatTheRest);
        assertEquals(0, posixParser.tokens.size());
    }

    @Test
    public void testBurstToken() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "description"));
        options.addOption(new Option("b", "optionB", true, "description"));

        boolean stopAtNonOption = false;

        posixParser.burstToken("-ab", stopAtNonOption);

        assertEquals(2, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
        assertEquals("-b", posixParser.tokens.get(1));
        assertNotNull(posixParser.currentOption);
        assertEquals("b", posixParser.currentOption.getOpt());
        assertEquals("optionB", posixParser.currentOption.getLongOpt());

        posixParser.init();
        posixParser.burstToken("-abc", stopAtNonOption);

        assertEquals(3, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
        assertEquals("-b", posixParser.tokens.get(1));
        assertEquals("-c", posixParser.tokens.get(2));

        stopAtNonOption = true;

        posixParser.init();
        posixParser.burstToken("-abcd", stopAtNonOption);

        assertTrue(posixParser.eatTheRest);
        assertEquals(0, posixParser.tokens.size());
    }

}