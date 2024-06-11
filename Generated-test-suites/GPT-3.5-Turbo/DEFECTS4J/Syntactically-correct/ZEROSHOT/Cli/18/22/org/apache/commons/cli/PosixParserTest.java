import org.junit.*;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();

        assertFalse(posixParser.eatTheRest);
        assertEquals(0, posixParser.tokens.size());
        assertNull(posixParser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "test", "--b=test", "-c"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, true);

        assertEquals(6, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("test", flattenedArguments[1]);
        assertEquals("--", flattenedArguments[2]);
        assertEquals("b", flattenedArguments[3]);
        assertEquals("test", flattenedArguments[4]);
        assertEquals("-c", flattenedArguments[5]);
    }

    @Test
    public void testGobble() {
        PosixParser posixParser = new PosixParser();
        Iterator iterator = Arrays.asList("test1", "test2", "test3").iterator();
        posixParser.eatTheRest = true;
        posixParser.gobble(iterator);

        assertEquals(3, posixParser.tokens.size());
        assertEquals("test1", posixParser.tokens.get(0));
        assertEquals("test2", posixParser.tokens.get(1));
        assertEquals("test3", posixParser.tokens.get(2));
    }

    @Test
    public void testProcess() {
        PosixParser posixParser = new PosixParser();
        Option option = new Option("t", false, "test option");
        option.setArgs(2);
        posixParser.currentOption = option;

        posixParser.process("value");

        assertEquals(1, posixParser.tokens.size());
        assertEquals("value", posixParser.tokens.get(0));
        assertNull(posixParser.currentOption);

        posixParser.process("value1");

        assertEquals(2, posixParser.tokens.size());
        assertEquals("value1", posixParser.tokens.get(1));
        assertNull(posixParser.currentOption);

        posixParser.currentOption = option;
        posixParser.currentOption.setArgName("arg");

        posixParser.process("value2");

        assertEquals(3, posixParser.tokens.size());
        assertEquals("value2", posixParser.tokens.get(2));
        assertNull(posixParser.currentOption);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser posixParser = new PosixParser();
        posixParser.processSingleHyphen("-");

        assertEquals(1, posixParser.tokens.size());
        assertEquals("-", posixParser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        Option option = new Option("a", false, "test option");
        options.addOption(option);

        posixParser.options = options;
        posixParser.processOptionToken("-a", true);

        assertEquals(1, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
        assertEquals(option, posixParser.currentOption);

        posixParser.processOptionToken("-b", true);

        assertEquals(1, posixParser.tokens.size());
        assertNull(posixParser.currentOption);
        assertTrue(posixParser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("a", false, "test option1");
        options.addOption(option1);
        Option option2 = new Option("b", true, "test option2");
        options.addOption(option2);

        posixParser.options = options;
        posixParser.burstToken("-ab", false);

        assertEquals(2, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
        assertEquals("-b", posixParser.tokens.get(1));
        assertEquals(option1, posixParser.currentOption);

        posixParser.burstToken("-ab", true);

        assertEquals(4, posixParser.tokens.size());
        assertEquals("--", posixParser.tokens.get(2));
        assertEquals("b", posixParser.tokens.get(3));

        posixParser.burstToken("-cd", true);

        assertEquals(6, posixParser.tokens.size());
        assertEquals("--", posixParser.tokens.get(4));
        assertEquals("cd", posixParser.tokens.get(5));
    }
}