package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        options.addOption("D", true, "Property option");
        options.addOption("foo", false, "Foo option");
    }

    @Test
    public void testFlatten() {
        String[] args = parser.flatten(options, new String[]{"-Dproperty=value", "--foo", "-bar"}, true);
        assertArrayEquals(new String[]{"-D", "property=value", "--", "-bar"}, args);
    }
}