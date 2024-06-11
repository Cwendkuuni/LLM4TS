package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {
    
    @Test
    public void testFlattenOptionExists() {
        Options options = new Options();
        options.addOption("i", false, "Input option");
        
        GnuParser parser = new GnuParser();
        String[] arguments = {"-i", "test.txt"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-i", result[0]);
        assertEquals("test.txt", result[1]);
    }
    
    @Test
    public void testFlattenNonOption() {
        Options options = new Options();
        
        GnuParser parser = new GnuParser();
        String[] arguments = {"-i", "test.txt", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-i", result[0]);
        assertEquals("test.txt", result[1]);
        assertEquals("nonOption", result[2]);
    }
    
    @Test
    public void testFlattenPropertyOption() {
        Options options = new Options();
        
        GnuParser parser = new GnuParser();
        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }
    
    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("i", false, "Input option");
        
        GnuParser parser = new GnuParser();
        String[] arguments = {"test.txt", "-i"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("test.txt", result[0]);
        assertEquals("-i", result[1]);
    }
}