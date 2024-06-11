import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();

        assertEquals(false, posixParser.eatTheRest);
        assertEquals(null, posixParser.currentOption);
        assertEquals(0, posixParser.tokens.size());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "valueA", "-b", "valueB"};
        PosixParser posixParser = new PosixParser();
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);

        assertEquals(4, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("valueA", flattenedArguments[1]);
        assertEquals("-b", flattenedArguments[2]);
        assertEquals("valueB", flattenedArguments[3]);
    }

    @Test
    public void testProcess() {
        PosixParser posixParser = new PosixParser();
        posixParser.process("value");

        assertEquals(true, posixParser.eatTheRest);
        assertEquals(2, posixParser.tokens.size());
        assertEquals("--", posixParser.tokens.get(0));
        assertEquals("value", posixParser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("c", false, "Option c");

        PosixParser posixParser = new PosixParser();
        posixParser.processOptionToken("-c", false);

        assertEquals(null, posixParser.currentOption);
        assertEquals(1, posixParser.tokens.size());
        assertEquals("-c", posixParser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("d", false, "Option d");
      
        PosixParser posixParser = new PosixParser();
        posixParser.burstToken("-dvalueD", false);

        assertEquals(null, posixParser.currentOption);
        assertEquals(2, posixParser.tokens.size());
        assertEquals("-d", posixParser.tokens.get(0));
        assertEquals("valueD", posixParser.tokens.get(1));
    }
}