package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--foo", "-o", "optA", "-o", "optB", "--bar=value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "-o", "optA", "-o", "optB", "--bar=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}