import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};

        String[] expected = {};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};

        String[] expected = {"-a"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};

        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};

        String[] expected = {"-a", "-b"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};

        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value"};

        String[] expected = {"--longOption", "value"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg_MultipleArgs() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value1", "value2"};

        String[] expected = {"--longOption", "value1", "value2"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg_EmptyArg() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", ""};

        String[] expected = {"--longOption"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MixedOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--longOption", "-b"};

        String[] expected = {"-a", "--longOption", "-b"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "nonOption"};

        String[] expected = {"-a", "arg1"};
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_WithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "--optionWithArg", "value"};

        String[] expected = {"-a", "arg1", "--optionWithArg"};
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_MultipleArgs() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "--optionWithArgs", "value1", "value2"};

        String[] expected = {"-a", "arg1", "--optionWithArgs", "value1", "value2"};
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_Option() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-a";

        List expected = Arrays.asList("-a");
        List result = parser.burstToken(token, false);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstToken_SingleCharacterOptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-a";

        List expected = Arrays.asList("-a", "arg");
        List result = parser.burstToken(token, true);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstToken_LongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String token = "--longOption";

        List expected = Arrays.asList("--longOption");
        List result = parser.burstToken(token, false);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstToken_LongOptionWithArg() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String token = "--longOption=arg";

        List expected = Arrays.asList("--longOption", "arg");
        List result = parser.burstToken(token, false);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstToken_LongOptionWithMultipleArgs() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String token = "--longOption=arg1 arg2";

        List expected = Arrays.asList("--longOption", "arg1", "arg2");
        List result = parser.burstToken(token, false);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstToken_NonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "arg";

        List expected = Arrays.asList("--", "arg");
        List result = parser.burstToken(token, false);

        Assert.assertEquals(expected, result);
    }
}