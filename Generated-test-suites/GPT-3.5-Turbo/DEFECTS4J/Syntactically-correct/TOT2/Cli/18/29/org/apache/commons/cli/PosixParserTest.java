import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        
        // Test init method
        parser.init();
        assertEquals(parser.tokens.size(), 0);
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }
    
    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "-b", "-c=value", "--option"};
        
        // Test flatten method
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertEquals(flattenedArguments.length, 7);
        assertEquals(flattenedArguments[0], "-a");
        assertEquals(flattenedArguments[1], "arg1");
        assertEquals(flattenedArguments[2], "-b");
        assertEquals(flattenedArguments[3], "-c");
        assertEquals(flattenedArguments[4], "value");
        assertEquals(flattenedArguments[5], "--");
        assertEquals(flattenedArguments[6], "option");
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        
        // Test process method with current option
        parser.currentOption = new Option("a", "test option");
        parser.currentOption.setArgName("arg");
        parser.currentOption.setArgs(2);
        
        parser.process("arg1");
        assertEquals(parser.tokens.get(0), "arg1");
        assertNull(parser.currentOption);
        
        // Test process method without current option
        parser.process("val");
        assertEquals(parser.tokens.get(1), "--");
        assertEquals(parser.tokens.get(2), "val");
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        
        // Test processSingleHyphen method
        parser.processSingleHyphen("-");
        assertEquals(parser.tokens.get(0), "-");
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("z", "test option"));
        
        // Test processOptionToken method with existing option
        boolean stopAtNonOption = false;
        parser.processOptionToken("z", stopAtNonOption);
        assertEquals(parser.tokens.get(0), "z");
        
        // Test processOptionToken method with non-existing option
        parser.processOptionToken("x", stopAtNonOption);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        
        // Test burstToken method
        Options options = new Options();
        options.addOption(new Option("t", "test option"));
        parser.burstToken("-test", false);
        assertEquals(parser.tokens.get(0), "-t");
        assertEquals(parser.tokens.get(1), "est");
    }
}