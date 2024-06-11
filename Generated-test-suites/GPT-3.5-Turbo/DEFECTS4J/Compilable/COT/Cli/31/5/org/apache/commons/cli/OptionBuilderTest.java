package org.apache.commons.cli;

import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        // Perform asserts here
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Perform asserts here
    }
    
    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        // Perform asserts here
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        // Perform asserts here
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Perform asserts here
    }
    
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // Perform asserts here
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // Perform asserts here
    }
    
    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        // Perform asserts here
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Perform asserts here
    }
    
    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(3);
        // Perform asserts here
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Perform asserts here
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Perform asserts here
    }
    
    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(2);
        // Perform asserts here
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        // Perform asserts here
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        // Perform asserts here
    }
    
    @Test
    public void testCreateChar() {
        OptionBuilder.create('A');
        // Perform asserts here
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.create();
        // Perform asserts here
    }
    
    @Test
    public void testCreateString() {
        OptionBuilder.create("testOption");
        // Perform asserts here
    }
}
