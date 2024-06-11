package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        
        Options options = new Options();
        String[] arguments = {"--option1", "-option2", "argument1", "-argument2"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(6, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("-option2", result[1]);
        assertEquals("argument1", result[2]);
        assertEquals("-argument2", result[3]);
        assertEquals("-argument2", result[4]);
        assertEquals("argument2", result[5]);
    }
    
    @Test
    public void testFlattenStopAtNonOption() {
        GnuParser parser = new GnuParser();
        
        Options options = new Options();
        String[] arguments = {"--option1", "-option2", "argument1", "-argument2"};
        boolean stopAtNonOption = true;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(5, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("-option2", result[1]);
        assertEquals("argument1", result[2]);
        assertEquals("-argument2", result[3]);
        assertEquals("-argument2", result[4]);
    }
    
    @Test
    public void testFlattenWithDoubleDash() {
        GnuParser parser = new GnuParser();
        
        Options options = new Options();
        String[] arguments = {"--option1", "--", "-option2", "argument1", "-argument2"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(7, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-option2", result[2]);
        assertEquals("argument1", result[3]);
        assertEquals("-argument2", result[4]);
        assertEquals("-argument2", result[5]);
        assertEquals("argument2", result[6]);
    }
    
    @Test
    public void testFlattenWithSingleDash() {
        GnuParser parser = new GnuParser();
        
        Options options = new Options();
        String[] arguments = {"--option1", "-", "-option2", "argument1", "-argument2"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(7, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("-", result[1]);
        assertEquals("-option2", result[2]);
        assertEquals("argument1", result[3]);
        assertEquals("-argument2", result[4]);
        assertEquals("-argument2", result[5]);
        assertEquals("argument2", result[6]);
    }
    
    @Test
    public void testFlattenWithUnknownOption() {
        GnuParser parser = new GnuParser();
        
        Options options = new Options();
        String[] arguments = {"--option1", "-option2", "-unknownOption", "argument1", "-argument2"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(6, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("-option2", result[1]);
        assertEquals("-unknownOption", result[2]);
        assertEquals("argument1", result[3]);
        assertEquals("-argument2", result[4]);
        assertEquals("-argument2", result[5]);
    }
}