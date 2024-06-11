
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;
    private String[] arguments;

    @Test
    public void testFlatten_StopAtNonOption_EqualsTrue() {
        parser = new PosixParser();
        options = new Options();
        arguments = new String[] {"-a", "value1", "nonOption1", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[] {"-a", "value1", "nonOption1", "-b", "value2"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_EqualsFalse() {
        parser = new PosixParser();
        options = new Options();
        arguments = new String[] {"-a", "value1", "nonOption1", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithLongOption_EqualsTrue() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        arguments = new String[] {"--option1", "--option2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithLongOption_EqualsFalse() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        arguments = new String[] {"--option1", "--option2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"--option1", "--option2"}, result);
    }

    @Test
    public void testFlatten_WithShortOptionAndArg_EqualsTrue() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", true, "");
        options.addOption("b", true, "");
        arguments = new String[] {"-a", "value1", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithShortOptionAndArg_EqualsFalse() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", true, "");
        options.addOption("b", true, "");
        arguments = new String[] {"-a", "value1", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[] {"-a", "value1", "-b", "value2"}, result);
    }

    @Test
    public void testFlatten_WithShortOptionAndArg_MissingArg() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", true, "");
        options.addOption("b", true, "");
        arguments = new String[] {"-a", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }
}
