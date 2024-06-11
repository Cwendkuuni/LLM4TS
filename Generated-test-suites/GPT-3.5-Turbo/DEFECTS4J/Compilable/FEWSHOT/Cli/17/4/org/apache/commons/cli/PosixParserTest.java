
package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    private static final Options options = new Options();

    @Test
    public void testFlatten() {
        String[] arguments = {"--optionA", "-b", "valueB", "--optionC=valueC", "arg1", "arg2"};

        Option optionA = new Option("optionA", false, "option A");
        Option optionB = new Option("b", true, "option B");
        Option optionC = new Option("optionC", "option C");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] {"--optionA", "-b", "valueB", "--optionC", "valueC"}, flattenedArgs);
    }

    @Test
    public void testProcessSingleHyphen() {
        String hyphen = "-";
        
        String[] arguments = {hyphen};

        Option option = new Option("option", false, "option description");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] {hyphen}, flattenedArgs);
    }

    @Test
    public void testProcessOptionToken_ValidOption() {
        String token = "-option";
        
        String[] arguments = {token};

        Option option = new Option("option", false, "option description");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] {token}, flattenedArgs);
    }

    @Test
    public void testProcessOptionToken_InvalidOption_StopAtNonOption() {
        String token = "-invalid";
        
        String[] arguments = {token, "arg1"};

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] {"--", token, "arg1"}, flattenedArgs);
    }

    @Test
    public void testProcessOptionToken_InvalidOption_NoStopAtNonOption() {
        String token = "-invalid";
        
        String[] arguments = {token, "arg1"};

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        
        assertArrayEquals(new String[] {token}, flattenedArgs);
    }

    @Test
    public void testProcess_StopAtNonOption() {
        String value = "value";
        
        String[] arguments = {value, "arg1"};

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] {value}, flattenedArgs);
    }

    @Test
    public void testProcess_NoStopAtNonOption() {
        String value = "value";
        
        String[] arguments = {value, "arg1"};

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        
        assertArrayEquals(new String[] {value, "arg1"}, flattenedArgs);
    }

    @Test
    public void testGobble() {
        String[] arguments = {"--optionA", "-b", "--"};

        Option optionA = new Option("optionA", false, "option A");
        Option optionB = new Option("b", true, "option B");
        options.addOption(optionA);
        options.addOption(optionB);

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] {"--optionA", "-b"}, flattenedArgs);
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        
        String[] arguments = {token};

        Option optionA = new Option("a", false, "option A");
        Option optionB = new Option("b", true, "option B");
        Option optionC = new Option("c", "option C");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] {"-a", "-b", "-c"}, flattenedArgs);
    }
}
