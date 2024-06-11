import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};
         try {
             PosixParser parser = new PosixParser();
             String[] result = parser.flatten(options, arguments, false);
             Assert.assertArrayEquals(expected, result);
         } catch (ParseException e) {
             Assert.fail("Unexpected exception: " + e.getMessage());
         }
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] arguments = {"-a", "arg1", "arg2", "arg3"};
        String[] expected = {"-a", "arg1", "arg2", "arg3"};
         try {
             PosixParser parser = new PosixParser();
             String[] result = parser.flatten(options, arguments, false);
             Assert.assertArrayEquals(expected, result);
         } catch (ParseException e) {
             Assert.fail("Unexpected exception: " + e.getMessage());
         }
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};
         try {
             PosixParser parser = new PosixParser();
             String[] result = parser.flatten(options, arguments, false);
             Assert.assertArrayEquals(expected, result);
         } catch (ParseException e) {
             Assert.fail("Unexpected exception: " + e.getMessage());
         }
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] arguments = {"-a", "--d", "-c", "3", "arg1", "arg2", "arg3"};
        String[] expected = {"-a", "--d", "-c", "3"};
         try {
             PosixParser parser = new PosixParser();
             String[] result = parser.flatten(options, arguments, false);
             Assert.assertArrayEquals(expected, result);
         } catch (ParseException e) {
             Assert.fail("Unexpected exception: " + e.getMessage());
         }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"arg1", "-a", "-b", "arg3"};
        String[] expected = {"arg1", "-a"};
         try {
             PosixParser parser = new PosixParser();
             String[] result = parser.flatten(options, arguments, true);
             Assert.assertArrayEquals(expected, result);
         } catch (ParseException e) {
             Assert.fail("Unexpected exception: " + e.getMessage());
         }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] arguments = {"--invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingOptionArgument() {
        String[] arguments = {"-a", "arg1"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_TooManyArgumentsForOption() {
        String[] arguments = {"-c", "arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        parser.processSingleHyphen("-");
        tokens.add("-");
        Assert.assertEquals(tokens, parser.getTokens());
    }

    @Test
    public void testProcessOptionToken() {
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        parser.init();
        parser.flatten(options, arguments, false);
        List<String> tokens = parser.getTokens();
        tokens.remove(0); // remove the first element, which is "--"
        List<String> expected = new ArrayList<>();
        expected.add("-a");
        Assert.assertEquals(expected, tokens);
    }

    @Test
    public void testBurstToken() {
        String token = "--abcd";
        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("--d");
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        List<String> tokens = parser.getTokens();
        tokens.remove(0); // remove the first element, which is "--"
        Assert.assertEquals(expected, parser.getTokens());
    }

    private List<String> getTokens(PosixParser parser) {
        ArrayList tokens = (ArrayList) parser.tokens;
        return (List<String>) tokens;
    }
}