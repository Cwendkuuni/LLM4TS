package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten() {
        String[] arguments = {"--foo", "bar", "-abc", "def"};
        String[] expected = {"--", "--foo", "bar", "-a", "-b", "-c", "def"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenStopAtNonOptionFalse() {
        String[] arguments = {"--foo", "bar", "-abc", "def"};
        String[] expected = {"--", "--foo", "bar", "-a", "-b", "-c", "def"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNoOptions() {
        String[] arguments = {"--foo", "bar", "-abc", "def"};
        String[] expected = {"--foo", "bar", "-a", "-b", "-c", "def"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithLongOptionEquals() {
        String[] arguments = {"--foo=bar"};
        String[] expected = {"--", "--foo", "bar"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithSingleHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"--", "-"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithSingleOption() {
        options.addOption("a", "optionA", false, "Option A");
        
        String[] arguments = {"-a"};
        String[] expected = {"--", "-a"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithBurstingOption() {
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        
        String[] arguments = {"-abcf"};
        String[] expected = {"--", "-a", "bcf"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNonOptionToken() {
        options.addOption("a", "optionA", false, "Option A");
        
        String[] arguments = {"-a", "nonOption"};
        String[] expected = {"--", "-a", "nonOption"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNonOptionTokenAndStopAtNonOptionFalse() {
        options.addOption("a", "optionA", false, "Option A");
        
        String[] arguments = {"-a", "nonOption"};
        String[] expected = {"--", "-a", "nonOption"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNonOptionTokenAndStopAtNonOptionFalseAndRemainingTokens() {
        options.addOption("a", "optionA", false, "Option A");
        
        String[] arguments = {"-a", "nonOption", "remaining1", "remaining2"};
        String[] expected = {"--", "-a", "nonOption", "remaining1", "remaining2"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNonOptionTokenAndStopAtNonOptionTrueAndRemainingTokens() {
        options.addOption("a", "optionA", false, "Option A");
        
        String[] arguments = {"-a", "nonOption", "remaining1", "remaining2"};
        String[] expected = {"--", "-a", "nonOption"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
}