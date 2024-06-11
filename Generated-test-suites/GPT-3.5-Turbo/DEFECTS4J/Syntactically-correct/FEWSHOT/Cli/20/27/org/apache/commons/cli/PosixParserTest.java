
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("arg").hasArg().build());
        options.addOption(Option.builder("b").hasArg(false).build());
        options.addOption(Option.builder("c").hasArgs().build());
        
        PosixParser parser = new PosixParser();
        
        String[] arguments1 = {"-a", "value1", "-b", "arg1", "arg2", "arg3"};
        String[] expected1 = {"-a", "value1", "-b", "arg1", "arg2", "arg3"};
        assertArrayEquals(expected1, parser.flatten(options, arguments1, false));
        
        String[] arguments2 = {"-a", "value1", "-b", "arg1", "arg2", "arg3"};
        String[] expected2 = {"-a", "value1", "-b", "arg1", "arg2", "arg3"};
        assertArrayEquals(expected2, parser.flatten(options, arguments2, true));
        
        String[] arguments3 = {"-a", "value1", "--c=arg1", "--c", "arg2", "arg3"};
        String[] expected3 = {"-a", "value1", "--c", "arg1", "--c", "arg2", "arg3"};
        assertArrayEquals(expected3, parser.flatten(options, arguments3, false));
        
        String[] arguments4 = {"-a", "value1", "--c=arg1", "--c", "arg2", "arg3"};
        String[] expected4 = {"-a", "value1", "--c", "arg1", "--c", "arg2", "arg3"};
        assertArrayEquals(expected4, parser.flatten(options, arguments4, true));
        
        String[] arguments5 = {"-a", "value1", "arg1", "arg2", "arg3"};
        String[] expected5 = {"-a", "value1", "arg1", "arg2", "arg3"};
        assertArrayEquals(expected5, parser.flatten(options, arguments5, false));
        
        String[] arguments6 = {"-a", "value1", "arg1", "arg2", "arg3"};
        String[] expected6 = {"-a", "value1", "arg1", "arg2", "arg3"};
        assertArrayEquals(expected6, parser.flatten(options, arguments6, true));
        
        String[] arguments7 = {"--d", "value1", "-a", "arg1", "--b", "--c", "arg1", "arg2"};
        String[] expected7 = {"--d", "value1", "-a", "arg1", "--b", "--c", "arg1", "arg2"};
        assertArrayEquals(expected7, parser.flatten(options, arguments7, false));
        
        String[] arguments8 = {"--d", "value1", "-a", "arg1", "--b", "--c", "arg1", "arg2"};
        String[] expected8 = {"--d", "value1", "-a", "arg1", "--b", "--c", "arg1", "arg2"};
        assertArrayEquals(expected8, parser.flatten(options, arguments8, true));
    }
}
