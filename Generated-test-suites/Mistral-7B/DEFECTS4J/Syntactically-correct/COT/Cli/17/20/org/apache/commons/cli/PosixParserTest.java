import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class PosixParserTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a double argument");
        options.addOption("d", "defaultValue", "Option D with a string argument");
    }

    @Test
    public void testFlatten_EmptyArgs() {
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_NoArgs() {
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_SingleOptionWithArg() {
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] arguments = {"-a", "-b", "--d", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "--d", "value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] arguments = {"-z"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOptionWithArg() {
        String[] arguments = {"-z", "value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "--", "-c", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"-a", "--", "-c", "value"}, result);
    }

    @Test
    public void testFlatten_BurstOption() {
        String[] arguments = {"-abc"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c"}, result);
    }

    @Test
    public void testFlatten_BurstOptionWithArg() {
        String[] arguments = {"-abcd"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "d"}, result);
    }

    @Test
    public void testFlatten_BurstOptionWithArgAndStopAtNonOption() {
        String[] arguments = {"-abcd", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"-a", "-bcd"}, result);
    }
}