package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class GnuParserTest {

    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();
        String[] args = {"arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        String[] args = {"-t", "--", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"-t", "--", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_WithOptions() {
        Options options = new Options();
        options.addOption("t", false, "test option");
        String[] args = {"-t", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(args, result);
    }

    @Test
    public void testFlatten_SpecialPropertiesOption() {
        Options options = new Options();
        String[] args = {"-Dproperty=value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-D", "property=value"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("t", false, "test option");
        options.addOption("s", false, "another option");
        String[] args = {"-t", "--", "-s", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-t", "--", "-s", "arg1", "arg2"}, result);
    }
}