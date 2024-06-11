package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli.Option.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(ARG_REQUIRED, option.getArgType());
    }

    @Test
    public void testHasArgWithName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
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
    public void testWithDescription() {
        String description = "test description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option option = optionBuilder.create();
        Assert.assertEquals(description, option.getDescription());
    }

    @Test
    public void testCreateWithDefaults() {
        Option option = OptionBuilder.create();
        Assert.assertNull(option.getLongOpt());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(ARG_NONE, option.getArgType());
        Assert.assertEquals(NO_ARGUMENTS, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals(' ', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = optionBuilder.create();
        // This should throw an IllegalArgumentException
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals(ARG_UNLIMITED_N_ARGS, option.getArgType());
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(ARG_UNLIMITED_VALUES, option.getArgType());
        Assert.assertEquals(ARG_UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithType() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(1).withType(String.class);
        Option option = optionBuilder.create();
        Assert.assertEquals(ARG_STRING_ARRAY, option.getArgType());
        Assert.assertEquals(ARG_ONE_ARG, option.getArgs());
    }
}