package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest {

    

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test(expected = IllegalStateException.class)
    public void testOptionBuilderWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
        Assert.fail("An IllegalStateException is expected but was not thrown.");
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        Option option = optionBuilder.create();
        Assert.assertEquals(5, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertFalse(option.isOptional());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        String description = "This is a test option";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option option = optionBuilder.create();
        Assert.assertEquals(description, option.getDescription());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        Option option = optionBuilder.create();
        Assert.assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithType() {
        Class testType = String.class;
        OptionBuilder optionBuilder = OptionBuilder.withType(testType);
        Option option = optionBuilder.create();
        Assert.assertEquals(testType, option.getType());
    }

    @Test
    public void testOptionBuilderCreate() throws NoSuchMethodException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getArgs());
        Assert.assertFalse(option.isOptional());
        Assert.assertEquals(null, option.getLongOpt());
        Assert.assertEquals(null, option.getArgName());
        Assert.assertEquals(null, option.getDescription());
        Assert.assertEquals(null, option.getType());
        Assert.assertEquals(null, option.getValue());
    }

    
}