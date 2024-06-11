package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_WithArguments_ReturnsFlattenedArray() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Property option for test");
        options.addOption("f", false, "Flag option for test");
        String[] arguments = {"-Dproperty=value", "--", "-f", "test"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(6, flattened.length);
        assertEquals("-D", flattened[0]);
        assertEquals("property=value", flattened[1]);
        assertEquals("--", flattened[2]);
        assertEquals("-f", flattened[3]);
        assertEquals("test", flattened[4]);
        assertEquals("test", flattened[5]);
    }

    @Test
    public void testFlatten_NoOptionForArgument_AddsToProcessedTokensList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-x"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(1, flattened.length);
        assertEquals("-x", flattened[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption_True_ReturnsFlattenedArray() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("f", false, "Flag option for test");
        String[] arguments = {"-Dproperty=value", "--", "-f", "test"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(1, flattened.length);
        assertEquals("-Dproperty=value--f test", flattened[0]);
    }
}