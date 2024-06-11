import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten_WithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "--", "file1", "file2"};

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("file1", result[1]);
        assertEquals("file2", result[2]);
    }

    @Test
    public void testFlatten_WithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "--", "file1", "file2"};

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("file1", result[2]);
        assertEquals("file2", result[3]);
        assertEquals("file2", result[4]);
    }

    @Test
    public void testFlatten_WithValidOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value"};

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_WithInvalidOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-d", "--", "file1", "file2"};

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("-d", result[0]);
        assertEquals("--", result[1]);
        assertEquals("file1", result[2]);
        assertEquals("file2", result[3]);
    }

    @Test
    public void testFlatten_WithInvalidOption_WithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-d", "-a", "file1", "file2"};

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("-d", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("file1", result[2]);
        assertEquals("file2", result[3]);
        assertEquals("file1", result[4]);
        assertEquals("file2", result[5]);
    }
}