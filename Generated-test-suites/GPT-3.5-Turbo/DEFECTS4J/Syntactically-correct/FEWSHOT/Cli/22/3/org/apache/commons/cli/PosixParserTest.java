package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        options.addOption("b", "bar", true, "Bar option");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-f", "--bar", "value"};
        String[] expected = {"-f", "--", "--bar", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
    
    @Test
    public void testFlattenWithNoOptions() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-f", "--bar", "value"};
        String[] expected = {"-f", "--bar", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
    
    @Test
    public void testFlattenWithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-f", "--bar", "value"};
        String[] expected = {"-f", "--bar", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }
    
    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        options.addOption("b", "bar", true, "Bar option");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-f", "-bvalue"};
        String[] expected = {"-f", "-b", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
    
    @Test
    public void testFlattenWithMultipleOptionsAndArguments() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        options.addOption("b", "bar", true, "Bar option");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-f", "-bvalue", "arg1", "arg2"};
        String[] expected = {"-f", "-b", "value", "arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
    
    @Test
    public void testFlattenWithNonOptionAfterStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-f", "--bar", "-bvalue"};
        String[] expected = {"-f", "--", "--bar", "-bvalue"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
    
    @Test
    public void testFlattenWithoutNonOptionAfterStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        options.addOption("b", "bar", true, "Bar option");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-f", "-bvalue", "-a"};
        String[] expected = {"-f", "-b", "value", "-a"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
}