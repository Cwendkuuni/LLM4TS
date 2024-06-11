
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "first option");
        options.addOption("b", true, "second option");
        String[] arguments = {"-a", "-b", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "first option");
        options.addOption("b", true, "second option");
        String[] arguments = {"-a", "-b", "value", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_burstToken() {
        Options options = new Options();
        options.addOption("a", false, "first option");
        options.addOption("b", true, "second option");
        options.addOption("c", true, "third option");
        String[] arguments = {"-abcvalue"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
    }
    
    @Test
    public void testFlatten_burstToken_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "first option");
        options.addOption("b", true, "second option");
        options.addOption("c", true, "third option");
        String[] arguments = {"-abcvalue", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
    }

}
