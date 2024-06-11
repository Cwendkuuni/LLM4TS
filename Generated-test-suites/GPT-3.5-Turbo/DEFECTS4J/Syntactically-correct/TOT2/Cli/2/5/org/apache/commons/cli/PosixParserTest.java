import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_noOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_withOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("a", "optionA", false, "Option A");
        options.addOption(option1);
        String[] args = {"-a", "valueA"};
        String[] expected = {"-a", "valueA"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_edgeCases() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "File option");
        options.addOption(option1);
        String[] args = {"-f", "test.txt"};
        String[] expected = {"-f", "test.txt"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("a", "optionA", false, "Option A");
        options.addOption(option1);
        String[] args = {"-a", "valueA", "arg1", "arg2"};
        String[] expected = {"-a", "valueA", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_invalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("a", "optionA", false, "Option A");
        options.addOption(option1);
        String[] args = {"-b", "valueB"};
        String[] expected = {"-b", "valueB"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

}