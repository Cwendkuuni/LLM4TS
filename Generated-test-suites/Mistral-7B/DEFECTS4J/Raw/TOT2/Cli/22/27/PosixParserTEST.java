import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList();
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "An option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--option");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenSingleValueOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "An option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--option", "value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "--option2", "--option3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--option1", "--option2", "--option3");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOptionValue", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option")
                .longOpt("longOptionValue");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOptionValue", "value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknownOption"};
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingArgumentForOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", false, "An option");

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMultipleUnknownOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknownOption1", "--unknownOption2"};
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option", "value", "--nonOption"};
        Options options = new Options();
        options.addOption("option", true, "An option");

        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--option", "value");
        Assert.assertArrayEquals(expected.toArray(), Arrays.copyOfRange(result, 0, 2));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String token = "-a --longOptionValue --b --option --optionValue";

        parser.burstToken(token, false);

        List<String> expected = Arrays.asList("-a", "--longOptionValue", "--option", "--optionValue", "-b");
        List<String> result = new ArrayList<>(Arrays.asList(parser.tokens.toArray(new String[0])));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-a --longOptionValue --b --unknownOption";

        parser.burstToken(token, true);

        List<String> expected = Arrays.asList("-a", "--longOptionValue", "--unknownOption");
        List<String> result = new ArrayList<>(Arrays.asList(parser.tokens.toArray(new String[0])));
        Assert.assertEquals(expected, result);
    }
}