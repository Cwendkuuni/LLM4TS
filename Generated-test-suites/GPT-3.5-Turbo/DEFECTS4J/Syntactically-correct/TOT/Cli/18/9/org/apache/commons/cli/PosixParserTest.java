import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption"};
        String[] expected = {"-a", "value1", "-b", "value2", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption"};
        String[] expected = {"-a", "value1", "-b", "value2", "nonOption"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SpecialToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"--special=value"};
        String[] expected = {"--special", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-d", "value"};
        String[] expected = {"--", "-d", "value"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-d", "value"};
        String[] expected = {"-d", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-abvalue"};
        String[] expected = {"-a", "bvalue"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-abvalue", "nonOption"};
        String[] expected = {"-a", "bvalue", "nonOption"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NoRemainingTokens() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, argument, false);

        assertArrayEquals(expected, result);
    }
}