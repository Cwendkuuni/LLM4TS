package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class GnuParserTest {
    
    //Test flatten method with no arguments
    @Test
    public void testFlattenNoArguments() {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = false;
        
        String[] expected = {};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with only non-option arguments
    @Test
    public void testFlattenOnlyNonOptionArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        boolean stopAtNonOption = false;
        
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with only option arguments
    @Test
    public void testFlattenOnlyOptionArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        
        String[] arguments = {"-a", "-b"};
        boolean stopAtNonOption = false;
        
        String[] expected = {"-a", "-b"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with option and non-option arguments
    @Test
    public void testFlattenOptionAndNonOptionArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        
        String[] arguments = {"-a", "arg1", "arg2", "-b"};
        boolean stopAtNonOption = false;
        
        String[] expected = {"-a", "arg1", "arg2", "-b"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with stopAtNonOption=true
    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        
        String[] arguments = {"-a", "arg1", "arg2", "-b"};
        boolean stopAtNonOption = true;
        
        String[] expected = {"-a"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with "--" argument
    @Test
    public void testFlattenDoubleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        
        String[] arguments = {"-a", "--", "arg1", "arg2", "-b"};
        boolean stopAtNonOption = false;
        
        String[] expected = {"-a", "--", "arg1", "arg2", "-b"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with single hyphen argument
    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        
        String[] arguments = {"-a", "-", "arg1", "arg2", "-b"};
        boolean stopAtNonOption = false;
        
        String[] expected = {"-a", "-"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with chained options
    @Test
    public void testFlattenChainedOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        
        String[] arguments = {"-ab", "value1", "value2"};
        boolean stopAtNonOption = false;
        
        String[] expected = {"-ab", "value1", "value2"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with special properties option
    @Test
    public void testFlattenSpecialPropertiesOption() {
        Options options = new Options();
        options.addOption("D", true, "Special properties option");
        
        String[] arguments = {"-Dproperty=value"};
        boolean stopAtNonOption = false;
        
        String[] expected = {"-D", "property=value"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
    //Test flatten method with unrecognized option
    @Test
    public void testFlattenUnrecognizedOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String[] arguments = {"-b"};
        boolean stopAtNonOption = false;
        
        String[] expected = {"-b"};
        String[] actual = GnuParser.flatten(options, arguments, stopAtNonOption);
        
        assertArrayEquals(expected, actual);
    }
    
}