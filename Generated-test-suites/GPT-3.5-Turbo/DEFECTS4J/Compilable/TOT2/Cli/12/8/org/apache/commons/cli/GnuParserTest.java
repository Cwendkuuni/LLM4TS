package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GnuParserTest {

    private GnuParser parser;

    @Before
    public void setUp() {
        parser = new GnuParser();
    }

    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        String[] arguments = {"-a", "arg1", "-b", "arg2", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-a", "arg1", "-b", "arg2", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SpecialCase() {
        Options options = new Options();
        options.addOption("D", true, "Specify a property");
        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-D", "property=value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        Options options = new Options();
        String[] arguments = {"arg1", "--", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"arg1", "--", "arg2"};
        assertArrayEquals(expected, result);
    }
}