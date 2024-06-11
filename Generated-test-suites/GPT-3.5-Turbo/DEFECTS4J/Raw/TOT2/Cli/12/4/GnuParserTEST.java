import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_ValidOptions_ReturnFlattenedArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "valueA", "nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "valueA", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_StopFlatteningAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a", "valueA", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "valueA", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DashArgument_AddDashToProcessedTokens() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-", "argument"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-", "argument"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleDash_StopAtDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-a", "valueA", "--", "nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "valueA", "--", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DuplicateOptionValue_CheckForSpecialProperties() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Define a property");

        String[] arguments = {"-Dkey=value"};

        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-D", "key=value"};
        assertArrayEquals(expected, result);
    }

}