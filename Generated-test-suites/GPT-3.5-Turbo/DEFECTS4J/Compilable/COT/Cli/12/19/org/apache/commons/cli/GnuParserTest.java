package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    private GnuParser parser;

    @Before
    public void setUp() {
        parser = new GnuParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "-b=value", "--option", "--", "-c"};

        String[] flattened = parser.flatten(options, arguments, false);

        String[] expected = new String[]{"-a", "-b=value", "--option", "--", "-c"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenWithNonOptionStop() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "-b=value", "--option", "--", "-c"};

        String[] flattened = parser.flatten(options, arguments, true);

        String[] expected = new String[]{"-a", "-b=value", "--option", "--"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenWithHyphen() {
        Options options = new Options();
        String[] arguments = new String[]{"-", "--option", "-a"};

        String[] flattened = parser.flatten(options, arguments, false);

        String[] expected = new String[]{"-", "--option", "-a"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        Options options = new Options();
        String[] arguments = new String[]{"--", "--option"};

        String[] flattened = parser.flatten(options, arguments, false);

        String[] expected = new String[]{"--", "--option"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenWithOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = new String[]{"-a", "-b=value", "--option"};

        String[] flattened = parser.flatten(options, arguments, false);

        String[] expected = new String[]{"-a", "-b=value", "--option"};
        assertArrayEquals(expected, flattened);
    }
}
