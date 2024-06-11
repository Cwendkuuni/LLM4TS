package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {
    
    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        
        String[] arguments = new String[]{"-a", "-b", "--foo", "value1", "-Dproperty=value"};
        String[] expected = new String[]{"-a", "-b", "--foo", "value1", "-D", "property=value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithStopAtNonOptionFalse() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        
        String[] arguments = new String[]{"-a", "-b", "--foo", "value1", "-Dproperty=value"};
        String[] expected = new String[]{"-a", "-b", "--foo", "value1", "-D", "property=value"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNoOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        
        String[] arguments = new String[]{"arg1", "arg2"};
        String[] expected = new String[]{"arg1", "arg2"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithOnlyOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        
        String[] arguments = new String[]{"-a", "-b"};
        String[] expected = new String[]{"-a", "-b"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithOnlyStopAtNonOptionFalse() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        
        String[] arguments = new String[]{"arg1", "arg2"};
        String[] expected = new String[]{"arg1", "arg2"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        
        String[] arguments = new String[]{"-"};
        String[] expected = new String[]{"-"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithDoubleHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        
        String[] arguments = new String[]{"--"};
        String[] expected = new String[]{"--"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
}