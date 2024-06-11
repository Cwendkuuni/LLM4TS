import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class GnuParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("d", "debug", false, "Print debug information.");
        options.addOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
    }

    @After
    public void tearDown() {
        options = null;
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = new String[0];
        String[] expected = new String[0];
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] arguments = new String[]{"-"};
        String[] expected = new String[]{"-"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenOptionWithValue() {
        String[] arguments = new String[]{"-d", "value"};
        String[] expected = new String[]{"-d", "value"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenDoubleOption() {
        String[] arguments = new String[]{"--doubleOption"};
        String[] expected = new String[]{"--doubleOption"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOption() {
        String[] arguments = new String[]{"-longOption"};
        String[] expected = new String[]{"-longOption"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        String[] arguments = new String[]{"-d", "value1", "value2"};
        String[] expected = new String[]{"-d", "value1", "value2"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenOptionWithTwoCharShortcut() {
        String[] arguments = new String[]{"-dc", "value"};
        String[] expected = new String[]{"-d", "c", "value"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenOptionWithTwoCharShortcutAndArgument() {
        String[] arguments = new String[]{"-dc", "value1", "value2"};
        String[] expected = new String[]{"-d", "c", "value1", "value2"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenDoubleDash() {
        String[] arguments = new String[]{"--", "value1", "value2"};
        String[] expected = new String[]{"--", "value1", "value2"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() {
        String[] arguments = new String[]{"-unknowOption", "value"};
        new GnuParser().flatten(options, arguments, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = new String[]{"-d", "option1", "-n", "option2", "nonOption"};
        String[] expected = new String[]{"-d", "option1", "-n", "option2"};
        String[] actual = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, actual);
    }
}