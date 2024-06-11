package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        Option option = optionBuilder.create();
        Assert.assertEquals("optionLongName", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Option option = optionBuilder.create();
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();
        Assert.assertEquals(3, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        Assert.assertNotNull(option.getType());
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option Description");
        Option option = optionBuilder.create();
        Assert.assertEquals("Test Option Description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        Option option = OptionBuilder.create();
        Assert.fail("Expected IllegalArgumentException");
    }
}