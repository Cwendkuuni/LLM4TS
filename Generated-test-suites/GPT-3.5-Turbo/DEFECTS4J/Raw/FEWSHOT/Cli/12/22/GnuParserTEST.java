
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        
        String[] arguments1 = {"-a", "-b", "value1", "-c", "value2"};
        String[] expected1 = {"-a", "-b", "value1", "-c", "value2"};
        assertArrayEquals(expected1, GnuParser.flatten(options, arguments1, false));
        
        String[] arguments2 = {"-a", "--", "-b", "value1", "-c", "value2"};
        String[] expected2 = {"-a", "--", "-b", "value1", "-c", "value2"};
        assertArrayEquals(expected2, GnuParser.flatten(options, arguments2, false));
        
        String[] arguments3 = {"-a", "-b", "value1", "-c", "value2"};
        String[] expected3 = {"-a", "-b", "value1", "-c", "value2"};
        assertArrayEquals(expected3, GnuParser.flatten(options, arguments3, true));
        
        String[] arguments4 = {"-a", "--", "-b", "value1", "-c", "value2"};
        String[] expected4 = {"-a", "--", "-b", "value1", "-c", "value2"};
        assertArrayEquals(expected4, GnuParser.flatten(options, arguments4, true));
    }
}
