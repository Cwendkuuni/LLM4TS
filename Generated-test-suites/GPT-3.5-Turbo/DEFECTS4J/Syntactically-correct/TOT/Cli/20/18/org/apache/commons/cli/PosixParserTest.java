import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_stopAtNonOption_isTrueAndNoRemainingOptions_shouldReturnEmptyArray() {
        String[] arguments = new String[]{"--option1", "value1", "arg1", "arg2"};
        options.addOption(new Option("option1", true, "option 1"));

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_stopAtNonOption_isTrueAndRemainingOptionsExist_shouldReturnRemainingOptions() {
        String[] arguments = new String[]{"--option1", "value1", "arg1", "arg2"};
        options.addOption(new Option("option1", true, "option 1"));

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_stopAtNonOption_isFalseAndNoRemainingOptions_shouldReturnEmptyArray() {
        String[] arguments = new String[]{"--option1", "value1", "arg1", "arg2"};
        options.addOption(new Option("option1", true, "option 1"));

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_stopAtNonOption_isFalseAndRemainingOptionsExist_shouldReturnRemainingOptions() {
        String[] arguments = new String[]{"--option1", "value1", "arg1", "arg2"};
        options.addOption(new Option("option1", true, "option 1"));

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_longOptionWithValue_shouldReturnOptionAndValue() {
        String[] arguments = new String[]{"--option1", "value1"};
        options.addOption(new Option("option1", true, "option 1"));

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--option1", "value1"}, result);
    }

    @Test
    public void testFlatten_singleHyphenOption_shouldReturnOption() {
        String[] arguments = new String[]{"-"};
        options.addOption(new Option("-", false, "Single hyphen option"));

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_shortOptionWithValue_shouldReturnOptionAndValue() {
        String[] arguments = new String[]{"-o", "value1"};
        options.addOption(new Option("o", true, "option o"));

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-o", "value1"}, result);
    }

    @Test
    public void testFlatten_shortOptionWithMultipleValues_shouldReturnOptionAndValues() {
        String[] arguments = new String[]{"-o", "value1", "value2"};
        options.addOption(new Option("o", true, "option o"));

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-o", "value1", "value2"}, result);
    }

    @Test
    public void testFlatten_shortOptionWithNoValueAndRemainingOptionsExist_shouldReturnRemainingOptions() {
        String[] arguments = new String[]{"-o", "arg1", "arg2"};
        options.addOption(new Option("o", false, "option o"));

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_shortOptionWithNoValueAndRemainingOptionsDoNotExist_shouldReturnOption() {
        String[] arguments = new String[]{"-o"};
        options.addOption(new Option("o", false, "option o"));

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlatten_nonOptionAndStopAtNonOption_shouldReturnNonOption() {
        String[] arguments = new String[]{"arg1", "arg2"};
        options.addOption(new Option("option1", true, "option 1"));

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_nonOptionAndNotStopAtNonOption_shouldReturnNonOption() {
        String[] arguments = new String[]{"arg1", "arg2"};
        options.addOption(new Option("option1", true, "option 1"));

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }
}