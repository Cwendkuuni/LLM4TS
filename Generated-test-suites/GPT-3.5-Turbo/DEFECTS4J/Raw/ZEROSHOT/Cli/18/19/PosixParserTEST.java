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
    public void testFlatten_noOptions() {
        String[] args = new String[]{"arg1", "arg2"};
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_singleHyphen() {
        String[] args = new String[]{"-"};
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_singleOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = new String[]{"-a"};
        String[] expected = new String[]{"-a"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_optionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "option a");

        String[] args = new String[]{"-a", "arg1"};
        String[] expected = new String[]{"-a", "arg1"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_optionWithMultipleArgs() {
        Options options = new Options();
        options.addOption("a", true, "option a");

        String[] args = new String[]{"-a", "arg1", "arg2"};
        String[] expected = new String[]{"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialTokenWithValue() {
        String[] args = new String[]{"--special=value"};
        String[] expected = new String[]{"--special", "value"};
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialTokenWithoutValue() {
        String[] args = new String[]{"--special"};
        String[] expected = new String[]{"--special"};
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_burstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        String[] args = new String[]{"-ab"};
        String[] expected = new String[]{"-a", "-b"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_burstTokenWithArg() {
        Options options = new Options();
        options.addOption("a", true, "option a");

        String[] args = new String[]{"-ab", "arg1"};
        String[] expected = new String[]{"-a", "-b", "arg1"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = new String[]{"-a", "arg1", "arg2"};
        String[] expected = new String[]{"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOptionSpecialToken() {
        String[] args = new String[]{"--", "arg1", "arg2"};
        String[] expected = new String[]{"--", "arg1", "arg2"};
        String[] result = parser.flatten(null, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_eatTheRest() {
        String[] args = new String[]{"-a", "arg1", "-b", "arg2"};
        String[] expected = new String[]{"--", "-a", "arg1", "-b", "arg2"};
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_init() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = new String[]{"-a"};
        parser.flatten(options, args, false);
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_gobble() {
        String[] args = new String[]{"-a", "arg1", "-b", "arg2"};
        String[] expected = new String[]{"-a", "arg1", "-b", "arg2"};
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(expected, result);
    }
}