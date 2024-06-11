import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_ValidOptions_ShouldReturnCorrectTokens() {
        String[] arguments = new String[]{"--optionA", "--optionB=valueB", "-o", "optionC", "valueC", "--"};
        Options options = new Options();
        options.addOption("optionA", true, "A");
        options.addOption("optionB", true, "B");
        options.addOption("optionC", true, "C");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(new String[]{"--optionA", "--optionB", "-o", "optionC", "valueC"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test
    public void testFlatten_SingleHyphen_ShouldAddSingleHyphenToTokens() {
        String[] arguments = new String[]{"--", "-"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(new String[]{"--", "-"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test
    public void testFlatten_OptionWithoutValue_ShouldAddOptionToTokens() {
        String[] arguments = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("optionA", false, "A");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(new String[]{"--optionA"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test
    public void testFlatten_OptionWithValue_ShouldAddOptionAndValueToTokens() {
        String[] arguments = new String[]{"--optionA=valueA"};
        Options options = new Options();
        options.addOption("optionA", true, "A");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(new String[]{"--optionA", "valueA"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_UnknownOption_ShouldThrowIllegalArgumentException() {
        String[] arguments = new String[]{"--unknownOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption_ShouldStopProcessingAtNonOption() {
        String[] arguments = new String[]{"--optionA", "nonOption"};
        Options options = new Options();
        options.addOption("optionA", true, "A");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, true);

        List<String> expectedTokens = Arrays.asList(new String[]{"--optionA"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test
    public void testFlatten_MultipleNonOptions_ShouldStopProcessingAfterFirstNonOption() {
        String[] arguments = new String[]{"--optionA", "nonOption1", "nonOption2"};
        Options options = new Options();
        options.addOption("optionA", true, "A");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, true);

        List<String> expectedTokens = Arrays.asList(new String[]{"--optionA"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test
    public void testFlatten_LongOptionWithValue_ShouldAddOptionAndValueToTokens() {
        String[] arguments = new String[]{"--longOptionWithValue=longValue"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true, "LOV");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(new String[]{"--longOptionWithValue", "longValue"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue_ShouldAddLongOptionToTokens() {
        String[] arguments = new String[]{"--longOptionWithoutValue"};
        Options options = new Options();
        options.addOption("longOptionWithoutValue", false, "LOWV");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(new String[]{"--longOptionWithoutValue"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test
    public void testFlatten_LongOptionWithArgument_ShouldAddLongOptionAndArgumentToTokens() {
        String[] arguments = new String[]{"--longOptionWithArgument=argument"};
        Options options = new Options();
        options.addOption("longOptionWithArgument", true, "LOWArg");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(new String[]{"--longOptionWithArgument", "argument"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test
    public void testFlatten_LongOptionWithMultipleArguments_ShouldAddLongOptionAndArgumentsToTokens() {
        String[] arguments = new String[]{"--longOptionWithMultipleArguments=arg1 arg2 arg3"};
        Options options = new Options();
        options.addOption("longOptionWithMultipleArguments", true, "LOMMA");

        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(new String[]{"--longOptionWithMultipleArguments", "arg1", "arg2", "arg3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_LongOptionWithInvalidArgument_ShouldThrowIllegalArgumentException() {
        String[] arguments = new String[]{"--longOptionWithInvalidArgument=argument:"};
        Options options = new Options();
        options.addOption("longOptionWithInvalidArgument", true, "LOWIA");

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_UnknownLongOption_ShouldThrowIllegalArgumentException() {
        String[] arguments = new String[]{"--unknownLongOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testBurstToken_ValidOption_ShouldReturnCorrectTokens() {
        String token = "--optionA";
        PosixParser parser = new PosixParser();
        List<String> expectedTokens = Arrays.asList(new String[]{"--optionA"});

        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(expectedTokens, tokens);
    }

    @Test
    public void testBurstToken_UnknownOption_ShouldReturnEmptyList() {
        String token = "--unknownOption";
        PosixParser parser = new PosixParser();
        List<String> expectedTokens = Arrays.asList(new String[0]);

        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(expectedTokens, tokens);
    }

    @Test
    public void testBurstToken_OptionWithValue_ShouldReturnCorrectTokens() {
        String token = "--optionA=value";
        PosixParser parser = new PosixParser();
        List<String> expectedTokens = Arrays.asList(new String[]{"--optionA", "value"});

        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(expectedTokens, tokens);
    }

    @Test
    public void testBurstToken_OptionWithMultipleArguments_ShouldReturnCorrectTokens() {
        String token = "--optionA=arg1 arg2 arg3";
        PosixParser parser = new PosixParser();
        List<String> expectedTokens = Arrays.asList(new String[]{"--optionA", "arg1", "arg2", "arg3"});

        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(expectedTokens, tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstToken_OptionWithInvalidArgument_ShouldThrowIllegalArgumentException() {
        String token = "--optionA=arg:";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
    }
}