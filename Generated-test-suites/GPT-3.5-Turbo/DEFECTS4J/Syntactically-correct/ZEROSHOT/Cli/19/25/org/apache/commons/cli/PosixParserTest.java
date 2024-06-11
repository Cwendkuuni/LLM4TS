import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser posixParser;
    private String[] arguments;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        arguments = new String[]{"-a", "--flag", "value1", "-b", "value2"};
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
    }

    @Test
    public void testFlatten() {
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        String[] expectedArguments = new String[]{"-a", "--flag", "value1", "-b", "value2"};

        assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testProcess() {
        posixParser.process("value");

        assertTrue(posixParser.eatTheRest);
        assertTrue(posixParser.tokens.contains("--"));
        assertTrue(posixParser.tokens.contains("value"));
    }

    @Test
    public void testProcessOptionToken() {
        posixParser.processOptionToken("-a", false);

        assertEquals(options.getOption("a"), posixParser.currentOption);
        assertTrue(posixParser.tokens.contains("-a"));
    }

    @Test
    public void testBurstToken() {
        posixParser.burstToken("-a", false);

        assertEquals(options.getOption("a"), posixParser.currentOption);
        assertTrue(posixParser.tokens.contains("-a"));
    }

}