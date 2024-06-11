package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten_LongOptionWithoutValue_Success() {
        String[] arguments = {"--foo"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_LongOptionWithValue_Success() {
        String[] arguments = {"--foo=bar"};
        String[] expected = {"--foo", "bar"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen_Success() {
        String[] arguments = {"-"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_ValidShortOption_Success() {
        options.addOption("f", "foo", false, "Foo Option");
        
        String[] arguments = {"-f"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_InvalidShortOption_StopAtNonOption_Success() {
        String[] arguments = {"-x"};
        String[] expected = {"--", "-x"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_InvalidShortOption_Continue() {
        String[] arguments = {"-x"};
        String[] expected = {"-x"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_Success() {
        options.addOption("f", "foo", false, "Foo Option");
        
        String[] arguments = {"-fa"};
        String[] expected = {"-f", "a"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption_Success() {
        options.addOption("f", "foo", false, "Foo Option");
        String[] arguments = {"-fa"};
        String[] expected = {"--", "-fa"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NonOptionToken_Success() {
        String[] arguments = {"value1"};
        String[] expected = {"value1"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NonOptionToken_StopAtNonOption_Success() {
        String[] arguments = {"value1"};
        String[] expected = {"--", "value1"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_MultipleTokens_Success() {
        options.addOption("f", "foo", false, "Foo Option");
        options.addOption("b", "bar", true, "Bar Option");
        String[] arguments = {"-f", "--bar=value1", "-afb", "value2"};
        String[] expected = {"-f", "--bar", "value1", "-a", "-f", "b", "value2"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }
}