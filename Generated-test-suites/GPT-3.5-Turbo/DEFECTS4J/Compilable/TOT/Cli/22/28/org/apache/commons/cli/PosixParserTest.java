package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] arguments = {"--foo", "-a", "value"};
        String[] expected = {"--foo", "-a", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] arguments = {"--foo", "-a", "value"};
        String[] expected = {"--", "--foo", "-a", "value"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        Options options = new Options();
        options.addOption("foo", true, "Option foo");
        PosixParser parser = new PosixParser();

        String[] arguments = {"--foo=bar"};
        String[] expected = {"--foo", "bar"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutArg() {
        Options options = new Options();
        options.addOption("foo", false, "Option foo");
        PosixParser parser = new PosixParser();

        String[] arguments = {"--foo"};
        String[] expected = {"--foo"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithoutArg_Exists() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithoutArg_NotExists_StopAtNonOptionTrue() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a"};
        String[] expected = {"--", "-a"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithoutArg_NotExists_StopAtNonOptionFalse() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithArg_Exists() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithArg_NotExists_StopAtNonOptionTrue() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value"};
        String[] expected = {"--", "-a", "value"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithArg_NotExists_StopAtNonOptionFalse() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}