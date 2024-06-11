package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testFlattenWithSimpleArguments() throws ParseException {
        String[] arguments = new String[]{"arg1", "arg2", "--opt1", "-Dprop=val"};
        String[] expected = new String[]{"arg1", "--opt1", "-D", "prop", "val", "arg2"};
        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, false));
    }
}