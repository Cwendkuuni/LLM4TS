package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");
        options.addOption("c", "gamma", true, "Gamma option");

        String[] arguments = {"-a", "-b", "value1", "nonOptionValue1", "-c=value2", "--", "nonOptionValue2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("--", result[3]);
        assertEquals("nonOptionValue1", result[4]);
    }

    @Test
    public void testFlatten_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", true, "Beta option");

        String[] arguments = {"-a", "-b", "value1", "nonOptionValue1"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("nonOptionValue1", result[3]);
    }

    @Test
    public void testFlatten_MultipleOptionsInOneToken() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        options.addOption("b", "beta", false, "Beta option");
        options.addOption("c", "gamma", false, "Gamma option");

        String[] arguments = {"-abc"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlatten_SpecialCharacterInOption() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");

        String[] arguments = {"-a=value1", "-c=value2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");

        String[] arguments = {"-", "-", "-a"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-", result[0]);
        assertEquals("-", result[1]);
        assertEquals("-a", result[2]);
    }

    @Test
    public void testFlatten_SingleHyphenWithNonOptionValue() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");

        String[] arguments = {"-", "-", "-a", "value1"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-", result[0]);
        assertEquals("-", result[1]);
        assertEquals("-a", result[2]);
    }

    @Test
    public void testFlatten_InvalidOption() {
        Options options = new Options();

        String[] arguments = {"-x"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_BurstToken() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");

        String[] arguments = {"-abc"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }
}