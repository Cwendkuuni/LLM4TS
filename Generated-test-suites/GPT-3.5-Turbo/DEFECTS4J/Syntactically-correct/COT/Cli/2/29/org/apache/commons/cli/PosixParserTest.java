import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "-c"};
        String[] expected = {"-a", "-b", "-c"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "-c", "nonoption"};
        String[] expected = {"-a", "-b", "-c", "nonoption"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "-c", "nonoption"};
        String[] expected = {"-a", "-b", "-c", "nonoption"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("a", true, "Option A"));
        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptionValues() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("a", true, "Option A"));
        String[] arguments = {"-a", "value1", "value2"};
        String[] expected = {"-a", "value1", "value2"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        String[] arguments = {"-b"};
        String[] expected = {"-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithInvalidOptionWithoutStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        String[] arguments = {"-b"};
        String[] expected = {"-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBursting() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", false, "Option B"));
        String[] arguments = {"-ab"};
        String[] expected = {"-a", "-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstingAndOptionValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("a", true, "Option A"));
        options.addOption(new Option("b", false, "Option B"));
        String[] arguments = {"-ab", "value"};
        String[] expected = {"-a", "value", "-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }
}