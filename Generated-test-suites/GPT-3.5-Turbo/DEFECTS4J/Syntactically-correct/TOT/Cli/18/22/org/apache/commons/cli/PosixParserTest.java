import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        String[] arguments = {"-a", "value1", "arg1"};

        options.addOption(Option.builder("a")
                .argName("arg")
                .hasArg()
                .build());

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        String[] arguments = {"-a", "value1", "arg1"};

        options.addOption(Option.builder("a")
                .argName("arg")
                .hasArg()
                .build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlatten_TokenStartsWithDoubleHyphen() {
        String[] arguments = {"--option=value"};

        options.addOption(Option.builder("option")
                .hasArg()
                .build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_SingleHyphenToken() {
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOptionToken() {
        String[] arguments = {"-a"};

        options.addOption(Option.builder("a").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOptionToken_StopAtNonOptionTrue() {
        String[] arguments = {"-a"};

        options.addOption(Option.builder("a").build());

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOptionToken_StopAtNonOptionFalse() {
        String[] arguments = {"-a"};

        options.addOption(Option.builder("b").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_MultiCharacterOptionToken() {
        String[] arguments = {"--option"};

        options.addOption(Option.builder("option").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlatten_MultiCharacterOptionToken_StopAtNonOptionTrue() {
        String[] arguments = {"--option"};

        options.addOption(Option.builder("option").build());

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlatten_MultiCharacterOptionToken_StopAtNonOptionFalse() {
        String[] arguments = {"--option"};

        options.addOption(Option.builder("other").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlatten_BurstToken() {
        String[] arguments = {"-abc"};

        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOptionTrue() {
        String[] arguments = {"-abc"};

        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOptionFalse() {
        String[] arguments = {"-abc"};

        options.addOption(Option.builder("option").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-abc", result[0]);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOptionFalse_AdditionalArguments() {
        String[] arguments = {"-abc", "arg1"};

        options.addOption(Option.builder("option").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-abc", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_BurstToken_InvalidOption() {
        String[] arguments = {"-abcd"};

        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-abcd", result[0]);
    }

    @Test
    public void testFlatten_BurstToken_InvalidOption_StopAtNonOptionTrue() {
        String[] arguments = {"-abcd"};

        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-abcd", result[0]);
    }

    @Test
    public void testFlatten_BurstToken_InvalidOption_StopAtNonOptionFalse() {
        String[] arguments = {"-abcd"};

        options.addOption(Option.builder("option").build());

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-abcd", result[0]);
    }
}