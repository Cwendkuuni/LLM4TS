package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create();
        Assert.assertEquals("test", option.getLongOpt());
    }
    
    @Test
    public void testHasArgTrue() {
        Option option = OptionBuilder.hasArg(true).create();
        Assert.assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArgFalse() {
        Option option = OptionBuilder.hasArg(false).create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("arg").create();
        Assert.assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testIsRequiredTrue() {
        Option option = OptionBuilder.isRequired(true).create();
        Assert.assertTrue(option.isRequired());
    }
    
    @Test
    public void testIsRequiredFalse() {
        Option option = OptionBuilder.isRequired(false).create();
        Assert.assertFalse(option.isRequired());
    }
    
    @Test
    public void testWithOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        Assert.assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        int numArgs = 3;
        Option option = OptionBuilder.hasOptionalArgs(numArgs).create();
        Assert.assertEquals(numArgs, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithValueSeparatorChar() {
        char sep = ':';
        Option option = OptionBuilder.withValueSeparator(sep).create();
        Assert.assertEquals(sep, option.getValueSeparator());
    }
    
    @Test
    public void testWithValueSeparatorDefault() {
        char sep = '=';
        Option option = OptionBuilder.withValueSeparator().create();
        Assert.assertEquals(sep, option.getValueSeparator());
    }
    
    @Test
    public void testWithArgs() {
        Option option = OptionBuilder.hasArgs().create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testWithArgsNum() {
        int numArgs = 2;
        Option option = OptionBuilder.hasArgs(numArgs).create();
        Assert.assertEquals(numArgs, option.getArgs());
    }
    
    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        Assert.assertEquals(Integer.class, option.getType());
    }
    
    @Test
    public void testWithDescription() {
        String description = "description";
        Option option = OptionBuilder.withDescription(description).create();
        Assert.assertEquals(description, option.getDescription());
    }
}
