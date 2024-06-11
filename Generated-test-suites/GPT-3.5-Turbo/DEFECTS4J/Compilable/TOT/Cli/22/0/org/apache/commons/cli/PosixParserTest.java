package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setup() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_ValidOptions_ReturnFlattenedArray(){
        Options options = new Options();
        options.addOption("f", false, "First option");
        options.addOption("s", true, "Second option");

        String[] arguments = {"-f", "-s", "value"};

        String[] result = posixParser.flatten(options, arguments, true);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-s", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnFlattenedArray(){
        Options options = new Options();
        options.addOption("f", false, "First option");
        options.addOption("s", true, "Second option");

        String[] arguments = {"-f", "-s", "value", "nonOption"};

        String[] result = posixParser.flatten(options, arguments, true);

        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-s", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlatten_IncludeRemainingOptions_ReturnFlattenedArray(){
        Options options = new Options();
        options.addOption("f", false, "First option");
        options.addOption("s", true, "Second option");

        String[] arguments = {"-f", "-s", "value", "nonOption"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(6, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-s", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
        assertEquals("--", result[4]);
        assertEquals("nonOption", result[5]);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_ReturnFlattenedArray(){
        Options options = new Options();
        options.addOption("f", false, "First option");
        options.addOption("s", true, "Second option");

        String[] arguments = {"-f", "-x"};

        String[] result = posixParser.flatten(options, arguments, true);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-x", result[1]);
    }

}