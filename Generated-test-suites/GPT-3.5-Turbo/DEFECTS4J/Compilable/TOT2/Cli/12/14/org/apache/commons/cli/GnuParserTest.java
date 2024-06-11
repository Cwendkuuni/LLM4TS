package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_NoOptions() {
        GnuParser gnuParser = new GnuParser();
        String[] arguments = {"arg1", "arg2"};
        String[] result = gnuParser.flatten(new Options(), arguments, false);
        
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        GnuParser gnuParser = new GnuParser();
        Options options = new Options();
        options.addOption("o", false, "Option o");
        
        String[] arguments = {"-o", "value1", "arg2"};
        String[] result = gnuParser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[]{"-o", "value1", "arg2"}, result);
    }

    @Test
    public void testFlatten_WithSpecialOption() {
        GnuParser gnuParser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Special option D");
        
        String[] arguments = {"-Dproperty=value"};
        String[] result = gnuParser.flatten(options, arguments, false);
        
        assertArrayEquals(new String[]{"-D", "property=value"}, result);
    }

    @Test
    public void testFlatten_WithStopAtNonOption() {
        GnuParser gnuParser = new GnuParser();
        Options options = new Options();
        options.addOption("o", false, "Option o");
        
        String[] arguments = {"-o", "-notAnOption", "arg2"};
        String[] result = gnuParser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[]{"-o", "-notAnOption", "arg2"}, result);
    }

    @Test
    public void testFlatten_WithDoubleDash() {
        GnuParser gnuParser = new GnuParser();
        Options options = new Options();
        
        String[] arguments = {"arg1", "--", "-o", "value"};
        String[] result = gnuParser.flatten(options, arguments, false);
        
        assertArrayEquals(new String[]{"arg1", "--", "-o", "value"}, result);
    }
}