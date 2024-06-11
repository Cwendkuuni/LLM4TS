package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "-b", "--option=arg2", "arg3"};
        boolean stopAtNonOption = false;
        
        PosixParser posixParser = new PosixParser();
        String[] result = posixParser.flatten(options, arguments, stopAtNonOption);
        
        // Check if the result contains all the expected tokens
        Assert.assertArrayEquals(new String[]{"-a", "arg1", "-b", "--option", "arg2", "arg3"}, result);
    }
}
