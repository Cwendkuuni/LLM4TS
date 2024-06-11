import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");

        String[] args = {"--a", "1", "-b", "2", "-c", "3", "-d", "4", "5", "-e", "6"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--a",
                "-a",
                "true",
                "-b",
                "-b",
                "2",
                "-c",
                "-c",
                "3",
                "-d",
                "-d",
                "4",
                "-e",
                "-e",
                "5",
                "-e",
                "6"
        );

        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");

        String[] args = {"--a", "1", "-b", "2", "-c", "3", "-d", "4", "5", "-e", "6", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--a",
                "-a",
                "true",
                "--",
                "nonOption"
        );

        List<String> actual = Arrays.asList(parsedArgs);

        Assert.assertEquals(expected, actual);
    }
}