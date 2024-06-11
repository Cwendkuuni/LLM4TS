package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.GnuParser;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        String[] arguments = {"-Dproperty=value", "-property=value", "--", "-abc", "def"};
        Options options = new Options(); // Initialize Options
        Parser parser = new GnuParser(); // Initialize Parser
        String[] expected = {"-D", "property", "value", "--", "-", "abc", "def"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }
}