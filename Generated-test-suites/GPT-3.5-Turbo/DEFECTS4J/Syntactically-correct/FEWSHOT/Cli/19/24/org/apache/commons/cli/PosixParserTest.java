import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private static Options options;
    private static PosixParser posixParser;

    static {
        options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").argName("file").hasArg().build());
        options.addOption(Option.builder("a").longOpt("all").build());
        options.addOption(Option.builder("h").longOpt("help").build());

        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = { "arg1", "-f", "arg2", "--", "arg3", "arg4" };
        String[] expected = { "arg1", "-f", "arg2", "--", "arg3", "arg4" };

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlatten_DontStopAtNonOption() {
        String[] arguments = { "arg1", "-f", "arg2", "--", "arg3", "arg4" };
        String[] expected = { "arg1", "-f", "arg2", "--arg3", "arg4" };

        String[] result = posixParser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlatten_ShortOptionWithArg() {
        String[] arguments = { "arg1", "-o", "file.txt" };
        String[] expected = { "arg1", "-o", "file.txt" };

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlatten_ShortOptionWithMultipleArgs() {
        String[] arguments = { "arg1", "-o", "file.txt", "arg2" };
        String[] expected = { "arg1", "-o", "file.txt", "arg2" };

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlatten_LongOption() {
        String[] arguments = { "arg1", "--all" };
        String[] expected = { "arg1", "--all" };

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        String[] arguments = { "arg1", "--help", "file.txt" };
        String[] expected = { "arg1", "--help", "file.txt" };

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testFlatten_LongOptionWithMultipleArgs() {
        String[] arguments = { "arg1", "--help", "file.txt", "arg2" };
        String[] expected = { "arg1", "--help", "file.txt", "arg2" };

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
}