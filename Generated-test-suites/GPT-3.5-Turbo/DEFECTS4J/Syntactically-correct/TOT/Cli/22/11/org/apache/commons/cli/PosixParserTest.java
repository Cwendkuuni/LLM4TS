import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    Options options;
    PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_LongOptionWithoutValue_Success() {
        options.addOption(OptionBuilder.withLongOpt("foo").create());
        String[] arguments = {"--foo"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertNull(result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithValue_Success() {
        options.addOption(OptionBuilder.withLongOpt("foo").hasArg().create());
        String[] arguments = {"--foo=bar"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue_UnknownOption_Success() {
        String[] arguments = {"--foo"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("--foo", result[1]);
    }

    @Test
    public void testFlatten_SingleHyphen_Success() {
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ShortOption_Success() {
        options.addOption(OptionBuilder.withLongOpt("foo").create());
        String[] arguments = {"-f"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOption_Success() {
        String[] arguments = {"-f"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-f", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithExtraCharacters_Success() {
        options.addOption(OptionBuilder.withLongOpt("foo").hasArg().create());
        String[] arguments = {"--foo=bar-extra"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar-extra", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOption_Success() {
        String[] arguments = {"--foo", "-f", "non-option"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(4, result.length);
        assertEquals("--", result[0]);
        assertEquals("--foo", result[1]);
        assertEquals("-f", result[2]);
        assertEquals("non-option", result[3]);
    }

    @Test
    public void testFlatten_EatTheRest_Success() {
        String[] arguments = {"--foo", "-f", "non-option"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(5, result.length);
        assertEquals("--foo", result[0]);
        assertNull(result[1]);
        assertEquals("-f", result[2]);
        assertEquals("non-option", result[3]);
        assertEquals("--", result[4]);
    }
}