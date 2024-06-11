package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_basicScenario() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "--foo", "bar"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "--foo", "bar"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_withDoubleHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"--", "--foo", "bar"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"--", "--foo", "bar"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_withSingleHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-", "--foo", "bar"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-", "--foo", "bar"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_withOptionWithValue() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        String[] arguments = {"-a=value", "--foo", "bar"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a=value", "--foo", "bar"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "--foo", "bar", "nonOption1", "nonOption2"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-a", "--foo", "bar", "nonOption1", "nonOption2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialPropertiesOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo", "bar"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-D", "property=value", "--foo", "bar"};
        assertArrayEquals(expected, result);
    }
}