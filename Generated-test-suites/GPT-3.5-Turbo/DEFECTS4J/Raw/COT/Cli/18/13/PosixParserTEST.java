import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());

        String[] arguments = {"-a", "value", "-b"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("-b", flattened[2]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());

        String[] arguments = {"-a", "value", "-b", "nonOption"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("-b", flattened[2]);
    }

    @Test
    public void testFlattenWithHyphen() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a", "-"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-", flattened[1]);
    }

    @Test
    public void testFlattenWithSpecialTokens() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        String[] arguments = {"--test", "--test=value"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(2, flattened.length);
        assertEquals("--test", flattened[0]);
        assertEquals("--test=value", flattened[1]);
    }

    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a", "value", "nonOption"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("--", flattened[2]);
        assertEquals("nonOption", flattened[3]);
    }

    @Test
    public void testFlattenWithNonOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a", "value", "nonOption"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlattenWithNonOptionAndStopAtNonOptionAndHyphen() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a", "value", "-", "nonOption"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("-", flattened[2]);
    }

    @Test
    public void testFlattenWithNonOptionAndStopAtNonOptionAndSpecialToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a", "value", "--nonOption"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
    }
}