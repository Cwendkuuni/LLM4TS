import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertEquals(parser.tokens.size(), 0);
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "valueA", "--b", "valueB"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(flattened.length, 4);
        assertEquals(flattened[0], "-a");
        assertEquals(flattened[1], "valueA");
        assertEquals(flattened[2], "--b");
        assertEquals(flattened[3], "valueB");
    }

    @Test
    public void testOptionTokenProcessing() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", false);

        assertNotNull(parser.currentOption);
        assertEquals(parser.currentOption.getOpt(), "a");

        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "-a");
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();
        parser.burstToken("-ab", false);

        assertNotNull(parser.currentOption);
        assertEquals(parser.currentOption.getOpt(), "a");

        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "-a");
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.process("value");

        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "--");
        assertEquals(parser.tokens.get(1), "value");
    }

}