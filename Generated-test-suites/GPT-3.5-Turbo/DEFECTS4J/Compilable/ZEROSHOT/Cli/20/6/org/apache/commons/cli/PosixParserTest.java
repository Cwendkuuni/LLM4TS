package org.apache.commons.cli;
import org.apache.commons.cli.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten_StopAtNonOption_True() {
        String[] arguments = {"--foo", "bar", "-b", "baz", "non-option"};
        options.addOption("foo", true, "Option foo");
        options.addOption("b", true, "Option b");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("baz", result[3]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_False() {
        String[] arguments = {"--foo", "bar", "-b", "baz", "non-option"};
        options.addOption("foo", true, "Option foo");
        options.addOption("b", true, "Option b");
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("baz", result[3]);
        assertEquals("--", result[4]);
        assertEquals("non-option", result[5]);
    }
    
    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {"--foo", "bar", "-b", "baz"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("baz", result[3]);
    }
    
    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] arguments = {"--foo=bar"};
        options.addOption("foo", true, "Option foo");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_ShortOption() {
        String[] arguments = {"-b"};
        options.addOption("b", true, "Option b");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }
    
    @Test
    public void testFlatten_ShortOptionWithValue() {
        String[] arguments = {"-b", "baz"};
        options.addOption("b", true, "Option b");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("baz", result[1]);
    }
    
    @Test
    public void testFlatten_ShortOptionMultipleValues() {
        String[] arguments = {"-b", "baz", "qux"};
        options.addOption("b", true, "Option b");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-b", result[0]);
        assertEquals("baz", result[1]);
        assertEquals("qux", result[2]);
    }
    
    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_True() {
        String[] arguments = {"-x", "-b", "baz", "non-option"};
        options.addOption("b", true, "Option b");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("-x", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("baz", result[2]);
        assertEquals("non-option", result[3]);
    }
    
    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_False() {
        String[] arguments = {"-x", "-b", "baz", "non-option"};
        options.addOption("b", true, "Option b");
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(5, result.length);
        assertEquals("-x", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("baz", result[2]);
        assertEquals("--", result[3]);
        assertEquals("non-option", result[4]);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption_True() {
        String[] arguments = {"-abc", "non-option"};
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("non-option", result[3]);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption_False() {
        String[] arguments = {"-abc", "non-option"};
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("--", result[3]);
        assertEquals("non-option", result[4]);
    }
    
    @Test
    public void testFlatten_BurstTokenOutOfOrder_StopAtNonOption_True() {
        String[] arguments = {"-bac", "non-option"};
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("-b", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("non-option", result[3]);
    }
    
    @Test
    public void testFlatten_BurstTokenOutOfOrder_StopAtNonOption_False() {
        String[] arguments = {"-bac", "non-option"};
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("-b", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("--", result[3]);
        assertEquals("non-option", result[4]);
    }
    
}