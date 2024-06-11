import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PosixParserTest {

    @Test
    void testEmptyArgs() {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    void testFlattenSingleOption() {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option"}, result);
    }

    @Test
    void testFlattenSingleValueOption() {
        String[] args = {"--option=value"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    void testFlattenMultipleOptions() {
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test
    void testFlattenSingleDash() {
        String[] args = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-"}, result);
    }

    @Test
    void testFlattenDoubleDash() {
        String[] args = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--"}, result);
    }

    @Test
    void testFlattenSingleHyphenBeforeOption() {
        String[] args = {"-", "--option"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-", "--option"}, result);
    }

    @Test
    void testFlattenSingleHyphenAfterOption() {
        String[] args = {"--option-"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option-"}, result);
    }

    @Test
    void testFlattenOptionWithArgument() {
        String[] args = {"--option=value"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    void testFlattenOptionWithMultipleArguments() {
        String[] args = {"--option=value1 value2"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option", "value1", "value2"}, result);
    }

    @Test
    void testFlattenMultipleOptionsWithArguments() {
        String[] args = {"--option1=value1 value2", "--option2=value3"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option1", "value1", "--option2", "value3"}, result);
    }

    @Test
    void testFlattenMultipleOptionsWithArgumentsAndSingleHyphen() {
        String[] args = {"--option1=value1 value2", "--"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option1", "value1", "--"}, result);
    }

    @Test
    void testFlattenMultipleOptionsWithArgumentsAndDoubleDash() {
        String[] args = {"--option1=value1 value2", "--"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        PosixParser parser = new PosixParser();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, false));
    }

    @Test
    void testFlattenMultipleOptionsWithNoValue() {
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", false, "");
        options.addOption("option2", false, "");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--option1", "--option2"}, result);
    }

    @ParameterizedTest
    @ArgumentsSource(PosixParserArgumentsProvider.class)
    void testFlattenMultipleOptionsWithValuesAndValuesAndSingleHyphen(String[] args, String[] expected) {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void testFlattenStopAtNonOption() {
        String[] args = {"--option", "nonOption"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"--option", "nonOption"}, result);
    }

    @Test
    void testFlattenStopAtNonOptionWithSingleHyphen() {
        String[] args = {"--", "-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, true));
    }

    @Test
    void testFlattenStopAtNonOptionWithDoubleDash() {
        String[] args = {"--", "--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, true));
    }

    @Test
    void testFlattenStopAtNonOptionWithSingleOption() {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"--option"}, result);
    }
}

class PosixParserArgumentsProvider {
    static Collection<Object[]> arguments() {
        return Arrays.asList(new Object[][] {
                {"--option1=value1 value2", "--option1=value1 value3"},
                {"--option1=value1 value2", "--option2=value4"},
                {"--option1=value1 value2", "--option1=value4"}
        });
    }
}