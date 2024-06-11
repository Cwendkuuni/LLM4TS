import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value", "-c"};
        String[] expected = {"-a", "-b", "value", "-c"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value", "-c"};
        String[] expected = {"-a", "-b", "value", "-c"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
  
        String[] arguments = {"-b"};
        String[] expected = {"-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithRemainingArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = {"-a", "-c", "value"};
        String[] expected = {"-a", "--", "-c", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}