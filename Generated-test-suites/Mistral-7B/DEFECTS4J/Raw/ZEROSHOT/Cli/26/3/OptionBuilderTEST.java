package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithDefaultValues() {
        Option option = OptionBuilder.create('D');
        Assert.assertNull(OptionBuilder.longopt);
        Assert.assertNull(OptionBuilder.description);
        Assert.assertEquals("arg", OptionBuilder.argName);
        Assert.assertFalse(OptionBuilder.required);
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        Assert.assertNull(OptionBuilder.type);
        Assert.assertFalse(OptionBuilder.optionalArg);
        Assert.assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("build");
        Option option = optionBuilder.create('B');
        Assert.assertEquals("build", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('A');
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('O');
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("file");
        Option option = optionBuilder.create('F');
        Assert.assertEquals("file", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Build the project.");
        Option option = optionBuilder.create('B');
        Assert.assertEquals("Build the project.", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create('C');
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.TYPE);
        Option option = optionBuilder.create('I');
        Assert.assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testOptionBuilderWithNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create('X');
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        Assert.assertEquals(3, option.getMaxArgValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderMissingLongOpt() {
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderInvalidCharOpt() {
        OptionBuilder.create((char) 0);
    }

    @Test
    public void testOptionBuilderCreateOptionWithArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create('A');
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        Assert.assertEquals(2, option.getMaxArgValues());
        Assert.assertArrayEquals(new String[]{"arg0", "arg1"}, option.getArgs());
    }

    @Test
    public void testOptionBuilderCreateOptionWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(1).withValueSeparator(':');
        Option option = optionBuilder.create('P');
        String[] args = {"test", "value"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(Arrays.asList("--P", "test", "value").toArray(new String[0]));
        Assert.assertEquals("test", option.getValue(0));
        Assert.assertEquals("value", option.getValue(1));
    }
}