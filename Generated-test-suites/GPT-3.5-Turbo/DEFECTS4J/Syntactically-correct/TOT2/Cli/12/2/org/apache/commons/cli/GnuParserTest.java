import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_OptionsArgumentsStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        arguments = new String[]{"--foo", "-bar", "-Dproperty=value", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"--foo", "-bar", "-D", "property=value", "--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionsArgumentsNotStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        arguments = new String[]{"-foo", "-bar", "-Dproperty=value", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-foo", "-bar", "-D", "property=value", "--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionsArgumentsWithoutOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        arguments = new String[]{"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionsArgumentsWithDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        arguments = new String[]{"-", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"-", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionsArgumentsWithDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        arguments = new String[]{"--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

}