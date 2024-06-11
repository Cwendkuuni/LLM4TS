import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.tokens.size(), 0);
        assertEquals(parser.currentOption, null);
        assertEquals(parser.eatTheRest, false);
    }

    @Test
    public void testFlatten() {
        // Test for normal options parsing
        Options options = new Options();
        options.addOption("a", false, "description");
        options.addOption("b", true, "description");
        options.addOption("c", true, "description");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b", "value1", "-c=value2"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(flattenedArgs.length, 5);
        assertEquals(flattenedArgs[0], "-a");
        assertEquals(flattenedArgs[1], "-b");
        assertEquals(flattenedArgs[2], "value1");
        assertEquals(flattenedArgs[3], "-c");
        assertEquals(flattenedArgs[4], "value2");
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        String token = "-abc";
        parser.burstToken(token, false);
        assertEquals(parser.tokens.size(), 3);
        assertEquals(parser.tokens.get(0), "-a");
        assertEquals(parser.tokens.get(1), "-b");
        assertEquals(parser.tokens.get(2), "-c");
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.currentOption = new Option("a", false, "description");
        parser.process("value");
        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "value");
    }
}