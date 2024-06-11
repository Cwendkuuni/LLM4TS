
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());
        options.addOption(Option.builder("d").hasArg().build());

        String[] args = { "-a", "arg1", "-b", "arg2", "--", "nonOption" };
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());
        options.addOption(Option.builder("d").hasArg().build());

        String[] args = { "-a", "arg1", "-b", "arg2", "--", "nonOption" };
        String[] result = parser.flatten(options, args, false);

        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
        assertEquals("--", result[4]);
        assertEquals("nonOption", result[5]);
        assertEquals("nonOption", result[6]);
    }

    @Test
    public void testFlatten_HyphenOption() {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").hasArg().build());

        String[] args = { "-", "-a", "arg1", "-b", "arg2", "-c" };
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("-", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlatten_ExistingOption() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());
        options.addOption(Option.builder("d").hasArg().build());

        String[] args = { "-a", "-b", "arg1", "-c", "-d", "arg2" };
        String[] result = parser.flatten(options, args, false);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("-d", result[4]);
        assertEquals("arg2", result[5]);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOptionTrue() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] args = { "-a", "-x", "arg1", "-c" };
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-x", result[1]);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOptionFalse() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] args = { "-a", "-x", "arg1", "-c" };
        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-x", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("-c", result[3]);
    }

    @Test
    public void testFlatten_EmptyArgs() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] args = {};
        String[] result = parser.flatten(options, args, false);

        assertEquals(0, result.length);
    }

    @Test(expected = NullPointerException.class)
    public void testFlatten_NullOptions() {
        String[] args = { "-a", "arg1" };
        parser.flatten(null, args, false);
    }

    @Test(expected = NullPointerException.class)
    public void testFlatten_NullArgs() {
        parser.flatten(options, null, false);
    }
}
