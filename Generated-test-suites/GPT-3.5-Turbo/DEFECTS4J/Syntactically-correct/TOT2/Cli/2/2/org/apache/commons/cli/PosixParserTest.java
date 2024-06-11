import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", "append", false, "append the content");
        options.addOption("b", "backup", false, "backup the file");
    }

    @Test
    public void testFlattenWithOneOption() {
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] arguments = {"-a", "-b"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlattenWithOptionAndArgument() {
        String[] arguments = {"-a", "file.txt"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("file.txt", result[1]);
    }

    @Test
    public void testFlattenWithNonOptionStopAtNonOption() {
        String[] arguments = {"file1.txt", "file2.txt", "-a"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("file1.txt", result[1]);
    }

    @Test
    public void testFlattenWithBursting() {
        String[] arguments = {"-ab"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlattenWithSpecialToken() {
        String[] arguments = {"--option=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

}