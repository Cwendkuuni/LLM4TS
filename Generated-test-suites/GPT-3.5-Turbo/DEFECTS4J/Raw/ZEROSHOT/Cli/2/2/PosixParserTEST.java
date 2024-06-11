import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption_True() {
        String[] arguments = {"--option1", "value1", "-o", "value2", "argument1"};

        options.addOption("option1", true, "Option 1");
        options.addOption("o", true, "Option o");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-o", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        String[] arguments = {"--option1", "value1", "-o", "value2", "argument1"};

        options.addOption("option1", true, "Option 1");
        options.addOption("o", true, "Option o");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-o", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("argument1", result[4]);
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {"argument1", "argument2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("argument1", result[0]);
        assertEquals("argument2", result[1]);
    }

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(0, result.length);
    }
}