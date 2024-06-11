package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").desc("desc").build());
        
        PosixParser parser = new PosixParser();
        
        String[] args1 = {"-a"};
        String[] result1 = parser.flatten(options, args1, false);
        assertEquals(1, result1.length);
        assertEquals("-a", result1[0]);
        
        String[] args2 = {"-b", "value"};
        String[] result2 = parser.flatten(options, args2, false);
        assertEquals(2, result2.length);
        assertEquals("-b", result2[0]);
        assertEquals("value", result2[1]);
        
        String[] args3 = {"-c"};
        String[] result3 = parser.flatten(options, args3, false);
        assertEquals(1, result3.length);
        assertEquals("-c", result3[0]);
        
        String[] args4 = {"-d"};
        String[] result4 = parser.flatten(options, args4, false);
        assertEquals(1, result4.length);
        assertEquals("-d", result4[0]);
        
        String[] args5 = {"-e"};
        String[] result5 = parser.flatten(options, args5, true);
        assertEquals(2, result5.length);
        assertEquals("--", result5[0]);
        assertEquals("-e", result5[1]);
    }
    
}