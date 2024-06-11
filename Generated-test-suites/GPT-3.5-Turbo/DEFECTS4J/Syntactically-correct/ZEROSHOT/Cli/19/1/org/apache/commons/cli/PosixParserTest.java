import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        List tokens = parser.getTokens();
        assertTrue(tokens.isEmpty());
        assertFalse(parser.getEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "value1", "--option=value2", "arg1", "arg2"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        assertNotNull(flattenedArguments);
        assertEquals(5, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("value1", flattenedArguments[1]);
        assertEquals("--option", flattenedArguments[2]);
        assertEquals("value2", flattenedArguments[3]);
        assertEquals("arg1", flattenedArguments[4]);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("a", "option a");
        options.addOption(option);
        boolean stopAtNonOption = false;
        parser.processOptionToken("-a", stopAtNonOption);
        Option currentOption = parser.getCurrentOption();
        assertNotNull(currentOption);
        assertEquals(option, currentOption);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.setCurrentOption(new Option("a", "option a"));
        parser.process("value1");
        assertNull(parser.getCurrentOption());
        List tokens = parser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("value1", tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");
        options.addOption(optionA);
        options.addOption(optionB);
        
        boolean stopAtNonOption = false;
        parser.burstToken("-ab", stopAtNonOption);
        List tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        
        parser.init();
        stopAtNonOption = true;
        parser.burstToken("-ab", stopAtNonOption);
        tokens = parser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("-ab", tokens.get(0));
    }

}