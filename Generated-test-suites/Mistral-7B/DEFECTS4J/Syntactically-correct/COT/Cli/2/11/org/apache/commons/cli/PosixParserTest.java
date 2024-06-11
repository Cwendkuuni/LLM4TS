import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlattenBasicUsage() {
        String[] args = {"--optionA", "-o", "valueA", "--optionB"};
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("--optionA", "-o", "valueA", "--optionB"));
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--optionA", "-o", "valueA", "--", "nonOption"};
        Options options = new Options();
        options.addOption("optionA", true, "a");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("--optionA", "--", "nonOption"));
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] args = {"-", "--optionA"};
        Options options = new Options();
        options.addOption("optionA", true, "a");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("-", "--optionA"));
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenInvalidOption() {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("--invalidOption"));
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullOptions() {
        String[] args = {"--optionA"};
        Parser parser = new PosixParser();
        parser.flatten(null, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArguments() {
        Options options = new Options();
        Parser parser = new PosixParser();
        parser.flatten(options, null, false);
    }

    @Test
    public void testBurstTokenBasicOption() {
        String[] args = {"--optionA"};
        Options options = new Options();
        options.addOption("optionA", true, "a");

        Parser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);
        String token = tokens[1];

        parser.burstToken(token, false);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("-optionA"));
        Assert.assertArrayEquals(expected.toArray(), parser.tokens.toArray());
    }

    @Test
    public void testBurstTokenOptionWithValue() {
        String[] args = {"--optionA=valueA"};
        Options options = new Options();
        options.addOption("optionA", true, "a");

        Parser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);
        String token = tokens[1];

        parser.burstToken(token, false);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("-optionA", "valueA"));
        Assert.assertArrayEquals(expected.toArray(), parser.tokens.toArray());
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        String[] args = {"--optionA", "--optionB"};
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");

        Parser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);

        String token = tokens[1];
        parser.burstToken(token, false);

        List<String> expectedOptionA = new ArrayList<>();
        expectedOptionA.addAll(Arrays.asList("-optionA"));
        List<String> expectedOptionB = new ArrayList<>();
        expectedOptionB.addAll(Arrays.asList("-optionB"));

        Assert.assertEquals(expectedOptionA.size(), parser.tokens.size() - 1);
        Assert.assertEquals(expectedOptionA.get(0), parser.tokens.get(0));
        Assert.assertEquals(expectedOptionB.size(), parser.tokens.size());
        Assert.assertEquals(expectedOptionB.get(0), parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String[] args = {"--optionA", "--", "nonOption"};
        Options options = new Options();
        options.addOption("optionA", true, "a");

        Parser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, true);
        String token = tokens[1];

        parser.burstToken(token, false);

        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("--", "nonOption"));
        Assert.assertArrayEquals(expected.toArray(), parser.tokens.toArray());
    }
}