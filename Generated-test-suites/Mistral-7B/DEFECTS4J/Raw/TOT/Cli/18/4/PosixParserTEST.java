import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");
        options.addOption("c", 2, "Option C with two arguments");

        String[] arguments = {"-a", "--b=value", "-c", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("--a", "true", "--b", "value", "--c", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArguments);

        // Test with stopAtNonOption = true
        arguments = new String[]{"-a", "--b=value", "-c", "arg1", "arg2", "--"};
        parsedArguments = parser.flatten(options, arguments, true);
        expected = Arrays.asList("--a", "true", "--b=value", "--", "--c", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArguments);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-x", "invalidOption"};
        PosixParser parser = new PositParser();

        try {
            parser.flatten(options, arguments, false);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String[] parsedArguments = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("--x", "invalidOption");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArguments);
    }

    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList();
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArguments);
    }
}