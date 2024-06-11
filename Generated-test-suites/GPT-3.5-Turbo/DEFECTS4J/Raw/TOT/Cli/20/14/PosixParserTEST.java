import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "a option");
        options.addOption("b", true, "b option");
        options.addOption("c", false, "c option");
        options.addOption(Option.builder("d").required(true).hasArgs().desc("d option").build());
        options.addOption(Option.builder("e").optionalArg(true).hasArgs().desc("e option").build());
    }

    @Test
    public void testFlatten_NoArgument() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "-b", "value", "positional"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("positional", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithEquals() {
        String[] arguments = {"--b=", "--c", "--d", "value", "--e", "value1", "value2"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(7, result.length);
        assertEquals("--b", result[0]);
        assertEquals("", result[1]);
        assertEquals("--c", result[2]);
        assertEquals("--d", result[3]);
        assertEquals("value", result[4]);
        assertEquals("--e", result[5]);
        assertEquals("value1", result[6]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionWithArgument() {
        String[] arguments = {"-b", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_ShortOptionWithoutArgument() {
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_UnknownShortOption_StopAtNonOption() {
        String[] arguments = {"-x", "value"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-x", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_UnknownShortOption() {
        String[] arguments = {"-x", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_Bursting() {
        String[] arguments = {"-devalue"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-d", result[0]);
        assertEquals("-e", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_Bursting_StopAtNonOption() {
        String[] arguments = {"-devalue", "positional"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-d", result[0]);
        assertEquals("-evalue", result[1]);
    }

}