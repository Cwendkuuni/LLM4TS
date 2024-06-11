import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.Options;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_LongOptionWithNoValue_Success() {
        options.addOption("a", false, "Option a");

        String[] args = parser.flatten(options, new String[]{"--a"}, false);

        assertEquals(args.length, 1);
        assertEquals(args[0], "--a");
    }

    @Test
    public void testFlatten_LongOptionWithValue_Success() {
        options.addOption("a", true, "Option a");

        String[] args = parser.flatten(options, new String[]{"--a=value"}, false);

        assertEquals(args.length, 2);
        assertEquals(args[0], "--a");
        assertEquals(args[1], "value");
    }

    @Test
    public void testFlatten_LongNonExistentOptionWithStopAtNonOption_Success() {
        String[] args = parser.flatten(options, new String[]{"--b=value"}, true);

        assertEquals(args.length, 2);
        assertEquals(args[0], "--");
        assertEquals(args[1], "--b=value");
    }

    @Test
    public void testFlatten_LongOptionWithValueAndStopAtNonOption_Success() {
        options.addOption("a", true, "Option a");

        String[] args = parser.flatten(options, new String[]{"--a=value", "--b=otherValue"}, true);

        assertEquals(args.length, 3);
        assertEquals(args[0], "--a");
        assertEquals(args[1], "value");
        assertEquals(args[2], "--b=otherValue");
    }

    @Test
    public void testFlatten_SingleHyphen_Success() {
        String[] args = parser.flatten(options, new String[]{"-"}, false);

        assertEquals(args.length, 1);
        assertEquals(args[0], "-");
    }

    @Test
    public void testFlatten_ShortOptionWithNoValue_Success() {
        options.addOption("a", false, "Option a");

        String[] args = parser.flatten(options, new String[]{"-a"}, false);

        assertEquals(args.length, 1);
        assertEquals(args[0], "-a");
    }

    @Test
    public void testFlatten_ShortOptionWithValue_Success() {
        options.addOption("a", true, "Option a");

        String[] args = parser.flatten(options, new String[]{"-a=value"}, false);

        assertEquals(args.length, 2);
        assertEquals(args[0], "-a");
        assertEquals(args[1], "value");
    }

    @Test
    public void testFlatten_ShortNonExistentOptionWithStopAtNonOption_Success() {
        String[] args = parser.flatten(options, new String[]{"-b=value"}, true);

        assertEquals(args.length, 2);
        assertEquals(args[0], "--");
        assertEquals(args[1], "-b=value");
    }

    @Test
    public void testFlatten_ShortOptionWithValueAndStopAtNonOption_Success() {
        options.addOption("a", true, "Option a");

        String[] args = parser.flatten(options, new String[]{"-a=value", "-b=otherValue"}, true);

        assertEquals(args.length, 3);
        assertEquals(args[0], "-a");
        assertEquals(args[1], "value");
        assertEquals(args[2], "-b=otherValue");
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithValue_Success() {
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");

        String[] args = parser.flatten(options, new String[]{"-a=value1", "-b=value2", "-c=value3"}, false);

        assertEquals(args.length, 6);
        assertEquals(args[0], "-a");
        assertEquals(args[1], "value1");
        assertEquals(args[2], "-b");
        assertEquals(args[3], "value2");
        assertEquals(args[4], "-c");
        assertEquals(args[5], "value3");
    }

    @Test
    public void testFlatten_MultipleOptionsWithMixedValuesAndStopAtNonOption_Success() {
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", true, "Option c");

        String[] args = parser.flatten(options, new String[]{"-a=value1", "--b", "-c=value3", "-d=value4"}, true);

        assertEquals(args.length, 6);
        assertEquals(args[0], "-a");
        assertEquals(args[1], "value1");
        assertEquals(args[2], "--");
        assertEquals(args[3], "--b");
        assertEquals(args[4], "-c=value3");
        assertEquals(args[5], "-d=value4");
    }

}