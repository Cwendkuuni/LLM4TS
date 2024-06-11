package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        String[] arguments = {"-Dproperty=value", "--option", "-flag", "argument"};
        Options options = new Options();

        String[] expected = {"-D", "property", "value", "--option", "-flag"};
        String[] result = new GnuParser().flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }
}