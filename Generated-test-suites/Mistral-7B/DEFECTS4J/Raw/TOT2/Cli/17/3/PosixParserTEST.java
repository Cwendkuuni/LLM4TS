import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with an argument");
        options.addOption("c", 3, "Option C with an argument");
        options.addOption("d", false, "Option D without an argument");

        String[] args = {"--a", "--b", "1", "--c", "2", "--d", "test"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--a",
                "--b",
                "1",
                "--c",
                "2",
                "--d",
                "test"
        );

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithUnknownOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] args = {"--x", "test"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", 1, "Option A with a required argument");
        String[] args = {"--a", "test", "--a", "invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] args = {"--a", "--b", "1", "--c", "2", "--d", "test", "other"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--a",
                "--b",
                "1",
                "--c",
                "2",
                "--d",
                "test",
                "other"
        );

        Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        Assert.assertNotNull(parser.tokens);
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertNull(parser.currentOption);
    }
}