import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    private PosixParser parser = new PosixParser();

    @Test
    public void testFlatten_withEmptyOptions_returnsEmptyArray() {
        Options options = new Options();
        String[] arguments = new String[]{};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withNoOptions_returnsSameArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
        assertEquals("arg3", result[2]);
    }

    @Test
    public void testFlatten_withStopAtNonOption_returnsSameArgumentsUntilNonOptionFound() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "arg1", "--option", "arg2", "-b", "-c", "arg3"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--option", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOption_returnsNonOptionAndRemainingArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "arg1", "nonOption", "-b", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(4, result.length);
        assertEquals("--", result[0]);
        assertEquals("nonOption", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_withOptionExistsAndNoArgument_returnsOptionAsArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = new String[]{"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_withOptionExistsAndHasArgument_returnsOptionAndArgumentAsArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = new String[]{"-a", "arg1"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_withOptionExistsAndHasArguments_returnsOptionAndRemainingArgumentsAsArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = new String[]{"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOptionFalse_returnsOptionCharacterAsArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"-x"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOptionFalseAndMultipleOptions_returnsAllOptionsAsArguments_untilNonOptionFound() {
        Options options = new Options();
        String[] arguments = new String[]{"-x", "-y", "-z", "nonOption"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("-x", result[0]);
        assertEquals("-y", result[1]);
        assertEquals("-z", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOptionFalseAndMultipleOptionsNoNonOption_returnsAllOptionsAsArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"-x", "-y", "-z"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("-x", result[0]);
        assertEquals("-y", result[1]);
        assertEquals("-z", result[2]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOptionFalseAndLongOption_returnsOptionAndArgumentAsArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--option", "arg1"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOptionTrue_returnsOptionCharacterAsArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"-x"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOptionTrueAndMultipleOptions_returnsOnlyFirstOptionAsArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"-x", "-y", "-z", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOptionTrueAndMultipleOptionsNoNonOption_returnsOnlyFirstOptionAsArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"-x", "-y", "-z"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_withOptionDoesNotExistAndStopAtNonOptionTrueAndLongOption_returnsOptionAndArgumentAsArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--option", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("option", result[1]);
    }

}