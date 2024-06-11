package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test cases for OptionBuilder class
 */
public class OptionBuilderTest {

    

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasArg_withArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isUnbounded());
    }

    @Test
    public void testOptionBuilderHasArgs_withNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals(2, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isUnbounded());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs_withNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();
        Assert.assertEquals(3, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test description");
        Option option = optionBuilder.create();
        Assert.assertEquals("test description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(-1);
    }

    @Test
    public void testCreateOptionWithMultipleArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create('A');
        List<String> args = option.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("arg1", args.get(0));
        Assert.assertEquals("arg2", args.get(1));
    }

    @Test
    public void testCreateOptionWithOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('A');
        List<String> args = option.getArgs();
        Assert.assertEquals(1, args.size());
        Assert.assertNull(args.get(0));
    }

    @Test
    public void testCreateOptionWithOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create('A');
        List<String> args = option.getArgs();
        Assert.assertTrue(args.isEmpty());
    }

    @Test
    public void testCreateOptionWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create('A');
        List<String> args = option.getArgs();
        Assert.assertEquals(1, args.size());
        Assert.assertEquals("[arg1, arg2]", args.get(0));
    }

    @Test
    public void testCreateOptionWithRequiredArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('A');
        List<String> args = option.getArgs();
        Assert.assertEquals(1, args.size());
        Assert.assertEquals("arg", args.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator('\\');
        Option option = optionBuilder.create('A');
    }

    @Test
    public void testCreateOptionWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test description");
        Option option = optionBuilder.create('A');
        Assert.assertEquals("test description", option.getDescription());
    }

    @Test
    public void testCreateOptionWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create('A');
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreateOptionWithType_withArg() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String[].class);
        Option option = optionBuilder.create('A');
        List<String> args = option.getArgs();
        Assert.assertTrue(args.isEmpty());
        Assert.assertEquals(String[].class, option.getType());
    }

    
}