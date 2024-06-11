package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // assertion here
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // assertion here
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // assertion here
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        // assertion here
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // assertion here
    }

    @Test
    public void testWithRequiredBoolean() {
        OptionBuilder.isRequired(true);
        // assertion here
    }
    
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // assertion here
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // assertion here
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // assertion here
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        // assertion here
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // assertion here
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // assertion here
    }
    
    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        // assertion here
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        // assertion here
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        // assertion here
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('c');
        // assertion here
    }
    
    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        // assertion here
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");
        // assertion here
    }
    
}