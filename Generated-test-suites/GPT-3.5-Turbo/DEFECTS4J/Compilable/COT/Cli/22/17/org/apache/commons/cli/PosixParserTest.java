package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.cli.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        arguments = new String[] {};
    }

    @Test
    public void testFlattenNoOptions() {
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenSingleOption() {
        options.addOption("a", false, "Option a");

        arguments = new String[] {"-a"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlattenMultipleOptions() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        arguments = new String[] {"-a", "-b", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenNonOptionToken() {
        arguments = new String[] {"token"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("token", result[0]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        options.addOption("a", false, "Option a");

        arguments = new String[] {"-a", "token"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlattenBurstToken() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        arguments = new String[] {"-abc"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("c", result[2]);
    }

    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        options.addOption("a", false, "Option a");

        arguments = new String[] {"-abc", "token"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-abc", result[1]);
    }

    @Test
    public void testFlattenLongOption() {
        options.addOption("a", false, "Option a");

        arguments = new String[] {"--option"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        options.addOption("a", false, "Option a");

        arguments = new String[] {"--option=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenLongOptionNotExist() {
        arguments = new String[] {"--option"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlattenLongOptionNotExistStopAtNonOption() {
        arguments = new String[] {"--option", "token"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("--option", result[1]);
    }

}