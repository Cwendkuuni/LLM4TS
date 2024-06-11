import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArguments_ShouldReturnEmptyArray() {
        String[] arguments = new String[0];
        Options options = new Options();
        Parser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleToken_ShouldAddTokenToList() {
        String[] arguments = {"token"};
        Options options = new Options();
        Parser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);
        List expectedResult = Arrays.asList("token");
        Assert.assertArrayEquals(expectedResult.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleLongOption_ShouldAddOptionToList() {
        String[] arguments = {"--option"};
        Options options = new Options();
        Parser parser = new PosixParser();

        options.addOption("option", true, "Option description");
        String[] result = parser.flatten(options, arguments, false);
        List expectedResult = Arrays.asList("--option");
        Assert.assertArrayEquals(expectedResult.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleShortOption_ShouldAddOptionToList() {
        String[] arguments = {"-o"};
        Options options = new Options();
        Parser parser = new PosixParser();

        options.addOption("o", true, "Option description");
        String[] result = parser.flatten(options, arguments, false);
        List expectedResult = Arrays.asList("-o");
        Assert.assertArrayEquals(expectedResult.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleShortOptionWithArgument_ShouldAddOptionAndArgumentToList() {
        String[] arguments = {"-o", "argument"};
        Options options = new Options();
        Parser parser = new PosixParser();

        options.addOption("o", true, "Option description");
        String[] result = parser.flatten(options, arguments, false);
        List expectedResult = Arrays.asList("-o", "argument");
        Assert.assertArrayEquals(expectedResult.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_MultipleTokens_ShouldAddTokensToList() {
        String[] arguments = {"token1", "token2"};
        Options options = new Options();
        Parser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);
        List expectedResult = Arrays.asList("token1", "token2");
        Assert.assertArrayEquals(expectedResult.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_ShouldThrowParseException() {
        String[] arguments = {"--invalidOption"};
        Options options = new Options();
        Parser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOptionWithArgument_ShouldThrowParseException() {
        String[] arguments = {"--invalidOption", "argument"};
        Options options = new Options();
        Parser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption_ShouldStopAtNonOption() {
        String[] arguments = {"token1", "--nonOption", "token2"};
        Options options = new Options();
        Parser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);
        List expectedResult = Arrays.asList("token1", "--nonOption");
        Assert.assertArrayEquals(expectedResult.toArray(new String[0]), result);
    }

    @Test
    public void testBurstToken_EmptyToken_ShouldReturnEmptyList() {
        String token = "";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();

        List result = parser.burstToken(token, stopAtNonOption);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testBurstToken_SingleCharacterOption_ShouldReturnSingleCharacterList() {
        String token = "-o";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();

        List result = parser.burstToken(token, stopAtNonOption);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("-o", result.get(0));
    }

    @Test
    public void testBurstToken_SingleCharacterOptionWithArgument_ShouldReturnSingleCharacterAndArgumentList() {
        String token = "-o";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("o", true, "Option description");

        List result = parser.burstToken(token, stopAtNonOption, options);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("-o", result.get(0));
        Assert.assertEquals("Option description", result.get(1));
    }

    @Test
    public void testBurstToken_SingleLongOption_ShouldReturnLongOptionList() {
        String token = "--option";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("option", true, "Option description");

        List result = parser.burstToken(token, stopAtNonOption, options);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("--option", result.get(0));
    }

    @Test
    public void testBurstToken_LongOptionWithArgument_ShouldReturnLongOptionAndArgumentList() {
        String token = "--option=argument";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("option", true, "Option description");

        List result = parser.burstToken(token, stopAtNonOption, options);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("--option", result.get(0));
        Assert.assertEquals("argument", result.get(1));
    }

    @Test
    public void testBurstToken_InvalidOption_ShouldReturnEmptyList() {
        String token = "--invalidOption";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();

        List result = parser.burstToken(token, stopAtNonOption);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testBurstToken_InvalidOptionWithArgument_ShouldReturnEmptyList() {
        String token = "--invalidOption=argument";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();

        List result = parser.burstToken(token, stopAtNonOption);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testBurstToken_StopAtNonOption_ShouldReturnEmptyList() {
        String token = "--nonOption";
        boolean stopAtNonOption = true;
        PosixParser parser = new PosixParser();

        List result = parser.burstToken(token, stopAtNonOption);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testBurstToken_StopAtNonOptionWithRemainingCharacters_ShouldReturnEmptyList() {
        String token = "--nonOption-";
        boolean stopAtNonOption = true;
        PosixParser parser = new PosixParser();

        List result = parser.burstToken(token, stopAtNonOption);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testBurstToken_StopAtNonOptionWithRemainingTokens_ShouldReturnTokensList() {
        String[] arguments = {"--nonOption1", "--nonOption2"};
        boolean stopAtNonOption = true;
        PosixParser parser = new PosixParser();

        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        List expectedResult = Arrays.asList("--nonOption1", "--nonOption2");

        List resultList = parser.burstToken(arguments[0], stopAtNonOption);
        Assert.assertEquals(expectedResult.size(), resultList.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            Assert.assertEquals(expectedResult.get(i), resultList.get(i));
        }
    }
}