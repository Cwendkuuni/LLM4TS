import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser gnuParser;
    private Options options;

    @Before
    public void setUp() {
        gnuParser = new GnuParser();
        options = new Options();
    }

    @After
    public void tearDown() {
        gnuParser = null;
        options = null;
    }

    @Test
    public void testFlatten_WithNullOptions_ReturnsEmptyArray() {
        String[] arguments = {"-a", "-b", "value"};

        String[] result = gnuParser.flatten(null, arguments, false);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_WithNullArguments_ReturnsEmptyArray() {
        options.addOption("a", "optionA", false, "Option A");

        String[] result = gnuParser.flatten(options, null, false);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue_StopsAtNonOption() {
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        String[] arguments = {"-a", "nonOption", "-b"};

        String[] result = gnuParser.flatten(options, arguments, true);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse_DoesNotStopAtNonOption() {
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        String[] arguments = {"-a", "nonOption", "-b"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_WhenDoubleDash_AddsDoubleDashToResult() {
        options.addOption("a", "optionA", false, "Option A");

        String[] arguments = {"-a", "--", "-b"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_WhenHyphen_AddsHyphenToResult() {
        options.addOption("a", "optionA", false, "Option A");

        String[] arguments = {"-a", "-", "-b"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_WhenOptionExists_AddsOptionToResult() {
        options.addOption("a", "optionA", false, "Option A");

        String[] arguments = {"-a", "-b"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_WhenOptionDoesNotExist_AddsOptionToResult() {
        options.addOption("a", "optionA", false, "Option A");

        String[] arguments = {"-c"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-c", result[0]);
    }

    @Test
    public void testFlatten_WhenOptionIsSpecialProperty_AddsOptionAndValueToResult() {
        options.addOption(Option.builder("D").hasArg().build());

        String[] arguments = {"-Dproperty=value"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlatten_WhenOptionIsSpecialPropertyWithDoubleDash_AddsOptionAndValueToResult() {
        options.addOption(Option.builder("D").hasArg().build());

        String[] arguments = {"--Dproperty=value"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("--D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlatten_WhenOptionIsSpecialPropertyWithHyphen_AddsOptionAndValueToResult() {
        options.addOption(Option.builder("D").hasArg().build());

        String[] arguments = {"-D-property=value"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("-property=value", result[1]);
    }

}