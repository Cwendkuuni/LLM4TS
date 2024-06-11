package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        // test implementation here
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // test implementation here
    }
    
    @Test
    public void testHasArgWithParameter() {
        OptionBuilder.hasArg(true);
        // test implementation here
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        // test implementation here
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // test implementation here
    }
    
    @Test
    public void testWithValuseSeparator() {
        OptionBuilder.withValueSeparator(':');
        // test implementation here
    }
    
    @Test
    public void testWithValuesSeparator() {
        OptionBuilder.withValueSeparator();
        // test implementation here
    }
    
    @Test
    public void testIsRequiredWithParameter() {
        OptionBuilder.isRequired(true);
        // test implementation here
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // test implementation here
    }
    
    @Test
    public void testHasArgsWithParameter() {
        OptionBuilder.hasArgs(2);
        // test implementation here
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // test implementation here
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // test implementation here
    }
    
    @Test
    public void testHasOptionalArgsWithParameter() {
        OptionBuilder.hasOptionalArgs(3);
        // test implementation here
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        // test implementation here
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        // test implementation here
    }
    
    @Test
    public void testCreateWithChar() {
        OptionBuilder.create('o');
        // test implementation here
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.create();
        // test implementation here
    }
    
    @Test
    public void testCreateWithString() {
        OptionBuilder.create("o");
        // test implementation here
    }
}