import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption_True_ReturnFlattenedArrayWithNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "nonOption"};
        boolean stopAtNonOption = true;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption_False_ReturnFlattenedArrayWithoutNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "nonOption"};
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_IncludeLongOptionWithValue_ReturnFlattenedArrayWithLongOptionValue() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option").hasArg().create());

        String[] arguments = {"--option=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_IncludeShortOptionWithoutValue_ReturnFlattenedArrayWithShortOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option").hasArg().create());

        String[] arguments = {"-o"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-o", result[0]);
    }

    @Test
    public void testFlatten_IncludeShortOptionWithValue_ReturnFlattenedArrayWithShortOptionValue() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option").hasArg().create());

        String[] arguments = {"-o=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_IncludeShortOptionWithMultipleValues_ReturnFlattenedArrayWithShortOptionValues() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("option").hasArgs().create());

        String[] arguments = {"-o", "value1", "value2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlatten_IncludeUnknownShortOptionWithStopAtNonOption_ReturnFlattenedArrayWithUnknownShortOptionAndNonOption() {
        Options options = new Options();

        String[] arguments = {"-x", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-x", result[0]);
        assertEquals("nonOption", result[1]);
    }

    @Test
    public void testFlatten_IncludeUnknownShortOptionWithoutStopAtNonOption_ReturnFlattenedArrayWithUnknownShortOption() {
        Options options = new Options();

        String[] arguments = {"-x"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_IncludeSpecialToken_ReturnFlattenedArrayWithSpecialToken() {
        Options options = new Options();

        String[] arguments = {"--"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlatten_IncludeSpecialTokenWithValue_ReturnFlattenedArrayWithSpecialTokenAndValue() {
        Options options = new Options();

        String[] arguments = {"--option=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("option=value", result[1]);
    }

}