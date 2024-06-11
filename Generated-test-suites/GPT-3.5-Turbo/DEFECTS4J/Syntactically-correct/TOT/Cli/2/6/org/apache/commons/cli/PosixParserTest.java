import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_OptionsWithValidArguments_Success() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "arg1", "-b"};
        String[] expected = {"-a", "arg1", "-b"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionsWithInvalidArguments_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "arg1", "-b", "-d"};
        String[] expected = {"-a", "arg1", "-b", "-d"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionsWithInvalidArguments_ContinueFlattening() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "arg1", "-b", "-d"};
        String[] expected = {"-a", "arg1", "-b", "-d"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphenToken_Success() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SpecialToken_Success() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"--option1", "--option2=value"};
        String[] expected = {"--option1", "--option2=value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOptionMatch_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "arg1", "-f", "-b"};
        String[] expected = {"-a", "arg1", "--", "-f", "-b"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOptionMatch_ContinueFlattening() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "arg1", "-f", "-b"};
        String[] expected = {"-a", "arg1", "-f", "-b"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOptionMatch_StopAtNonOption_NoRemainingTokens() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "arg1", "arg2"};
        String[] expected = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptionsInOneToken_Success() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}