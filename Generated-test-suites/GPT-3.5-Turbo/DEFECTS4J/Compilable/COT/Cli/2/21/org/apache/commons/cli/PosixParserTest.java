package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value", "--", "-c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--", result[3]);
        assertEquals("-c", result[4]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value", "--", "-c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--", result[3]);
        assertEquals("-c", result[4]);
    }

    @Test
    public void testFlattenOptionWithoutArg() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--", result[3]);
        assertEquals("non-option", result[4]);
    }

    @Test
    public void testFlattenNonOptionNoStop() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("non-option", result[3]);
    }

    @Test
    public void testFlattenOptionBurst() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-abvalue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenOptionBurstStop() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-abvalue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenOptionBurstNoStop() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-abvalue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-abvalue", result[0]);
    }

    @Test
    public void testFlattenStopAfterNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value", "non-option", "-c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

}