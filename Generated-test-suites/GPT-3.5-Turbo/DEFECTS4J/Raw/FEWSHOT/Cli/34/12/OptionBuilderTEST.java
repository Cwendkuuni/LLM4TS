package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create();
        Assert.assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(-1, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argname");
        Option option = OptionBuilder.create();
        Assert.assertEquals("argname", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create();
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.hasArgs());
        Assert.assertEquals(Integer.MAX_VALUE, option.getArgs());
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.hasArgs());
        Assert.assertEquals(3, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.hasArg());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.hasArgs());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(Integer.MAX_VALUE, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.hasArgs());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        Assert.assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithCharOpt() {
        Option option = OptionBuilder.create('o');
        Assert.assertEquals("o", option.getOpt());
    }

    @Test
    public void testCreateWithStringOpt() {
        Option option = OptionBuilder.create("opt");
        Assert.assertEquals("opt", option.getOpt());
    }

}