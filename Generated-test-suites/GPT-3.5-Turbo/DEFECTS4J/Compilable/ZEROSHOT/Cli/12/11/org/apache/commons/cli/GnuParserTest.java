package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        arguments = new String[0];
    }

    @Test
    public void testFlatten_noArguments() {
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(0, flattened.length);
    }

    @Test
    public void testFlatten_singleArgument() {
        arguments = new String[]{"-h"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(1, flattened.length);
        assertEquals("-h", flattened[0]);
    }

    @Test
    public void testFlatten_multipleArguments() {
        arguments = new String[]{"-a", "--option", "value", "file.txt"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--option", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("file.txt", flattened[3]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        options.addOption("a", false, "option a");

        arguments = new String[]{"-a", "file.txt", "-b", "-c"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertNotNull(flattened);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("file.txt", flattened[1]);
    }

    @Test
    public void testFlatten_doubleDash() {
        arguments = new String[]{"-a", "--", "-b", "--option", "value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("--option", flattened[3]);
        assertEquals("value", flattened[4]);
    }

    @Test
    public void testFlatten_singleDash() {
        arguments = new String[]{"-", "-a", "-b"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(3, flattened.length);
        assertEquals("-", flattened[0]);
        assertEquals("-a", flattened[1]);
        assertEquals("-b", flattened[2]);
    }

    @Test
    public void testFlatten_unknownOption() {
        arguments = new String[]{"-a", "--option", "value", "-x"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--option", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("-x", flattened[3]);
    }

    @Test
    public void testFlatten_specialOption() {
        options.addOption("D", true, "Define property");

        arguments = new String[]{"-Dproperty=value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(2, flattened.length);
        assertEquals("-D", flattened[0]);
        assertEquals("property=value", flattened[1]);
    }

    @Test
    public void testFlatten_eatTheRest() {
        arguments = new String[]{"-a", "file.txt", "--", "-b", "--option", "value"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertNotNull(flattened);
        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("file.txt", flattened[1]);
        assertEquals("--", flattened[2]);
        assertEquals("-b", flattened[3]);
        assertEquals("--option", flattened[4]);
    }

}