import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        options.addOption(Option.builder("a").argName("arg").desc("Option a").build());
        options.addOption(Option.builder("b").desc("Option b").build());
        options.addOption(Option.builder("c").desc("Option c").build());
    }

    @Test
    public void testFlatten_typicalUseCase() {
        String[] args = {"-a", "value1", "-b", "-c", "value2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlatten_noOptions() {
        String[] args = {"value1", "value2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] args = {"-a", "value1", "nonoption", "-c", "value2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_longOptionWithValue() {
        String[] args = {"--a=value1"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("--a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_singleHyphen() {
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_shortOptionWithoutValue() {
        String[] args = {"-b"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_shortOptionWithInvalidValue() {
        String[] args = {"-a=invalid"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("invalid", result[1]);
    }

    @Test
    public void testFlatten_shortOptionWithMultipleValues() {
        String[] args = {"-a", "value1", "value2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlatten_shortOptionNotExisting() {
        String[] args = {"-d"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(1, result.length);
        assertEquals("-d", result[0]);
    }

    @Test
    public void testFlatten_shortOptionNotExisting_stopAtNonOption() {
        String[] args = {"-d", "value1", "value2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-d", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlatten_burstToken() {
        String[] args = {"-ab"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("b", result[2]);
    }

    @Test
    public void testFlatten_burstToken_invalidOption_stopAtNonOption() {
        String[] args = {"-ad"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-d", result[1]);
        assertEquals("d", result[2]);
    }

    @Test
    public void testFlatten_burstToken_invalidOption() {
        String[] args = {"-ad"};
        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-ad", result[0]);
    }

    @Test
    public void testFlatten_invalidOptionsAfterStopAtNonOption() {
        String[] args = {"-a", "value1", "nonoption", "-d", "value2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("nonoption", result[2]);
    }

    @Test
    public void testFlatten_multipleStopAtNonOption() {
        String[] args = {"-a", "value1", "nonoption", "-c", "value2", "nonoption2"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("nonoption", result[2]);
    }

}