import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "include an apple");
        options.addOption("b", "banana", true, "include a banana");
        
        String[] arguments = {"-a", "--apple", "-b", "myBanana", "--", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("Flatten result should have 5 elements", 5, result.length);
        assertEquals("First element should be '-a'", "-a", result[0]);
        assertEquals("Second element should be '--apple'", "--apple", result[1]);
        assertEquals("Third element should be '-b'", "-b", result[2]);
        assertEquals("Fourth element should be 'myBanana'", "myBanana", result[3]);
        assertEquals("Fifth element should be '--'", "--", result[4]);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();

        parser.init();

        assertEquals("Tokens list should be empty after init", 0, parser.tokens.size());
        assertFalse("eatTheRest should be false after init", parser.eatTheRest);
        assertNull("currentOption should be null after init", parser.currentOption);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList("arg1", "arg2", "arg3").iterator();
        parser.eatTheRest = true;

        parser.gobble(iter);

        assertEquals("Tokens list should have 3 elements after gobble", 3, parser.tokens.size());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "apple", true, "include an apple");

        parser.process("myApple");

        assertEquals("Tokens list should have 1 element after process", 1, parser.tokens.size());
        assertEquals("Processed element should be 'myApple'", "myApple", parser.tokens.get(0));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();

        parser.processSingleHyphen("-");

        assertEquals("Tokens list should have 1 element after processSingleHyphen", 1, parser.tokens.size());
        assertEquals("Processed element should be '-'", "-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "include an apple");

        parser.options = options;

        parser.processOptionToken("-a", false);

        assertEquals("Current option id should be 'a'", "a", parser.currentOption.getId());
        assertEquals("Tokens list should have 1 element after processOptionToken", 1, parser.tokens.size());
        assertEquals("Processed element should be '-a'", "-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "include an apple");

        parser.options = options;

        parser.burstToken("-ab", false);

        assertEquals("Current option id should be 'a'", "a", parser.currentOption.getId());
        assertEquals("Tokens list should have 2 elements after burstToken", 2, parser.tokens.size());
        assertEquals("Processed element should be '-a'", "-a", parser.tokens.get(0));
        assertEquals("Processed element should be '-b'", "-b", parser.tokens.get(1));
    }
}