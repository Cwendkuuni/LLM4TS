package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        Options options = new Options();
        String[] expected = new String[0];
        String[] actual = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }
}