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
        options.addOption("a", "test option a");
        options.addOption("b", "test option b");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "value");
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("c", "test option c");
        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("-cvalue", false);
        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "-c");
        assertEquals(parser.tokens.get(1), "value");
    }

    @Test
    public void testGobble() {
        Options options = new Options();
        options.addOption("d", "test option d");
        PosixParser parser = new PosixParser();
        parser.init();
        Iterator iter = Arrays.asList(new String[]{"arg1", "arg2", "arg3"}).iterator();
        parser.gobble(iter);
        assertEquals(parser.tokens.size(), 0);
    }

}