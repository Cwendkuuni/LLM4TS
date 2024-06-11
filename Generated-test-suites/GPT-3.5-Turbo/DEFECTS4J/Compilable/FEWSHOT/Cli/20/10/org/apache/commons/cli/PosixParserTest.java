package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        
        assertEquals(new String[] {"--foo"}, new PosixParser().flatten(options, new String[] {"--foo"}, false));
        assertEquals(new String[] {"--foo", "bar"}, new PosixParser().flatten(options, new String[] {"--foo", "bar"}, false));
        assertEquals(new String[] {"--foo", "bar", "--boo", "baz"}, new PosixParser().flatten(options, new String[] {"--foo", "bar", "--boo", "baz"}, false));
        assertEquals(new String[] {"-", "-"}, new PosixParser().flatten(options, new String[] {"-", "-"}, false));
        assertEquals(new String[] {"-foo"}, new PosixParser().flatten(options, new String[] {"-foo"}, false));
        assertEquals(new String[] {"-f", "oo"}, new PosixParser().flatten(options, new String[] {"-f", "oo"}, false));
        assertEquals(new String[] {"-", "f", "oo"}, new PosixParser().flatten(options, new String[] {"-", "f", "oo"}, false));
        assertEquals(new String[] {"-", "-"}, new PosixParser().flatten(options, new String[] {"-", "-"}, true));
        assertEquals(new String[] {"-", "foo"}, new PosixParser().flatten(options, new String[] {"-", "foo"}, true));
        assertEquals(new String[] {"--", "foo"}, new PosixParser().flatten(options, new String[] {"--", "foo"}, false));
        assertEquals(new String[] {"--", "foo", "bar"}, new PosixParser().flatten(options, new String[] {"--", "foo", "bar"}, false));
        assertEquals(new String[] {"--", "foo", "bar", "--", "baz"}, new PosixParser().flatten(options, new String[] {"--", "foo", "bar", "--", "baz"}, false));
    }
}