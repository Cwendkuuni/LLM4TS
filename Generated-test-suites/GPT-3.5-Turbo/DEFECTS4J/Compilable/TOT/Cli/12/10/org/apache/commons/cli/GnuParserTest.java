package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.assertEquals;

public class GnuParserTest {

    private GnuParser parser;

    private Options options;
    
    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
    }
    
    @Test
    public void testFlatten_WithNoArguments_ShouldReturnEmptyArray() {
        String[] arguments = {};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_WithSingleNoOptionArgument_ShouldReturnSameArgument() {
        String[] arguments = {"arg"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("arg", result[0]);
    }
    
    @Test
    public void testFlatten_WithDashArgument_ShouldAddToTokens() {
        String[] arguments = {"-"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_WithDoubleDashArgument_ShouldAddToTokens() {
        String[] arguments = {"--"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }
    
    @Test
    public void testFlatten_WithSingleOptionArgument_ShouldAddToTokens() {
        String[] arguments = {"-a"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }
    
    @Test
    public void testFlatten_WithUnrecognizedOption_ShouldAddToTokens() {
        String[] arguments = {"-d"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-d", result[0]);
    }
    
    @Test
    public void testFlatten_WithSpecialOptionFormat_ShouldSplitTokens() {
        String[] arguments = {"-Dproperty=value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }
    
    @Test
    public void testFlatten_WithStopAtNonOptionSetToFalse_ShouldEatTheRest() {
        String[] arguments = {"arg1", "-a", "arg2", "--", "arg3", "arg4"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("arg2", result[2]);
    }
    
    @Test
    public void testFlatten_WithStopAtNonOptionSetToTrue_ShouldNotEatTheRest() {
        String[] arguments = {"arg1", "-a", "arg2", "--", "arg3", "arg4"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("--", result[3]);
    }
}