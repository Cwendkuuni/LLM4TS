import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSimpleArgs() {
        Options options = new Options();
        String[] args = {"option1", "option2"};
        PosixParser parser = new PosixParser();
        String[] expected = {"option1", "option2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "l");
        String[] args = {"--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--", "option1", "value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--", "option1"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBursting() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"-o--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-o", "--longOption"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstingWithValue() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("longOption", true, "l");
        String[] args = {"-o--longOption=value", "arg"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-o", "--longOption", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstingStopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"-o--longOption", "value", "--"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-o", "--longOption"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }
}