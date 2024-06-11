package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", null, false, "");
        options.addOption("b", null, true, "");
        options.addOption("c", null, false, "");
        options.addOption("d", null, true, "");

        String[] args = {"-a", "-b", "value", "-c", "xnyz"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("xnyz", result[4]);
    }

    @Test
    public void testFlatten_ContinueAtNonOption() {
        Options options = new Options();
        options.addOption("a", null, false, "");

        String[] args = {"-a", "value", "-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "");
        options.addOption("b", "beta", true, "");
        options.addOption("c", "charlie", false, "");

        String[] args = {"-a", "--beta", "-c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--beta", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("charlie", result[3]);
        assertEquals("charlie", result[4]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "");
        options.addOption("b", "beta", true, "");
        options.addOption("c", "charlie", false, "");

        String[] args = {"--alpha", "--beta=value", "--charlie"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("--alpha", result[0]);
        assertEquals("--beta", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--charlie", result[3]);
        assertEquals("charlie", result[4]);
    }

    @Test
    public void testFlatten_HyphenOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", null, false, "");
        options.addOption("b", null, true, "");
        options.addOption("c", null, false, "");

        String[] args = {"-", "-b", "-c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("-", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("c", result[3]);
        assertEquals("c", result[4]);
    }

    @Test
    public void testFlatten_HyphenOptionWithValue() {
        Options options = new Options();
        options.addOption("a", null, false, "");
        options.addOption("b", null, true, "");
        options.addOption("c", null, false, "");

        String[] args = {"-a", "-b=value", "-c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("c", result[4]);
    }

    @Test
    public void testFlatten_MixedOptions() {
        Options options = new Options();
        options.addOption("a", null, false, "");
        options.addOption("b", null, true, "");
        options.addOption("c", null, false, "");

        String[] args = {"-a", "--beta=value", "-c", "-d"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--beta", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--beta", result[3]);
        assertEquals("value", result[4]);
        assertEquals("-c", result[5]);
        assertEquals("c", result[6]);
    }

    @Test
    public void testFlatten_NullArgs() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, null, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[]{}, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_NoArgs() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[]{"value"}, false);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }
}