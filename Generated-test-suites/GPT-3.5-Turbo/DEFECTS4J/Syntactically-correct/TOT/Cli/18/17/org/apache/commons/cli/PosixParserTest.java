import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption_True() {
        // Test case 1: Stop at non-option is true
        // Argument array contains both options and non-options
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        String[] args = {"-a","value1", "-b", "value2", "non-option"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals("Argument array length is incorrect", 1, result.length);
        assertEquals("Argument array contains incorrect elements", "non-option", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        // Test case 2: Stop at non-option is false
        // Argument array contains both options and non-options
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        String[] args = {"-a","value1", "-b", "value2", "non-option"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("Argument array length is incorrect", 3, result.length);
        assertEquals("Argument array contains incorrect elements", "-a", result[0]);
        assertEquals("Argument array contains incorrect elements", "value1", result[1]);
        assertEquals("Argument array contains incorrect elements", "-b", result[2]);
    }

    @Test
    public void testFlatten_WithEqualsSeparator() {
        // Test case 3: Option value has equals separator
        // Argument array contains an option with value separated by '='
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] args = {"-a=value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals("Argument array length is incorrect", 2, result.length);
        assertEquals("Argument array contains incorrect elements", "-a", result[0]);
        assertEquals("Argument array contains incorrect elements", "value", result[1]);
    }

    @Test
    public void testFlatten_WithSingleHyphen() {
        // Test case 4: Argument array contains a single hyphen
        Options options = new Options();
        String[] args = {"-"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals("Argument array length is incorrect", 1, result.length);
        assertEquals("Argument array contains incorrect elements", "-", result[0]);
    }

    @Test
    public void testFlatten_InvalidOption() {
        // Test case 5: Argument array contains an invalid option
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] args = {"-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals("Argument array length is incorrect", 1, result.length);
        assertEquals("Argument array contains incorrect elements", "-b", result[0]);
    }

    @Test
    public void testFlatten_WithBursting() {
        // Test case 6: Argument array contains option that requires bursting
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] args = {"-abursting"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals("Argument array length is incorrect", 2, result.length);
        assertEquals("Argument array contains incorrect elements", "-a", result[0]);
        assertEquals("Argument array contains incorrect elements", "bursting", result[1]);
    }

    @Test
    public void testFlatten_WithoutBursting() {
        // Test case 7: Argument array contains option that doesn't require bursting
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] args = {"-a", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals("Argument array length is incorrect", 2, result.length);
        assertEquals("Argument array contains incorrect elements", "-a", result[0]);
        assertEquals("Argument array contains incorrect elements", "value", result[1]);
    }
}