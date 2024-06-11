package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

    // Test cases for flatten method
    
    @Test
    public void testFlatten_OptionExistsForFirstCharacter() {
        Options options = new Options();
        options.addOption("o", "output", true, "output filename");
        
        String[] arguments = {"-o", "output.txt"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("output.txt", result[1]);
    }
    
    @Test
    public void testFlatten_OptionDoesNotExistForWholeArgument() {
        Options options = new Options();
        options.addOption("o", "output", true, "output filename");
        
        String[] arguments = {"-Dtest"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-Dtest", result[0]);
    }
    
    @Test
    public void testFlatten_DashOnlyArgument() {
        Options options = new Options();
        options.addOption("o", "output", true, "output filename");
        
        String[] arguments = {"-"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_DoubleDashArgument() {
        Options options = new Options();
        options.addOption("o", "output", true, "output filename");
        
        String[] arguments = {"--"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }
    
    @Test
    public void testFlatten_LeadingHyphensOption() {
        Options options = new Options();
        options.addOption("D", "property", true, "system property");
        
        String[] arguments = {"-Dkey=value"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("key=value", result[1]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "output", true, "output filename");
        
        String[] arguments = {"-o", "output.txt", "arg1", "arg2"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("output.txt", result[1]);
        assertEquals("arg1", result[2]);
    }
    
    // Test cases for other methods (if any)...
    
}