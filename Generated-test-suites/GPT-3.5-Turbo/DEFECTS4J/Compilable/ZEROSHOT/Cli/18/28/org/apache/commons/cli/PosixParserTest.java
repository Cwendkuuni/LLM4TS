package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testFlattenWithEmptyOptions() {
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlattenWithOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A with argument");
        String[] arguments = {"-a", "value", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlattenWithOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A with multiple arguments");
        String[] arguments = {"-a", "value1", "value2", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlattenWithBursting() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] arguments = {"-ab", "arg1", "-c", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("arg2", result[4]);
    }

}