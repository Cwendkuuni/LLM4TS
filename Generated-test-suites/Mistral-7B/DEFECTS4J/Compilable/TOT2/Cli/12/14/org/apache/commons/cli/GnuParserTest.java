package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "--foo", "-bar"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        String[] expected = new String[]{"-D", "property", "value", "--", "foo", "-bar"};
        assertArrayEquals(expected, flattenedArgs);
    }
}