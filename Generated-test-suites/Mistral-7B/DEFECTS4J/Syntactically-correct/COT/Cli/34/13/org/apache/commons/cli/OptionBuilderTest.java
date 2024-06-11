package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;

/**
 * Test class for OptionBuilder class in the Apache Commons CLI library.
 */
public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = OptionBuilder.create('l', "longOpt");
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        Assert.assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder().withLongOpt("longOpt").hasArg().build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        Assert.assertEquals(expectedOption.getArgs().numberOfArgs(), actualOption.getArgs().numberOfArgs());
    }

    @Test
    public void testOptionBuilderHasArg_WithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName").hasArg();
        Option expectedOption = Option.builder().withLongOpt("longOpt")
                .withArgName("argName").hasArg().build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        Assert.assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        Assert.assertEquals(expectedOption.getArgs().numberOfArgs(), actualOption.getArgs().numberOfArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderHasArg_InvalidArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("");
        optionBuilder.create();
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder().withLongOpt("longOpt").isRequired().build();
        Option actualOption = optionBuilder.create();

        Assert.assertTrue(expectedOption.isRequired());
        Assert.assertFalse(actualOption.getArgs().hasOptionalArg());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder().withLongOpt("longOpt")
                .hasOptionalArg().build();
        Option actualOption = optionBuilder.create();

        Assert.assertTrue(actualOption.getArgs().hasOptionalArg());
        Assert.assertEquals(Option.UNINITIALIZED, actualOption.getArgs().numberOfArgs());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder().withLongOpt("longOpt")
                .hasArgs(Option.UNLIMITED_VALUES).build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, actualOption.getArgs().numberOfArgs());
    }

    @Test
    public void testOptionBuilderHasArgs_WithNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        Option expectedOption = Option.builder().withLongOpt("longOpt")
                .hasArgs(5).build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(5, actualOption.getArgs().numberOfArgs());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = Option.builder().withLongOpt("longOpt")
                .withType(String.class).build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(String.class, actualOption.getType());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option expectedOption = Option.builder().withLongOpt("longOpt")
                .withDescription("description").build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals("description", actualOption.getDescription());
    }

    @Test
    public void testOptionBuilderCreateWithNoSettings() {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        try {
            Option actualOption = optionBuilder.create();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testOptionBuilderCreateWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");

        try {
            Option actualOption = optionBuilder.create();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid long opt: ''", e.getMessage());
        }
    }

    @Test
    public void testOptionBuilderCreateWithNullDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription(null);

        try {
            Option actualOption = optionBuilder.create();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Description cannot be null", e.getMessage());
        }
    }

    @Test
    public void testOptionBuilderCreate_WithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option expectedOption = Option.builder().withLongOpt("longOpt")
                .hasArg().setValueSeparator(',').build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals(',', actualOption.getValueSeparator());
    }

    @Test
    public void testOptionBuilderCreate_WithValueSeparator_EmptySeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator('');
        Option expectedOption = Option.builder().withLongOpt("longOpt")
                .hasArg().setValueSeparator('').build();
        Option actualOption = optionBuilder.create();

        Assert.assertEquals("", actualOption.getValueSeparator());
    }

    @Test
    public void testOptionBuilderCreate_WithValueSeparator_InvalidSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(' ');

        try {
            Option actualOption = optionBuilder.create();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid value separator: ' '", e.getMessage());
        }
    }
}