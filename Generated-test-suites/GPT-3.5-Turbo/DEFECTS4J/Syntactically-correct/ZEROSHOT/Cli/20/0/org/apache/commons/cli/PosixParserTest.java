import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.currentOption);
        assertEquals(parser.tokens.size(), 0);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "input file");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "test.txt"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(flattened.length, 2);
        assertEquals(flattened[0], "-f");
        assertEquals(flattened[1], "test.txt");
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList("-f", "test.txt", "arg1", "arg2").iterator();
        parser.gobble(iter);
        List tokens = parser.tokens;

        assertEquals(tokens.size(), 4);
        assertEquals(tokens.get(0), "-f");
        assertEquals(tokens.get(1), "test.txt");
        assertEquals(tokens.get(2), "arg1");
        assertEquals(tokens.get(3), "arg2");
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.currentOption = option;
        parser.process("test.txt");
        List tokens = parser.tokens;

        assertEquals(tokens.size(), 1);
        assertEquals(tokens.get(0), "test.txt");
        assertNull(parser.currentOption);

        parser.currentOption = option;
        parser.process("arg1");
        tokens = parser.tokens;

        assertEquals(tokens.size(), 1);
        assertEquals(tokens.get(0), "arg1");
        assertNull(parser.currentOption);

        parser.currentOption = null;
        parser.process("arg2");
        tokens = parser.tokens;

        assertEquals(tokens.size(), 3);
        assertEquals(tokens.get(0), "--");
        assertEquals(tokens.get(1), "arg2");
        assertEquals(tokens.get(2), "arg1");
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "input file");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.processOptionToken("-f", false);

        List tokens = parser.tokens;
        assertEquals(tokens.size(), 1);
        assertEquals(tokens.get(0), "-f");
        assertNull(parser.currentOption);

        parser.options = options;
        parser.processOptionToken("-f", true);
        tokens = parser.tokens;

        assertEquals(tokens.size(), 1);
        assertEquals(tokens.get(0), "-f");
        assertNull(parser.currentOption);
        assertEquals(true, parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-fabc", false);

        List tokens = parser.tokens;
        assertEquals(tokens.size(), 1);
        assertEquals(tokens.get(0), "-fabc");
        assertNull(parser.currentOption);

        parser.options = options;
        parser.currentOption = option;
        parser.burstToken("-fg", false);
        tokens = parser.tokens;

        assertEquals(tokens.size(), 2);
        assertEquals(tokens.get(0), "-f");
        assertEquals(tokens.get(1), "g");
        assertNull(parser.currentOption);

        parser.options = options;
        parser.burstToken("-fh", true);
        tokens = parser.tokens;

        assertEquals(tokens.size(), 1);
        assertEquals(tokens.get(0), "-fh");
        assertNull(parser.currentOption);
        assertEquals(true, parser.eatTheRest);
    }
}