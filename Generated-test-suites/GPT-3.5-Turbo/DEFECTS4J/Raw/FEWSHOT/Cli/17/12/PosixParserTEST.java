package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        
        Option option1 = new Option("o", true, "Option 1");
        options.addOption(option1);
        
        Option option2 = new Option("t", "test", false, "Option 2");
        options.addOption(option2);
        
        Option option3 = new Option("a", "append", true, "Option 3");
        options.addOption(option3);
        
        Option option4 = new Option("b", false, "Option 4");
        options.addOption(option4);
        
        Option option5 = new Option("c", true, "Option 5");
        options.addOption(option5);
        
        String[] arguments1 = {"-o", "value1", "-t", "--", "file1", "file2"};
        String[] expected1 = {"-o", "value1", "-t", "--", "file1", "file2"};
        assertArrayEquals(expected1, new PosixParser().flatten(options, arguments1, false));
        
        String[] arguments2 = {"-a", "value2", "-b", "-c", "value3"};
        String[] expected2 = {"-a", "value2", "-b", "-c", "value3"};
        assertArrayEquals(expected2, new PosixParser().flatten(options, arguments2, false));
        
        String[] arguments3 = {"-o", "value1", "-t", "--", "file1", "file2"};
        String[] expected3 = {"-o", "value1", "-t"};
        assertArrayEquals(expected3, new PosixParser().flatten(options, arguments3, true));
        
        String[] arguments4 = {"-a", "value2", "-b", "-c", "value3"};
        String[] expected4 = {"-a", "value2", "-b"};
        assertArrayEquals(expected4, new PosixParser().flatten(options, arguments4, true));
    }
    
    @Test
    public void testProcess() {
        Options options = new Options();
        
        Option option1 = new Option("o", true, "Option 1");
        options.addOption(option1);
        
        Option option2 = new Option("t", "test", false, "Option 2");
        options.addOption(option2);
        
        Option option3 = new Option("a", "append", true, "Option 3");
        options.addOption(option3);
        
        Option option4 = new Option("b", false, "Option 4");
        options.addOption(option4);
        
        String[] arguments1 = {"-o", "value1"};
        String[] expected1 = {"-o", "value1"};
        assertArrayEquals(expected1, new PosixParser().flatten(options, arguments1, false));
        
        String[] arguments2 = {"-t"};
        String[] expected2 = {"-t"};
        assertArrayEquals(expected2, new PosixParser().flatten(options, arguments2, false));
        
        String[] arguments3 = {"-a", "value2"};
        String[] expected3 = {"-a", "value2"};
        assertArrayEquals(expected3, new PosixParser().flatten(options, arguments3, false));
        
        String[] arguments4 = {"-b"};
        String[] expected4 = {"-b"};
        assertArrayEquals(expected4, new PosixParser().flatten(options, arguments4, false));
    }
    
    @Test
    public void testProcessSingleHyphen() {
        Options options = new Options();
        
        Option option1 = new Option("o", true, "Option 1");
        options.addOption(option1);
        
        String[] arguments1 = {"-"};
        String[] expected1 = {"-"};
        assertArrayEquals(expected1, new PosixParser().flatten(options, arguments1, false));
    }
    
    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        
        Option option1 = new Option("o", true, "Option 1");
        options.addOption(option1);
        
        String[] arguments1 = {"-o", "value1"};
        String[] expected1 = {"-o", "value1"};
        assertArrayEquals(expected1, new PosixParser().flatten(options, arguments1, false));
        
        String[] arguments2 = {"-x"};
        String[] expected2 = {"-x"};
        assertArrayEquals(expected2, new PosixParser().flatten(options, arguments2, true));
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        
        Option option1 = new Option("o", true, "Option 1");
        options.addOption(option1);
        
        String[] arguments1 = {"-xy"};
        String[] expected1 = {"-x", "-y"};
        assertArrayEquals(expected1, new PosixParser().flatten(options, arguments1, false));
        
        String[] arguments2 = {"-xy", "value1"};
        String[] expected2 = {"-x", "-y", "value1"};
        assertArrayEquals(expected2, new PosixParser().flatten(options, arguments2, false));
    }
}