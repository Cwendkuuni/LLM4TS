package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_AddOptionToProcessedTokensList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Set a property");

        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlatten_AddTokenToProcessedTokensList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("h", false, "Display help message");

        String[] arguments = {"-h"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-h", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("o", true, "Output file");

        String[] arguments = {"-o", "output.txt", "input.txt"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("output.txt", result[1]);
    }

    @Test
    public void testFlatten_EndsWithDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"--", "-Dproperty=value", "-h"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("--", result[0]);
        assertEquals("-Dproperty=value", result[1]);
        assertEquals("-h", result[2]);
    }

    @Test
    public void testFlatten_SingleHyphenCharacter() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("v", false, "Verbose output");

        String[] arguments = {"-v"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-v", result[0]);
    }

}