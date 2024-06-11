import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_NoOptions_ReturnsArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_OptionExists_ReturnsOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Sample option");
        String[] arguments = {"-o"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_StopFlattening() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Sample option");
        String[] arguments = {"-o", "arg1", "--", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = new String[]{"-o", "arg1", "--", "arg2", "arg3"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SpecialPropertiesOption_ReturnsTokens() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-Dproperty=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-D", "property=value"}, result);
    }

    @Test
    public void testFlatten_StartsWithHyphen_AddsToken() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-test", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_EndsWithHyphen_AddsToken() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }
}