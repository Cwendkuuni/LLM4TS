import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "valueA", "-b", "valueB", "-c", "valueC"};
        
        options.addOption(new Option("a", true, "Option A"));
        options.addOption(new Option("b", true, "Option B"));
        options.addOption(new Option("c", true, "Option C"));
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertTrue(Arrays.asList(result).containsAll(Arrays.asList(arguments)));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"-a", "valueA", "-b", "valueB", "nonOption", "-c", "valueC"};

        options.addOption(new Option("a", true, "Option A"));
        options.addOption(new Option("b", true, "Option B"));
        options.addOption(new Option("c", true, "Option C"));

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertTrue(Arrays.asList(result).contains("-a"));
        assertTrue(Arrays.asList(result).contains("-b"));
        assertTrue(Arrays.asList(result).contains("nonOption"));
        assertFalse(Arrays.asList(result).contains("-c"));
        assertFalse(Arrays.asList(result).contains("valueC"));
    }

    @Test
    public void testFlattenWithSpecialToken() {
        String[] arguments = {"--option1=value1"};

        options.addOption(new Option("option1", true, "Option 1"));

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertTrue(Arrays.asList(result).contains("--option1"));
        assertTrue(Arrays.asList(result).contains("value1"));
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertTrue(Arrays.asList(result).contains("-"));
    }

    @Test
    public void testFlattenWithInvalidOption() {
        String[] arguments = {"-d"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertTrue(Arrays.asList(result).contains("-d"));
    }

    @Test
    public void testFlattenWithBurstToken() {
        String[] arguments = {"-abc"};

        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", false, "Option B"));
        options.addOption(new Option("c", false, "Option C"));

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertTrue(Arrays.asList(result).contains("-a"));
        assertTrue(Arrays.asList(result).contains("-b"));
        assertTrue(Arrays.asList(result).contains("-c"));
    }

    @Test
    public void testFlattenWithBurstTokenAndStopAtNonOption() {
        String[] arguments = {"-abc", "nonOption"};

        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", false, "Option B"));
        options.addOption(new Option("c", false, "Option C"));

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertTrue(Arrays.asList(result).contains("-a"));
        assertTrue(Arrays.asList(result).contains("-b"));
        assertTrue(Arrays.asList(result).contains("-c"));
        assertTrue(Arrays.asList(result).contains("nonOption"));
    }

    @Test
    public void testFlattenWithBurstTokenAndStopAtNonOptionAndRemainingOptions() {
        String[] arguments = {"-abc", "nonOption", "-d", "valueD"};

        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", false, "Option B"));
        options.addOption(new Option("c", false, "Option C"));
        options.addOption(new Option("d", true, "Option D"));

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(6, result.length);
        assertTrue(Arrays.asList(result).contains("-a"));
        assertTrue(Arrays.asList(result).contains("-b"));
        assertTrue(Arrays.asList(result).contains("-c"));
        assertTrue(Arrays.asList(result).contains("nonOption"));
        assertTrue(Arrays.asList(result).contains("-d"));
        assertTrue(Arrays.asList(result).contains("valueD"));
    }
}