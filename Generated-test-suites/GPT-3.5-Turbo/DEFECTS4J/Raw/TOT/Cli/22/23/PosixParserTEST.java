import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        arguments = new String[]{};
    }

    @Test
    public void testFlatten_LongOptionWithNoValue_Success() {
        options.addOption(Option.builder("f").longOpt("file").desc("File option").build());
        arguments = new String[]{"--file"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--file", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithValue_Success() {
        options.addOption(Option.builder("f").longOpt("file").desc("File option").hasArg().build());
        arguments = new String[]{"--file", "test.txt"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--file", result[0]);
        assertEquals("test.txt", result[1]);
    }

    @Test
    public void testFlatten_LongOptionNotDefined_StopAtNonOption() {
        arguments = new String[]{"--unknown"};
        options.addOption(Option.builder("f").longOpt("file").desc("File option").build());

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("--unknown", result[1]);
    }

    @Test
    public void testFlatten_ShortOption_Success() {
        options.addOption(Option.builder("v").desc("Verbose option").build());
        arguments = new String[]{"-v"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-v", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionNotDefined_StopAtNonOption() {
        arguments = new String[]{"-u"};
        options.addOption(Option.builder("v").desc("Verbose option").build());

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-u", result[1]);
    }

    @Test
    public void testFlatten_OptionRequiresBursting_Success() {
        options.addOption(Option.builder("f").desc("File option").build());
        arguments = new String[]{"-fa"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("a", result[1]);
    }

    @Test
    public void testFlatten_NonOptionToken_Success() {
        arguments = new String[]{"value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testFlatten_NonOptionToken_StopAtNonOption() {
        arguments = new String[]{"value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_HyphenToken_Success() {
        arguments = new String[]{"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_HyphenToken_StopAtNonOption() {
        arguments = new String[]{"-"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-", result[1]);
    }

    @Test
    public void testFlatten_EmptyArguments_Success() {
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

}