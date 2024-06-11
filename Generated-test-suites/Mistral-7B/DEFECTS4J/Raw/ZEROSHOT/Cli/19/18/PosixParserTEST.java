import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testPosixParser() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 3, "Option D");
        options.addOption("e", 4, "Option E");
        options.addOption("f", 5, "Option F");

        String[] args = {"--a", "-b", "--c", "1", "--d", "2", "3", "--e", "4", "5"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        List<String> expected = Arrays.asList(
                "--a",
                "-b",
                "--c",
                "1",
                "--d",
                "2",
                "3",
                "--e",
                "4",
                "5"
        );
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);

        // Test with stopAtNonOption = false
        args = new String[]{"--a", "-b", "--c", "1", "--d", "2", "3", "--e", "4", "5", "-x"};
        parsedArgs = parser.parse(options, args, false);
        expected = Arrays.asList(
                "--a",
                "-b",
                "--c",
                "1",
                "--d",
                "2",
                "3",
                "--e",
                "4",
                "5",
                "--x"
        );
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);

        // Test with invalid option
        args = new String[]{"--invalid", "option"};
        try {
            parser.parse(options, args, true);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test with long option with value
        options.addOption("z", "zValue", "Option Z");
        args = new String[]{"--z", "value"};
        parsedArgs = parser.parse(options, args, true);
        expected = Arrays.asList(
                "--z",
                "--zValue=value"
        );
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);

        // Test with empty arg list
        args = new String[0];
        parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(Collections.emptyList().toArray(new String[0]), parsedArgs);

        // Test with no options
        options.clearOptions();
        args = new String[]{"test"};
        parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(new String[]{"test"}, parsedArgs);
    }
}