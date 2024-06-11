
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlattenNoOptions() {
        String[] input = new String[] {"foo", "bar"};
        String[] expected = new String[] {"foo", "bar"};

        String[] result = parser.flatten(options, input, false);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithNoArg() {
        options.addOption(new Option("f", false, "test option"));

        String[] input = new String[] {"foo", "-f", "bar"};
        String[] expected = new String[] {"foo", "-f", "bar"};

        String[] result = parser.flatten(options, input, false);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithArg() {
        options.addOption(new Option("f", true, "test option"));

        String[] input = new String[] {"foo", "-f", "value", "bar"};
        String[] expected = new String[] {"foo", "-f", "value", "bar"};

        String[] result = parser.flatten(options, input, false);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithArgs() {
        options.addOption(new Option("f", true, "test option"));

        String[] input = new String[] {"foo", "-f", "value1", "value2", "bar"};
        String[] expected = new String[] {"foo", "-f", "value1", "value2", "bar"};

        String[] result = parser.flatten(options, input, false);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithArgStopAtNonOption() {
        options.addOption(new Option("f", true, "test option"));

        String[] input = new String[] {"foo", "-f", "value", "bar"};
        String[] expected = new String[] {"foo", "-f", "value"};

        String[] result = parser.flatten(options, input, true);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithArgsStopAtNonOption() {
        options.addOption(new Option("f", true, "test option"));

        String[] input = new String[] {"foo", "-f", "value1", "value2", "bar"};
        String[] expected = new String[] {"foo", "-f", "value1", "value2"};

        String[] result = parser.flatten(options, input, true);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithArgAfterNonOption() {
        options.addOption(new Option("f", true, "test option"));

        String[] input = new String[] {"foo", "-f", "value", "bar", "-f", "otherValue"};
        String[] expected = new String[] {"foo", "-f", "value", "bar"};

        String[] result = parser.flatten(options, input, false);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithArgsAfterNonOption() {
        options.addOption(new Option("f", true, "test option"));

        String[] input = new String[] {"foo", "-f", "value1", "value2", "bar", "-f", "otherValue"};
        String[] expected = new String[] {"foo", "-f", "value1", "value2", "bar"};

        String[] result = parser.flatten(options, input, false);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithArgBurst() {
        options.addOption(new Option("f", true, "test option"));

        String[] input = new String[] {"foo", "-fg", "value", "bar"};
        String[] expected = new String[] {"foo", "-f", "-g", "value", "bar"};

        String[] result = parser.flatten(options, input, false);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testFlattenOptionWithArgsBurst() {
        options.addOption(new Option("f", true, "test option"));

        String[] input = new String[] {"foo", "-fg", "value1", "value2", "bar"};
        String[] expected = new String[] {"foo", "-f", "-g", "value1", "value2", "bar"};

        String[] result = parser.flatten(options, input, false);

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testGobble() {
        String[] input = new String[] {"foo", "bar"};
        String[] expected = new String[] {"foo", "bar"};

        parser.flatten(options, input, true);

        String[] result = parser.gobble();

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testGobbleEmpty() {
        String[] expected = new String[] {};

        String[] result = parser.gobble();

        assertTrue(Arrays.equals(expected, result));
    }

    @Test
    public void testInit() {
        parser.eatTheRest = true;
        parser.tokens = new ArrayList<>(Arrays.asList("foo", "bar"));
        parser.currentOption = new Option("f");

        parser.init();

        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }

}
