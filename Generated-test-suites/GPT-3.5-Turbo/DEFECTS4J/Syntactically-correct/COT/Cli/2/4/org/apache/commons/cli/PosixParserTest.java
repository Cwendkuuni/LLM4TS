import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

public class PosixParserTest {

    private PosixParser parser;
  
    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("a", "test option a"));
        options.addOption(new Option("b", true, "test option b with value"));
        String[] arguments = { "-a", "-b", "value" };

        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals("Incorrect number of tokens", 3, flattened.length);
        assertEquals("Incorrect token", "-a", flattened[0]);
        assertEquals("Incorrect token", "-b", flattened[1]);
        assertEquals("Incorrect token", "value", flattened[2]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", "test option a"));
        options.addOption(new Option("b", true, "test option b with value"));
        String[] arguments = { "-a", "-b", "value", "non-option" };

        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals("Incorrect number of tokens", 3, flattened.length);
        assertEquals("Incorrect token", "-a", flattened[0]);
        assertEquals("Incorrect token", "-b", flattened[1]);
        assertEquals("Incorrect token", "value", flattened[2]);
    }

    @Test
    public void testFlattenBurstToken() {
        Options options = new Options();
        options.addOption(new Option("a", "test option a"));
        options.addOption(new Option("b", true, "test option b with value"));
        options.addOption(new Option("c", false, "test option c"));
        String[] arguments = { "-abc", "value" };

        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals("Incorrect number of tokens", 4, flattened.length);
        assertEquals("Incorrect token", "-a", flattened[0]);
        assertEquals("Incorrect token", "-b", flattened[1]);
        assertEquals("Incorrect token", "-c", flattened[2]);
        assertEquals("Incorrect token", "value", flattened[3]);
    }

    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", "test option a"));
        options.addOption(new Option("b", true, "test option b with value"));
        options.addOption(new Option("c", false, "test option c"));
        String[] arguments = { "-abc", "value", "non-option" };

        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals("Incorrect number of tokens", 4, flattened.length);
        assertEquals("Incorrect token", "-a", flattened[0]);
        assertEquals("Incorrect token", "-b", flattened[1]);
        assertEquals("Incorrect token", "-c", flattened[2]);
        assertEquals("Incorrect token", "value", flattened[3]);
    }

    @Test
    public void testFlattenSpecialToken() {
        Options options = new Options();
        options.addOption(new Option("a", "test option a"));
        options.addOption(new Option("b", true, "test option b with value"));
        String[] arguments = { "--special=value" };

        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals("Incorrect number of tokens", 1, flattened.length);
        assertEquals("Incorrect token", "--special", flattened[0]);
    }

    @Test
    public void testFlattenHyphenToken() {
        Options options = new Options();
        options.addOption(new Option("a", "test option a"));
        options.addOption(new Option("b", true, "test option b with value"));
        String[] arguments = { "-" };

        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals("Incorrect number of tokens", 1, flattened.length);
        assertEquals("Incorrect token", "-", flattened[0]);
    }
}