package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private static final String[] ARGS = {"--foo=bar", "--qux", "-a", "-b", "-c--d", "-e", "--f=quux", "--g=quux", "--h=quux", "--"};
    private static final String[] ARGS2 = {"--foo=bar", "--qux", "-a", "-b", "--", "-c--d", "-e", "--f=quux", "--g=quux", "--h=quux", "quuz"};
    private static final String[] ARGS3 = {"--foo=bar", "--qux", "-a", "-b", "-c--d", "--"};
    private static final String[] ARGS4 = {"--foo=bar", "--qux", "-a", "--"};
    private static final String[] ARGS5 = {"--", "--foo=bar"};
    private static final String[] ARGS6 = {"--foo=bar", "--qux", "-a", "--foo2=quux", "--g=quux", "--h=quux", "--"};
    private static final String[] ARGS7 = {"--foo=bar", "--qux", "-a", "--foo2=quux", "--g=quux", "--h=quux", "--h=quuz"};
    private static final String[] ARGS8 = {"--foo=bar", "--qux", "-a", "--foo2=quux", "--g=quux", "--h=quuz", "--h"};

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        String[] result = parser.flatten(new Options(), ARGS, false);
        assertEquals("--foo, bar, --qux, -a, -b, --, -c--d, -e, --f, quux, --g, quux, --h, quux, --", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
        result = parser.flatten(new Options(), ARGS2, false);
        assertEquals("--foo, bar, --qux, -a, --, -c--d, -e, --f, quux, --g, quux, --h, quuz, --", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
        result = parser.flatten(new Options(), ARGS3, false);
        assertEquals("--foo, bar, --qux, -a, --, -c--d, --", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
        result = parser.flatten(new Options(), ARGS4, false);
        assertEquals("--foo, bar, --qux, -a, --", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
        result = parser.flatten(new Options(), ARGS5, false);
        assertEquals("--, --foo, bar", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
        result = parser.flatten(new Options(), ARGS6, false);
        assertEquals("--foo, bar, --qux, -a, --foo2, quux, --g, quux, --h, quuz, --", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
        result = parser.flatten(new Options(), ARGS7, false);
        assertEquals("--foo, bar, --qux, -a, --foo2, quux, --g, quux, --h, quuz, --h, quuz", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
        result = parser.flatten(new Options(), ARGS8, false);
        assertEquals("--foo, bar, --qux, -a, --foo2, quux, --g, quuz, --h, quuz, --h", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));

        result = parser.flatten(new Options(), ARGS, true);
        assertEquals("--foo, bar, --qux, -a, -b, -c--d, -e, --f, quux, --g, quux, --h, quux, quuz", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
    }

    @Test
    public void testFlattenWithNull() {
        String[] result = parser.flatten(new Options(), null, false);
        assertNull(result);
    }

    @Test
    public void testFlattenWithEmpty() {
        String[] result = parser.flatten(new Options(), new String[0], false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] result = parser.flatten(new Options(), ARGS, true);
        assertEquals("--foo, bar, --qux, -a, --c--d, -e, quux, quux, quux", new String(new String(Arrays.toString(result)).replaceAll("\\[|\\]", "")));
    }
}