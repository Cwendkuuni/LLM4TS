import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.getTokens().size(), 0);
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        // Test case for typical command line arguments
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "valueA", "-b", "valueB"};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertArrayEquals(flattenedArguments, arguments);
        
        // Test case for stopping at non-option
        String[] arguments2 = {"-a", "valueA", "-b", "valueB", "nonOption"};
        String[] flattenedArguments2 = parser.flatten(options, arguments2, true);
        assertArrayEquals(flattenedArguments2, new String[]{"-a", "valueA", "-b", "valueB", "nonOption"});
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        tokens.add("-a");
        tokens.add("valueA");
        tokens.add("nonOption");
        Iterator<String> iter = tokens.iterator();
        parser.gobble(iter);
        assertTrue(iter.hasNext());
    }

    @Test
    public void testProcess() {
        // Test case when current option has argument
        PosixParser parser = new PosixParser();
        Option optionWithArg = new Option("a", "Option with argument", true, "arg");
        parser.setCurrentOption(optionWithArg);
        parser.process("value");
        assertNull(parser.getCurrentOption());
        assertEquals(parser.getTokens().get(0), "value");

        // Test case when current option does not have argument
        Option optionWithoutArg = new Option("b", "Option without argument", false);
        parser.setCurrentOption(optionWithoutArg);
        parser.process("value");
        assertNull(parser.getCurrentOption());
        assertTrue(parser.isEatTheRest());
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals(parser.getTokens().get(0), "-");
    }

    @Test
    public void testProcessOptionToken() {
        // Test case when option exists
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        parser.setOptions(options);
        parser.processOptionToken("a", false);
        assertNotNull(parser.getCurrentOption());
        assertEquals(parser.getTokens().get(0), "a");

        // Test case when option does not exist
        parser.processOptionToken("c", false);
        assertNull(parser.getCurrentOption());
        assertFalse(parser.isEatTheRest());
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        parser.setOptions(options);

        // Test case for bursting token with valid options
        parser.burstToken("-ab", false);
        assertEquals(parser.getTokens().get(0), "-a");
        assertEquals(parser.getTokens().get(1), "-b");

        // Test case for bursting token with invalid option
        parser.burstToken("-c", false);
        assertEquals(parser.getTokens().get(2), "-c");
    }

}