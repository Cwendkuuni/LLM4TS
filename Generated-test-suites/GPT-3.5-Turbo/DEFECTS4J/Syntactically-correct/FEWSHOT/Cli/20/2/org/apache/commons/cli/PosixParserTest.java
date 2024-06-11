import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"-a", "-b", "value", "nonOption"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_ContinueAfterNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"-a", "-b", "value", "nonOption"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"--a=value"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("--a", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithEqualsSign() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"--a=1"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--a", result[0]);
        assertEquals("1", result[1]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"-"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"-a"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"-c", "value"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-c", result[1]);
    }

    @Test
    public void testFlatten_UnknownOption_ContinueAfterNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"-c", "value"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-c", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"-a", "1", "-b", "2"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("2", result[3]);
    }

    @Test
    public void testFlatten_RemainingTokens() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = new String[]{"-a", "value", "nonOption", "-b"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("nonOption", result[2]);
        assertEquals("--", result[3]);
        assertEquals("-b", result[4]);
    }
}