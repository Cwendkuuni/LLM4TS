
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", false, "option b");

        String[] arguments = {"-a", "value", "-b"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("Expecting flattened argument length of 3", 3, result.length);
        assertEquals("Expecting option value to be 'value'", "value", result[1]);
        assertEquals("Expecting non-option to be '-b'", "-b", result[2]);
    }
}
