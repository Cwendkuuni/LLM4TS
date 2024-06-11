import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption_EmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_StopAtNonOption_NonOptionArgument() {
        Options options = new Options();
        String[] arguments = {"arg1"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("arg1", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption_LongOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("option").hasArg().build());
        String[] arguments = {"--option=value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOption_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("option").build());
        String[] arguments = {"--option"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ShortOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ShortOptionWithoutValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_LongOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("option").hasArg().build());
        String[] arguments = {"--option=value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("option").build());
        String[] arguments = {"--option"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_ShortOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_ShortOptionWithoutValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-ab"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlatten_BurstToken_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-ab"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-ab", result[0]);
    }

    @Test
    public void testFlatten_IgnoreHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_IgnoreDoubleHyphen() {
        Options options = new Options();
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlatten_EatTheRest() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"arg1", "-a"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("arg1", result[2]);
    }

}