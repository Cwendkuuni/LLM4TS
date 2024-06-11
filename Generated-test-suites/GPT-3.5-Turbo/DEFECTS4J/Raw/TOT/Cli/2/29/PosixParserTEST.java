import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOptionIsTrue_ReturnFlattenedArrayWithoutProcessingNonOptions() {
        String[] arguments = {"--option1", "value1", "nonOption", "--option2"};

        String[] result = posixParser.flatten(null, arguments, true);

        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--option2", result[2]);
        assertEquals(3, result.length);
    }

    @Test
    public void testFlatten_StopAtNonOptionIsFalse_ReturnFlattenedArrayIncludingAllArguments() {
        String[] arguments = {"--option1", "--", "nonOption", "-"};

        String[] result = posixParser.flatten(null, arguments, false);

        assertEquals("--option1", result[0]);
        assertEquals("--", result[1]);
        assertEquals("nonOption", result[2]);
        assertEquals("-", result[3]);
        assertEquals(4, result.length);
    }

    @Test
    public void testProcess_SingleHyphenToken_AddsHyphenToProcessedTokensList() {
        posixParser.processSingleHyphen("-");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("-", result[0]);
    }

    @Test
    public void testProcess_OptionWithHasArg_AddsTokenToProcessedTokensListAndSetCurrentOptionToNull() {
        Option option = new Option("a", true, "option a");
        posixParser.setCurrentOption(option);

        posixParser.process("value");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("value", result[0]);
        assertEquals(null, posixParser.getCurrentOption());
    }

    @Test
    public void testProcess_OptionWithHasArgs_AddsTokenToProcessedTokensList() {
        Option option = new Option("a", false);
        option.setArgs(2);
        posixParser.setCurrentOption(option);

        posixParser.process("value");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("value", result[0]);
    }

    @Test
    public void testProcess_NoCurrentOption_StopAtNonOptionIsTrue_AddsTokenAndNonOptionToProcessedTokensList() {
        posixParser.setStopAtNonOption(true);

        posixParser.process("value");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("--", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testProcess_NoCurrentOption_StopAtNonOptionIsFalse_AddsTokenOnlyToProcessedTokensList() {
        posixParser.setStopAtNonOption(false);

        posixParser.process("value");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("value", result[0]);
    }

    @Test
    public void testBurstToken_ValidOption_AddsBurstTokensToProcessedTokensList() {
        Option option = new Option("a", "option a");
        Option option2 = new Option("b", true, "option b");
        posixParser.getOptions().addOption(option);
        posixParser.getOptions().addOption(option2);

        posixParser.burstToken("-ab");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testBurstToken_ValidOptionWithArg_AddsBurstTokensAndArgToProcessedTokensList() {
        Option option = new Option("a", true, "option a");
        posixParser.getOptions().addOption(option);

        posixParser.burstToken("-avalue");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testBurstToken_InvalidOption_StopAtNonOptionIsTrue_AddsTokenAndNonOptionToProcessedTokensList() {
        posixParser.setStopAtNonOption(true);

        posixParser.burstToken("-ab");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("--", result[0]);
        assertEquals("-ab", result[1]);
    }

    @Test
    public void testBurstToken_InvalidOption_StopAtNonOptionIsFalse_AddsTokenOnlyToProcessedTokensList() {
        posixParser.setStopAtNonOption(false);

        posixParser.burstToken("-ab");

        String[] result = (String[])privateMethod.invoke(posixParser, "getTokens");

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    private Method privateMethod(String methodName, Class... parameterTypes) {
        Method method = PosixParser.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }
}