package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Test case for normal input
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] arguments = {"-a", "-b"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("Result length", 2, result.length);
        assertEquals("Result element 0", "-a", result[0]);
        assertEquals("Result element 1", "-b", result[1]);
    }
}