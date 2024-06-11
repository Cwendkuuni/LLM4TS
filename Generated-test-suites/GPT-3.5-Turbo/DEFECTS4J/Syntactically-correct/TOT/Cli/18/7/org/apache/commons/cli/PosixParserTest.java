import org.apache.commons.cli.*;
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
    public void testFlatten_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a", "arg1", "--", "-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a", "arg1", "--", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--", result[2]);
        assertEquals("-b", result[3]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ExistingOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_ExistingOptionWithArg() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a", "arg1"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_NotExistingOption_StopAtNonOptionTrue() {
        Options options = new Options();

        String[] arguments = {"-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_NotExistingOption_StopAtNonOptionFalse() {
        Options options = new Options();

        String[] arguments = {"-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_NotExistingOption_Bursting() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-abc"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("c", result[2]);
    }

    @Test
    public void testFlatten_NotExistingOption_StopAtNonOptionTrue_Bursting() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-abc"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-abc", result[0]);
    }
}