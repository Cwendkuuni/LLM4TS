package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class GnuParserTest {
    
    @Test
    public void testFlattenWithEmptyOptions() {
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "--option2", "value2"};
        String[] expected = {"--option1", "value1", "--option2", "value2"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "nonOption", "--option2", "value2"};
        String[] expected = {"--option1", "value1", "nonOption"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithDoubleHyphen() {
        Options options = new Options();
        String[] arguments = {"--option1", "--", "--option2"};
        String[] expected = {"--option1", "--", "--option2"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"--option1", "-", "--option2"};
        String[] expected = {"--option1", "-", "--option2"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithOptionExists() {
        Options options = new Options();
        options.addOption("opt1", false, "option1");
        options.addOption("opt2", false, "option2");
        String[] arguments = {"--opt1", "value1", "--opt2", "value2"};
        String[] expected = {"--opt1", "value1", "--opt2", "value2"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithOptionsSubstring() {
        Options options = new Options();
        options.addOption("opt1", false, "option1");
        String[] arguments = {"--opt", "value1"};
        String[] expected = {"--opt1", "value1"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "nonOption"};
        String[] expected = {"--option1", "value1", "nonOption"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNullOptions() {
        Options options = null;
        String[] arguments = {"--option1", "value1"};
        String[] expected = {"--option1", "value1"};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNullArguments() {
        Options options = new Options();
        String[] arguments = null;
        String[] expected = null;
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};
        
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(expected, result);
    }

}