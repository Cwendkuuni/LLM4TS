import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {
    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();

        String[] args = {"-a", "-b", "value", "-c"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"-a", "-b", "value", "-c"};

        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_notStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();

        String[] args = {"-a", "-b", "value", "-c"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a", "-b", "value", "-c"};

        assertEquals(expected, result);
    }

    @Test
    public void testFlatten_multipleOptions() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();

        String[] args = {"-a", "-b", "value", "-c"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a", "-b", "value", "-c"};

        assertEquals(expected, result);
    }
}